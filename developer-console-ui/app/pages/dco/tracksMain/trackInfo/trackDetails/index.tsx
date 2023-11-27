import { useQuery } from "@apollo/client";
import { Box, Flex, Headline, Spacer, Value, ValueGroup } from "@dco/sdv-ui"
import { useStoreState } from "easy-peasy";
import { getCompNVersion, getDevices, getVehicleList } from "../../../../../services/functionTrack.service";
import { TRACK_DETAILS } from "../../../../../services/queries";
import { getValArray } from "../../../../../services/functionShared";
import TagLists from "../../../../shared/tagLists";
export function TrackId() {
    return useStoreState((state: any) => state.tid);
}
// displays list of track details page, on click of track from listing page->left page
const TrackDetails = () => {
    const { data } = useQuery(TRACK_DETAILS, {
        variables: { id: TrackId() },
    });
    let arrCountry: any[] = [];
    let arrBrand: any[] = [];
    let arr: any[] = [];
    let arrComponent: any[] = [];
    let arrCompnVersionVal: any[] = [];
    let arrDevices: any[] = [];
    const datasVehicle = getVehicleList(data);
    arrCountry = getValArray(datasVehicle, 'country');
    arrBrand = getValArray(datasVehicle, 'brand');
    arr = getValArray(datasVehicle, 'devices');
    arrDevices=getCompNVersion(arr, arrDevices, arrComponent)
    arrCompnVersionVal=getDevices(arrComponent, arrCompnVersionVal)
    return (
        <Box padding="sidebar">
            <Flex>
                <Flex.Item flex={2}>
                    <Headline level={2}>Details</Headline>
                    <Spacer space={2} />
                    <ValueGroup title="Region">
                        <Value>
                            <Spacer space={0.3} />
                            <TagLists data={arrCountry} type={'countries'} from={"trackDetails"} />
                        </Value>
                    </ValueGroup>
                    <Spacer space={0.2} />
                    <ValueGroup title="Brand">
                        <Value>
                            <Spacer space={0.3} />
                            <TagLists data={arrBrand} type={'brands'} from={"trackDetails"} />
                        </Value>
                    </ValueGroup>
                    <Spacer space={0.2} />
                    <ValueGroup title="Software component, Version">
                        <Value>
                            <Spacer space={0.3} />
                            <TagLists data={arrCompnVersionVal} type={'compNVersion'} from={"trackDetails"} />
                        </Value>
                    </ValueGroup>
                    <Spacer space={0.2} />
                    <ValueGroup title="Devices">
                        <Value>
                            <Spacer space={0.3} />
                            <TagLists data={arrDevices} type={'devices'} from={"trackDetails"} />
                        </Value>
                    </ValueGroup>
                    <Spacer space={0.2} />
                    <ValueGroup title="Name">
                        <Value>
                            {data?.findTrackById.name}
                        </Value>
                    </ValueGroup>
                    <ValueGroup title="Type">
                        <Value>
                            {data?.findTrackById.trackType}
                        </Value>
                    </ValueGroup>
                    <ValueGroup title="Description">
                        <Value>
                            {data?.findTrackById.description}
                        </Value>
                    </ValueGroup>
                    <ValueGroup title="Duration">
                        <Value>
                            {data?.findTrackById.duration} {data && 'days'}
                        </Value>
                    </ValueGroup>
                </Flex.Item>
            </Flex>
        </Box>
    )
}

export default TrackDetails