import { render } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { Status } from '../../pages/shared/status';
import { store } from '../../services/store.service';
describe('Status Page', () => {
    test('statuses of vehicles in track add and details page (Vehicle DDetails-VD)', async () => {
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'Testing'} type={'VD'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'READY'} type={'VD'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'DRAFT'} type={'VD'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'TEST'} type={'VD'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'SUSPEND'} type={'VD'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'PRODUCTION'} type={'VD'} />
            </StoreProvider>
        )
    });
 
    test('status of simulations for simulation listing page(Simulation Status-SS)', async () => {
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'Done'} type={'SS'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'Pending'} type={'SS'} />
            </StoreProvider>
        )
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'Running'} type={'SS'} />
            </StoreProvider>
        )  
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'Timeout'} type={'SS'} />
            </StoreProvider>
        )  
        render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <Status status={'Error'} type={'SS'} />
            </StoreProvider>
        )
    });
}) 
