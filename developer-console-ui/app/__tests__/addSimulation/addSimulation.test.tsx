import { act, fireEvent, render, screen } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { MockedProvider } from "@apollo/react-testing";
import { store } from '../../services/store.service';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import AddSimulation from '../../pages/dco/addSimulation';
import Simulation from '../../pages/dco/simulation';
import { GET_SIMULATIONS } from '../../services/queries';
import { gql } from '@apollo/client';

describe('Table render in scenario', () => {
  const useRouter = jest.spyOn(require('next/router'), 'useRouter');
  useRouter.mockImplementation(() => ({
    pathname: '/dco/simulation',
  }));
  const mockList = [{
    request: {
      query: gql(GET_SIMULATIONS),
      variables: {
        search: null,
        query: null,
        page: 1,
        size: 10,
        sort: null,
      }
    },
    result: { data: { simulationReadByQuery: { pages: 1, total: 2 } } },
  }]
  test('table with props backbutton', async () => {
    store.getActions().setCount(0);
    useRouter
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/addSimulation">
          <MockedProvider addTypename={false}>
            <AddSimulation />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
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
        <MemoryRouterProvider url="/dco/addSimulation">
          <MockedProvider addTypename={false}>
            <AddSimulation />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    const checkBtn = screen.getByTestId("backButton");
    fireEvent.click(checkBtn);
    useRouter
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/simulation">
          <MockedProvider mocks={mockList}>
            <Simulation />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    await act(async () => {
      await new Promise((resolve) => setTimeout(resolve, 0));
    });
  });
  beforeEach(() => {
    fetch.resetMocks();
  });

})
