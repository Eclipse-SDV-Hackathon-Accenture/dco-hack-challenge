import '@testing-library/jest-dom';
import { cleanup, fireEvent, render, screen } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import React from 'react';
import Layout from '../components/layout/layout';
import { store } from '../services/store.service';

describe('<Layout>', () => {
  const useRouter = jest.spyOn(require('next/router'), 'useRouter');
  useRouter.mockImplementation(() => ({
    pathname: '/dco',
    push: jest.fn()
  }));
  afterEach(cleanup);
  it("should render index", () => {
    render(
      //  @ts-ignore
      //  @ts-ignore 
      <StoreProvider store={store}>
      <MemoryRouterProvider url="/dco">
        <Layout />
      </MemoryRouterProvider>
    </StoreProvider>);
    store.getActions().setInvert(true);
    store.getActions().setUser(true);
  });

  it("should fire an event on button click", () => {
    useRouter
    render(
        //  @ts-ignore
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco">
          <Layout />
        </MemoryRouterProvider>
      </StoreProvider>);
    store.getActions().setInvert(true);
    const logoInHeader = screen.getByTestId('logo');
    fireEvent.click(logoInHeader)
    store.getActions().setUser(true);

  });
  it("should fire an event on check click", () => {
    render(
        //  @ts-ignore
      //  @ts-ignore 
      <StoreProvider store={store}>  <MemoryRouterProvider url="/dco">
        <Layout />
      </MemoryRouterProvider>
      </StoreProvider>);
    store.getActions().setUser(true);

  });
  it("check if logo is there", () => {
    render(
      //  @ts-ignore 
      //  @ts-ignore 
      <StoreProvider store={store}>  <MemoryRouterProvider url="/dco">
        <Layout />
      </MemoryRouterProvider>
      </StoreProvider>);
    const logoInHeader = screen.getAllByTestId('logo');
    expect(logoInHeader.length).toBe(1);
    store.getActions().setUser(true);

  });
  it("check if logo is there2", () => {
    render(
      //  @ts-ignore 
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/scenario">
          <Layout />
        </MemoryRouterProvider>
      </StoreProvider>);
    const logoInHeader = screen.getAllByTestId('logo');
    expect(logoInHeader.length).toBe(1);
    store.getActions().setUser(true);

  });
  // test('checks if logo rendered', () => {

  // });
  // it("should handle if else", () => {
  //   render(   <MemoryRouterProvider url="/dco">
  //   <Layout />
  // </MemoryRouterProvider>);
  //   const btnClick = screen.getByTestId("logout");
  //   fireEvent.click(btnClick);
  // });
  // customHeaderRender(<Layout />);

  // const children = screen.getByText('children');

  // test('checks if children rendered', () => {
  //   expect(children).toBeDefined();
  // });

  // test('checks if vehicles nav link is active', () => {
  //   expect(within(header).getByTestId('header-active-link').textContent).toBe('vehicles');
  // });

  // test('checks if displays authenticated user name', () => {
  //   expect(within(header).getByTestId('header-user-name').textContent).toBe('test_user');
  // });

  // test('checks if header menu rendered', () => {
  //   const menuInHeader = within(header).getAllByTestId('header-menu');

  //   expect(menuInHeader.length).toBe(1);
  // });

  // test('checks if navigation rendered', () => {
  //   const navigationInHeader = within(header).getAllByTestId('header-navigation');

  //   expect(navigationInHeader.length).toBe(1);
  // });


});
