import {Box, Table, Toast} from "@dco/sdv-ui"
import {useStoreActions, useStoreState} from "easy-peasy"
import {useEffect, useState} from "react"
import Pagination from "../../shared/paginationTable"
import BoxToast from "../../../components/layout/boxToast"
import LaunchButton from "./LaunchButton"
import {
    getReleaseData,
    numberOfReleases,
    releaseManagementRowData
} from "../../../services/functionReleaseManagement.services"
import DeleteButton from "./DeleteButton";

export function Selectedscenario() {
    return useStoreState((state: any) => state.selectedscenario)
}

export function Searchval() {
    return useStoreState((state: any) => state.searchval);
}

const ReleaseManagementList = ({path}: any) => {
    const setCount = useStoreActions((actions: any) => actions.setCount)
    const [isToastOpenScenario, setToastOpenScenario] = useState(false)
    const [successMsgScenario, setSuccessMsgScenario] = useState('')
    const [currentPage, setCurrentPage] = useState(1)
    const searchval = Searchval();
    const [deleteRelease, setDeleteRelease] = useState<any>(false)
    const [pageData, setPageData] = useState({
        rowData: [],
        isLoading: false,
        totalPages: 0,
        totalReleases: 0,
    })

    function onDeleteRelease() {
        setDeleteRelease(!deleteRelease)
    }

    useEffect(() => {
        setPageData((prevState) => ({
            ...prevState,
            rowData: [],
            isLoading: true,
        }))
        getReleaseData().then((info) => {
            setPageData({
                isLoading: false,
                rowData: releaseManagementRowData(info),
                totalPages: 1,
                totalReleases: numberOfReleases(info),
            })
            setCount(numberOfReleases(info));
        })
    }, [deleteRelease])
    const columns = [
        {
            Header: 'Release ID',
            accessor: 'releaseId',
        },
        {
            Header: 'Release Date',
            accessor: 'releaseDate',
        },
        {
            Header: 'Release Status',
            accessor: 'releaseStatus',
            formatter: (value: any, cell: any) => {
                if (value == "TESTING") {
                    return <div
                        style={{display: 'flex', alignItems: 'center', justifyContent: 'center', width: '40%'}}>🟨</div>
                } else if (value == "FAILED") {
                    return <div
                        style={{display: 'flex', alignItems: 'center', justifyContent: 'center', width: '40%'}}>🟥</div>
                } else if (value == "READY_FOR_RELEASE") {
                    return <div
                        style={{display: 'flex', alignItems: 'center', justifyContent: 'center', width: '40%'}}>🟩</div>
                }
            }
        },
        {
            Header: '',
            accessor: 'menu',
            formatter: (value: any, cell: any) => {
                return (<LaunchButton releaseID={cell.row.values.releaseId}></LaunchButton>)
            }
        },
        {
            Header: '',
            accessor: 'delete',
            formatter: (value: any, cell: any) => {
                return (<DeleteButton releaseID={cell.row.values.releaseId} onclose={onDeleteRelease}></DeleteButton>)
            }
        },
    ]
    return (
        <>
            <Table data-testid="table" columns={columns}
                   data={pageData.rowData} initialSortBy={[
                {
                    id: 'lastUpdated',
                    desc: true
                }
            ]}
                   noDataMessage='No Rows To Show'
            />
            <Box align='right' padding='small'>
                <Pagination totalRows={pageData.totalReleases} pageChangeHandler={setCurrentPage} rowsPerPage={10}/>
            </Box>
            {/* Toast message for deleting a scenario */}
            {<Toast show={isToastOpenScenario}>
                <div>
                    <BoxToast toastMsg={successMsgScenario}/>
                </div>
            </Toast>
            }
        </>
    )
}
export default ReleaseManagementList