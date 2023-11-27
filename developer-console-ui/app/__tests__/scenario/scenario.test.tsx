import { act, render } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { MockedProvider } from "@apollo/react-testing";
import { store } from '../../services/store.service';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import Scenario from '../../pages/dco/scenario';
describe('Table render in scenario', () => {
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    useRouter.mockImplementation(() => ({
        pathname: '/dco/scenario',
    }));
    test('table with props', async () => {
        store.getActions().setCount(0);
        useRouter
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <MemoryRouterProvider url="/dco/scenario">
                    <MockedProvider>
                            <Scenario/>
                    </MockedProvider>
                </MemoryRouterProvider>
            </StoreProvider>
        )
        await act(async () => {
            await new Promise((resolve) => setTimeout(resolve, 0));
        });
        store.getActions().setInvert(true);
    });
    beforeEach(() => {
        fetch.resetMocks();
    });

})
