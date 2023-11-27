import { Button, Flex, FlexItem, Icon } from "@dco/sdv-ui";
import { BoxToastProps } from "../../types";

export const ErrorToast = ({toastMsg}:BoxToastProps) => {
    return (
        <div>
            <Flex
                gutters="small"
                valign="center"
                className='error'
            >
                <FlexItem autoSize>
                    <Icon name="status-error-stop" />
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
export default ErrorToast;