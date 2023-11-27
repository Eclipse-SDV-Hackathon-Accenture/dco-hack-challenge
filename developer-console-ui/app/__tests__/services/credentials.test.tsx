import { store } from "../../services/store.service";
import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks()
jest.mock('next/router', () => require('next-router-mock'));
import fetch from 'jest-fetch-mock'
import { onSubmit } from "../../services/credentials.service";
describe('credentials', () => {
    it('on submit', async () => {
        try {
            return await (onSubmit('dco','dco',jest.fn(),''))
                .then((result) => result)
                .then((res:any)=>{})
        } catch (e) {
            return e;
        }
    })
    beforeEach(() => {
        fetch.resetMocks();
    });
    store.getActions().setPage(1);
})