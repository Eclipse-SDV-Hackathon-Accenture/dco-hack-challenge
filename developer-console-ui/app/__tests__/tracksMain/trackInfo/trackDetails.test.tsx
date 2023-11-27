import { render } from '@testing-library/react';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import { StoreProvider } from 'easy-peasy';
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { store } from '../../../services/store.service';
import { MockedProvider } from '@apollo/react-testing';
import { TRACK_DETAILS } from '../../../services/queries';
import TrackDetails from '../../../pages/dco/tracksMain/trackInfo/trackDetails';

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
        trackType: "Test"
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
                        <TrackDetails />
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