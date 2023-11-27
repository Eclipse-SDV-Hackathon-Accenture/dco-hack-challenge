import { act, fireEvent, render, screen } from '@testing-library/react';
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
import TracksMain from '../../../pages/dco/tracksMain';
import Login from '../../../pages/login';

describe('Track info page', () => {
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    useRouter.mockImplementation(() => ({
        pathname: '/dco/tracksMain/trackInfo/trackVehicleDetails',
    }));
    const router = {
        push: jest.fn(),
        replace: jest.fn(),
        go: jest.fn(),
        createHref: jest.fn(),
        createLocation: jest.fn(),
        isActive: jest.fn(),
        matcher: {
            match: jest.fn(),
            getRoutes: jest.fn(),
            isActive: jest.fn(),
            format: jest.fn()
        },
        addTransitionHook: jest.fn()
    };
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
    test('', () => {
        router.replace('/login')
        render(
            //  @ts-ignore 
            <StoreProvider store={store}>
                <MemoryRouterProvider url="/dco/tracksMain/trackInfo/trackVehicleDetails">
                    {/* <Dco> */}
                    <MockedProvider mocks={mockList}>
                        <Login />
                    </MockedProvider>
                    {/* </Dco> */}
                </MemoryRouterProvider>
            </StoreProvider>
        )
    })
    test('track info render', () => {
        store.getActions().setCount(0);
        useRouter
        render(
            //  @ts-ignore 
            <StoreProvider store={store}>
                <MemoryRouterProvider url="/dco/tracksMain/trackInfo/trackVehicleDetails">
                    {/* <Dco> */}
                    <MockedProvider mocks={mockList}>
                        <TrackInfo />
                    </MockedProvider>
                    {/* </Dco> */}
                </MemoryRouterProvider>
            </StoreProvider>
        )
    });
    test('table with props bakbutton', async () => {
        store.getActions().setCount(0);
        useRouter
        render(
            //  @ts-ignore 
            <StoreProvider store={store}>
                <MemoryRouterProvider url="/dco/tracksMain/trackInfo/trackVehicleDetails">
                    <MockedProvider mocks={mockList}>
                        <TrackInfo />
                    </MockedProvider>
                </MemoryRouterProvider>
            </StoreProvider>
        )
        const checkBtn = screen.getByTestId("backButton");
        fireEvent.click(checkBtn);
        useRouter
        render(
            //  @ts-ignore 
            <StoreProvider store={store}>
                <MemoryRouterProvider url="/dco/tracksMain">
                    <MockedProvider>
                        <TracksMain />
                    </MockedProvider>
                </MemoryRouterProvider>
            </StoreProvider>
        )
        await act(async () => {
            await new Promise((resolve) => setTimeout(resolve, 0));
        });
    });
    beforeEach(() => {
        fetch.resetMocks();
    });
}) 