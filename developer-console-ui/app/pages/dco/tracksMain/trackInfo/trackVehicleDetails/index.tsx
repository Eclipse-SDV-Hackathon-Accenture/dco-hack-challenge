import { useQuery } from '@apollo/client'
import { Box, Flex, Headline, Spacer, Table } from '@dco/sdv-ui'
import { useStoreState } from 'easy-peasy'
import TrackInfo from '..'
import { getVehicleList, } from '../../../../../services/functionTrack.service'
import { TRACK_DETAILS } from '../../../../../services/queries'
import Status from '../../../../shared/status'

export function TrackId() {
  return useStoreState((state: any) => state.tid)
}
// displays list of vehicles
const TrackVehicleDetails = (args: any) => {
  const { data } = useQuery(TRACK_DETAILS, {
    variables: { id: TrackId() },
  })
  const columns = [
    {
      Header: 'VIN',
      accessor: 'vin',
    },
    {
      Header: 'Type',
      accessor: 'type',
    },
    {
      Header: 'Last connected',
      accessor: 'lastconnected',
    },
    {
      Header: 'Country',
      accessor: 'country',
    },
    {
      Header: 'Brand',
      accessor: 'brand',
    },
    {
      Header: 'Staus',
      accessor: 'status',
      formatter: (value: any, cell: any) => {
        return <Status status={cell?.row.values.status} type={'VD'}></Status>
      },
    },
  ]
  const datas = getVehicleList(data)
  return (
    <TrackInfo>
      <Flex column>
        <Flex.Item>
          <Box paddingLeft='small' escapePaddingBottom paddingTop='large'>
            <Headline level={2}>
              {datas?.length} {datas?.length == 1 ? 'vehicle' : 'vehicles'}
            </Headline>
            <Spacer />
          </Box>
          <Box>
            {/* @ts-ignore */}
            <Table columns={columns} data={datas} noDataMessage='No rows to show'></Table>
          </Box>
        </Flex.Item>
      </Flex>
    </TrackInfo>
  )
}

export default TrackVehicleDetails
