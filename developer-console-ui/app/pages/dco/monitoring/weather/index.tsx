import { Flex, Box, Headline, Button, NavigationBar, NavigationBarItem, StatusMessage, Spacer } from '@dco/sdv-ui'
import { useStoreActions, useStoreState } from 'easy-peasy'
import { useRouter } from 'next/router'
import { useState } from 'react'
import Layout from "../../../../components/layout/layout";
import {checkRoute, invert} from "../../../../services/functionShared";
import {onClickNewTrack} from "../../../../services/functionTrack.service";
import {onClickNewSimulation} from "../../../../services/functionSimulation.service";
import NewScenario from "../../addEditScenario/newScenario";
import NewRelease from "../../addRelease/newRelease";
import ActiveLink from "../../../../components/layout/ActiveLink";
export function Count() {
    return useStoreState((state: any) => state.count)
}
// main landing page
const Another = ({ children }: any) => {
    const router = useRouter()
    const [showScenarioPopup, setShowScenarioPopup] = useState(false)
    const [showReleasePopup, setShowReleasePopup] = useState(false)
    const setCompid = useStoreActions((actions: any) => actions.setCompid)
    const pathname = router?.pathname
    const trackTabClicked = router.pathname === '/dco/tracksMain'
    const libTabClicked = router.pathname.includes('/dco/scenario')
    const simulationTabClicked = router.pathname === '/dco/simulation'
    const releaseTabClicked = router.pathname === '/dco/release_management'
    const token = localStorage.getItem('token')
    return (<>{
        <Layout>
            <Box padding='none' invert={invert()} variant='body' fullHeight>
                <Flex column fullHeight>
                    {token ? <> <Flex.Item autoSize>
                            {/* Header */}
                            <Flex valign='bottom'>
                                {/* HEADLINE */}
                                <Flex.Item flex={1} textAlign='right'>
                                    <Box padding='sidebar'>
                                        <Flex gutters='small'>
                                            <Flex.Item autoSize>
                                                {libTabClicked && <Headline level={1}> {Count() || 0} scenarios</Headline>}
                                                {releaseTabClicked && <Headline level={1}> {Count() || 0} releases</Headline>}
                                                {trackTabClicked && <Headline level={1}>{Count() || 0} tracks</Headline>}
                                                {simulationTabClicked && <Headline level={1}> {Count() || 0} simulations</Headline>}
                                            </Flex.Item>
                                            <Flex.Item textAlign='left' valign='bottom'>
                                                {libTabClicked && (<Button style={{ marginTop: '-.3em' }} data-testid='newReleaseBtn'
                                                                           onClick={() => { setShowScenarioPopup(true) }}>
                                                    New Scenario
                                                </Button>)}

                                                {/* New Release I guess */}
                                                {releaseTabClicked && (<Button style={{ marginTop: '-.3em' }} data-testid='newReleaseBtn'
                                                                               onClick={() => { setShowReleasePopup(true) }}>
                                                    New Release
                                                </Button>)}
                                                {/*  */}

                                                {trackTabClicked && (<Button style={{ marginTop: '-.3em' }} data-testid='addTrackbtn'
                                                                             onClick={() => onClickNewTrack(setCompid)}>
                                                    New Track
                                                </Button>)}
                                                {simulationTabClicked && (<Button style={{ marginTop: '-.3em' }} data-testid='addSimulationBtn'
                                                                                  onClick={() => onClickNewSimulation()}>
                                                    New Simulation
                                                </Button>)}
                                            </Flex.Item>
                                        </Flex>
                                    </Box>
                                </Flex.Item>
                                <NewScenario show={showScenarioPopup} onClose={setShowScenarioPopup} path='create' />
                                <NewRelease show={showReleasePopup} onClose={setShowReleasePopup} path='create'/>
                                {/* NAV */}
                                <Flex.Item autoSize>
                                    <NavigationBar>
                                        <ActiveLink href={checkRoute('/dco/scenario', router, pathname)}>
                                            <NavigationBarItem>scenario</NavigationBarItem>
                                        </ActiveLink>
                                        <ActiveLink href={checkRoute('/dco/tracksMain', router, pathname)}>
                                            <NavigationBarItem>tracks</NavigationBarItem>
                                        </ActiveLink>
                                        <ActiveLink href={checkRoute('/dco/simulation', router, pathname)}>
                                            <NavigationBarItem>simulations</NavigationBarItem>
                                        </ActiveLink>
                                        <ActiveLink href={checkRoute('/dco/release_management', router, pathname)}>
                                            <NavigationBarItem>release management</NavigationBarItem>
                                        </ActiveLink>
                                        <ActiveLink href={checkRoute('/dco/monitoring', router, pathname)}>
                                            <NavigationBarItem>monitoring</NavigationBarItem>
                                        </ActiveLink>
                                    </NavigationBar>
                                </Flex.Item>
                                {/* RIGHT */}
                                <Flex.Item flex={1} textAlign='right'>
                                </Flex.Item>
                            </Flex>
                        </Flex.Item>
                            <Flex.Item>
                                <Flex fullHeight>
                                    <Flex.Item>
                                        <Box fullHeight variant='high'>{children}</Box>
                                    </Flex.Item>
                                </Flex>
                            </Flex.Item>
                        </>
                        :
                        <>
                            <Flex.Item align="center" autoSize >
                                <Flex>
                                    <Flex.Item>
                                        <Spacer space={10}></Spacer>
                                        <Spacer space={5}></Spacer>
                                        <Box interactive variant='high' padding='large' elevation='low'>
                                            <Flex rows={1} gutters="small">
                                                <Flex.Item  valign='center'> <StatusMessage noIcon variant='secondary'>Session has been expired click
                                                </StatusMessage></Flex.Item>
                                                <Flex.Item > <Button  variant='primary' size='small' align='right' onClick={() => { router.replace('/login') }}> here </Button>
                                                </Flex.Item>
                                                <Flex.Item valign='center'>     <StatusMessage noIcon variant='secondary' >to login again
                                                </StatusMessage></Flex.Item>
                                            </Flex>


                                        </Box>
                                    </Flex.Item>
                                </Flex>
                            </Flex.Item>
                        </>}
                </Flex>
            </Box>
        </Layout>

    }

    </>)
}
export default Another
