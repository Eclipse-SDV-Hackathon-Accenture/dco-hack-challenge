import React from "react";
import ActiveLink from "../../../components/layout/ActiveLink";
import {checkRoute} from "../../../services/functionShared";
import router from "next/router";
import {Flex, FlexItem, NavigationBar, NavigationBarItem} from "@dco/sdv-ui";

const MonitoringList = ({ children }: any) => {
  const pathname = router?.pathname
  const trackTabClicked = router.pathname === '/dco/tracksMain'
  const libTabClicked = router.pathname.includes('/dco/scenario')
  const simulationTabClicked = router.pathname === '/dco/simulation'
  const releaseTabClicked = router.pathname === '/dco/release_management'
  const token = localStorage.getItem('token')

  return (
    <>
      {/* ... (other code) */}
      <Flex>
        <Flex.Item autoSize>
          {/* LEFT SIDEBAR */}
          <NavigationBar variant="left-sidebar" sizes="large">
            <ActiveLink href={checkRoute('/dco/monitoring/weather', router, pathname)}>
              <NavigationBarItem>
                Weather
              </NavigationBarItem>
            </ActiveLink>
            <ActiveLink href={checkRoute('/dco/monitoring/terrain', router, pathname)}>
              <NavigationBarItem>
                Terrain
              </NavigationBarItem>
            </ActiveLink>
            <ActiveLink href={checkRoute('/dco/monitoring/routs', router, pathname)}>
              <NavigationBarItem>
                Routs
              </NavigationBarItem>
            </ActiveLink>
          </NavigationBar>
        </Flex.Item>

        <FlexItem>
          {/*  */}
        </FlexItem>
      </Flex>
    </>
  );
};

export default MonitoringList;