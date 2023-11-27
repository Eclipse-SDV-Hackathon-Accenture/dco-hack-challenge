import { MoreData, Tooltip } from "@dco/sdv-ui";
import { getToolTip } from "../../services/functionTrack.service";
import PrintTollTipValue from "./printTollTipValue";

export const CounterWithToolTip = ({ toolTipVal }: any) => {
    const case1 = (toolTipVal.length == 1)
    const case2 = (toolTipVal.length > 1)
    return <>
        {case1 && <PrintTollTipValue val={toolTipVal[0].country || toolTipVal[0]} />}
        {case2 && <><PrintTollTipValue val={toolTipVal[0].country || toolTipVal[0]} /> < MoreData><Tooltip
            inline
            tooltip={getToolTip(toolTipVal.slice(1))}
        >{toolTipVal.length - 1}+</Tooltip></MoreData></>}
    </>
}

export default CounterWithToolTip;