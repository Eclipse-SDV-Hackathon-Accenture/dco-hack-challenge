import '@dco/sdv-ui/dist/assets/main.css'
import React from "react";
import { useRouter } from "next/router";
import Link, { LinkProps } from 'next/link';

function ActiveLink(props: React.PropsWithChildren<LinkProps>) {
    const router = useRouter()
    return React.isValidElement(props.children) ? (
        <Link {...props} passHref>
            {React.cloneElement(props.children, {
                active: router?.pathname === props.href || router?.pathname === props.as
            })}
        </Link>
    ) : null;
}
export default ActiveLink;