import { action, createStore, persist } from "easy-peasy";

export const store = createStore(persist({
  invert: true,
  setInvert: action((state: any, payload: any) => {
    state.invert = payload;
  }),
  count: 0,
  setCount: action((state: any, payload: any) => {
    state.count = payload;
  }),
  tname: 'Track',
  setTname: action((state: any, payload: any) => {
    state.tname = payload;
  }),
  tid: '0',
  setTid: action((state: any, payload: any) => {
    state.tid = payload;
  }),
  compid: '',
  setCompid: action((state: any, payload: any) => {
    state.compid = payload;
  }),
  
  page: '',
  setPage: action((state: any, payload: any) => {
    state.setPage = payload;
  }),
  selectedscenario: [{id:'1234',checked:false}],
  setSelectedscenario: action((state: any, payload: any) => {
      state.selectedscenario = payload;
  }),
  selectedtrack: [{id:'5678',checked:false}],
  setSelectedtrack: action((state: any, payload: any) => {
      state.selectedtrack = payload;
  }),
  searchval:"",
  setSearchval: action((state: any, payload: any) => {
    state.searchval = payload;
  }),
  user:false,
  setUser:action((state: any, payload: any) => {
    state.user = payload;
  }),
}));
