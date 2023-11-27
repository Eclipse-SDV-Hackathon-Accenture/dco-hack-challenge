import { act, fireEvent, render, screen } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { store } from '../../services/store.service';
import { CREATE_TRACK, LIST_TRACKS, VEHICLE_LIST } from '../../services/queries';
import { MockedProvider } from '@apollo/react-testing';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import BoxToast from '../../components/layout/boxToast';
import AddTrack from '../../pages/dco/addTrack';
import TracksMain from '../../pages/dco/tracksMain';
import { gql } from '@apollo/client';
import { BoxToastProps } from '../../types';
describe('Adding new tracks', () => {
  const mockPerformanceMark = jest.fn();
  window.performance.mark = mockPerformanceMark;
  // jest.useFakeTimers();
  beforeEach(() => {
    fetch.resetMocks();
  });
  const toastMsg= "Track has been created successfuly"
  
  // for getting vehicles api list
  const inputVal = [{
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
  }]
  const mockListVehicles = [{
    request: {
      query: VEHICLE_LIST,
      variables: { "search": null, "query": null, "page": 1, "size": 10, "sort": null }
    },
    result: { data: { vehicleReadByQuery: inputVal } },
  }]

  // on click of save button
  const mockList = [{
    request: {
      query: CREATE_TRACK,
      variables: { id: '' }
    },
    result: { data: { createTrack: '' } },
  }]

  // on click of back button
  const inputValTracksMain = {
    content: {
      id: '',
      name: '',
      state: '',
      trackType: '',
      vehicles: {
        vin: '',
        country: '',
      }
    },
    empty: '',
    first: '',
    last: '',
    page: '',
    size: '',
    pages: '',
    elements: '',
    total: '',

  }
  const mockListTracksMain = [{
    request: {
      query: gql(LIST_TRACKS),
      variables: { trackPattern: '', page: '', size: '' }
    },
    result: { data: { searchTrackByPattern: inputValTracksMain } },
  }]

  test('add track page render', async () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/addTrack">
          <MockedProvider mocks={mockList} addTypename={false}>
            <AddTrack />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    await act(async () => {
      await new Promise((resolve) => setTimeout(resolve, 0));
    });
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/addTrack">
          <MockedProvider mocks={mockListVehicles} addTypename={false}>
            <AddTrack />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    await act(async () => {
      await new Promise((resolve) => setTimeout(resolve, 0));
    });
    store.getActions().setInvert(true);
    store.getActions().setPage(1);
  });

  test('on click of back button', async () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/addTrack">
          <MockedProvider mocks={mockList} addTypename={false}>
            <AddTrack />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    const checkBtn = screen.getByTestId("backButtondata");
    fireEvent.click(checkBtn);
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/tracksMain">
          <MockedProvider mocks={mockListTracksMain} addTypename={false}>
            <TracksMain />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    store.getActions().setInvert(true);
  });

  test('on click of save button', () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/addTrack">
          <MockedProvider mocks={mockList} addTypename={false}>
            <AddTrack />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    const checkBtn = screen.getByTestId("saveButton");
    fireEvent.click(checkBtn);
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/tracksMain">
          <MockedProvider mocks={mockListTracksMain} addTypename={false}>
            <TracksMain />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <BoxToast toastMsg={toastMsg}></BoxToast>
      </StoreProvider>
    )
    store.getActions().setInvert(true);
  })

}) 
