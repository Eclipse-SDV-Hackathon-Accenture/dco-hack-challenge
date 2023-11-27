import {Button, DangerButton, Icon, Menu} from "@dco/sdv-ui";
import {deleteReleaseData} from "../../../services/functionReleaseManagement.services";

type Prop = {
    releaseID: string
    onclose(): void
}

export const DeleteButton = (prop: Prop) => {

    const deleteRelease = async () => {
        const shouldDelete = window.confirm(`Delete release ${prop.releaseID}?`);
        if (shouldDelete) {
            await deleteReleaseData(prop.releaseID);
            prop.onclose();
        }
    }

    return <>
        <DangerButton name={"delete-button"} onClick={deleteRelease}>Delete</DangerButton>
    </>
}
export default DeleteButton
