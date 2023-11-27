import { fireEvent, render, screen } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { store } from '../../services/store.service';
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import MenuForScenario from '../../pages/dco/addEditScenario/menuForScenario';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import { MockedProvider } from '@apollo/react-testing';
import { DELETE_SCENARIO } from '../../services/functionScenario.services';

describe('MenuForScenario', () => {
    const mockPerformanceMark = jest.fn();
    window.performance.mark = mockPerformanceMark;
    jest.useFakeTimers();
    beforeEach(() => {
        fetch.resetMocks();
    });
    const props = {
        variant: "naked",
        cellData: {
            check: "",
            filename: "scenarioLib.txt",
            lastUpdated: "2023-03-29, 4:35:08 p.m.",
            menu: "",
            scenario: "scenario 1",
            sid: "bcbb76e2-650f-4ce0-a7da-b3082a58b8f3",
            type: "MQTT"
        },
        insideTrack: undefined, setSuccessMsgScenario: () => { }, setToastOpenScenario: () => { }

    }
    const props2 = {
        variant: "naked",
        cellData: {},
        insideTrack: undefined, setSuccessMsgScenario: () => { }, setToastOpenScenario: () => { }
    }
    const mockList = [{
        request: {
            query: DELETE_SCENARIO,
            variables: { id: '' }
        },
        result: { data: { deleteTrack: "Track deleted" } },
    }]

    test('delete scenario render', () => {
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <MemoryRouterProvider url="/dco/scenario">
                    <MockedProvider mocks={mockList}>
                        <MenuForScenario {...props} />
                    </MockedProvider>
                </MemoryRouterProvider>
            </StoreProvider>
        )
        const checkBtn = screen.getByTestId("btn");
        fireEvent.click(checkBtn);

    });

    test('delete scenario render', () => {
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <MemoryRouterProvider url="/dco/scenario">
                    <MockedProvider mocks={mockList}>
                        <MenuForScenario {...props2} />
                    </MockedProvider>
                </MemoryRouterProvider>
            </StoreProvider>
        )
        const checkBtn = screen.getByTestId("btn");
        fireEvent.click(checkBtn);

    });
}) 