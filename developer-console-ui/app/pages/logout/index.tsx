import { Box, Button, Flex, Headline, Icon, Spacer, StatusMessage, Value } from "@dco/sdv-ui"
import { useStoreState } from "easy-peasy"
import {  useRouter } from "next/router";
import Layout from "../../components/layout/layout";
export function Logout() {
    const invert = useStoreState((state: any) => state.invert)
    const router=useRouter();
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
                                <Value>Please login for best experience</Value>
                            </Box>
                            <Box variant="body" transparency="high" padding="large" >
                                <StatusMessage variant="success" noIcon>
                                    You have been successfully logged out
                                </StatusMessage>
                                <Spacer space={4}></Spacer>
                                <Button width="compact" data-testId="loginBtn" onClick={() => { router.push('login')}}>Login</Button>
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
export default Logout