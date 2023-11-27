import { Button, Flex, Headline, Paragraph, Popup, Spacer } from "@dco/sdv-ui";
import { invert } from "../../services/functionShared";

export const ConditionalAlertBtn = ({ ...props }) => {
    return <>
        <Popup invert={!invert()} show={props.show} width={30} dim={true} data-testid="closeAlert" onClick={() => { props.onClose(false) }}>
            <Headline>{props.popupState}</Headline>
            <Paragraph>{props.popupMsg}</Paragraph>
            <Spacer space={2} />
            <Flex>
                <Flex.Item textAlign="right">
                    <Button width="compact" variant="secondary" data-testid="closeAlert1" onClick={() => { props.onClose(false) }}>
                        {props.no}
                    </Button>
                </Flex.Item>
                <Flex.Item textAlign="center">
                    <Button width="compact" data-testid="deleteTrack" onClick={() => { props.respectiveFun(props.respectiveId, props.mutationLoad, props.onClose) }}>{props.yes}</Button>
                </Flex.Item>
            </Flex>
        </Popup>
    </>
}
export default ConditionalAlertBtn;
