import { enableFetchMocks } from 'jest-fetch-mock'
import { fireEvent, render, screen } from '@testing-library/react';
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { store } from '../services/store.service';
import { StoreProvider } from 'easy-peasy';
import Error from '../pages/error';
import { onSubmit } from '../services/credentials.service';
describe('error page', () => {
  it("should render error page and fire submit btn", async () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <Error />
      </StoreProvider>
    );
    const submit = screen.getByTestId("submitBtn");
    fireEvent.click(submit);
    try {
      return await (onSubmit('dco','dco',jest.fn(),''))
          .then((result) => result)
          .then((res:any)=>{})
  } catch (e) {
      return e;
  }
    store.getActions().setCount(0);
  });
  beforeEach(() => {
    fetch.resetMocks();
  });
  store.getActions().setPage(1);
})

