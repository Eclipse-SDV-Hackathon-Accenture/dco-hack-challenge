import router from 'next/router'
import { Link } from '../libs/apollo'
import { LIST_TRACKS } from './queries'
import { setTimeOutFunction } from './functionShared'
import {
  ComponentTypes,
  GetVehicleListType,
  RawDataTrackType,
  VehicleTypes,
} from '../types'
import { useStoreState } from 'easy-peasy'
//  track tab data start****
export const trackRowData = (rawData: RawDataTrackType) =>
  rawData?.data?.searchTrackByPattern?.content?.map((item: any) => {
    return {
      check: '',
      trackID: item.id,
      trackName: item.name,
      trackNameSim: item.name,
      trackStatus: item.state,
      trackType: item.trackType,
      numberofvehicles: item.vehicles.length,
      country: [...new Set(item.vehicles.map((items: any) => items.country))],
      delete: '',
    }
  })
export const getTrackData = async (pageNo: any, searchval: any) => {
  if (searchval != '') {
    pageNo = 1
  }
  const token = localStorage.getItem('token');
  return fetch(Link, {
    method: 'POST',
    mode: 'cors',
    headers: {
      'content-type': 'application/json',
      'Authorization': token ? `Basic ${token}` : "",
    },
    body: JSON.stringify({
      query: LIST_TRACKS,
      variables: { trackPattern: searchval, page: pageNo - 1, size: 10 },
    }),
  })
    .then((res) => res.json())
    .then((result) => result)
    .catch((error) => {
      console.log('Error fetching data:::', error.message)
    })
}
//  track tab data end****
export function Selectedtrack() {
  return useStoreState((state: any) => state.selectedtrack)
}
export function Searchval() {
  return useStoreState((state: any) => state.searchval);
}
export function uploadFile(files: any) {
  let fileSize: any = 0
  for (let val of files) {
    fileSize += val.size
  }
  fileSize = (fileSize / 1024 / 1024).toFixed(2)
  return parseFloat(fileSize)
}
export function onClickDeleteTrack(trackID: string, deleteTrack: Function, setShowAlert: Function) {
  deleteTrack({
    variables: {
      id: trackID,
    },
  })
  setShowAlert(false)
}
export function setToastMessageForDeleteTrack(data: any, setToastMsg: Function, setToastOpen: Function, type: string) {
  setToastOpen(true)
  setTimeout(() => {
    setToastOpen(false)
  }, 2000)
  if (type == 'success') {
    setTimeOutFunction(setToastOpen, 3000)
    setToastMsg('Track has been deleted successfully')
    setTimeout(() => {
      window.location.reload()
    }, 2000)
    return true
  } else {
    setTimeOutFunction(setToastOpen, 3000)
    setToastMsg(data.message)
    return false
  }
}
export function getToolTip(country: any) {
  if (country.length == 1) {
    return country[0].country || country[0]
  } else {
    return country.reduce((a: any, b: any) => {
      return (a.country || a) + ', ' + (b.country || b)
    })
  }
}
export function onselectionchange(
  selectedRows: any,
  setArrVehicle: Function,
  vehicles: any,
  onSelectionChangedFun: any,
  gridRef: any,
  setonSelectedRows: Function
) {
  setArrVehicle([])
  selectedRows.map((val: { data: { vin: any; country: any } }) => {
    vehicles.push({
      vin: val.data.vin,
      country: val.data.country,
    })
    onSelectionChangedFun(gridRef, setonSelectedRows)
    return setArrVehicle(vehicles)
  })
}
export function saveNewTrack(title: string, arrVehicle: any, duration: string, description: string, createTrack: Function, setVariable: any) {
  if (title && arrVehicle.length != 0 && duration) {
    createTrack({
      variables: {
        trackInput: {
          name: title,
          description: description,
          trackType: 'Test',
          duration: duration,
          vehicles: arrVehicle,
          state: 'CREATED',
        },
      },
    })
  } else {
    setVariable.setTitleError(true)
    setVariable.setDurationError(true)
    setVariable.setVehicleError(true)
  }
}
export function onClickTrackColumn(index: any, setTname: Function, setTid: Function) {
  setTname(index?.row?.values?.trackName)
  setTid(index?.row?.values?.trackID)
  router.push(`/dco/tracksMain/trackInfo/trackVehicleDetails`)
}
export function mapDataToTable(data: any, setDatas: any, datas: any) {
  let mapDatas: any
  mapDatas = data?.vehicleReadByQuery?.content?.map((val: any) => {
    return {
      vin: val?.vin,
      type: val?.type,
      lastconnected: val?.updatedAt,
      country: val?.country,
      brand: val?.brand,
      model: val?.model,
      status: val?.status,
    }
  })
  if ((datas.length != 0 || datas.length == 0) && mapDatas) {
    setDatas([...datas, ...mapDatas])
  }
}
export function deleteTrackFun(
  setShowAlert: Function,
  setdeleteTrackId: Function,
  isShowAlert: boolean,
  trackId: string
) {
  setShowAlert(!isShowAlert)
  setdeleteTrackId(trackId)
}
export function getCompNVersion(deviceArr: any, arrType: any, arrComponent: any) {
  deviceArr?.map((val: any) => {
    val.map((x: any) => {
      arrComponent.push(x.components)
      arrType.push(x.id)
    })
  })
  return arrType
}
export function getDevices(arrComponent: any, compVal: any) {
  arrComponent?.map((val: any) => {
    val.map((x: any) => {
      compVal.push({ name: x.name, version: x.version })
    })
  })
  return compVal
}
export function onSelectionChanged(gridRef: any, setonSelectedRows: Function) {
  const checkedRows = gridRef.current!.api.getSelectedRows()
  if (checkedRows) {
    setonSelectedRows([...checkedRows])
  }
}
export function selectedCheckboxFunction(onSelectedRows: any, gridRef: any) {
  let temp = onSelectedRows.map((data: any) => {
    return data.vin
  })
  if (temp.length != 0) {
    gridRef.current!.api &&
      gridRef.current!.api.forEachNode((node: any) => {
        temp.includes(node.data['vin']) ? node.setSelected(true) : node.setSelected(false)
      })
  }
}
export function getUploadFormDataForNewComponent(
  selectedUploadFile: any,
  deviceValue: [],
  componentValue: string,
  typeValue: string,
  versionValue: string,
  sessionUser: any,
  HWValue: any
) {
  let formData = new FormData()
  // formData.append('operations', `{\"operationName\": \"CREATE_COMPONENT\",\"variables\":{},\"query\": \"mutation CREATE_COMPONENT {\\n createComponent(componentInput: {name: \\"${componentValue}\\", type: \\"${typeValue}\\", status: DRAFT, version: \\"${versionValue}\\", updatedBy: \\"${sessionUser}\\", targetHardwareModule: [\\"${HWValue}\\"], targetDevices: [\\"${deviceValue}\\"]})\\n}\\n"}`)
  formData.append(
    'operations',
    `{ "query": "mutation CREATE_COMPONENT($file: Upload!, $componentInput: ComponentInput) {createComponent(file: $file, componentInput: $componentInput)}" , "variables": {"file": null, "componentInput": {"name": "${componentValue}", "type": "${typeValue}", "status": "DRAFT", "version": "${versionValue}", "targetDevices": ["${deviceValue}"], "targetHardwareModule": ["${HWValue}"], "updatedBy": "${sessionUser}"}}}`
  )
  if (selectedUploadFile) {
    formData.append('file', selectedUploadFile[0])
  }
  formData.append('map', '{"file": ["variables.file"]}')

  return formData
}
export function onClickMenuItem(setShowAlert: Function) {
  setShowAlert(true)
}
export function onCompletedCreateTrack(
  setButtonDisable: any,
  setIsToastOpen: any,
  setToastMsg: any,
  res: any,
  flag: boolean
) {
  if (flag) {
    setButtonDisable(true)
    setIsToastOpen(true)
    setToastMsg('Track has been added successfully')
    setTimeout(() => {
      router.push('/dco/tracksMain')
    }, 2500)
  } else {
    setButtonDisable(false)
    setIsToastOpen(true)
    setToastMsg(JSON.parse(JSON.stringify(res)).message)
    setTimeout(() => {
      setIsToastOpen(false)
      router.push('/dco/tracksMain')
    }, 6000)
  }
}
export function onClickNewTrack(setCompid: Function) {
  setTimeout(() => {
    router.push('/dco/addTrack')
    setCompid('')
  }, 0)
}
export function getVehicleList(data: GetVehicleListType) {
  return data?.findTrackById?.vehicles?.map((val: VehicleTypes) => {
    return {
      vin: val.vin,
      type: val.type,
      lastconnected: val.updatedAt,
      country: val.country,
      status: val.status,
      brand: val.brand || undefined,
      devices: val?.devices?.map((x: any) => {
        return {
          type: x.type,
          id: x.id,
          components: x?.components.map((y: ComponentTypes) => {
            return {
              name: y.name,
              version: y.version,
            }
          }),
        }
      }),
    }
  })
}
