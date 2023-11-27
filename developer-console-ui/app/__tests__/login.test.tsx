import { enableFetchMocks } from 'jest-fetch-mock'
import { fireEvent, render, screen } from '@testing-library/react';
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { store } from '../services/store.service';
import { StoreProvider } from 'easy-peasy';
import Login from '../pages/login';
describe('login ', () => {
  it("should render login page", () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <Login />
      </StoreProvider>
    );
    const submit = screen.getByTestId("submitBtn");
    fireEvent.click(submit);
    store.getActions().setCount(0);
  });
  beforeEach(() => {
    fetch.resetMocks();
  });
  store.getActions().setPage(1);
})

