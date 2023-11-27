import { avoidSplChars, checkRoute, displayBrandVal, getValArray, onLoadMore } from "../../services/functionShared";
import { store } from "../../services/store.service";

describe('shared test cases', () => {
    it('onLoadMore', async () => {
        expect(onLoadMore(jest.fn(), jest.fn(), jest.fn(), 1, 1)).toBe(undefined);
    })
    it('getValArray', async () => {
        expect(getValArray([""], "")).toEqual([]);
    }),
        it('tests displayBrandVal', () => {
            expect(displayBrandVal("Audi")).toBe('logo-audi');
            expect(displayBrandVal("Mercedes")).toBe('mercedes');
            expect(displayBrandVal("BMW")).toBe('logo-bmw');
            expect(displayBrandVal("eGO")).toBe('logo-ego');
            expect(displayBrandVal("Ford")).toBe('logo-ford');
            expect(displayBrandVal("Mitsubishi")).toBe('logo-mitsubishi');
            expect(displayBrandVal("Nissan")).toBe('logo-nissan');
            expect(displayBrandVal("Porsche")).toBe('logo-porsche');
            expect(displayBrandVal("Renault")).toBe('logo-renault');
            expect(displayBrandVal("Skoda")).toBe('logo-skoda');
            expect(displayBrandVal("Volvo")).toBe('logo-volvo');
            expect(displayBrandVal("VW")).toBe('logo-vw');
            expect(displayBrandVal("Volkswagen")).toBe('logo-vw');
            expect(displayBrandVal("test")).toBe('vehicle');
        })
    it('not allowing special characters', () => {
        expect(avoidSplChars(jest.fn())).toBe(undefined);
    })
    it('check route path', () => {
        const useRouter = jest.spyOn(require('next/router'), 'useRouter');
        useRouter.mockImplementation(() => ({
            pathname: '/dco',

        }));
        const useRouter2 = jest.spyOn(require('next/router'), 'useRouter');
        useRouter.mockImplementation(() => ({
            pathname: '/dco/tracksMain',

        }));
        const useRouter3 = jest.spyOn(require('next/router'), 
        'useRouter');
        useRouter.mockImplementation(() => ({
            pathname: '/dco/simulation',

        }));
        expect(checkRoute('/dco', useRouter, '/dco/scenario')).toBe("/dco");
        expect(checkRoute('/dco/tracksMain', useRouter2, '/dco/tracksMain')
        ).toBe("/dco/tracksMain");
        expect(checkRoute('/dco/simulation', useRouter3, '/dco/simulation')
        ).toBe("/dco/simulation");

    })
    store.getActions().setPage(1);
})