import { Box, Button, Flex, Headline, Icon, Input, Spacer, Value } from "@dco/sdv-ui"
import { useStoreActions, useStoreState } from "easy-peasy"
import { useRouter } from "next/router";
import { useRef } from "react";
import Layout from "../../components/layout/layout";
import { Link } from "../../libs/apollo";
import { onSubmit } from "../../services/credentials.service";
export function Login() {
    localStorage.removeItem('token');
    const invert = useStoreState((state: any) => state.invert)
    const setUser = useStoreActions((actions: any) => actions.setUser)
    const userName = useRef("");
    const router = useRouter();
    const pass = useRef("");
    return (
        <Layout>
            <Box fullHeight invert={invert} variant='high' padding="large">
                <Spacer space={5} />
                <Box padding="large" >
                    <Flex justify="center">
                        <Flex.Item>
                        </Flex.Item>
                        <Flex.Item justify-content="center" textAlign="center" align="center" >
                            <Box variant="body" padding="large" >
                                <Headline level={2}>Welcome to SDV Developer Console  <Icon name='tbbui'></Icon></Headline>
                                <Value>Please enter your credentials for best experience</Value>
                            </Box>
                            <Box variant="body" transparency="high" padding="large" >
                                <Flex column>
                                    <Flex.Item>
                                        <Input withAccessory accessoryIcon="user" label="Username" onValueChange={(e) => (userName.current = e)
                                        }></Input>
                                    </Flex.Item>
                                    <Spacer />
                                    <Flex.Item>
                                        <Input withAccessory accessoryIcon="lock" type="password" label="Password" onValueChange={(e) => (pass.current = e)
                                        }></Input>
                                    </Flex.Item>
                                    <Spacer space={4} />
                                    <Flex.Item align="left">
                                        {/* <Label text="Remember you?">
                                            <Check
                                                name="demo"
                                                value="1"
                                            />
                                        </Label> */}
                                    </Flex.Item>
                                    <Spacer space={1} />
                                    <Flex.Item textAlign="center">
                                        <Button data-testId="submitBtn" width="full" onClick={() => {
                                            onSubmit(userName, pass, setUser, router)
                                                .then(result => {
                                                    if (result.data.searchScenarioByPattern) {
                                                    setUser(true)
                                                    router.replace('/dco/scenario')}
                                                    else{
                                                        router.replace('/error') 
                                                    }
                                                })
                                                .then((res)=>res)
                                                .catch(error => { router.replace('/error') })
                                        }}>Login</Button>
                                    </Flex.Item>
                                </Flex>
                            </Box>
                        </Flex.Item>
                        <Flex.Item>
                        </Flex.Item>
                    </Flex>
                </Box>
            </Box >
        </Layout>
    )
}
export default Login



