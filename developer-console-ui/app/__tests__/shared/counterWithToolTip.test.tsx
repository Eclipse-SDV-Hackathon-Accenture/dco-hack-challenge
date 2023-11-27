import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { StoreProvider } from 'easy-peasy';
import { store } from '../../services/store.service';
import CounterWithToolTip from '../../pages/shared/counterWithToolTip';
import { render } from '@testing-library/react';

describe('CounterWithToolTip Page', () => {
    const mockPerformanceMark = jest.fn();
    window.performance.mark = mockPerformanceMark;
    jest.useFakeTimers();
    beforeEach(() => {
        fetch.resetMocks();
    });

    test('add CounterWithToolTip page render', async () => {
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <CounterWithToolTip toolTipVal={["1"]} />
            </StoreProvider>
        )
    });
    test('add CounterWithToolTip page render', async () => {
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <CounterWithToolTip toolTipVal={["1", "2"]} />
            </StoreProvider>
        )
    });
}) 
