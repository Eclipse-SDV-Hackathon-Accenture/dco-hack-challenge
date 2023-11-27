import { useStoreState } from 'easy-peasy'

export function setTimeOutFunction(setToastOpen: Function, timer: number) {
  setTimeout(() => {
    setToastOpen(false)
  }, timer)
}
export function onLoadMore(setPageSize: Function, setPage: Function, getVehicle: Function, pageSize: number, page: number) {
  setPageSize(pageSize)
  setPage(page + 1)
  getVehicle()
}
export function capitalizeFirstLetter(status: string) {
  return status[0].toUpperCase() + status.slice(1)
}
export function displayBrandVal(brand: string) {
  if (brand == 'Audi') {
    return 'logo-audi'
  }
  if (brand == 'Mercedes' || brand == 'Mercedes Benz' || brand == 'Mercedes-Benz') {
    return 'mercedes'
  }
  if (brand == 'BMW') {
    return 'logo-bmw'
  }
  if (brand == 'eGO') {
    return 'logo-ego'
  }
  if (brand == 'Ford') {
    return 'logo-ford'
  }
  if (brand == 'Mitsubishi') {
    return 'logo-mitsubishi'
  }
  if (brand == 'Nissan') {
    return 'logo-nissan'
  }
  if (brand == 'Porsche') {
    return 'logo-porsche'
  }
  if (brand == 'Renault') {
    return 'logo-renault'
  }
  if (brand == 'Skoda') {
    return 'logo-skoda'
  }
  if (brand == 'Volvo') {
    return 'logo-volvo'
  }
  if (brand == 'VW' || brand == 'Volkswagen') {
    return 'logo-vw'
  }
  return 'vehicle'
}
export function getValArray(datas: any, objName: string) {
  let temp = [
    ...new Set(
      datas?.map((val: any) => {
        return val[objName]
      })
    ),
  ]
  temp = temp
    .filter(function (element) {
      return element !== null
    })
    .filter((v, i, a) => a.indexOf(v) === i)
  return temp
}
export function invert() {
  return useStoreState((state: any) => state.invert)
}
export function avoidSplChars(e: any) {
  var bad = /[^\sa-z\d]/i,
    key = String.fromCharCode(e.keyCode || e.which)
  if (e.which !== 0 && e.charCode !== 0 && bad.test(key)) {
    e.returnValue = false
    if (e.preventDefault) {
      return e.preventDefault()
    }
  }
}
export function checkRoute(path:string,router:any, pathname:any){
 return (router?.pathname?.includes(path) ? pathname : path)
}
