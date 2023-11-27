import '@testing-library/jest-dom';
import { render } from '@testing-library/react';
import { StoreProvider } from 'easy-peasy';
import { store } from '../../services/store.service';
import BoxToast from '../../components/layout/boxToast';
import SuccessToast from '../../components/layout/successToast';
import ErrorToast from '../../components/layout/errorToast';
const props = {
    toastMsg: "Package has been created successfully"
}
const propsError = {
    toastMsg: "There is an error"
}
const includes = props.toastMsg?.includes("has been deleted successfully");
const status = (props.toastMsg == "Package has been created successfully" || props.toastMsg == "Track has been added succesfully" || includes)

describe("Common toast message", () => {
    it("should render toast message: Package has been created successfully", () => {
        if (status == true) {
            render(
                //  @ts-ignore 
                <StoreProvider store={store}>
                    <BoxToast toastMsg={props.toastMsg}></BoxToast>
                </StoreProvider>
            );
            render(
                //  @ts-ignore 
                <StoreProvider store={store}>
                    <SuccessToast toastMsg={props.toastMsg}></SuccessToast>
                </StoreProvider>
            );
        }
        else {
            render(
                //  @ts-ignore 
                <StoreProvider store={store}>
                    <BoxToast toastMsg={props.toastMsg}></BoxToast>
                </StoreProvider>
            );
            render(
                //  @ts-ignore 
                <StoreProvider store={store}>
                    <ErrorToast toastMsg={props.toastMsg}></ErrorToast>
                </StoreProvider>
            );
        }
    });
    it("should render toast message: There is an error", () => {
        if (status == true) {
            render(
                //  @ts-ignore 
                <StoreProvider store={store}>
                    <BoxToast toastMsg={propsError.toastMsg}></BoxToast>
                </StoreProvider>
            );
            render(
                //  @ts-ignore 
                <StoreProvider store={store}>
                    <SuccessToast toastMsg={propsError.toastMsg}></SuccessToast>
                </StoreProvider>
            );
        }
        else {
            render(
                //  @ts-ignore 
                <StoreProvider store={store}>
                    <BoxToast toastMsg={propsError.toastMsg}></BoxToast>
                </StoreProvider>
            );
            render(
                //  @ts-ignore 
                <StoreProvider store={store}>
                    <ErrorToast toastMsg={propsError.toastMsg}></ErrorToast>
                </StoreProvider>
            );
        }
    });
});
