import { act, render } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { MockedProvider } from "@apollo/react-testing";
import { store } from '../../services/store.service';
import { MemoryRouterProvider } from 'next-router-mock/dist/MemoryRouterProvider';
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { LIST_TRACKS } from '../../services/queries';
import { gql } from '@apollo/client';
import TrackList from '../../pages/dco/tracksMain/trackList';
import ConditionalAlertBtn from '../../pages/shared/conditionalAlertBtn';
import { DELETE_SCENARIO } from '../../services/functionScenario.services';
describe('Table render in scenario', () => {
  const useRouter = jest.spyOn(require('next/router'), 'useRouter');
  useRouter.mockImplementation(() => ({
    pathname: '/dco/scenario',
  }));
  const mockList = [{
    request: {
      query: gql(LIST_TRACKS),
      variables: { trackPattern: '', page: 1, size: 10 }
    },
    result: { data: { searchTrackByPattern: '' } },
  }]
  const mockListDel = [{
    request: {
      query: DELETE_SCENARIO,
      variables: { id: '' }
    },
    result: { data: { deleteTrack: "Track deleted" } },
  }]
  const props = { path: 'sim' }
  test('table with props', async () => {
    store.getActions().setCount(0);
    useRouter
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/scenario">
          <MockedProvider mocks={mockList}>
            <TrackList {...props} />
            <ConditionalAlertBtn></ConditionalAlertBtn>
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
    await act(async () => {
      await new Promise((resolve) => setTimeout(resolve, 0));
    });
    store.getActions().setInvert(true);
  });

  test('delete track render', () => {
    render(
      //  @ts-ignore 
      <StoreProvider store={store}>
        <MemoryRouterProvider url="/dco/track">
          <MockedProvider mocks={mockListDel}>
            <TrackList {...props} />
          </MockedProvider>
        </MemoryRouterProvider>
      </StoreProvider>
    )
  });
  beforeEach(() => {
    fetch.resetMocks();
  });

})
