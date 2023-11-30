import { Box, Button, Table, Toast } from "@dco/sdv-ui"
import { useStoreActions, useStoreState } from "easy-peasy"
import { useEffect, useState } from "react"
import Pagination from "../../shared/paginationTable"
import BoxToast from "../../../components/layout/boxToast"
import {
  getReleaseData,
  numberOfReleases,
  releaseManagementRowData
} from "../../../services/functionReleaseManagement.services"
import GraphModal from "./graphModal"


export function Selectedscenario() {
  return useStoreState((state: any) => state.selectedscenario)
}


const MonitoringList = ({ path }: any) => {
  const setCount = useStoreActions((actions: any) => actions.setCount)
  const [isToastOpenScenario, setToastOpenScenario] = useState(false)
  const [successMsgScenario, setSuccessMsgScenario] = useState('')
  const [currentPage, setCurrentPage] = useState(1)
  const [showGraphPopup, setShowGraphPopup] = useState(false)
  const [pageData, setPageData] = useState({
    rowData: [],
    isLoading: false,
    totalPages: 0,
    totalReleases: 0,
  })

  useEffect(() => {
    setPageData((prevState) => ({
      ...prevState,
      rowData: [],
      isLoading: true,
    }))
    getReleaseData().then((info) => {
      const filteredReleases = info?.filter((item: any) => item.releaseStatus == 'READY_FOR_RELEASE')
      setPageData({
        isLoading: false,
        rowData: releaseManagementRowData(filteredReleases),
        totalPages: 1,
        totalReleases: numberOfReleases(filteredReleases),
      })
      setCount(numberOfReleases(filteredReleases));
    })
  }, [])

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
      Header: '',
      accessor: 'menu',
      formatter: (value: any, cell: any) => {
         return (<Button name={"show-graph"} onClick={() => showGraph(cell.row.values.releaseId)}>Analytics View</Button>)
      }
    },
  ]

  const showGraph = (releaseId: string) => <GraphModal releaseId={releaseId} show={setShowGraphPopup} onClose={setShowGraphPopup}/>

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
        <Pagination totalRows={pageData.totalReleases} pageChangeHandler={setCurrentPage} rowsPerPage={10} />
      </Box>
      {/* Toast message for deleting a scenario */}
      {<Toast show={isToastOpenScenario}>
        <div>
          <BoxToast toastMsg={successMsgScenario} />
        </div>
      </Toast>
      }
    </>
  )
}
export default MonitoringList