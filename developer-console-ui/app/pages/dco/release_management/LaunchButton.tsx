import {Button, LoadingSpinner, NotificationBadge} from "@dco/sdv-ui";
import {launchReleaseData} from "../../../services/functionReleaseManagement.services";
import React from "react";

export const LaunchButton = ({releaseID, cellData, setSuccessMsgScenario, setToastOpenScenario}: any) => {
    const [istesting, setIstesting] = React.useState(false);
    const [isStatus, setIsStatus] = React.useState(false);

    const launchRelease = async () => {
        setIstesting(true);
        const result = await launchReleaseData(releaseID);
        window.alert(`Testing completed for release: ${releaseID}\n\nResults: ${result ? "Success" : "Failed"  }`);
        setIstesting(false);
    }

    return <>
        {istesting ? <LoadingSpinner>Testing</LoadingSpinner> :
            <Button name={"launch-button"} onClick={launchRelease}>Launch</Button>}
        {isStatus ?
            <NotificationBadge value={1} fixed={true} dataTestId={"loading"}>Testing completed</NotificationBadge> : null}
    </>
}
export default LaunchButton;
