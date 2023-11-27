import '@testing-library/jest-dom';
import { render } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import Home from '../pages';
import { store } from '../services/store.service';

describe("Index page", () => {
  it("should render index", () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <Home />
      </StoreProvider>
    );
    store.getActions().setInvert(true);
  });
  it("should render count", () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <Home />
      </StoreProvider>
    );
    store.getActions().setCount(0);
  });
  it("should render id", () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <Home />
      </StoreProvider>
    );
  });
  it("should render spinner", () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco">
          <Home />
        </MemoryRouterProvider>
      </StoreProvider>
    );
  });
});
