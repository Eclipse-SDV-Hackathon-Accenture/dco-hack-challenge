import { Box } from '@dco/sdv-ui'
import Dco from '..'
import MonitoringList from './MonitoringList'

// scenario listing table
const Monitoring = (args: any) => {
  return (<Dco>
    <Box fullHeight scrollY>
      <MonitoringList></MonitoringList>
    </Box>
  </Dco>)
}

export default Monitoring
