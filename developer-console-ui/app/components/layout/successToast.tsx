import { Button, Flex, FlexItem, Icon } from "@dco/sdv-ui";
import { BoxToastProps } from "../../types";

export const SuccessToast = ({toastMsg}:BoxToastProps) => {
    return (
        <div>
            <Flex
                gutters="small"
                valign="center"
                className='success'
            >
                <FlexItem autoSize>
                    <Icon name="status-ok-check" />
                </FlexItem>
                <FlexItem>
                    {toastMsg}
                </FlexItem>
                <FlexItem autoSize>
                    <Button
                        round
                        variant="naked"
                        data-testid="toastBtn">
                        <Icon name="remove-close" />
                    </Button>
                </FlexItem>
            </Flex>
        </div>

    )
}
export default SuccessToast;