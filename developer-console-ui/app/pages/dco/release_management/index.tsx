import { Box } from '@dco/sdv-ui'
import Dco from '..'
import ReleaseManagementList from './releaseManagementList'

// scenario listing table
const ReleaseManagement = (args: any) => {
  return (<Dco>
    <Box fullHeight scrollY>
      <ReleaseManagementList></ReleaseManagementList>
    </Box>
  </Dco>)
}

export default ReleaseManagement
