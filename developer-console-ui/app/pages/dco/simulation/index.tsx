import { Box, Table } from '@dco/sdv-ui'
import { useStoreActions } from 'easy-peasy'
import { useEffect, useState } from 'react'
import Dco from '..'
import { getSimData, simRowData } from '../../../services/functionSimulation.service'
import CounterWithToolTip from '../../shared/counterWithToolTip'
import Pagination from '../../shared/paginationTable'
import Status from '../../shared/status'

// simulation table 
const Simulation = () => {
  const setCount = useStoreActions((actions: any) => actions.setCount)
  const [currentPage, setCurrentPage] = useState(1)
  const [pageData, setPageData] = useState({
    rowData: [],
    isLoading: false,
    totalPages: 0,
    totalSimulations: 0,
  })
  useEffect(() => {
    setPageData((prevState) => ({
      ...prevState,
      rowData: [],
      isLoading: true,
    }))
    getSimData(currentPage).then((info) => {
      setPageData({
        isLoading: false,
        rowData: simRowData(info)  as any,
        totalPages: info?.data?.simulationReadByQuery?.pages,
        totalSimulations: info?.data?.simulationReadByQuery?.total,
      })
      setCount(info?.data.simulationReadByQuery?.total);
    })
  }, [currentPage])

  const columns = [{
    Header: 'Simulation Name',
    accessor: 'name',
  }, {
    Header: 'Status',
    accessor: 'status',
    formatter: (value: any, cell: any) => {
      return <Status status={cell?.row?.values?.status} type={'SS'}></Status>
    }
  }, {
    Header: 'Number of vehicles',
    accessor: 'numberVehicles',
  }, {
    Header: 'Brand',
    accessor: 'brand',
    formatter: (value: any, cell: any) => {
      return <CounterWithToolTip toolTipVal={[...new Set(cell?.row?.values?.brand)]}></CounterWithToolTip>
    },

  }, {
    Header: 'Scenario Type',
    accessor: 'type',
  }, {
    Header: 'Number of Scenarios',
    accessor: 'numberScenarios',
  }, {
    Header: 'Platform',
    accessor: 'platform',
  }, {
    Header: 'Environment',
    accessor: 'env',
  },
  {
    Header: 'Start Date ',
    accessor: 'date',
  }]

  return (<Dco>
      {/* @ts-ignore */}
    <Table columns={columns}
      data={pageData.rowData}
    />
    <Box align='right' padding='small'>
      <Pagination totalRows={pageData.totalSimulations} pageChangeHandler={setCurrentPage} rowsPerPage={10} />
    </Box>
  </Dco>
  )
}
export default Simulation
