#!/usr/bin/env python
# Eclipse SUMO, Simulation of Urban MObility; see https://eclipse.dev/sumo
# Copyright (C) 2009-2023 German Aerospace Center (DLR) and others.
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License 2.0 which is available at
# https://www.eclipse.org/legal/epl-2.0/
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the Eclipse
# Public License 2.0 are satisfied: GNU General Public License, version 2
# or later which is available at
# https://www.gnu.org/licenses/old-licenses/gpl-2.0-standalone.html
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0-or-later

# @file    evacuateAreas.py
# @author  Yun-Pang Floetteroed
# @date    2021-03-30


"""
1. get populations of the affected areas from wikidata in json format
2. get neighbor areas of the affected areas as destination areas (ToDo)
3. generate TAZ file containing polygon and connections used as origin/destination edges
   when giving a polygon file
4. generate trips with od2trips and the given network file and TAZ file

"""

from __future__ import absolute_import
from __future__ import print_function
import sys
import subprocess
import json
import pprint

import sumolib
from sumolib.output import parse


class Node():
    def __init__(self, nid, lat, lon):
        self.label = nid
        self.lat = lat
        self.lon = lon


optParser = sumolib.options.ArgumentParser(description="Get network from OpenStreetMap")
optParser.add_argument("--prefix", category="processing", default="evacuation", help="for output files")
optParser.add_argument("--wikidata", category="processing", default="osm.wikidata.xml", metavar="FILE",
                       help="give the wikidata file name")
optParser.add_argument("--osmfile", category="input", help="give the osm bbox file name generated by osmGet.py",
                       default="osm_bbox.osm.xml", metavar="FILE")
optParser.add_argument("--netfile", category="input", help="give the corresponding osm-based net file name",
                       default="osm.net.xml", metavar="FILE")
optParser.add_argument("--cost-modifier", category="processing", action="store_true", default=False,
                       help="change edge weights for priority edges/routes for evacuation")
optParser.add_argument("--timeline", category="processing", help="Set the evacuation portions, separated by comma; " +
                       "the ratios are decided together with the given duration")
optParser.add_argument("--begin", category="processing", type=float, default=0, help="Set simulation/routing begin")
optParser.add_argument("--duration", category="processing", type=float, default=2,
                       help="Set evacuation duration in hour")
optParser.add_argument("--evacuate-areas", category="processing",
                       help=("the names of the evacuated areas, separated by comma," +
                             "if no area is defined, all areas according to the defined " +
                             "admin_level will be evacuated"))
optParser.add_argument("--admin_level", category="processing", type=float, default=10,
                       help="select the areas according to the defined admin_level")
optParser.add_argument("--dest-areas", category="processing",
                       help=("the names of the destination areas, separated by comma," +
                             "if no area is defined, all demands will be evacuated " +
                             "to the network borders"))
optParser.add_argument("--simulation", category="processing", action="store_true", default=False,
                       help="directly run the simulation at the end")
optParser.add_argument("--debug", category="processing", action="store_true", default=False,
                       help="print out information for debugging")
optParser.add_argument("--verbose", category="processing", action="store_true", default=False,
                       help="print out more information")


def genMatrix(demandMap, originSet, destSet, options):
    matrixFile = options.prefix + '.fma'
    diffSet = originSet.difference(destSet)
    allTazList = list(diffSet)
    for dest in destSet:
        allTazList.append(dest)
    with open(matrixFile, "w") as outf:
        outf.write('$VM\n')
        outf.write('* vehicle type\n')
        outf.write('4\n')
        outf.write('* From-Time  To-Time\n')
        outf.write('%.2f %.2f\n' % (options.begin, (options.begin+options.duration)))
        outf.write('* Factor\n')
        outf.write('1.00\n')
        outf.write('*\n')
        outf.write('* some\n')
        outf.write('* additional\n')
        outf.write('* comments\n')
        outf.write('* District number\n')
        outf.write('%s\n' % len(allTazList))
        outf.write('* names:\n')
        for taz in allTazList:
            outf.write('    %s' % taz)
        outf.write('\n')
        outf.write('*\n')

        for taz in allTazList:
            if taz in destSet:
                outf.write('* District %s Sum = 0\n' % (taz))
                for _ in range(len(allTazList)):
                    outf.write('    0')
                outf.write('\n')
            else:
                outf.write('* District %s Sum = %s\n' % (taz, demandMap[taz]))
                for _ in range(len(originSet)):
                    outf.write('    0')
                # assume the demand is uniformly distributed to the pre-defined destination areas
                # TODO: consider different customized ratios
                for _ in range(len(destSet)):
                    outf.write('    %s' % (round(demandMap[taz]/len(destSet))))
                outf.write('\n')
    outf.close()

    return matrixFile


def genPolyTypeFile(prefix):
    typeFile = prefix + '_boundary.typ.xml'
    with open(typeFile, 'w') as outf:
        outf.write('''<polygonTypes>
    <polygonType id="waterway"                name="water"       color=".71,.82,.82" layer="-4" discard="true"/>
    <polygonType id="natural"                 name="natural"     color=".55,.77,.42" layer="-4" discard="true"/>
    <polygonType id="natural.water"           name="water"       color=".71,.82,.82" layer="-4" discard="true"/>
    <polygonType id="natural.wetland"         name="water"       color=".71,.82,.82" layer="-4" discard="true"/>
    <polygonType id="natural.wood"            name="forest"      color=".55,.77,.42" layer="-4" discard="true"/>
    <polygonType id="natural.land"            name="land"        color=".98,.87,.46" layer="-4" discard="true"/>\n
    <polygonType id="landuse"                 name="landuse"     color=".76,.76,.51" layer="-3" discard="true"/>
    <polygonType id="landuse.forest"          name="forest"      color=".55,.77,.42" layer="-3" discard="true"/>
    <polygonType id="landuse.park"            name="park"        color=".81,.96,.79" layer="-3" discard="true"/>
    <polygonType id="landuse.residential"     name="residential" color=".92,.92,.89" layer="-3" discard="true"/>
    <polygonType id="landuse.commercial"      name="commercial"  color=".82,.82,.80" layer="-3" discard="true"/>
    <polygonType id="landuse.industrial"      name="industrial"  color=".82,.82,.80" layer="-3" discard="true"/>
    <polygonType id="landuse.military"        name="military"    color=".60,.60,.36" layer="-3" discard="true"/>
    <polygonType id="landuse.farm"            name="farm"        color=".95,.95,.80" layer="-3" discard="true"/>
    <polygonType id="landuse.greenfield"      name="farm"        color=".95,.95,.80" layer="-3" discard="true"/>
    <polygonType id="landuse.village_green"   name="farm"        color=".95,.95,.80" layer="-3" discard="true"/>\n
    <polygonType id="tourism"                 name="tourism"     color=".81,.96,.79" layer="-2" discard="true"/>
    <polygonType id="military"                name="military"    color=".60,.60,.36" layer="-2" discard="true"/>
    <polygonType id="sport"                   name="sport"       color=".31,.90,.49" layer="-2" discard="true"/>
    <polygonType id="leisure"                 name="leisure"     color=".81,.96,.79" layer="-2" discard="true"/>
    <polygonType id="leisure.park"            name="tourism"     color=".81,.96,.79" layer="-2" discard="true"/>
    <polygonType id="aeroway"                 name="aeroway"     color=".50,.50,.50" layer="-2" discard="true"/>
    <polygonType id="aerialway"               name="aerialway"   color=".20,.20,.20" layer="-2" discard="true"/>
    <polygonType id="highway.services"        name="services"    color=".93,.78,1.0" layer="-2" discard="true"/>\n
    <polygonType id="shop"                    name="shop"        color=".93,.78,1.0" layer="-1" discard="true"/>
    <polygonType id="historic"                name="historic"    color=".50,1.0,.50" layer="-1" discard="true"/>
    <polygonType id="man_made"                name="man_made"    color="1.0,.90,.90" layer="-1" discard="true"/>
    <polygonType id="man_made.pipeline"       name="pipeline"    color="1.0,.90,.90" layer="-1" discard="true"/>
    <polygonType id="building"                name="building"    color="1.0,.90,.90" layer="-1" discard="true"/>
    <polygonType id="amenity"                 name="amenity"     color=".93,.78,.78" layer="-1" discard="true"/>
    <polygonType id="amenity.parking"         name="parking"     color=".72,.72,.70" layer="-1" discard="true"/>\n
    <polygonType id="barrier"                 name="barrier"     color="1.0,.3,.3" layer="0" fill="false" discard="true"/>
    <polygonType id="power"                   name="power"       color=".10,.10,.30" layer="-1" discard="true"/>
    <polygonType id="highway"                 name="highway"     color=".10,.10,.10" layer="-1" discard="true"/>\n
    <polygonType id="boundary"                name="boundary"    color="1.0,.33,.33" layer="0" fill="false" discard="true"/>
    <polygonType id="boundary.administrative" name="administrative" color="1.0,.33,.33" layer="0" fill="false" discard="false"/>
    <polygonType id="admin_level"             name="admin_level" color="1.0,.33,.33" layer="0" fill="false" discard="false"/>
    <polygonType id="place"                   name="admin_level" color="1.0,.9,.0"   layer="0" fill="false" discard="true"/>\n
    <polygonType id="railway"                 name="railway"     color=".10,.10,.10" layer="-1" discard="true"/>
    <polygonType id="railway:position"        name="railway.position"        color="blue"  layer="1" discard="true"/>
    <polygonType id="railway:position:exact"  name="railway.position.exact"  color="green" layer="2" discard="true"/>\n
</polygonTypes>''')  # noqa
    return typeFile


def main(args=None):
    options = optParser.parse_args(args=args)
    demandMap = {}  # osm area code: population
    nameMap = {}  # area name: osm area code
    # tazMap = {}   # osm area code: edges in the taz
    netfile = options.netfile
    osmfile = options.osmfile

    if not options.wikidata or not options.netfile or not osmfile:
        optParser.error("One of the files (wikidata, network, osmfile) is not given.")

    if options.verbose:
        print("Step 1: get the taz codes of the selected areas from the given osm_bbox-file")
    originSet = set()
    admin_level = None
    nameList = []
    if options.evacuate_areas:
        nameList = options.evacuate_areas.split(',')
    else:
        admin_level = options.admin_level
    for rel in parse(osmfile, 'relation'):
        getWikidata = False
        isAdmin = False
        if rel.tag:
            for tag in rel.tag:
                if admin_level:
                    if tag.k == "admin_level" and int(tag.v) == admin_level:
                        getWikidata = True
                else:
                    if tag.k == "name" and tag.v in nameList:
                        getWikidata = True

                if tag.k == "boundary" and tag.v == "administrative":
                    isAdmin = True

                if getWikidata and isAdmin and tag.k == "wikidata":
                    originSet.add(tag.v)
                    getWikidata = False
                    isAdmin = False

    if options.verbose:
        print("Step 2: read wikidata")
    objList = [json.loads(line) for line in open(options.wikidata, 'r')]
    if options.debug:
        for obj in objList:
            pprint.pprint(obj)

    # TODO: for evacuating demands to the neighbor areas
    if options.verbose:
        print("Step 3: get the population data and the neighbor areas of the selected evacuation areas")
    neighborMap = {}  # taz: [neighbor1_id, ....]
    for i, obj in enumerate(objList):
        print("    ObjList:%s" % i)
        for taz, description in obj["entities"].items():
            ex_time = "+0"
            if taz in originSet:
                print('    evacuated area: ', taz)
                if "en" in description["labels"]:
                    language = "en"
                elif "de" in description["labels"]:
                    language = "de"
                elif "fr" in description["labels"]:
                    language = "fr"
                elif "es" in description["labels"]:
                    language = "es"
                else:
                    print("    no information about the area name in English, German, French and Spanish")
                    print("    please check which language is available in the raw data and " +
                          "adjust the code with the available language name")

                if "claims" in description and "P1082" in description["claims"]:
                    for history in description["claims"]["P1082"]:
                        pop_value = history["mainsnak"]["datavalue"]["value"]["amount"]
                        if "P585" in history["qualifiers"]:
                            pop_time = history["qualifiers"]["P585"][0]["datavalue"]["value"]["time"]
                        else:
                            pop_time = "+1"
                            print("    no P585: ", taz)
                        if pop_time > ex_time:
                            ex_time = pop_time
                            demandMap[taz] = int(pop_value)
                    nameMap[taz] = description["labels"][language]["value"]
                    neighborMap[taz] = set()

                # get the adjacent administrative areas
                if "claims" in description and "P47" in description["claims"]:
                    for history in description["claims"]["P47"]:
                        neighborMap[taz].add(history["mainsnak"]["datavalue"]["value"]["id"])

    if options.debug:
        print("demandMap:")
        pprint.pprint(demandMap)
        print("nameMap:")
        pprint.pprint(nameMap)
        print("neighborMap:")
        pprint.pprint(neighborMap)

    # todo: currently only one pre-defined safeZone in the destSet
    if options.verbose:
        print("Step 4: get the edges on the network borders as possible evacuation destinations")
    safeZone = "safeZone"
    destSet = {safeZone}
    nameMap[safeZone] = safeZone

    if options.verbose:
        print("Step 5: generate matrix file for od2trips")
    matrixFile = genMatrix(demandMap, originSet, destSet, options)

    if options.verbose:
        print("Step 6: generate taz polygon file for getting edges in each taz with polyconvert")
    # get the boundary polygon from the osm.poly.xml
    # set the polygon type file and use it with polyconvert
    # todo: need to revise, currently all types are still written in the boundaries.poly.xml
    tazPolyFile = options.prefix + ".poly.xml"
    typeFile = genPolyTypeFile(options.prefix)
    # with open(typeFile, 'w') as outf:
    #    outf.write('<polygonTypes>\n')
    #    outf.write('    <polygonType id="railway.rail" name="boundary"    color="1.0,.33,.33" layer="0" fill="false" discard="true"/>\n')  # noqa
    #    outf.write('    <polygonType id="boundary"     name="boundary"    color="1.0,.33,.33" layer="0" fill="false" discard="false"/>\n')  # noqa
    #    outf.write('    <polygonType id="admin_level"  name="admin_level" color="1.0,.33,.33" layer="0" fill="false" discard="false"/>\n')  # noqa
    #    outf.write('    <polygonType id="place"        name="admin_level" color="1.0,.9,.0"   layer="0" fill="false" discard="false"/>\n')  # noqa
    #    outf.write('</polygonTypes>\n')
    # outf.close()
    exeCall = "polyconvert -n %s --osm-files %s --type-file %s --output-file %s --osm.keep-full-type" % (
        netfile, options.osmfile, typeFile, tazPolyFile)
    subprocess.call(exeCall, stdout=sys.stdout, stderr=sys.stderr, shell=True)

    if options.verbose:
        print("Step 7: get edges in each Taz and generate a taz file")
    tazFile = options.prefix + '.taz.xml'
    exeCall = "python edgesInDistricts.py -n %s -t %s -o %s -f" % (netfile, tazPolyFile, tazFile)
    subprocess.call(exeCall, stdout=sys.stdout, stderr=sys.stderr, shell=True)

    if options.verbose:
        print("Step 8: get the edges on the network borders")
    safeEdges = set()
    net = sumolib.net.readNet(options.netfile)
    for edge in net.getEdges():
        if edge.is_fringe():
            safeEdges.add(edge._id)

    if options.verbose:
        print("Step 9: add the edges on the network borders in the existing taz file")
    with open(tazFile, 'a') as outf:
        outf.write('<taz id="%s" edges="' % safeZone)
        for i, e in enumerate(safeEdges):
            if i == 0:
                outf.write('%s' % e)
            else:
                outf.write(' %s' % e)
        outf.write('"/>\n')
    outf.close()

    if options.verbose:
        print("Step 10: generate trip file with od2trips")
    timeline = []
    if options.timeline:
        timeline = ["--timeline", options.timeline]
    tripFile = options.prefix + '.trips.xml'
    exeCall = [sumolib.checkBinary("od2trips"), "-n", tazFile, "-d", matrixFile,
               "--output-prefix", options.prefix, "--output-file", tripFile,
               "--different-source-sink", "--begin", str(options.begin),
               "--end", str(options.begin+options.duration)] + timeline
    subprocess.call(exeCall, stdout=sys.stdout, stderr=sys.stderr)

    # if options.simulation:
    #    exeCall =


if __name__ == "__main__":
    main()
