import { BackButton, Box, Flex, Headline, NavigationBar } from '@dco/sdv-ui'
import { useStoreState } from 'easy-peasy'
import router from 'next/router'
import Layout from '../../../../components/layout/layout'
import {invert } from '../../../../services/functionShared'
import TrackDetails from './trackDetails'
export function TrackName() {
  return useStoreState((state: any) => state.tname)
}
// tracks info page 
const TrackInfo = ({ children }: any) => {
  return (
    <Layout>
      <Box padding='none' invert={invert()} variant='body' fullHeight>
        <Flex column fullHeight>
          <Flex.Item autoSize>
            {/* HEADER */}
            <Flex valign='bottom'>
              {/* HEADLINE */}
              <Flex.Item flex={1}>
                <Box padding='sidebar'>
                  <Flex gutters='large'>
                    <Flex.Item autoSize>
                      <BackButton data-testid='backButton' onClick={() => router.push('/dco/tracksMain')} />
                      <Headline level={1}>{TrackName()}</Headline>
                    </Flex.Item>
                  </Flex>
                </Box>
              </Flex.Item>
              {/* NAV */}
              <Flex.Item autoSize>
                <NavigationBar>
                </NavigationBar>
              </Flex.Item>
              {/* RIGHT */}
              <Flex.Item flex={1} textAlign='right'></Flex.Item>
            </Flex>
          </Flex.Item>
          {/* MAIN CONTENT */}
          <Flex.Item>
            <Flex fullHeight>
              <Flex.Item flex={2}>
                <Flex fullHeight>
                  <Flex.Item>
                    <Box variant='high' transparency='medium' fullHeight scrollVertical>
                      <TrackDetails />
                    </Box>
                  </Flex.Item>
                </Flex>
              </Flex.Item>
              <Flex.Item flex={6}>
                <Flex fullHeight>
                  <Flex.Item>
                    <Box variant='high' fullHeight scrollVertical>
                      {children}
                    </Box>
                  </Flex.Item>
                </Flex>
              </Flex.Item>
            </Flex>
          </Flex.Item>
        </Flex>
      </Box>
    </Layout>
  )
}

export default TrackInfo
