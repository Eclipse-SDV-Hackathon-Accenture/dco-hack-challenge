import { BackButton, Box, Button, Flex, Headline, Input, NavigationBar, NavigationBarItem, SearchField, Select, Spacer, StatusMessage, Toast } from "@dco/sdv-ui"
import Layout from "../../../components/layout/layout"
import router from "next/router";
import { useState } from "react";
import { clearAll, launchSimulation, onLaunchedSimulation } from "../../../services/functionSimulation.service";
import { useStoreActions, useStoreState } from "easy-peasy";
import { useMutation, useQuery } from "@apollo/client";
import { HARDWARE_MODULE, LAUNCH_SIMULATION } from "../../../services/queries";
import BoxToast from "../../../components/layout/boxToast";
import TrackList from "../tracksMain/trackList";
import ScenarioList from "../scenario/scenarioList";
import { avoidSplChars, invert } from "../../../services/functionShared";
export function Selectedscenario() {
    return useStoreState((state: any) => state.selectedscenario)
}
export function Selectedtrack() {
    return useStoreState((state: any) => state.selectedtrack)
}
export function Searchval() {
    return useStoreState((state: any) => state.searchval);
}
// click on New simulation and used to launch or discard simulation
const AddSimulation = ({ children }: any) => {
    const [title, setTitle] = useState('');
    const [titleError, setTitleError] = useState(false);
    const [description, setDescription] = useState('');
    const [environment, setEnvironment] = useState('');
    const [platform, setPlatform] = useState('');
    const [scenarioType, setScenarioType] = useState('');
    const [stypeError, setSTypeError] = useState(false);
    const [hardware, setHardware] = useState('');
    const [isToastOpen, setIsToastOpen] = useState(false);
    const [toastMsg, setToastMsg] = useState<string>('')
    const [activeScenario, setActiveScenario] = useState('active');
    const [activeTrack, setActiveTrack] = useState('');
    const enviroList = ['Development', 'Demo'];
    const platformList = ['Task Mangement'];
    const scenarioTypeList = ['Over-The-Air Service',
        'Vehicle Management',
        'Data Collection',
        'Remote Control'];
    const { data } = useQuery(HARDWARE_MODULE);
    let foundTrack = false;
    let foundScenario = false;
    const scenario = Selectedscenario();
    const track = Selectedtrack();
    const [trackError, setTrackError] = useState(false);
    const [scenarioError, setScenarioError] = useState(false);
    const [createSimulation] = useMutation(LAUNCH_SIMULATION, {
        onCompleted(value) {
            onLaunchedSimulation(setSelectedscenario, setSelectedtrack, setIsToastOpen, setToastMsg, value, true)
        },
        onError(error) {
            onLaunchedSimulation(setSelectedscenario, setSelectedtrack, setIsToastOpen, setToastMsg, error, false)
        }
    });
    const setSelectedscenario = useStoreActions((actions: any) => actions.setSelectedscenario)
    const setSelectedtrack = useStoreActions((actions: any) => actions.setSelectedtrack)
    const setSearchval = useStoreActions((actions: any) => actions.setSearchval);
    const searchval = Searchval();
    return (<><Layout>
        <Box fullHeight padding="none" invert={invert()} variant="body">
            <Flex fullHeight>
                <Flex.Item flex={5}>
                    <Box padding="none" variant="body" fullHeight>
                        <Flex column fullHeight>
                            <Flex.Item autoSize>
                                {/* HEADER */}
                                <Flex gutters="small" valign="bottom">
                                    {/* HEADLINE */}
                                    <Flex.Item flex={1}>
                                        <Box padding='sidebar'>
                                            <Flex gutters='large'>
                                                <Flex.Item>
                                                    <BackButton data-testid='backButton'
                                                        onClick={() => {
                                                            clearAll({ setTitle, setDescription, setEnvironment, setPlatform, setSelectedscenario, setSelectedtrack, setScenarioType, setHardware, setSearchval, setTitleError, setSTypeError, setTrackError, setScenarioError } as const);
                                                            router.push('/dco/simulation')
                                                        }} />
                                                    <Headline level={1}>New Simulation</Headline>
                                                </Flex.Item>
                                            </Flex>
                                        </Box>
                                    </Flex.Item>
                                    {/* NAV */}
                                    <Flex.Item autoSize>
                                        <NavigationBar>
                                            <Box padding="small">
                                                <NavigationBarItem className={activeScenario} onClick={(x: any) => { setActiveTrack(""); setActiveScenario("active"); setSearchval('') }}>scenario
                                                </NavigationBarItem>
                                            </Box>
                                            <Box padding="small">
                                                <NavigationBarItem className={activeTrack} onClick={(x: any) => { setActiveTrack("active"); setActiveScenario(""); setSearchval('') }}>track
                                                </NavigationBarItem>
                                            </Box>
                                        </NavigationBar>
                                    </Flex.Item>
                                    {/* RIGHT */}
                                    <Flex.Item flex={1} textAlign='right'>
                                        {
                                            <SearchField value={searchval} onChange={(val: any) => { setSearchval(val.target.value) }} placeholder="search" width="compact" />
                                        }
                                    </Flex.Item>
                                </Flex>
                            </Flex.Item>
                            {/* MAIN CONTENT */}
                            <Flex.Item>
                                <Flex fullHeight>
                                    <Flex.Item>
                                        <Box padding="sidebar" escapePaddingBottom escapePaddingLeft variant='high' transparency="high" fullHeight >
                                            {activeScenario && <ScenarioList path={'sim'}></ScenarioList>}
                                            {activeTrack && <TrackList path={'sim'}></TrackList>}

                                            {/* scenario screen  */}
                                            {scenario.map((x: any) => { if (x.checked) { foundScenario = true; } })}
                                            {!foundScenario && scenarioError && <StatusMessage variant="error">
                                                Please select at least one scenario
                                            </StatusMessage>}
                                            {/* track screen  */}
                                            {track.map((x: any) => { if (x.checked) { foundTrack = true; } })}
                                            {!foundTrack && trackError && <StatusMessage variant="error">
                                                Please select at least one track
                                            </StatusMessage>}
                                        </Box>
                                    </Flex.Item>
                                </Flex>
                            </Flex.Item>
                        </Flex>
                    </Box>
                </Flex.Item>
                <Flex.Item flex={2}>
                    <Box padding="large" variant="high" fullHeight>
                        <Headline level={1}>Simulation</Headline>
                        <Spacer space={1} />
                        <Input label="Title *" type="text" onKeyPress={avoidSplChars} labelPosition="floating" value={title}
                            setValue={setTitle} />
                        {(!title && titleError) && <> <Spacer space={1} /><StatusMessage variant="error">
                            Please add a title
                        </StatusMessage></>}
                        <Spacer space={1} />
                        <Input label="Description" labelPosition="floating" value={description}
                            setValue={setDescription} />
                        <Spacer space={1} />
                        <Select placeholder="Environment" width="wide"
                            data-testid="environment"
                            options={enviroList}
                            value={environment}
                            searchable={true}
                            label='Environment'
                            labelPosition='floating'
                            onChange={(e: any) => { setEnvironment(e) }}
                        ></Select >
                        <Spacer space={1} />
                        <Select placeholder="Platform" width="wide"
                            data-testid="platform"
                            options={platformList}
                            value={platform}
                            searchable={true}
                            label='Platform'
                            labelPosition='floating'
                            onChange={(e: any) => { setPlatform(e) }}
                        ></Select >
                        <Spacer space={1} />
                        <Select placeholder="Scenario Type *" width="wide"
                            data-testid="scenarioType"
                            options={scenarioTypeList}
                            value={scenarioType}
                            searchable={true}
                            label='Scenario Type'
                            labelPosition='floating'
                            onChange={(e: any) => { setScenarioType(e) }}
                        ></Select >
                        {(!scenarioType && stypeError) && <> <Spacer space={1} /><StatusMessage variant="error">
                            Please select a scenario type
                        </StatusMessage></>}
                        <Spacer space={1} />
                        <Select placeholder="Hardware" width="wide"
                            data-testid="hardware"
                            options={data?.getHardwareModule}
                            value={hardware}
                            searchable={true}
                            label='Hardware'
                            labelPosition='floating'
                            onChange={(e: any) => { setHardware(e) }}
                        ></Select >
                        <Spacer space={8}></Spacer>
                        <Flex>
                            <Flex.Item textAlign="right" colSpan={2}>
                                <Button onClick={() => { launchSimulation({ title, description, environment, platform, scenario, track, scenarioType, hardware } as const, createSimulation, { setIsToastOpen, setToastMsg, setTitleError, setSTypeError, setTrackError, setScenarioError } as const); setSearchval('') }}>
                                    Launch Simulation
                                </Button>
                            </Flex.Item>
                            <Flex.Item textAlign="center" colSpan={1}>
                                <Button variant="secondary" onClick={() => { clearAll({ setTitle, setDescription, setEnvironment, setPlatform, setSelectedscenario, setSelectedtrack, setScenarioType, setHardware, setSearchval, setTitleError, setSTypeError, setTrackError, setScenarioError } as const) }}>
                                    Discard
                                </Button>
                            </Flex.Item>

                        </Flex>
                    </Box>
                </Flex.Item>
            </Flex>
        </Box>
        <Box>
            {/* Toast message for launching new simulation */}
            {<Toast show={isToastOpen}>
                <div>
                    <BoxToast toastMsg={toastMsg} />
                </div>
            </Toast>}
        </Box>
    </Layout>
    </>)
}
export default AddSimulation