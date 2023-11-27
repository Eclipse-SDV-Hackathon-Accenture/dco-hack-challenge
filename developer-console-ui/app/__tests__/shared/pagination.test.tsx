import '@testing-library/jest-dom';
import { fireEvent, render ,screen} from '@testing-library/react';
import { MemoryRouterProvider } from 'next-router-mock/MemoryRouterProvider';
import { StoreProvider } from 'easy-peasy';
import { store } from '../../services/store.service';
import Pagination from '../../pages/shared/paginationTable';
const props = {
    pageChangeHandler:jest.fn(), totalRows:20, rowsPerPage:10
}
describe("pagination function", () => {
    it("should render pagination", () => {
        render(
          //  @ts-ignore 
      <StoreProvider store={store}>
             <MemoryRouterProvider url="/dco">
                <Pagination {...props}/>
          </MemoryRouterProvider>
          </StoreProvider>
           );
           const checkBtn1 = screen.getByTestId("btn1");
           fireEvent.click(checkBtn1);
           const checkBtn = screen.getByTestId("btn");
           fireEvent.click(checkBtn);
    });
});
