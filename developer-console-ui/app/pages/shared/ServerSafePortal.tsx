import { usePortalOutlet } from '@dco/sdv-ui';
import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';

/**
 * You cannot provide the outlet as a param here as the oultet is defined
 * via the PortalOutletProvider Component. Please only provide the children here.
 */
export const ServerSafePortal = ({
  children,
  key,
}: {
  children: React.ReactNode;
  key?: string;
}) => {
  const outlet = usePortalOutlet();
  const [activated, setActivated] = useState(false);

  useEffect(() => {
    setActivated(true);
  }, []);

  // in a server environment outlet can be null when no other target is provided
  // as by default it points to document.body
  // so we only render it when it is non-empty :)
  // also we need to use the work-around with setState and useEffect to ensure we do not
  // get hydration problems

  return <>{outlet && activated && ReactDOM.createPortal(children, outlet, key)}</>;
};
export default ServerSafePortal;