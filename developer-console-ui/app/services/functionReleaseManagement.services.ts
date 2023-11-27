interface EcuData {
    ecu: string;
    hardwareVersion: string;
    componentId: string;
    componentName: string;
    componentVersion: string;
    status: string;
    lastChange: string;
    actualBatteryCapacity: string;
}

interface FunctionData {
    name: string;
    ecuDatas: EcuData[];
}

interface ReleaseData {
    releaseId: string;
    metaTrack: string;
    isHardwareChangesAllowed: boolean;
    brands: string[];
    models: string[];
    countries: string[];
    functions: FunctionData[];
    releaseDate: string;
    createdDate: string;
    releaseStatus: string;
}

interface SUMOdata {
    actualBatteryCapacity: string;
}

interface EcuSUMOData {
    ecu: string;
    hardwareVersion: string;
    componentId: string;
    componentName: string;
    componentVersion: string;
    status: string;
    lastChange: string;
    data: SUMOdata;
}

interface FunctionSUMOData {
    name: string;
    ecuDatas: EcuSUMOData[];
}

interface ReleaseSUMOData {
    releaseId: string;
    metaTrack: string;
    isHardwareChangesAllowed: boolean;
    brands: string[];
    models: string[];
    countries: string[];
    functions: FunctionSUMOData[];
    releaseDate: string;
    createdDate: string;
    releaseStatus: string;
}

function convertReleaseDataToSUMOData(input: ReleaseData): ReleaseSUMOData {
    const convertEcuData = (ecuData: EcuData): EcuSUMOData => ({
        ecu: ecuData.ecu,
        hardwareVersion: ecuData.hardwareVersion,
        componentId: ecuData.componentId,
        componentName: ecuData.componentName,
        componentVersion: ecuData.componentVersion,
        status: ecuData.status,
        lastChange: ecuData.lastChange,
        data: {
            actualBatteryCapacity: ecuData.actualBatteryCapacity,
        },
    });

    const convertFunctionData = (functionData: FunctionData): FunctionSUMOData => ({
        name: functionData.name,
        ecuDatas: functionData.ecuDatas.map(convertEcuData),
    });

    return {
        releaseId: input.releaseId,
        metaTrack: input.metaTrack,
        isHardwareChangesAllowed: input.isHardwareChangesAllowed,
        brands: input.brands,
        models: input.models,
        countries: input.countries,
        functions: input.functions.map(convertFunctionData),
        releaseDate: input.releaseDate,
        createdDate: input.createdDate,
        releaseStatus: input.releaseStatus,
    };
}

export const getReleaseData = async () => {
    const token = localStorage.getItem('token');
    return fetch("http://localhost:8083/api/release/listRelease", {
        method: 'GET',
        headers: {
            'content-type': 'application/json',
            'Authorization': token ? `Basic ${token}` : "",
        },
    })
        .then((res) => res.json())
        .then((result) => result)
        .catch((error) => {
            console.log('Error fetching data:::', error.message)
        })
}

export const releaseManagementRowData = (rawData: any) =>
    rawData?.map((item: any) => {
        return {
            check: '',
            releaseId: item.releaseId,
            releaseDate: item.releaseDate,
            releaseStatus: item.releaseStatus,
            menu: '', delete: '',
        }
    })

export const numberOfReleases = (rawData: any) => {
    return rawData?.length
}

export const deleteReleaseData = async (releaseId: any) => {
    const token = localStorage.getItem('token');
    return fetch("http://localhost:8083/api/release/" + releaseId, {
        method: 'DELETE',
        headers: {
            'content-type': 'application/json',
            'Authorization': token ? `Basic ${token}` : "",
        },
    })
        .then((res) => res.json())
        .then((result) => result)
        .catch((error) => {
            console.log('Error fetching data:::', error.message)
        })
}
export const createReleaseData = async (newRelease: any) => {

    const data: ReleaseData = newRelease;
    const token = localStorage.getItem('token');
    return fetch("http://localhost:8083/api/release/create", {
        method: 'POST',
        headers: {
            'content-type': 'application/json',
            'Authorization': token ? `Basic ${token}` : "",
        },
        body: JSON.stringify(data),
    })
        .then((res) => res.json())
        .then((result) => result)
        .catch((error) => {
            console.log('Error fetching data:::', error.message)
        })
}

const getReleaseById = async (releaseId: any): Promise<ReleaseData> => {
    const token = localStorage.getItem('token');
    return fetch("http://localhost:8083/api/release/" + releaseId, {
        method: 'GET',
        headers: {
            'content-type': 'application/json',
            'Authorization': token ? `Basic ${token}` : "",
        },
    })
        .then((res) => res.json())
        .then((result) => result)
        .catch((error) => {
            console.log('Error fetching data:::', error.message)
        })
}

const runSimulation = async (release: ReleaseSUMOData): Promise<boolean> => {
    const token = localStorage.getItem('token');
    return fetch("http://localhost:8086/api/run-simulation-with-release-info", {
        method: 'POST',
        headers: {
            'Access-Control-Allow-Origin': '*',
            // 'Authorization': token ? `Basic ${token}` : "",
            'content-type': 'application/json',
            'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
            'Access-Control-Allow-Headers': 'Origin, Content-Type',
            'Access-Control-Allow-Credentials': 'false',
        },
        body: JSON.stringify(release),
    })
        .then((res) => res.json())
        .then((result) => result)
        .catch((error) => {
            console.log('Error fetching data:::', error.message)
        })
}
export const launchReleaseData = async (releaseId: any) => {
    const id: ReleaseData = await getReleaseById(releaseId);
    const convertedId: ReleaseSUMOData = convertReleaseDataToSUMOData(id);
    const res: boolean = await runSimulation(convertedId) as boolean;
    return res;
}

export const handleNewReleaseSubmitInService = async (
    releaseId: string,
    hardwareChangesAllowed: string,
    brands: string,
    countries: string,
    metaTrack: string,
    models: string,
    ecuDatas: Array<{
        ecu: string,
        hardwareVersion: string,
        componentId: string,
        componentName: string,
        componentVersion: string,
        status: string,
        lastChange: string,
        actualBatteryCapacity: string,
    }>,
    releaseDate: string,
    releaseStatus: string,
    onClose: any
) => {
    const isHardwareChangesAllowed = hardwareChangesAllowed === 'Allowed' ? true : false;
    const name = "ecu";
    const newRelease = {
        releaseId,
        metaTrack,
        isHardwareChangesAllowed,
        brands: [brands],
        countries: [countries],
        models: [models],
        functions: [{name, ecuDatas}],
        releaseDate,
        releaseStatus,
    };
    try {
        await createReleaseData(newRelease);
        // refresh the page
        window.location.reload();
        onClose();
    } catch (error) {
        console.error('Error creating release');
    }
}

// ---------------------------- Mock Data ----------------------------

const mockReleaseData: ReleaseData = {
    releaseId: 'string123',
    metaTrack: 'string',
    isHardwareChangesAllowed: true,
    brands: ['string'],
    models: ['string'],
    countries: ['string'],
    functions: [
        {
            name: 'string',
            ecuDatas: [
                {
                    ecu: 'string',
                    hardwareVersion: 'string',
                    componentId: '3fa85f64-5717-4562-b3fc-2c963f66afa6',
                    componentName: 'string',
                    componentVersion: 'string',
                    status: 'string',
                    lastChange: 'string',
                    actualBatteryCapacity: 'string',
                },
            ],
        },
    ],
    releaseDate: 'dd-mm-yyyy',
    createdDate: 'dd-mm-yyyy',
    releaseStatus: 'TESTING',
};

export const mockGetReleaseData = async () => {
    return new Promise<ReleaseData[]>((resolve) => {
        // Simulating asynchronous behavior (e.g., API fetch)
        setTimeout(() => {
            resolve([mockReleaseData, mockReleaseData, mockReleaseData]);
        }, 500);
    });
};

// ---------------------------- Mock Data End ----------------------------