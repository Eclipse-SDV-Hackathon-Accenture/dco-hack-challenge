import { BackButton, Box, Button, Flex, FormRow, GridTable, Headline, Input, Spacer, StatusMessage, Toast } from "@dco/sdv-ui";
import { useRouter } from "next/router";
import { useEffect, useRef, useState } from "react";
import Layout from "../../../components/layout/layout";
import { useLazyQuery, useMutation } from "@apollo/client";
import { CREATE_TRACK, VEHICLE_LIST } from "../../../services/queries";
import { mapDataToTable, onCompletedCreateTrack, onselectionchange, onSelectionChanged, saveNewTrack, selectedCheckboxFunction } from "../../../services/functionTrack.service";
import BoxToast from "../../../components/layout/boxToast";
import { AgGridReact } from 'ag-grid-react';
import Status from "../../shared/status";
import { avoidSplChars, invert, onLoadMore } from "../../../services/functionShared";

// on click of New track used to add a track
const AddTrack = (args: any) => {
    const router = useRouter();
    const [title, setTitle] = useState('');
    const [titleError, setTitleError] = useState(false);
    const [buttonDisable, setButtonDisable] = useState(false);
    const [vehiclerror, setVehicleError] = useState(false);
    const [isToastOpen, setIsToastOpen] = useState(false);
    const [toastMsg, setToastMsg] = useState<string>('')
    const [durationError, setDurationError] = useState(false);
    const [description, setDescription] = useState('');
    const [duration, setDuration] = useState('7');
    const [datas, setDatas] = useState<any>([]);
    const [pageSize, setPageSize] = useState<number>(10);
    const [page, setPage] = useState<number>(1);
    const gridRef = useRef<AgGridReact>(null);
    let [arrVehicle, setArrVehicle] = useState<any>([]);
 
    const [createTrack] = useMutation(CREATE_TRACK, {
        onCompleted(value) {
            onCompletedCreateTrack(setButtonDisable, setIsToastOpen, setToastMsg, value, true)
        },
        onError(error) {
            onCompletedCreateTrack(setButtonDisable, setIsToastOpen, setToastMsg, error, false)
        }
    });
    let vehicles: { vin: any; country: any; }[] = [];
    const [getVehicle, { data }] = useLazyQuery(VEHICLE_LIST, {
        variables: { search: null, query: null, page: page, size: pageSize, sort: null },
    });
    const [onSelectedRows, setonSelectedRows] = useState<any>([]);
    useEffect(() => {
        getVehicle()
    }, [])
    useEffect(() => {
        mapDataToTable(data, setDatas, datas)
    }, [data])
    const columns = [
        {
            checkboxSelection: true,
            headerName: 'VIN',
            field: 'vin',
        },
        {
            headerName: 'Type',
            field: 'type',
        },
        {
            headerName: 'Last connected',
            field: 'lastconnected',
        },
        {
            headerName: 'Country',
            field: 'country',
        },
        {
            headerName: 'Brand',
            field: 'brand'
        },
        {
            headerName: 'Model',
            field: 'model'
        },
        {
            headerName: 'Staus',
            field: 'status',
        }
    ]
    useEffect(() => {
        selectedCheckboxFunction(onSelectedRows, gridRef)
    }, [datas]);
    function checkLoadmore() {
        if (data?.vehicleReadByQuery.pages != page) {
            return 'Load more';
        }
        return data?.vehicleReadByQuery.pages == page ? 'No more data' : 'Loading';
    }
    return (<Layout>
        <Box fullHeight padding="none" invert={invert()} variant="body">
            <Flex fullHeight>
                <Flex.Item flex={5}>
                    <Box padding="none" variant="body" fullHeight>
                        <Flex column fullHeight>
                            <Flex.Item autoSize>
                                <Flex column>
                                    <Flex.Item >
                                        {/* HEADER */}
                                        <Flex gutters="small" valign="bottom">
                                            {/* HEADLINE */}
                                            <Flex.Item flex={1}>
                                                <Box padding="sidebar">
                                                    <Flex gutters="large">
                                                        <Flex.Item>
                                                            <BackButton data-testid="backButtondata" onClick={() => router.back()} />
                                                            <Headline level={1}>New Track</Headline>
                                                        </Flex.Item>
                                                    </Flex>
                                                </Box>
                                            </Flex.Item>
                                        </Flex>
                                    </Flex.Item>
                                </Flex>
                            </Flex.Item>
                            {/* MAIN CONTENT */}
                            <Flex.Item>
                                <Flex fullHeight>
                                    <Flex.Item>
                                        <Box padding="sidebar" fullHeight escapePaddingBottom variant='high' transparency="high" >
                                            <Flex gutters="large">
                                                <Flex.Item autoSize>
                                                    <Headline level={3}>{datas?.length} Potential vehicles</Headline>
                                                </Flex.Item>
                                                <Flex.Item>
                                                    <Headline level={3}>{arrVehicle?.length} Selected</Headline>
                                                </Flex.Item>
                                            </Flex>
                                            <Flex>
                                                <Flex.Item>
                                                    <Box escapePaddingLeft escapePaddingRight escapePaddingBottom>
                                                        {(arrVehicle?.length == 0 && vehiclerror) && <StatusMessage variant="error">
                                                            Please select at least one vehicle
                                                        </StatusMessage>}
                                                        <GridTable rowSelection={"multiple"} rowMultiSelectWithClick={true}
                                                            cellRendererPostProcessor={(preRendered: any, cell: any) => {
                                                                return <>{cell?.colDef.field === 'status' && <Status status={cell.data.status} type={'VD'}></Status> || preRendered}
                                                                </>
                                                            }}
                                                            {...args} ref={gridRef} columnDefs={columns} rowData={datas}
                                                            onSelectionChanged={(e) => { onselectionchange(e.api.getSelectedNodes(), setArrVehicle, vehicles, onSelectionChanged, gridRef, setonSelectedRows) }}
                                                        ></ GridTable>
                                                        {datas?.length > 10 && (
                                                            <Box align='center' padding='small'>
                                                                <Button onClick={() => onLoadMore(setPageSize, setPage, getVehicle, pageSize, page)} disabled={data?.vehicleReadByQuery.pages == page || data == undefined} size='small' variant='naked'>
                                                                    {checkLoadmore()}
                                                                </Button>
                                                            </Box>
                                                        )}
                                                    </Box>
                                                </Flex.Item>
                                            </Flex>
                                        </Box>
                                    </Flex.Item>
                                </Flex>
                            </Flex.Item>
                        </Flex>
                    </Box>
                </Flex.Item>
                <Flex.Item flex={2}>
                    <Box padding="large" variant="high" fullHeight>
                        <Headline level={1}>Track Config</Headline>
                        <Spacer space={1} />
                        <FormRow>
                            <Input label="Track Title *" type="text" onKeyPress={avoidSplChars} labelPosition="floating" value={title}
                                setValue={setTitle} />
                        </FormRow>
                        <Spacer space={1} />
                        {(!title && titleError) && <StatusMessage variant="error">
                            Please add a title
                        </StatusMessage>}
                        <FormRow>
                            <Input label="Description" labelPosition="floating" value={description}
                                setValue={setDescription} />
                        </FormRow>
                        <Spacer space={1} />
                        <FormRow>
                            <Input label="Duration *" labelPosition="floating" value={duration} type="number"
                                setValue={setDuration} accessoryText="days" withAccessory />
                        </FormRow>
                        <Spacer space="05" />
                        {(!duration && durationError) && <StatusMessage variant="error">
                            Please define a duration
                        </StatusMessage>}
                        <Spacer space={10}></Spacer>
                        <Button width="wide" data-testid="saveButton" onClick={() => saveNewTrack(title, arrVehicle, duration, description, createTrack, { setIsToastOpen, setToastMsg, setTitleError, setDurationError, setVehicleError, setButtonDisable } as const)} disabled={buttonDisable}>Save Track</Button>
                    </Box>
                </Flex.Item>
            </Flex>
        </Box>
        {/* Toast message for creating new track */}
        {<Toast show={isToastOpen}>
            <div>
                <BoxToast toastMsg={toastMsg} />
            </div>
        </Toast>}
    </Layout >
    )
}
export default AddTrack;
