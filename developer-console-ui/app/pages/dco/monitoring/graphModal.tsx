import { Headline, Popup, Spacer } from "@dco/sdv-ui";
import { useState } from "react";

export function GraphModal (props: any) {
    const [closeModal, setCloseModal] = useState(false)
    
    return <>
    <Popup invert={true} dim show={props.show} onClose={props.onClose} style={{ zIndex: 100 }}>
            <Headline>
                Release ${props.releaseId}
            </Headline>
            <Spacer />
        </Popup>
    </>
}