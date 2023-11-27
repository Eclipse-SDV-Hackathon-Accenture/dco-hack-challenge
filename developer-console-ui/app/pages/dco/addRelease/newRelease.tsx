import {
    Button,
    Flex,
    FlexItem,
    Headline,
    Input,
    Popup,
    Select,
    Spacer,
    Toast,
} from "@dco/sdv-ui";
import {useState} from "react";
import BoxToast from "../../../components/layout/boxToast";
import {avoidSplChars} from "../../../services/functionShared";
import {handleNewReleaseSubmitInService} from "../../../services/functionReleaseManagement.services";

export function NewRelease({show, onClose, path, cellData, setToastOpenScenario, setSuccessMsgScenario}: any) {
    const [releaseId, setReleaseId] = useState<string>("");
    const [hardwareChange, setHardwareChange] = useState<boolean>(false);
    const [brands, setBrands] = useState<string>("");
    const [countries, setCountries] = useState<string>("");
    const [metaTrack, setMetaTrack] = useState<string>("");
    const [models, setModels] = useState<string>("");
    const [ecuData, setEcuData] = useState([
        {
            ecu: '',
            hardwareVersion: '',
            componentId: '',
            componentName: '',
            componentVersion: '',
            status: '',
            lastChange: '',
            actualBatteryCapacity: '',
        },
    ]);
    const [releaseDate, setReleaseDate] = useState<string>("30-Nov-2023");
    const [releaseStatus, setReleaseStatus] = useState<string>("TESTING");
    const [type, setType] = useState<any>(cellData?.type);
    const typeList = ["Not Allowed", "Allowed"];
    const [isToastOpen, setToastOpen] = useState(false);
    let sid = cellData?.sid;
    const [toastMsg, setToastMsg] = useState<string>('')

    const addNewEcu = () => {
        setEcuData((prevData) => [
            ...prevData,
            {
                ecu: '',
                hardwareVersion: '',
                componentId: '',
                componentName: '',
                componentVersion: '',
                status: '',
                lastChange: '',
                actualBatteryCapacity: '',
            },
        ]);
    };

    const updateEcuData = (index: number, field: string, value: any) => {
        setEcuData((prevData) => {
            const newData = [...prevData];
            // @ts-ignore
            newData[index][field] = value;
            return newData;
        });
    };

    return <>
        <Popup invert={true} dim show={show} onClose={onClose} style={{zIndex: 100}}>
            <Headline data-testid="newComponentPackageHeadline">
                {path == 'create' ? 'New Release' : 'Update Release'}</Headline>
            <Spacer/>
            <Flex gutters="default" align="left" justify="space-between">
                <Flex.Item>
                    <Input labelPosition="floating"
                           label='Release ID *'
                           type="text" onKeyPress={avoidSplChars}
                           placeholder="Release ID *"
                           onValueChange={(x: any) => {
                               setReleaseId(x)
                           }} value={releaseId}
                    />
                    <Spacer space={1}/>
                </Flex.Item>
                <Flex.Item>
                    <Select label={"Hardware Change  *"}
                            labelPosition="floating"
                            placeholder={"Hardware Change *"}
                            value={type}
                            searchable={true}
                            multiple={false}
                            options={typeList}
                            onChange={(e: string) => {
                                setType(e)
                            }
                            } noResultsMessage="Type not found"
                    />
                    <Spacer space={1}/>
                </Flex.Item>
            </Flex>
            <Flex>
                <Flex.Item>
                    <Input labelPosition="floating"
                           label='Brands  *'
                           placeholder="Brands  *"
                           onValueChange={(x: any) => {
                               setBrands(x)
                           }} value={brands}/>
                </Flex.Item>
            </Flex>
            <Flex>
                <Flex.Item>
                    <Input labelPosition="floating"
                           label='Countries  *'
                           placeholder="Countries  *"
                           onValueChange={(x: any) => {
                               setCountries(x)
                           }} value={countries}/>
                </Flex.Item>
            </Flex>
            <Flex.Item>
                <Input labelPosition="floating"
                       label='Meta Track  *'
                       placeholder="Meta Track  *"
                       onValueChange={(x: any) => {
                           setMetaTrack(x)
                       }} value={metaTrack}/>
            </Flex.Item>
            <Flex.Item>
                <Input labelPosition="floating"
                       label='Models  *'
                       placeholder="Models  *"
                       onValueChange={(x: any) => {
                           setModels(x)
                       }} value={models}/>
            </Flex.Item>
            <Spacer space={2}/>
            <FlexItem>
                {ecuData.map((ecu, index) => (
                    <div key={index}>
                        <Spacer space={1}/>
                        <h4>{`Ecu ${index + 1} Data`}</h4>
                        <FlexItem>
                            <Input
                                labelPosition="floating"
                                label='Ecu'
                                placeholder="Ecu  *"
                                value={ecu.ecu}
                                onValueChange={(x: any) => updateEcuData(index, 'ecu', x)}
                            />
                        </FlexItem>
                        <FlexItem>
                            <Input
                                labelPosition="floating"
                                label='Hardware Version  *'
                                placeholder="Hardware Version  *"
                                value={ecu.hardwareVersion}
                                onValueChange={(x: any) => updateEcuData(index, 'hardwareVersion', x)}
                            />
                        </FlexItem>
                        <FlexItem>
                            <Input
                                labelPosition="floating"
                                label='Component ID * (Must be UUID)'
                                placeholder="Component ID *"
                                value={ecu.componentId}
                                onValueChange={(x: any) => updateEcuData(index, 'componentId', x)}
                            />
                        </FlexItem>
                        <FlexItem>
                            <Input
                                labelPosition="floating"
                                label='Component Name  *'
                                placeholder="Component Name  *"
                                value={ecu.componentName}
                                onValueChange={(x: any) => updateEcuData(index, 'componentName', x)}
                            />
                        </FlexItem>
                        <FlexItem>
                            <Input
                                labelPosition="floating"
                                label='Component Version  *'
                                placeholder="Component Version  *"
                                value={ecu.componentVersion}
                                onValueChange={(x: any) => updateEcuData(index, 'componentVersion', x)}
                            />
                        </FlexItem>
                        <FlexItem>
                            <Input
                                labelPosition="floating"
                                label='Status  *'
                                placeholder="Status  *"
                                value={ecu.status}
                                onValueChange={(x: any) => updateEcuData(index, 'status', x)}
                            />
                        </FlexItem>
                        <FlexItem>
                            <Input
                                labelPosition="floating"
                                label='Last Change  *'
                                placeholder="Last Change  *"
                                value={ecu.lastChange}
                                onValueChange={(x: any) => updateEcuData(index, 'lastChange', x)}
                            />
                        </FlexItem>
                        <FlexItem>
                            <Input
                                labelPosition="floating"
                                label='Actual Battery Capacity  *'
                                placeholder="Actual Battery Capacity  *"
                                value={ecu.actualBatteryCapacity}
                                onValueChange={(x: any) => updateEcuData(index, 'actualBatteryCapacity', x)}
                            />
                        </FlexItem>
                    </div>
                ))}
            </FlexItem>
            <Button type="button" onClick={addNewEcu}>
                Add ECU
            </Button>
            <Spacer space={2}/>
            <Flex.Item>
                <Input labelPosition="floating" label='Release Date  *'
                       placeholder="Release Date  *" onValueChange={(x: any) => {
                    setReleaseDate(x)
                }} value={releaseDate}/>
            </Flex.Item>
            <Flex.Item>
                <Input labelPosition="floating" label='Release Status  *'
                       placeholder="Release Status  *" onValueChange={(x: any) => {
                    x === "READY_FOR_RELEASE" ? setReleaseStatus("READY_FOR_RELEASE") : setReleaseStatus("TESTING");
                }} value={releaseStatus}/>
            </Flex.Item>
            <Spacer space={2}/>
            <Flex>
                <Flex.Item>
                    <Button width="full" data-testid="btn1"
                            onClick={() => handleNewReleaseSubmitInService(releaseId, type, brands, countries, metaTrack, models, ecuData, releaseDate, releaseStatus, onClose)}>Create
                        Release</Button>
                </Flex.Item>
            </Flex>
            <Spacer space={0.5}/>
        </Popup>

        {<Toast show={isToastOpen}>
            <div>
                <BoxToast toastMsg={toastMsg}/>
            </div>
        </Toast>
        }
    </>
}

export default NewRelease;