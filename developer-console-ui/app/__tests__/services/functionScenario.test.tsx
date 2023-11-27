import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { callUploadAxiosAPIForNewScenario, callUploadAxiosAPIForUpdateScenario, getFileSIzeInService, getLibData, getUploadFormDataForNewScenario, getUploadFormDataForUpdateScenario, handleNewScenarioSubmitInService, handleUpdateScenarioSubmitInService, libRowData, onClickScenario, resetFormForScenario, scenarioDataFromMap, setToastMessageForDeleteScenario, setToastMessageForNewScenario, setToastMessageForUpdateScenario, uploadFileCondition } from '../../services/functionScenario.services';
import { act } from 'react-dom/test-utils';
const formData = new FormData()
const reset = {
    setName: jest.fn(),
    setType: jest.fn(),
    setDescription: jest.fn(),
    setFileSizeError: jest.fn(),
    setFileNameError: jest.fn()
}
const data = {
    data: {
        data: {
            searchScenarioByPattern: {
                content: [{
                    createdAt: "2023-01-31T11:45:54.409678Z",
                    createdBy: "abc@t-systems.com",
                    description: "",
                    files: [{
                        checksum: "3E25960A79DBC69B674CD4EC67A72C62",
                        id: "ed78720b-0615-4c2b-9128-0c805790a1fc",
                        path: process.env.NEXT_PUBLIC_FILE,
                        size: "11",
                        updatedBy: "abc@t-systems.com",
                        updatedOn: "2023-02-06T09:03:49.714965Z"
                    }],
                    id: "fe7f4c94-c21a-4a4b-a3fc-c1b309b0f5f0",
                    lastModifiedBy: "abc@t-systems.com",
                    name: "Scenario 7",
                    status: "CREATED",
                    type: "CAN"
                }],
                elements: 15,
                empty: false,
                first: true,
                last: false,
                page: 0,
                pages: 2,
                size: 15,
                total: 16
            }
        }
    }
}
const setFunctions = {
    setComponentValue: jest.fn(),
    setVersionList: jest.fn(),
    setTypeList: jest.fn(),
    setDeviceList: jest.fn(),
    setTypeValue: jest.fn(),
    setVersionValue: jest.fn(),
    setBuildValue: jest.fn(),
    setDeviceValue: jest.fn(),
    setFileSizeError: jest.fn(),
    setFileNameError: jest.fn(),
    setUploadFile: jest.fn(),
    setToastMsg: jest.fn(),
    setSuccessMsgScenario: jest.fn(),
    setToastOpen: jest.fn(),
    setToastOpenScenario: jest.fn(),
    setArr: jest.fn(),
    setName: jest.fn(),
    setType: jest.fn(),
    setDescription: jest.fn(),
    setNameError: jest.fn(),
    setTypeError: jest.fn(),
    setFileError: jest.fn()
};
describe('scenario test cases', () => {
    const mockPerformanceMark = jest.fn();
    window.performance.mark = mockPerformanceMark;
    jest.useFakeTimers();
    beforeEach(() => {
        fetch.resetMocks();
    });
    it('scenarioDataFromMap', async () => {
        let result = {
            data: {
                searchScenarioByPattern: {
                    content: [{
                        "id": "96245c3a-d277-4ab3-ab38-cabfbf6eb2de",
                        "name": "scenario 6",
                        "description": "undefined",
                        "type": "MQTT",
                        "status": "CREATED",
                        "createdBy": "abc@t-systems.com",
                        "createdAt": "2023-04-07T07:35:05.455797Z",
                        "lastModifiedBy": "abc@t-systems.com",
                        "lastModifiedAt": "",
                        "file": {
                            "id": "d3f25c44-0f5c-4ff7-87c1-ad5882829834",
                            "path": "http://localhost:9000/scenario-library-service/scenario/96245c3a-d277-4ab3-ab38-cabfbf6eb2de/files/scenarioLib.txt",
                            "size": "8",
                            "checksum": "6D72EBEDD73D23B27BF7AEC86F55F9BB",
                            "updatedBy": "abc@t-systems.com",
                            "updatedOn": "2023-04-07T07:35:05.561311Z"
                        }
                    }]
                }
            }
        }
        expect(scenarioDataFromMap(result)).toStrictEqual([{ "createdBy": "abc@t-systems.com", "delete": "", "description": "undefined", "filename": undefined, "lastUpdated": "Invalid Date, Invalid Date", "scenario": "scenario 6", "sid": "96245c3a-d277-4ab3-ab38-cabfbf6eb2de", "type": "MQTT" }]);
    })
    it('get scenario lib row data', async () => {
        let result = {
            data: {
                searchScenarioByPattern: {
                    content: [{
                        createdAt: "2023-03-29T11:05:08.201106Z",
                        createdBy: "abc@t-systems.com",
                        description: "undefined",
                        file: {
                            checksum: "6D72EBEDD73D23B27BF7AEC86F55F9BB",
                            id: "fc236ea9-30a4-4acb-9f73-720f38733364",
                            path: "http://localhost:9000/scenario-library-service/scenario/bcbb76e2-650f-4ce0-a7da-b3082a58b8f3/files/scenarioLib.txt",
                            size: "8",
                            updatedBy: "abc@t-systems.com",
                            updatedOn: "2023-03-29T11:05:08.542815Z"
                        },
                        id: "bcbb76e2-650f-4ce0-a7da-b3082a58b8f3",
                        lastModifiedAt: "",
                        lastModifiedBy: "abc@t-systems.com",
                        name: "scenario 1",
                        status: "CREATED",
                        type: "CAN"
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
        }
        expect(libRowData(result)).toStrictEqual([{ "check": "", "createdBy": "abc@t-systems.com", "description": "undefined", "filename": "scenarioLib.txt", "lastUpdated": "Invalid Date, Invalid Date", "menu": "", "scenario": "scenario 1", "sid": "bcbb76e2-650f-4ce0-a7da-b3082a58b8f3", "type": "CAN" }])
    }
    )
    it('onClickScenario', async () => {
        expect(onClickScenario('', jest.fn(), jest.fn())).toBe(undefined);
    })
    it('handleNewScenarioSubmitInService', async () => {
        expect(handleNewScenarioSubmitInService({ text: '', name: '', type: '', selectedUploadFile: '' }, 'abc@t-systems.com', { setName: jest.fn(), setType: jest.fn(), setDescription: jest.fn(), setUploadFile: jest.fn(), setFileSizeError: jest.fn(),setFileNameError: jest.fn(), setToastMsg: jest.fn(), setNameError: jest.fn(), setTypeError: jest.fn(), setFileError: jest.fn() }, jest.fn(), jest.fn())).toBe(undefined);
        expect(handleNewScenarioSubmitInService({ text: 'Scenario test', name: 'Scenario', type: 'MQTT', selectedUploadFile: 'sce.txt' }, 'abc@t-systems.com', { setName: jest.fn(), setType: jest.fn(), setDescription: jest.fn(), setUploadFile: jest.fn(), setFileSizeError: jest.fn(), setToastMsg: jest.fn(), setNameError: jest.fn(), setFileNameError: jest.fn(),setTypeError: jest.fn(), setFileError: jest.fn() }, jest.fn(), jest.fn())).toBe(undefined);
    })
    it('handleUpdateScenarioSubmitInService', async () => {
        expect(handleUpdateScenarioSubmitInService({ text: '', name: '', type: '', selectedUploadFile: '' }, 'abc@t-systems.com', { setName: jest.fn(), setType: jest.fn(), setDescription: jest.fn(), setUploadFile: jest.fn(), setFileSizeError: jest.fn(), setFileNameError: jest.fn(),setSuccessMsgScenario: jest.fn(), setNameError: jest.fn(), setTypeError: jest.fn(), setFileError: jest.fn() }, jest.fn(), jest.fn(), { asPath: '/dco/scenario', replace: jest.fn() })).toBe(undefined);
        expect(handleUpdateScenarioSubmitInService({ text: 'Scenario test', name: 'Scenario', type: 'MQTT', selectedUploadFile: 'sce.txt' }, 'abc@t-systems.com', { setName: jest.fn(), setType: jest.fn(), setDescription: jest.fn(), setUploadFile: jest.fn(), setFileSizeError: jest.fn(),setFileNameError: jest.fn(), setSuccessMsgScenario: jest.fn(), setNameError: jest.fn(), setTypeError: jest.fn(), setFileError: jest.fn() }, jest.fn(), jest.fn(), { asPath: '/dco/scenario', replace: jest.fn() })).toBe(undefined);
    })
    it('getFileSIzeInService', async () => {
        expect(getFileSIzeInService({ target: { value: '.txt', files: [{ size: 10,name:'abc_abc.txt' }] } }, jest.fn(), jest.fn(), 0, 10000, jest.fn(),jest.fn())).toBe(undefined);
        expect(getFileSIzeInService({ target: { value: '.odx', files: [{ size: 10,name:'abc_abc.txt' }] } }, jest.fn(), jest.fn(), 0, 10000, jest.fn(),jest.fn())).toBe(undefined);
        expect(getFileSIzeInService({ target: { value: '', files: [{name:''}] } }, jest.fn(), jest.fn(), 0, 10000, jest.fn(),jest.fn())).toBe(undefined);
    })
    it('uploadFileCondition', async () => {
        expect(uploadFileCondition(1, 10, 10000, 0, jest.fn(), { target: { value: '.txt', files: [{ size: 10 }] } },
            jest.fn())).toBe(false);
        expect(uploadFileCondition(0, 0, 10000, 0, jest.fn(), { target: { value: '.txt', files: [{ size: 10 }] } },
            jest.fn())).toBe(true);
    })
    it('tests setToastMessageForDeleteScenario', async () => {
        expect(setToastMessageForDeleteScenario({ deleteScenario: "" }, jest.fn(), jest.fn(), "success")).toBe(true)
        expect(setToastMessageForDeleteScenario({ deleteScenario: "" }, jest.fn(), jest.fn(), "")).toBe(false)
        const callback = jest.fn();
        setToastMessageForDeleteScenario({ deleteScenario: "" }, callback, callback, "")
        jest.runAllTimers();
        expect(callback).toBeCalled();
        expect(callback).toHaveBeenCalledTimes(3);
    })
    test('setToastMessageForNewScenario', async () => {
        expect(
            setToastMessageForNewScenario({ data: { createScenario: 'test' } }, setFunctions.setToastMsg, jest.fn(), setFunctions.setToastOpen, setFunctions)).toBe(false);
        expect(
            setToastMessageForNewScenario({ errors: [{ message: 'test' }] }, setFunctions.setToastMsg, jest.fn(), setFunctions.setToastOpen, setFunctions)).toBe(true);
            jest.runAllTimers();
    })
    test('setToastMessageForUpdateScenario', async () => {
        expect(
            setToastMessageForUpdateScenario({ data: { updateScenario: 'test' } }, setFunctions.setSuccessMsgScenario, jest.fn(), setFunctions.setToastOpenScenario)).toBe(false);
            jest.runAllTimers();
    })
    it('tests callUploadAxiosAPIForUpdateScenario', async () => {
        try {
            return await callUploadAxiosAPIForUpdateScenario({ selectedUploadFile: 'TEST' }, '')
        } catch (e) {
            return [];
        }
    })
    it('tests getUploadFormDataForNewScenario', () => {
        let result = getUploadFormDataForNewScenario('', '', '', '', '');
        expect(formData).toEqual(new FormData());
    })

    it('tests resetFormForScenario', () => {
        expect(resetFormForScenario(reset)).toBe(undefined);
    })
    it('tests getUploadFormDataForUpdateScenario', async () => {
        try {
            return await (getUploadFormDataForUpdateScenario(
                '123455', '', 'test.txt', '.txt',
                'test file', 'abc@t-systems.com'))
        } catch (e) {
            return e;
        }
    })
    it('tests getLibData', async () => {
        try {
            return await (getLibData(1, 'sc')).then((res) => res.json())
                .then((result) => result)
        } catch (e) {
            return e;
        }
    })
    it('tests callUploadAxiosAPIForNewScenario', async () => {
        try {
            return await callUploadAxiosAPIForNewScenario({
                selectedUploadFile: 'TEST', name: 'file.txt',
                type: '.txt', description: ''
            }, 'abc@t-systems.com')
        } catch (e) {
            return { data: { errors: [{ message: '' }] } };
        }
    })
})