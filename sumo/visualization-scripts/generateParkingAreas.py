#!/usr/bin/env python
# Eclipse SUMO, Simulation of Urban MObility; see https://eclipse.dev/sumo
# Copyright (C) 2010-2023 German Aerospace Center (DLR) and others.
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License 2.0 which is available at
# https://www.eclipse.org/legal/epl-2.0/
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the Eclipse
# Public License 2.0 are satisfied: GNU General Public License, version 2
# or later which is available at
# https://www.gnu.org/licenses/old-licenses/gpl-2.0-standalone.html
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0-or-later

# @file    generateParkingAreas.py
# @author  Jakob Erdmann
# @date    2021-11-25


from __future__ import print_function
from __future__ import absolute_import
import os
import sys
import random

if 'SUMO_HOME' in os.environ:
    sys.path.append(os.path.join(os.environ['SUMO_HOME'], 'tools'))
import sumolib  # noqa


def get_options(args=None):
    optParser = sumolib.options.ArgumentParser(description="Generate trips between random locations")
    optParser.add_argument("-n", "--net-file", category="input", dest="netfile",
                           help="define the net file (mandatory)")
    optParser.add_argument("-o", "--output-file", category="output", dest="outfile",
                           default="parkingareas.add.xml", help="define the output filename")
    optParser.add_argument("-p", "--probability", category="processing", type=float, default=1,
                           help="Probability for an edge to receive a parkingArea")
    optParser.add_argument("-L", "--length", category="processing", type=float, default=6,
                           help="Length required per parking space")
    optParser.add_argument("-l", "--space-length", category="processing", type=float, default=5, dest="spaceLength",
                           help="visual length of each parking space")
    optParser.add_argument("-w", "--width", category="processing", type=float,
                           help="visual width of each parking space")
    optParser.add_argument("-r", "--random-capacity", category="processing", action="store_true", dest="randCapacity",
                           default=False, help="Randomize roadsideCapacity")
    optParser.add_argument("--min", category="processing", type=int, default=0,
                           help="Minimum capacity for parkingAreas")
    optParser.add_argument("--max", category="processing", type=int, default=int(1e9),
                           help="Maximum capacity for parkingAreas")
    optParser.add_argument("--edge-type.keep", category="processing", dest="edgeTypeKeep",
                           help="Optional list of edge types to keep exclusively")
    optParser.add_argument("--edge-type.remove", category="processing", dest="edgeTypeRemove",
                           help="Optional list of edge types to exclude")
    optParser.add_argument("--keep-all", category="processing", action="store_true", default=False, dest="keepAll",
                           help="whether to keep parkingAreas with 0 capacity")
    optParser.add_argument("--lefthand", category="processing", action="store_true", default=False, dest="lefthand",
                           help="whether to place parkingareas on the left of the road")
    optParser.add_argument("-a", "--angle", category="processing", type=float,
                           help="parking area angle")
    optParser.add_argument("--prefix", category="processing", default="pa", help="prefix for the parkingArea ids")
    optParser.add_argument("-s", "--seed", category="processing", type=int, default=42, help="random seed")
    optParser.add_argument("--random", category="processing", action="store_true", default=False,
                           help="use a random seed to initialize the random number generator")
    optParser.add_argument("--vclass", category="processing", default="passenger",
                           help="only use edges which permit the given vehicle class")
    optParser.add_argument("-v", "--verbose", category="processing", action="store_true",
                           default=False, help="tell me what you are doing")

    options = optParser.parse_args(args=args)
    if not options.netfile:
        optParser.print_help()
        sys.exit(1)

    if options.edgeTypeKeep:
        options.edgeTypeKeep = options.edgeTypeKeep.split(',')
    if options.edgeTypeRemove:
        options.edgeTypeRemove = options.edgeTypeRemove.split(',')

    return options


def main(options):
    if not options.random:
        random.seed(options.seed)

    net = sumolib.net.readNet(options.netfile)

    with open(options.outfile, 'w') as outf:
        sumolib.writeXMLHeader(outf, "$Id$", "additional", options=options)  # noqa
        for edge in net.getEdges():
            if options.edgeTypeKeep and not edge.getType() in options.edgeTypeKeep:
                continue
            if options.edgeTypeRemove and edge.getType() in options.edgeTypeRemove:
                continue
            lanes = edge.getLanes()
            if options.lefthand:
                lanes = reversed(lanes)
            for lane in lanes:
                if lane.allows(options.vclass):
                    if random.random() < options.probability:
                        capacity = lane.getLength() / options.length
                        if options.randCapacity:
                            capacity *= random.random()
                        capacity = min(options.max, max(options.min, int(capacity)))
                        if capacity > 0 or capacity == options.max or options.keepAll:
                            angle = '' if options.angle is None else ' angle="%s"' % options.angle
                            length = '' if options.spaceLength <= 0 else ' length="%s"' % options.spaceLength
                            width = '' if options.width is None else ' width="%s"' % options.width
                            lefthand = '' if not options.lefthand else ' lefthand="true"'
                            outf.write('    <parkingArea id="%s%s" lane="%s" roadsideCapacity="%s"%s%s%s%s/>\n' % (
                                options.prefix, edge.getID(), lane.getID(),
                                capacity, length, width, angle, lefthand))
                    break
        outf.write("</additional>\n")


if __name__ == "__main__":
    if not main(get_options()):
        sys.exit(1)
