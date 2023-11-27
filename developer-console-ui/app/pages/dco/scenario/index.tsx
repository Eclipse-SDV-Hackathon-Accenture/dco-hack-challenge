import { Box } from '@dco/sdv-ui'
import Dco from '..'
import ScenarioList from './scenarioList'

// scenario listing table
const Scenario = (args: any) => {
  return (<Dco>
    <Box fullHeight scrollY>
      <ScenarioList></ScenarioList>
    </Box>
  </Dco>)
}

export default Scenario
