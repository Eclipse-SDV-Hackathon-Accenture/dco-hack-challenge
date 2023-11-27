import { useMutation } from "@apollo/client";
import { Button, Icon, Menu } from "@dco/sdv-ui";
import { useState } from "react";
import { onClickMenuItem } from "../../../services/functionTrack.service";
import { DELETE_SCENARIO, onClickScenario, setToastMessageForDeleteScenario } from "../../../services/functionScenario.services";
import ConditionalAlertBtn from "../../shared/conditionalAlertBtn";
import NewScenario from "./newScenario";

// used for update and delete menu options for scenario
export const MenuForScenario = ({  variant, cellData, setSuccessMsgScenario, setToastOpenScenario }: any) => {
    const [isOpen, setIsOpen] = useState(false);
    const [target, setTarget]: any = useState(null);
    const [isShowDelete, setShowDelete] = useState(false);
    const [isShowUpdate, setShowUpdate] = useState(false);
    let sid = cellData?.sid
    const [deleteScenario] = useMutation(DELETE_SCENARIO, {
        onCompleted(data) {
            setToastMessageForDeleteScenario(data, setSuccessMsgScenario, setToastOpenScenario, 'success');
        },
        onError(err) {
            setToastMessageForDeleteScenario(JSON.parse(JSON.stringify(err)), setSuccessMsgScenario, setToastOpenScenario, 'fail');
        },
    });
    let trackData = [{ text: "Update" }, { text: "Delete" }]
    return <>
        <Button size="small" style={{
            paddingTop: "0px",
            paddingBottom: "0px",
        }} ref={setTarget} variant={variant} onClick={(e: any) => {
            e.preventDefault();
            setIsOpen(!isOpen);
        }} data-testid="btn">
            <Icon name={"context-menu"} />
        </Button>
        {/* DON'T REMOVE THIS disabled={trackComp.highestBuild != trackComp.build}  DON'T REMOVE THIS */}
        <Menu
            open={isOpen}
            target={target}
            items={trackData}
            onItemClick={(e: any) => { onClickMenuItem(e?.text == 'Delete' ? setShowDelete : setShowUpdate) }}
            onHide={() => { setIsOpen(false); }}
        />
        {isShowDelete && <ConditionalAlertBtn show={isShowDelete} onClose={setShowDelete} respectiveId={sid} mutationLoad={deleteScenario} popupMsg={"Are you sure you want to delete this scenario?"} popupState={"Warning!"} respectiveFun={onClickScenario} yes={'Yes'} no={'No'}></ConditionalAlertBtn>}
        {isShowUpdate && <NewScenario show={isShowUpdate} onClose={setShowUpdate} path='update' cellData={cellData} setToastOpenScenario={setToastOpenScenario} setSuccessMsgScenario={setSuccessMsgScenario} />}
    </>
}
export default MenuForScenario;

