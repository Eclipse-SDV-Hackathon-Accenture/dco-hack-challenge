import { Icon, StatusMessage } from "@dco/sdv-ui";
import { capitalizeFirstLetter } from "../../services/functionShared";
import { StatusTypes } from "../../types";

export const Status = ({ status, type }: StatusTypes) => {
    return <>
        {/* Simulation page start */}
        {status == 'Done' && type == 'SS' && <><StatusMessage variant='success'>{capitalizeFirstLetter(status).replaceAll('_', ' ')}</StatusMessage></>}
        {status == 'Pending' && type == "SS" && <><StatusMessage variant='pending'>{capitalizeFirstLetter(status)}</StatusMessage></>}
        {status == 'Running' && type == "SS" && <><StatusMessage variant='loading'>{capitalizeFirstLetter(status)}</StatusMessage></>}
        {status == 'Timeout' && type == "SS" && <><StatusMessage variant='warning'>{capitalizeFirstLetter(status)}</StatusMessage></>}
        {status == 'Error' && type == "SS" && <><StatusMessage variant='error'>{capitalizeFirstLetter(status)}</StatusMessage></>}
        {/* Simulation page end */}

        {/* Vehicle details start */}
        {type == "VD" && status == 'Testing' && <><StatusMessage variant='loading'>{capitalizeFirstLetter(status)}</StatusMessage></>}
        {type === 'VD' && status == 'READY' && <><StatusMessage variant='success'>{capitalizeFirstLetter(status).replaceAll('_', ' ')}</StatusMessage></>}
        {type === 'VD' && status == 'DRAFT' && <><Icon name='edit' /> {capitalizeFirstLetter(status)}</>}
        {type === 'VD' && status == 'TEST' && <><StatusMessage variant='loading'>{capitalizeFirstLetter(status)}</StatusMessage></>}
        {type === 'VD' && status == 'SUSPEND' && <><StatusMessage variant='warning'>{capitalizeFirstLetter(status)}</StatusMessage></>}
        {type === 'VD' && status == 'PRODUCTION' && <><StatusMessage variant='success'>{capitalizeFirstLetter(status)}</StatusMessage></>}
        {/* Vehicle details end */}
    </>
}
export default Status;
