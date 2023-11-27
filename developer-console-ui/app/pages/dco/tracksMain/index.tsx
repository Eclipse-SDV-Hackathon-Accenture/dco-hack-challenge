import { Box } from '@dco/sdv-ui'
import Dco from '..'
import TrackList from './trackList'

// used in tracklisting page and new simulation 
const TracksMain = () => {
  return (
    <>
      <Dco>
          <Box fullHeight padding='none' scrollY>
            <TrackList></TrackList>
          </Box>
      </Dco>
    </>
  )
}
export default TracksMain
