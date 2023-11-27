import { Box, Flex, Icon, Menu, NavigationBar, NavigationBarItem } from '@dco/sdv-ui'
import { useStoreActions, useStoreState } from 'easy-peasy'
import { useRouter } from 'next/router'
import React, { useEffect, useState } from 'react'
import ActiveLink from './ActiveLink'
export function User() {
    return useStoreState((state: any) => state.user)
}
export function Layout({ children }: any) {
    const router = useRouter()
    const invert = useStoreState((state: any) => state.invert)
    const pathname = router?.pathname
    const token = localStorage.getItem('token');
    const setUser = useStoreActions((actions: any) => actions.setUser)
    const [openMenu, setOpenMenu] = useState(false)
    const [target, setTarget]: any = useState(null);
    useEffect(() => {
        if (pathname === '/') {
            router.replace('/login')
        } else if (pathname === '/dco') {
            if (token) {
                router.replace('/dco/scenario')
            } else { router.push('login') }
        }
    })
    const username = localStorage.getItem('user')
    const user = User();
    return (
        <Box fullHeight>
            <Flex column fullHeight>
                <Flex.Item autoSize >
                    <Box fullHeight padding='none' invert={invert} variant='deep'>
                        <NavigationBar>
                            <ActiveLink href={router?.pathname.includes('/dco/') ? pathname : '/dco/scenario'}>
                                <NavigationBarItem logo>
                                    <Icon data-testid='logo' name='tbbui'
                                    /> SDV Developer
                                    Console
                                </NavigationBarItem>
                            </ActiveLink>
                            {(user && token) ? <NavigationBarItem
                                noLink ref={setTarget}
                                right data-testid='logout'
                                onClick={() => { setOpenMenu(true) }}
                            >
                                {username} <Icon name='user' id='Logout' />
                            </NavigationBarItem> : ''}

                        </NavigationBar>
                    </Box>
                </Flex.Item>
                <Flex.Item>
                    <Flex fullHeight>
                        <Flex.Item>
                            <Box fullHeight>
                                {children}
                            </Box>
                        </Flex.Item>
                    </Flex>
                </Flex.Item>
            </Flex>
            <Menu
                open={openMenu}
                target={target}
                items={[{ text: 'logout' }]}
                onItemClick={(e: any) => {
                    localStorage.removeItem('token')
                    setUser(false)
                    router.replace('/logout')
                }}
                onHide={() => { setOpenMenu(false); }}
            />
        </Box>

    )
}
export default Layout
