import router from "next/router";
import {  Link } from '../libs/apollo'
import { ClearAllTypes, RawDataSimType } from "../types";
import { GET_SIMULATIONS } from "./queries";
// Simulation data start****
export const simRowData = (rawData: RawDataSimType) =>
  rawData?.data?.simulationReadByQuery?.content?.map((item: any) => {
    return {
      name: item.name,
      status: item.status,
      numberVehicles: item.noOfVehicle,
      brand: item.brands,
      type: item.scenarioType,
      numberScenarios: item.noOfScenarios,
      platform: item.platform,
      env: item.environment,
      date: new Date(item.startDate).toLocaleDateString() +
        ', ' +
        new Date(item.startDate).toLocaleTimeString(),
    }
  })
export const getSimData = async (pageNo: number) => {
  const token = localStorage.getItem('token');
  return fetch(Link, {
    method: 'POST',
    headers: {
      'content-type': 'application/json',
      'Authorization': token ? `Basic ${token}` : "",
    },
    body: JSON.stringify({
      query: GET_SIMULATIONS,
      variables: {
        search: null,
        query: null,
        page: pageNo - 1,
        size: 10,
        sort: "DESC"
      },
    }),
  })
    .then((res) => res.json())
    .then((result) => result).catch(error => {
      console.log("Error fetching data:::", error.message);
    })
}
//  Simulation data end****

export function launchSimulation(variable: any, createSimulation: Function, setVariable: any) {
  variable.scenario = variable.scenario.filter((c: any) => { if (c.checked) { return c.id } }).map((l: any) => l.id)
  variable.track = variable.track.filter((c: any) => { if (c.checked) { return c.id } }).map((l: any) => l.id)
  if (variable.title && variable.scenarioType && variable.scenario.length != 0 && variable.track.length != 0) {
    createSimulation({
      variables: {
        simulationInput: {
          name: variable.title,
          environment: variable.environment,
          description: variable.description,
          platform: variable.platform,
          scenarioType: variable.scenarioType,
          hardware: variable.hardware,
          tracks: variable.track,
          scenarios: variable.scenario,
          createdBy: "abc@t-systems.com"
        },
      },
    })
  } else {
    setVariable.setTitleError(true)
    setVariable.setSTypeError(true)
    setVariable.setTrackError(true)
    setVariable.setScenarioError(true)
  }
}
export function onLaunchedSimulation(setSelectedscenario: Function, setSelectedtrack: Function, setIsToastOpen: Function, setToastMsg: Function, res: any, flag: boolean) {
  if (flag) {
    setIsToastOpen(true)
    setToastMsg('Simulation has been launched successfully')
    setTimeout(() => {
      router.push('/dco/simulation')
      setSelectedscenario([{ id: '1234', checked: false }])
      setSelectedtrack([{ id: '5678', checked: false }])
    }, 2500)

  } else {
    setIsToastOpen(true)
    setToastMsg(JSON.parse(JSON.stringify(res)).message)
    setTimeout(() => {
      router.push('/dco/simulation')
      setSelectedscenario([{ id: '1234', checked: false }])
      setSelectedtrack([{ id: '5678', checked: false }])
    }, 3000)
  }
}
export function clearAll(setVariable: ClearAllTypes) {
  setVariable.setTitle('');
  setVariable.setDescription('');
  setVariable.setEnvironment('');
  setVariable.setPlatform('');
  setVariable.setSelectedscenario([{ id: '1234', checked: false }]);
  setVariable.setSelectedtrack([{ id: '5678', checked: false }]);
  setVariable.setScenarioType('');
  setVariable.setHardware('');
  setVariable.setSearchval('');
  setVariable.setTitleError(false);
  setVariable.setSTypeError(false);
  setVariable.setTrackError(false);
  setVariable.setScenarioError(false);
}
export function onClickNewSimulation() {
  setTimeout(() => {
    router.push('/dco/addSimulation')
  }, 0)
}