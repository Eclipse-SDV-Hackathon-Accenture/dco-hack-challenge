import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { clearAll, getSimData, launchSimulation, onClickNewSimulation, onLaunchedSimulation, simRowData } from '../../services/functionSimulation.service';
import { store } from '../../services/store.service';
import { act } from 'react-dom/test-utils';
describe('scenario test cases', () => {
    const mockPerformanceMark = jest.fn();
    const variable = {title:'Scenario', scenario: [{checked:true,id:'344'},{checked:false,id:'12'}], track: [{checked:true,id:'233'},{checked:false,id:'239'}],scenarioType:'MQTT' } as const
    const setVariable = { setTitleError: jest.fn(), setSTypeError: jest.fn(), setTrackError: jest.fn(), setScenarioError: jest.fn() }

    const variable2 = {title:null, scenario: [], track: [],scenarioType:null } as const
    const setVariable2 = { setTitleError: jest.fn(), setSTypeError: jest.fn(), setTrackError: jest.fn(), setScenarioError: jest.fn() }

    const setVariables={setTitle:jest.fn(), setDescription:jest.fn(), setEnvironment:jest.fn(), setPlatform:jest.fn(), setSelectedscenario:jest.fn(), setSelectedtrack:jest.fn(), setScenarioType:jest.fn(), setHardware:jest.fn(), setSearchval:jest.fn(), setTitleError:jest.fn(), setSTypeError:jest.fn(),setTrackError:jest.fn(), setScenarioError:jest.fn(),}
    window.performance.mark = mockPerformanceMark;
    jest.useFakeTimers();
    beforeEach(() => {
        fetch.resetMocks();
    });
    it('get simulation row data', () => {
        let result = simRowData(
            {
                simulationReadByQuery: {
                    content: [{
                        brands: [],
                        createdBy: "abc@t-systems.com",
                        description: "",
                        environment: "",
                        hardware: "",
                        id: "e0f4430b-5b9c-4cb8-9e39-bea5f31ece9a",
                        name: "sim",
                        noOfScenarios: 1,
                        noOfVehicle: 0,
                        platform: "",
                        scenarioType: "Over-The-Air Service",
                        startDate: "2023-03-29T11:06:19.695468Z",
                        status: "Timeout"
                    }],
                    elements: 10,
                    empty: false,
                    first: true,
                    last: true,
                    page: 0,
                    pages: 1,
                    size: 10,
                    total: 1
                }
            }
        );
        expect(result).toBe(undefined);
        let result2 = simRowData(
            {
                simulationReadByQuery: {
                    content: [],
                    elements: 0,
                    empty: false,
                    first: true,
                    last: true,
                    page: 0,
                    pages: 1,
                    size: 1,
                    total: 1
                }
            }
        );
        expect(result2).toBe(undefined);
    })
    it('launch simulation', () => {
        expect(launchSimulation(variable, jest.fn(), setVariable)).toBe(undefined);
        expect(launchSimulation(variable2, jest.fn(), setVariable2)).toBe(undefined);
    })

    it('launched simulation', () => {
        expect(onLaunchedSimulation(jest.fn(), jest.fn(), jest.fn(), jest.fn(), [], true)).toBe(undefined);
        jest.runAllTimers();

        expect(onLaunchedSimulation(jest.fn(), jest.fn(), jest.fn(), jest.fn(), [], false)).toBe(undefined);
        jest.runAllTimers();

        expect.assertions(2);
    })
    it('clearAll', () => {
        expect(clearAll(setVariables)).toBe(undefined);
    })
    it('clearAll', () => {
        expect(onClickNewSimulation()).toBe(undefined);
        jest.runAllTimers();
    })
    store.getActions().setSearchval('scen')
    store.getActions().setSelectedscenario('234');
    store.getActions().setSelectedtrack('234');
})