import { usePortalOutlet } from '@dco/sdv-ui';
import { useEffect, useState } from 'react'

type Props = {
  children: any
}
const ForceSession = ({ children }: Props) => {
  const outlet = usePortalOutlet();
  const [activated, setActivated] = useState(false);
  useEffect(() => {
    setActivated(true);
  }, []);
  if (outlet) {
    return children
  }
  return null
}

export default ForceSession
