import { act, fireEvent, render,screen } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { MockedProvider } from "@apollo/react-testing";
import { store } from '../../services/store.service';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import NewScenario from '../../pages/dco/addEditScenario/newScenario';

describe('Table render in scenario', () => {
  const props = { show: true, onClose: jest.fn(), path: 'create', cellData: { scenario: 'scenario', description: 'test', type: 'MQTT', sid: '12344', filename: 'test.txt' }, setToastOpenScenario: jest.fn(), setSuccessMsgScenario: jest.fn() }

  const props2 = { show: true, onClose: jest.fn(), path: 'update', cellData: { scenario: 'scenario', description: 'test', type: 'CAN', sid: '12344', filename: 'test.txt' }, setToastOpenScenario: jest.fn(), setSuccessMsgScenario: jest.fn() }
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
          <MockedProvider addTypename={false}>
            <NewScenario {...props} />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    const checkBtn = screen.getByTestId("btn1");
    fireEvent.click(checkBtn);
    await act(async () => {
      await new Promise((resolve) => setTimeout(resolve, 0));
    });
  });
  test('table with props', async () => {
    store.getActions().setCount(0);
    useRouter
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/scenario">
          <MockedProvider addTypename={false}>
            <NewScenario {...props2} />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    // const checkBtn = screen.getByTestId("btn2");
    // fireEvent.click(checkBtn);
    await act(async () => {
      await new Promise((resolve) => setTimeout(resolve, 0));
    });
  });
  beforeEach(() => {
    fetch.resetMocks();
  });

})
