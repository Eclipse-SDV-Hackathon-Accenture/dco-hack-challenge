import { fireEvent, render } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { store } from '../../services/store.service';
import ConditionalAlertBtn from '../../pages/shared/conditionalAlertBtn';
describe('conditionalAlertBtn', () => {
    const props = {
        show: true,
        popupState: '',
        popupMsg: '',
        no: '',
        onClose: jest.fn(),
        respectiveFun: jest.fn(),
        yes: ''
    }
    test('conditionalAlertBtn render', async () => {
        const component = render(
            //  @ts-ignore 
      <StoreProvider store={store}>
                <ConditionalAlertBtn show="true" onClose={jest.fn()} respectiveFun={jest.fn()} props={props} />
            </StoreProvider>
        )
        const closeAlert = component.getByTestId("closeAlert");
        fireEvent.click(closeAlert);
        const closeAlert1 = component.getByTestId("closeAlert1");
        fireEvent.click(closeAlert1);
        const deleteTrack = component.getByTestId("deleteTrack");
        fireEvent.click(deleteTrack);
    });
}) 