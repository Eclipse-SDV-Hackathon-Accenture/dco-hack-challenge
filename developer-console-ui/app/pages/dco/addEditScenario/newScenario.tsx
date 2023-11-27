import { Button, FileInput, Flex, Headline, Icon, Input, Popup, Select, Spacer, StatusMessage, Title, Toast, Value } from "@dco/sdv-ui";
import { useRouter } from "next/router";
import { useState } from "react";
import BoxToast from "../../../components/layout/boxToast";
import { getFileSIzeInService, handleNewScenarioSubmitInService, handleUpdateScenarioSubmitInService } from "../../../services/functionScenario.services";
import { avoidSplChars } from "../../../services/functionShared";
import { uploadFile } from "../../../services/functionTrack.service";

// called on click of new scenario from scenario list page
export function NewScenario({ show, onClose, path, cellData, setToastOpenScenario, setSuccessMsgScenario }: any) {
    const [name, setName] = useState<any>(cellData?.scenario);
    const [description, setDescription] = useState<any>(cellData?.description);
    const [type, setType] = useState<any>(cellData?.type);
    const [selectedUploadFile, setUploadFile] = useState();
    const [fileSizeError, setFileSizeError] = useState<boolean>(false);
    const [fileNameError, setFileNameError] = useState<boolean>(false);
    const maxFileSizeInMB = 1000000;
    const minFileSizeInMB = 0;
    const typeList = ['MQTT', 'CAN'];
    const [isToastOpen, setToastOpen] = useState(false);
    let sid = cellData?.sid;
    const router = useRouter();
    const [toastMsg, setToastMsg] = useState<string>('')
    const [nameError, setNameError] = useState<boolean>(false)
    const [typeError, setTypeError] = useState<boolean>(false)
    const [fileError, setFileError] = useState<boolean>(false)

    return <>
        <Popup invert={true} dim show={show} onClose={onClose} style={{ zIndex: 100 }}>
            <Headline data-testid="newComponentPackageHeadline">
                {path == 'create' ? 'New Scenario' : 'Update Scenario'}</Headline>
            <Spacer />
            <Flex gutters="default" align="left" justify="space-between">
                <Flex.Item>
                    <Input labelPosition="floating" label='Name *' type="text" onKeyPress={avoidSplChars}
                        placeholder="Name *" onValueChange={(x: any) => { setName(x) }} value={name} />
                    <Spacer space={1} />
                    {nameError && !name && <StatusMessage variant="error">
                        Please add a name
                    </StatusMessage>}
                </Flex.Item>
                <Flex.Item>
                    <Select label={"Type *"} labelPosition="floating" placeholder={"Type *"}
                        value={type} searchable={true} multiple={false} options={typeList}
                        onChange={(e: string) => {
                            setType(e)
                        }
                        } noResultsMessage="Type not found"
                    />
                    <Spacer space={1} />
                    {typeError && !type && <StatusMessage variant="error">
                        Please select a type
                    </StatusMessage>}
                </Flex.Item>
            </Flex>
            <Flex>
                <Flex.Item >
                    <Input labelPosition="floating" label='Description'
                        placeholder="Description" onValueChange={(x: any) => {
                            setDescription(x)
                        }} value={description} />
                </Flex.Item>
            </Flex>
            <Flex align="right">
                <Flex.Item align="right">
                    <Spacer space={2} />
                    <Title style={{ 'fontSize': 'large', 'opacity': '.7', 'right': '2em' }}>{path == 'create' ? 'File *' : 'File'}</Title>
                    <Spacer />
                    {path == 'create' ? <FileInput data-testid="uploadFile" accept={".txt, .odx"} onChange={(e: any) => e.target.value ? getFileSIzeInService(e, setUploadFile, uploadFile, maxFileSizeInMB, minFileSizeInMB, setFileSizeError, setFileNameError) : setUploadFile(undefined)} />
                        :
                        <p>
                            {!selectedUploadFile && <Value><Icon name={"archive"} />{cellData?.filename}</Value>}
                            <Spacer />
                            <FileInput message='Upload a new file' data-testid="uploadFile" accept={".txt, .odx"} onChange={(e: any) => e.target.value ? getFileSIzeInService(e, setUploadFile, uploadFile, maxFileSizeInMB, minFileSizeInMB, setFileSizeError, setFileNameError) : setUploadFile(undefined)} />
                            {/* </Box> */}
                        </p>
                    }
                    <Spacer space={1} />
                    {fileSizeError ?
                        <StatusMessage variant="error">
                            Please upload file more than {minFileSizeInMB} bytes and upto {maxFileSizeInMB} MB.
                        </StatusMessage> : <></>}
                    {fileNameError ?
                        <><StatusMessage variant="error">
                            File name is invalid, remove spaces or
                        </StatusMessage>
                            <StatusMessage variant="error" noIcon>
                                any special character (only accepted charcters - , _ )
                            </StatusMessage>
                        </>
                        : <></>}
                    {fileError && !selectedUploadFile && <StatusMessage variant="error">
                        Please upload a file
                    </StatusMessage>
                    }
                </Flex.Item>
            </Flex>
            <Spacer space={2} />
            <Flex>
                <Flex.Item>
                    {path == 'create' ? <Button width="full" data-testid="btn1" onClick={() => handleNewScenarioSubmitInService({ name, type, description, selectedUploadFile } as const, 'abc@t-systems.com', { setName, setType, setDescription, setUploadFile, setFileSizeError, setFileNameError, setToastMsg, setNameError, setTypeError, setFileError } as const, setToastOpen, onClose)}>Create Scenario</Button> :
                        <Button width="full" data-testid="btn2" onClick={() => handleUpdateScenarioSubmitInService({ sid, name, type, description, selectedUploadFile } as const, 'abc@t-systems.com', { setName, setType, setDescription, setUploadFile, setFileSizeError, setFileNameError, setSuccessMsgScenario, setNameError, setTypeError, setFileError } as const, setToastOpenScenario, onClose, router)}>Update Scenario</Button>}
                </Flex.Item>
            </Flex>
            <Spacer space={0.5} />
        </Popup>
        {/* Toast message for creating new or updationg old scenario */}
        {<Toast show={isToastOpen}>
            <div>
                <BoxToast toastMsg={toastMsg} />
            </div>
        </Toast>
        }
    </>
}

export default NewScenario;