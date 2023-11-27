import { enableFetchMocks } from 'jest-fetch-mock'
import { cleanup, fireEvent, render, screen } from '@testing-library/react';
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { store } from '../services/store.service';
import Dco from '../pages/dco';
import { StoreProvider } from 'easy-peasy';
describe('dco', () => {
  it("should render dco", () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <Dco />
      </StoreProvider>
    );
    store.getActions().setCount(0);
  });
  beforeEach(() => {
    fetch.resetMocks();
  });
  store.getActions().setPage(1);
})

