import { enableFetchMocks } from 'jest-fetch-mock'
import { fireEvent, render, screen } from '@testing-library/react';
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { store } from '../services/store.service';
import { StoreProvider } from 'easy-peasy';
import Logout from '../pages/logout';
describe('logout', () => {
  it("should render logout app", () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <Logout />
      </StoreProvider>
    );
    const login = screen.getByTestId("loginBtn");
    fireEvent.click(login);
    store.getActions().setCount(0);
  });
  beforeEach(() => {
    fetch.resetMocks();
  });
  store.getActions().setPage(1);
})

