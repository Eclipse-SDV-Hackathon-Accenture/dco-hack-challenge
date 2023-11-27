import { Box } from "@dco/sdv-ui";
import { BoxToastProps } from "../../types";
import ErrorToast from "./errorToast";
import SuccessToast from "./successToast";

export const BoxToast = ({ toastMsg }: BoxToastProps) => {
    const status = (toastMsg == "Package has been created successfully" || toastMsg == "Track has been added successfully" || toastMsg == "Component has been created successfully" || toastMsg?.includes("has been deleted successfully") || toastMsg == 'Track deleted' || toastMsg == 'Workflow creation window open successfully' || toastMsg == 'Campaign Restarted Successfully' || toastMsg == "Scenario has been created successfully" ||toastMsg =="Scenario has been updated successfully"|| toastMsg?.includes("Scenario deleted")||toastMsg=='Simulation has been launched successfully')
    return (
        <Box
            elevation="medium"
            padding="small"
            variant="high"
            radius="round"
            style={{ background: 'rgba(2, 14, 20, 1)' }}
        >
            {status && <SuccessToast toastMsg={toastMsg}></SuccessToast>}
            {!status && <ErrorToast toastMsg={toastMsg}></ErrorToast>}
        </Box>
    )
}
export default BoxToast;