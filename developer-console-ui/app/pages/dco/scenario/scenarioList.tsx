import { Box, Check, Table, Toast } from "@dco/sdv-ui"
import { useStoreActions, useStoreState } from "easy-peasy"
import { useEffect, useState } from "react"
import MenuForScenario from "../addEditScenario/menuForScenario"
import Pagination from "../../shared/paginationTable"
import { getLibData, libRowData } from "../../../services/functionScenario.services"
import BoxToast from "../../../components/layout/boxToast"
export function Selectedscenario() {
  return useStoreState((state: any) => state.selectedscenario)
}
export function Searchval() {
  return useStoreState((state: any) => state.searchval);
}
const ScenarioList = ({ path }: any) => {
  const setSelectedscenario = useStoreActions((actions: any) => actions.setSelectedscenario)
  const theArray = Selectedscenario();
  const setCount = useStoreActions((actions: any) => actions.setCount)
  const [isToastOpenScenario, setToastOpenScenario] = useState(false)
  const [successMsgScenario, setSuccessMsgScenario] = useState('')
  const [currentPage, setCurrentPage] = useState(1)
  const searchval = Searchval();
  const [pageData, setPageData] = useState({
    rowData: [],
    isLoading: false,
    totalPages: 0,
    totalScenarios: 0,
  })

  useEffect(() => {
    setPageData((prevState) => ({
      ...prevState,
      rowData: [],
      isLoading: true,
    }))
    getLibData(currentPage, searchval).then((info) => {
      setPageData({
        isLoading: false,
        rowData: libRowData(info),
        totalPages: info?.data?.searchScenarioByPattern?.pages,
        totalScenarios: info?.data?.searchScenarioByPattern?.total,
      })
      setCount(info?.data.searchScenarioByPattern?.total);
    })
  }, [currentPage, searchval])
  const columns = [
    {
      Header: '',
      accessor: 'check',
      hidden: path == 'sim' ? false : true,
      formatter: (value: any, cell: any) => {
        return (<Check
          checked={theArray?.find((e: any) => {
            if (e.id == cell.row.values.sid && e.checked) {
              return true
            } else { return false }
          })}
          onChange={(e: any, val: any) => {
            theArray.map((v: any) => {
              if (v?.id == cell.row.values.sid) {
                v.checked = e.target.checked
              } else {
                theArray.push({ id: cell.row.values.sid, checked: e.target.checked })
              }
            })
            let newArr = [...new Map(theArray.map((s: any) => [s['id'], s])).values()]
            setSelectedscenario(newArr)
          }}
        />)
      }
    }, {
      Header: 'ID',
      accessor: 'sid',
    },
    {
      Header: 'Scenario',
      accessor: 'scenario',
    },
    {
      Header: 'Type',
      accessor: 'type',
    },
    {
      Header: 'Filename',
      accessor: 'filename',
    },
    {
      Header: 'Last Updated',
      accessor: 'lastUpdated',
    },
    {
      Header: '',
      accessor: 'menu',
      hidden: path == 'sim' ? true : false,
      formatter: (value: any, cell: any) => {
        return (<MenuForScenario variant='naked' cellData={cell?.row.values} setToastOpenScenario={setToastOpenScenario} setSuccessMsgScenario={setSuccessMsgScenario}></MenuForScenario>)
      }
    },
  ]
  return (<>
      {/* @ts-ignore */}
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
      <Pagination totalRows={pageData.totalScenarios} pageChangeHandler={setCurrentPage} rowsPerPage={10} />
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
export default ScenarioList