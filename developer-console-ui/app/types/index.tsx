export interface BoxToastProps {
    toastMsg: string;
}
export interface StatusTypes {
    status: string;
    type: string;
}
export interface CountryNameType {
    val: string;
}
// clear all fields in simulation 
export interface ClearAllTypes {
    setTitle: Function;
    setDescription: Function;
    setEnvironment: Function;
    setPlatform: Function;
    setSelectedscenario: Function;
    setSelectedtrack: Function;
    setScenarioType: Function;
    setHardware: Function;
    setSearchval: Function;
    setTitleError: Function;
    setSTypeError: Function;
    setTrackError: Function;
    setScenarioError: Function;
}
// vehicle list in track list
export interface GetVehicleListType {
    findTrackById: {
        description: string;
        duration: string;
        id: string;
        name: string;
        state: string;
        trackType: string;
        vehicles: [{
            brand: string;
            country: string;
            devices: [{
                components: [{
                    environmentType: string;
                    id: string;
                    name: string;
                    status: string;
                    version: string;
                }]
                createdAt: string;
                dmProtocol: string;
                dmProtocolVersion: string;
                gatewayId: string;
                id: string;
                modelType: string;
                modifiedAt: string;
                serialNumber: string;
                status: string;
                type: string;
            }];
            status: string;
            type: string;
            updatedAt: string;
            vin: string;
        }]
    }
}
// simulation data
export interface RawDataSimType {
    data: {
        simulationReadByQuery: {
            content: [{
                brands: [];
                createdBy: string;
                description: string;
                environment: string;
                hardware: string;
                id: string;
                name: string;
                noOfScenarios: number;
                noOfVehicle: number;
                platform: string;
                scenarioType: string;
                startDate: string;
                status: string;
            }];
            elements: number;
            empty: boolean;
            first: boolean;
            last: boolean;
            page: number;
            pages: number;
            size: number;
            total: number;
        }
    }
}
// track data
export interface RawDataTrackType {
    data: {
        searchTrackByPattern: {
            content: [{
                id: string;
                name: string;
                state: string;
                trackType: string;
                vehicles: [{
                    country: string;
                    vin: string;
                }]
            }];
            elements: number;
            empty: boolean;
            first: boolean;
            last: boolean;
            page: number;
            pages: number;
            size: number;
            total: number;
        }
    }
}

export interface VehicleTypes {
    brand: string,
    country: string,
    devices: [{}],
    status: string,
    type: string,
    updatedAt: string,
    vin: string,
}
export interface ComponentTypes {
    name: string,
    version: string,
}