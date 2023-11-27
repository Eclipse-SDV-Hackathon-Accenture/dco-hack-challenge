import '@testing-library/jest-dom';
import { render } from '@testing-library/react';
import ActiveLink from '../../components/layout/ActiveLink';
import { MemoryRouterProvider } from 'next-router-mock/MemoryRouterProvider';
import { StoreProvider } from 'easy-peasy';
import { store } from '../../services/store.service';
const props = {
    children: {},
    href: "/dco/scenario",
}
describe("ActiveLink function", () => {
    it("should render active link", () => {
        render(
          //  @ts-ignore 
      <StoreProvider store={store}>
             <MemoryRouterProvider url="/dco">
            <ActiveLink href={props.href}></ActiveLink>
          </MemoryRouterProvider>
          </StoreProvider>
           );

    });
});
