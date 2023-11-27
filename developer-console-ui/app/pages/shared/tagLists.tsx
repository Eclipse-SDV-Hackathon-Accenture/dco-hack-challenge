import { Tag } from "@dco/sdv-ui";
import { displayBrandVal } from "../../services/functionShared";

export const TagLists = ({ data, type, from }: any) => {
    return <>
        {from == 'relaseDetails' && data?.getReleaseById?.[type]?.filter((v: any, i: any, a: any) => a.indexOf(v) === i).map((x: any) => {
            return (<Tag icon={type == 'brands' ? displayBrandVal(x) : ''} text={x} key={x} className={""} />)
        })}
        {from == 'trackDetails' && type != 'compNVersion' && data.map((x: any) => {
            return (<Tag icon={type == 'brands' ? displayBrandVal(x) : ''} text={x} key={x} className={""} />)
        })}
        {from == 'trackDetails' && type == 'compNVersion' && data.map((x: any) => {
            return (<Tag text={x.name + ", " + x.version} key={x} className={""} />)
        })}

    </>
}
export default TagLists;