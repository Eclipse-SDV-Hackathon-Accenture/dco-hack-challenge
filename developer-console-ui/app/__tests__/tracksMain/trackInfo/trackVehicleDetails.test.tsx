import { render } from '@testing-library/react';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import { StoreProvider } from 'easy-peasy';
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import TrackInfo from '../../../pages/dco/tracksMain/trackInfo';
import { store } from '../../../services/store.service';
import { MockedProvider } from '@apollo/react-testing';
import { TRACK_DETAILS } from '../../../services/queries';
import TrackDetails from '../../../pages/dco/tracksMain/trackInfo/trackDetails';
import TrackVehicleDetails from '../../../pages/dco/tracksMain/trackInfo/trackVehicleDetails';

describe('Track info page', () => {
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    useRouter.mockImplementation(() => ({
        pathname: '/dco/tracksMain/trackInfo',
    }));
    const input = {
        description: "",
        duration: "7",
        id: "ab2ce5db-a0ad-4b6b-a16f-7ba5760c9f06",
        name: "track",
        state: "CREATED",
        trackType: "Test",
        vehicles: [{
            brand: "Mercedes-Benz",
            country: "FR",
            devices: [{
                components: [],
                createdAt: "2022-12-15T21:30:46.443445",
                dmProtocol: "LWM2M",
                dmProtocolVersion: "1.0.2",
                gatewayId: "",
                id: "84c32703-982a-4efd-920b-b80977073b33",
                modelType: "HIGH_EU",
                modifiedAt: "2022-12-20T14:09:25.437637",
                serialNumber: null,
                status: "ACTIVE",
                type: "TCU"
            }],
            status: "DRAFT",
            updatedAt: "2023-02-22T00:58:01.734361",
            vin: "BBTEST00000000340"
        }]
    }
    const mockList = [{
        request: {
            query: TRACK_DETAILS,
            variables: { id: '' }
        },
        result: { data: { findTrackById: input } },
    }]

    test('track info render', () => {
        store.getActions().setCount(0);
        useRouter
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <MemoryRouterProvider url="/dco/tracksMain/trackInfo/trackVehicleDetails">
                    {/* <Dco> */}
                    <MockedProvider mocks={mockList}>
                        <TrackVehicleDetails />
                    </MockedProvider>
                    {/* </Dco> */}
                </MemoryRouterProvider>
            </StoreProvider>
        )
    });
    beforeEach(() => {
        fetch.resetMocks();
    });
}) 