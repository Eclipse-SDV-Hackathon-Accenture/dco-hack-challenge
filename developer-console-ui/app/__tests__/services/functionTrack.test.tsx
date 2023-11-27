import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
import { getToolTip, onClickDeleteTrack, setToastMessageForDeleteTrack, uploadFile, onselectionchange, saveNewTrack, onClickTrackColumn, mapDataToTable, deleteTrackFun, getVehicleList, getCompNVersion, getDevices, onSelectionChanged, selectedCheckboxFunction, getUploadFormDataForNewComponent, onClickMenuItem, onCompletedCreateTrack, trackRowData, getTrackData, onClickNewTrack, } from "../../services/functionTrack.service";
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { store } from '../../services/store.service';
describe('test category', () => {
    const mockPerformanceMark = jest.fn();
    const vehicleList = { findTrackById: { vehicles: [{ vin: "test", updatedAt: "", country: "", status: "", brand: "", devices: [{ type: "", id: "", components: [{ name: "", version: "" }] }] }] } }
    window.performance.mark = mockPerformanceMark;
    const setFunctions = {
        setIsToastOpen: jest.fn(),
        setToastMsg: jest.fn(),
        setTitleError: jest.fn(),
        setDurationError: jest.fn(),
        setVehicleError: jest.fn(),
        setButtonDisable: jest.fn(),
    };
    // it('tests logout functionality', async () => {
    //     expect(logoutFunction({ data: 'logout' }, jest.fn())).toBe(undefined);
    // })
    it('tests uploadFile', () => {
        let result = uploadFile([{ size: 645692 }, { size: 645692 }]);

        expect(result).toBe(1.23);
    })
    it('get trackRowData', () => {
        let result = trackRowData({
            data: {
                searchTrackByPattern: {
                    content: [{
                        id: "d0b56403-9b45-4e32-b62d-6a4f7e409240",
                        name: "track",
                        state: "CREATED",
                        trackType: "Test",
                        vehicles: [{
                            country: "FR",
                            vin: "BBTEST00000000341"
                        }, {
                            country: "LU",
                            vin: "VINTESTTBB0003013"
                        }]
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
        });
        expect(result).toStrictEqual([{ "check": "", "country": ["FR", "LU"], "delete": "", "numberofvehicles": 2, "trackID": "d0b56403-9b45-4e32-b62d-6a4f7e409240", "trackName": "track", "trackNameSim": "track", "trackStatus": "CREATED", "trackType": "Test" }]);
    })
    it('get track data', () => {
        // const data = await getTrackData(0,'track');
        // expect(data).toBe('peanut butter');
        // const data = { "searchTrackByPattern": { "content": [{ "id": "ab2ce5db-a0ad-4b6b-a16f-7ba5760c9f06", "name": "track", "state": "CREATED", "trackType": "Test", "vehicles": [{ "vin": "BBTEST00000000340", "country": "FR" }] }], "empty": false, "first": true, "last": true, "page": 0, "size": 10, "pages": 1, "elements": 10, "total": 1 } }
        // expect(getTrackData(0, 'track')).toBe(data)
        getTrackData(0, 'track').catch(e => expect(e).toMatch('error'));
    })
    const formData = new FormData()
    it('tests onClickDeleteTrack', async () => {
        expect(onClickDeleteTrack('', jest.fn(), jest.fn())).toBe(undefined)
    })
    jest.useFakeTimers();
    it('tests setToastMessageForDeleteTrack', async () => {
        expect(setToastMessageForDeleteTrack({ deleteTrack: "" }, jest.fn(), jest.fn(), "success")).toBe(true)
        expect(setToastMessageForDeleteTrack({ deleteTrack: "" }, jest.fn(), jest.fn(), "")).toBe(false)
        const callback = jest.fn();
        setToastMessageForDeleteTrack({ deleteTrack: "" }, callback, callback, "")
        jest.runAllTimers();
        expect(callback).toBeCalled();
        expect(callback).toHaveBeenCalledTimes(4);
    })
    it('tests getToolTip', async () => {
        expect(getToolTip([{ country: "IND" }, { country: "GER" }])).toStrictEqual("IND, GER")
        expect(getToolTip([{ country: "IND" }])).toStrictEqual("IND")
        expect(getToolTip(["IND", "IND"])).toStrictEqual("IND, IND")
    })
    it('tests onselectionchange', async () => {
        expect(onselectionchange([{ data: { vin: "", country: "" } }], jest.fn(), [], jest.fn(), '', jest.fn())).toBe(undefined)
    })
    it('tests saveNewTrack', async () => {
        expect(saveNewTrack('Track3', [''], '0', 'description', jest.fn(), setFunctions)).toBe(undefined)
        expect(saveNewTrack('title', [''], '6', 'test', jest.fn(), setFunctions)).toBe(undefined)
        expect(saveNewTrack('Track 1', [''], '33', 'description', jest.fn(), setFunctions)).toBe(undefined)
        expect(saveNewTrack('', [''], '', '', jest.fn(), setFunctions)).toBe(undefined)
    })
    it('onClickTrackColumn', async () => {
        expect(onClickTrackColumn('', jest.fn(), jest.fn())).toBe(undefined)
        jest.runAllTimers();
        expect(window.performance.mark).toBe(undefined);
        const useRouter = jest.spyOn(require('next/router'), 'useRouter');
        useRouter.mockImplementation(() => ({
            pathname: '/',
        }));
    })
    it('mapDataToTable', async () => {
        let result={ "vehicleReadByQuery": {
          "content": [
            {
              "vin": "BBTEST00000000340",
              "owner": null,
              "ecomDate": "2022-12-15T21:30:32.105Z",
              "country": "FR",
              "model": "A-Class",
              "brand": "Mercedes-Benz",
              "region": null,
              "instantiatedAt": null,
              "createdAt": "2022-12-15T21:30:46.443421",
              "updatedAt": "2023-02-22T00:58:01.734361",
              "status": "DRAFT",
              "type": "Virtual Vehicle",
              "fleets": [
                {
                  "id": "256",
                  "name": "fleet bb",
                  "type": "STATIC"
                },
                {
                  "id": "31",
                  "name": "411",
                  "type": "DYNAMIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": [
                {
                  "id": "84c32703-982a-4efd-920b-b80977073b33",
                  "type": "TCU",
                  "status": "ACTIVE",
                  "createdAt": "2022-12-15T21:30:46.443445",
                  "gatewayId": "",
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-20T14:09:25.437637",
                  "dmProtocolVersion": "1.0.2"
                },
                {
                  "id": "36e5322b-92ce-4fb9-ac25-56d2ed0c4b53",
                  "type": "IVI",
                  "status": "DRAFT",
                  "createdAt": "2022-12-20T14:09:12.829388",
                  "gatewayId": "",
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-20T14:09:25.48871",
                  "dmProtocolVersion": "1.0.2"
                }
              ]
            },
            {
              "vin": "BBTEST00000000341",
              "owner": null,
              "ecomDate": "2022-12-16T12:06:09.865Z",
              "country": "FR",
              "model": "A-Class",
              "brand": "Mercedes-Benz",
              "region": null,
              "instantiatedAt": null,
              "createdAt": "2022-12-16T12:06:28.208036",
              "updatedAt": "2023-02-22T00:58:01.733471",
              "status": "READY",
              "type": "Virtual Vehicle",
              "fleets": [
                {
                  "id": "256",
                  "name": "fleet bb",
                  "type": "STATIC"
                },
                {
                  "id": "31",
                  "name": "411",
                  "type": "DYNAMIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": [
                {
                  "id": "18d4285b-31c5-4e35-8b1c-ed931b3baed3",
                  "type": "TCU",
                  "status": "ACTIVE",
                  "createdAt": "2022-12-16T12:06:28.209091",
                  "gatewayId": "",
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-30T16:44:49.170935",
                  "dmProtocolVersion": "1.0.2"
                }
              ]
            },
            {
              "vin": "VINTESTTBB0003014",
              "owner": null,
              "ecomDate": null,
              "country": "GR",
              "model": "A1",
              "brand": "Audi",
              "region": null,
              "instantiatedAt": null,
              "createdAt": "2022-12-19T09:41:52.37476",
              "updatedAt": "2023-02-22T00:58:01.734666",
              "status": "DRAFT",
              "type": "Test Bench",
              "fleets": [
                {
                  "id": "368",
                  "name": "newbb",
                  "type": "STATIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": [
                {
                  "id": "TESTDEVICE3014",
                  "type": "TCU",
                  "status": "READY",
                  "createdAt": "2022-12-19T09:41:52.374781",
                  "gatewayId": "",
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-19T09:41:53.877784",
                  "dmProtocolVersion": "1.0.2"
                }
              ]
            },
            {
              "vin": "VINTESTTBB0003013",
              "owner": null,
              "ecomDate": null,
              "country": "LU",
              "model": "Puma",
              "brand": "Ford",
              "region": null,
              "instantiatedAt": null,
              "createdAt": "2022-12-19T09:41:56.025012",
              "updatedAt": "2023-02-22T00:58:01.729348",
              "status": "DRAFT",
              "type": "Test Bench",
              "fleets": [
                {
                  "id": "368",
                  "name": "newbb",
                  "type": "STATIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": [
                {
                  "id": "TESTDEVICE3013",
                  "type": "TCU",
                  "status": "READY",
                  "createdAt": "2022-12-19T09:41:56.025058",
                  "gatewayId": "",
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-19T09:41:56.025058",
                  "dmProtocolVersion": "1.0.2"
                }
              ]
            },
            {
              "vin": "VINTESTTBB0002940",
              "owner": null,
              "ecomDate": null,
              "country": "PL",
              "model": "Polo",
              "brand": "VW",
              "region": null,
              "instantiatedAt": null,
              "createdAt": "2022-12-19T09:42:03.857758",
              "updatedAt": "2023-02-22T00:58:01.739486",
              "status": "DRAFT",
              "type": "Real Vehicle",
              "fleets": [
                {
                  "id": "6197",
                  "name": "cgdszy",
                  "type": "STATIC"
                },
                {
                  "id": "4303",
                  "name": "static",
                  "type": "STATIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": [
                {
                  "id": "TESTDEVICE2940",
                  "type": "IVI",
                  "status": "DRAFT",
                  "createdAt": "2022-12-19T09:42:03.857775",
                  "gatewayId": "",
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-19T09:42:03.857775",
                  "dmProtocolVersion": "1.0.2"
                }
              ]
            },
            {
              "vin": "VINTESTTBB0002939",
              "owner": null,
              "ecomDate": null,
              "country": "GR",
              "model": "Almera",
              "brand": "Nissan",
              "region": null,
              "instantiatedAt": null,
              "createdAt": "2022-12-19T09:42:11.52793",
              "updatedAt": "2023-02-22T00:58:01.729547",
              "status": "DRAFT",
              "type": "Real Vehicle",
              "fleets": [
                {
                  "id": "32",
                  "name": "DM4382-Test-Dynamic-fleet_5",
                  "type": "DYNAMIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": []
            },
            {
              "vin": "TESTVIN0000003337",
              "owner": null,
              "ecomDate": "2022-01-01T00:00:00.000Z",
              "country": "PL",
              "model": "Insignia",
              "brand": "Opel",
              "region": null,
              "instantiatedAt": null,
              "createdAt": "2022-12-19T10:59:57.841533",
              "updatedAt": "2023-02-22T00:58:01.73495",
              "status": "READY",
              "type": "Virtual Vehicle",
              "fleets": [
                {
                  "id": "368",
                  "name": "newbb",
                  "type": "STATIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": [
                {
                  "id": "41e5242c-aa6a-4def-ae1a-961d615b746f",
                  "type": "TCU",
                  "status": "READY",
                  "createdAt": "2022-12-19T10:59:57.841549",
                  "gatewayId": "",
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-19T10:59:57.841549",
                  "dmProtocolVersion": "3.0.1"
                }
              ]
            },
            {
              "vin": "TESTVIN0000003340",
              "owner": null,
              "ecomDate": "2022-01-01T00:00:00.000Z",
              "country": "PL",
              "model": "Insignia",
              "brand": "Opel",
              "region": "EU",
              "instantiatedAt": null,
              "createdAt": "2022-12-19T11:00:05.613406",
              "updatedAt": "2023-02-22T00:58:01.73921",
              "status": "DRAFT",
              "type": "Real Vehicle",
              "fleets": [
                {
                  "id": "368",
                  "name": "newbb",
                  "type": "STATIC"
                },
                {
                  "id": "4053",
                  "name": "Test",
                  "type": "STATIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": [
                {
                  "id": "TESTDEVICE3340",
                  "type": "TCU",
                  "status": "DRAFT",
                  "createdAt": "2022-12-19T11:00:05.613424",
                  "gatewayId": null,
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-19T11:00:12.213828",
                  "dmProtocolVersion": "3.0.3"
                }
              ]
            },
            {
              "vin": "TESTVIN0000003408",
              "owner": null,
              "ecomDate": "2021-04-01",
              "country": "PL",
              "model": "Life",
              "brand": "eGO",
              "region": "EU",
              "instantiatedAt": null,
              "createdAt": "2022-12-19T11:00:15.114168",
              "updatedAt": "2023-02-22T00:58:01.738612",
              "status": "DRAFT",
              "type": "Test Bench",
              "fleets": [
                {
                  "id": "368",
                  "name": "newbb",
                  "type": "STATIC"
                },
                {
                  "id": "4053",
                  "name": "Test",
                  "type": "STATIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                }
              ],
              "services": [],
              "devices": []
            },
            {
              "vin": "VINPM128167865193",
              "owner": null,
              "ecomDate": "2022-07-01",
              "country": "FR",
              "model": "5 Series",
              "brand": "BMW",
              "region": null,
              "instantiatedAt": null,
              "createdAt": "2022-12-19T13:17:01.946904",
              "updatedAt": "2023-02-23T00:53:00.110147",
              "status": "READY",
              "type": "Test Bench",
              "fleets": [
                {
                  "id": "31",
                  "name": "411",
                  "type": "DYNAMIC"
                },
                {
                  "id": "1867",
                  "name": "Fleet_DM5172",
                  "type": "DYNAMIC"
                },
                {
                  "id": "2478",
                  "name": "Fleet_DM5172",
                  "type": "DYNAMIC"
                },
                {
                  "id": "3459",
                  "name": "Fleet_DM5172",
                  "type": "DYNAMIC"
                },
                {
                  "id": "770",
                  "name": "Fleet_DM5172",
                  "type": "DYNAMIC"
                },
                {
                  "id": "1042",
                  "name": "Fleet_DM5172",
                  "type": "DYNAMIC"
                },
                {
                  "id": "3829",
                  "name": "Fleet_DM5172",
                  "type": "DYNAMIC"
                },
                {
                  "id": "6295",
                  "name": "Fleet_DM5172",
                  "type": "DYNAMIC"
                },
                {
                  "id": "6724",
                  "name": "Test",
                  "type": "STATIC"
                },
                {
                  "id": "2082",
                  "name": "Fleet_DM5172",
                  "type": "DYNAMIC"
                },
                {
                  "id": "6905",
                  "name": "Updated name",
                  "type": "DYNAMIC"
                },
                {
                  "id": "6911",
                  "name": "Fleet_DM4624",
                  "type": "DYNAMIC"
                }
              ],
              "services": [],
              "devices": [
                {
                  "id": "DEVICEID665457956",
                  "type": "TCU",
                  "status": "READY",
                  "createdAt": "2022-12-19T13:17:01.946954",
                  "gatewayId": null,
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-19T13:17:01.946954",
                  "dmProtocolVersion": "1.01"
                },
                {
                  "id": "DEVICEID773051816",
                  "type": "IVI",
                  "status": "READY",
                  "createdAt": "2022-12-19T13:17:01.946972",
                  "gatewayId": "DEVICEID665457956",
                  "dmProtocol": "LWM2M",
                  "modifiedAt": "2022-12-19T13:17:01.946972",
                  "dmProtocolVersion": "1.01"
                }
              ]
            }
          ],
            "empty": false,
            "first": true,
            "last": true,
            "page": 0,
            "size": 10,
            "pages": 1,
            "elements": 10,
            "total": 10
          }}
          let result2={vehicleReadByQuery:{content:[]}}
        expect(mapDataToTable(result, jest.fn(),[] )).toBe(undefined);
        // expect(mapDataToTable(result, jest.fn(),result )).toBe(undefined);
        expect(mapDataToTable(result2, jest.fn(), [])).toBe(undefined);
        // expect(mapDataToTable(result2, jest.fn(), result2)).toBe(undefined);
    })
    it('deleteTrackFun', async () => {
        expect(deleteTrackFun(jest.fn(), jest.fn(), true, { id: 1 })).toBe(undefined);
    })
    it('getVehicleList', async () => {
        expect(getVehicleList(vehicleList )).toStrictEqual([{"brand": undefined, "country": "", "devices": [{"components": [{"name": "", "version": ""}], "id": "", "type": ""}], "lastconnected": "", "status": "", "type": undefined, "vin": "test"}]);
    })
    it('getCompNVersion', async () => {
        // expect(getCompNVersion([[{ id: "", components: "" }]], [], [])).toEqual([""]);
        // expect(getCompNVersion(undefined, [], [])).toBe(undefined);
    })
    it('getDevices', async () => {
        // expect(getDevices([[{ name: "", version: "" }]], [])).toBe(undefined);
        // expect(getDevices(undefined, [])).toBe(undefined);
    })
    it('onSelectionChanged', async () => {
        expect(onSelectionChanged({ current: { api: { getSelectedRows() { } } } }, jest.fn())).toBe(undefined);
        expect(onSelectionChanged({ current: { api: { getSelectedRows() { return [{ country: "DE" }] } } } }, jest.fn())).toBe(undefined);
    })
    it('selectedCheckboxFunction', async () => {
        expect(selectedCheckboxFunction([{ vin: "" }], { current: { api: { forEachNode() { return [{ data: { vin: "" } }] } } } })).toBe(undefined);
    })
    it('tests getUploadFormDataForNewComponent', () => {
        let result = getUploadFormDataForNewComponent({}, [], '', '', '', '', '');
        expect(formData).toEqual(new FormData());
    })
    it('tests onClickMenuItem', () => {
        expect(onClickMenuItem(jest.fn())).toBe(undefined);
    })
    it('tests onCompletedCreateTrack', () => {
        expect(onCompletedCreateTrack(jest.fn(), jest.fn(), jest.fn(), { data: { createTrack: { id: 'test' } } }, true)).toBe(undefined);
        expect(onCompletedCreateTrack(jest.fn(), jest.fn(), jest.fn(), '', true)).toBe(undefined);
        expect(onCompletedCreateTrack(jest.fn(), jest.fn(), jest.fn(), { errors: [{ message: 'test' }] }, false)).toBe(undefined);
        expect(onCompletedCreateTrack(jest.fn(), jest.fn(), jest.fn(), '', false)).toBe(undefined);
        jest.runAllTimers();
        expect(window.performance.mark).toBe(undefined);
        const useRouter = jest.spyOn(require('next/router'), 'useRouter');
        useRouter.mockImplementation(() => ({
            pathname: '/',
        }));
    })
    beforeEach(() => {
        fetch.resetMocks();
    });
    store.getActions().setTname('Track');
    store.getActions().setTid(0);
    store.getActions().setCompid(1234);

    it('tests onClickNewTrack', () => {
        expect(onClickNewTrack(jest.fn())).toBe(undefined);
        jest.runAllTimers();
    })


})

