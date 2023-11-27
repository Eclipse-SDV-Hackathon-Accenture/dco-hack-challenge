import { gql } from '@apollo/client'
// scenario start
export const GET_SCENARIO = `
  query SEARCH_SCENARIO( $scenarioPattern: String, $page: Int, $size: Int) {
    searchScenarioByPattern(scenarioPattern: $scenarioPattern,page: $page, size: $size) {
      content {
        id
        name
        description
        type
        status
        createdBy
        createdAt
        lastModifiedBy
        lastModifiedAt
        file {
          id
          path
          size
          checksum
          updatedBy
          updatedOn
        }
      }
      empty
      first
      last
      page
      size
      pages
      elements
      total
    }
  }
`
// scenario end

// tracks start
export const LIST_TRACKS = `query SEARCH_TRACK($trackPattern: String, $page: Int, $size: Int) {
  searchTrackByPattern(trackPattern: $trackPattern, page: $page, size: $size) {
   content{
     id
     name
     state
     trackType
     vehicles {
       vin
       country
     }
   }
   empty
   first
   last
   page
   size
   pages
   elements
   total
  }
 }
 `
export const VEHICLE_LIST = gql`
  query LIST_VEHICLE($search: String, $query: String, $page: Int, $size: Int, $sort: [String]) {
    vehicleReadByQuery(search: $search, query: $query, page: $page, size: $size, sort: $sort) {
      content {
        vin
        owner
        ecomDate
        country
        model
        brand
        region
        instantiatedAt
        createdAt
        updatedAt
        status
        type
        fleets {
          id
          name
          type
        }
        services {
          serviceId
          operation
          updatedAt
        }
        devices {
          id
          type
          status
          createdAt
          gatewayId
          dmProtocol
          modifiedAt
          dmProtocolVersion
        }
      }
      empty
      first
      last
      page
      size
      pages
      elements
      total
    }
  }
`
export const DELETE_TRACK = gql`
  mutation DELETE_TRACK($id: ID!) {
    deleteTrackById(id: $id)
  }
`
export const TRACK_DETAILS = gql`
  query TRACK_BY_ID($id: ID!) {
    findTrackById(id: $id) {
      id
      name
      state
      trackType
      duration
      description
      vehicles {
        vin
        country
        brand
        status
        updatedAt
        type
        devices {
          id
          type
          status
          createdAt
          gatewayId
          modelType
          dmProtocol
          modifiedAt
          dmProtocolVersion
          serialNumber
          components {
            id
            name
            status
            version
            environmentType
          }
        }
      }
    }
  }
`
export const CREATE_TRACK = gql`
  mutation CREATE_TRACK($trackInput: TrackInput) {
    createTrack(trackInput: $trackInput) {
      id
      name
      trackType
      state
      description
      duration
    }
  }
`
// tracks end

// simulation start
export const GET_SIMULATIONS = `
  query LIST_SIMULATION($search: String, $query: String, $page: Int, $size: Int, $sort: [String]) {
    simulationReadByQuery(search: $search, query: $query, page: $page, size: $size, sort: $sort){
      content {
        id
        name
        status
        environment
        platform
        scenarioType
        noOfVehicle
        noOfScenarios
        brands
        hardware
        description
        createdBy
        startDate
      }
      empty
      first
      last
      page
      size
      pages
      elements
      total
    }
  }
`
export const LAUNCH_SIMULATION = gql`
  mutation LAUNCH_SIMULATION($simulationInput: SimulationInput) {
    launchSimulation(simulationInput: $simulationInput)
  }
`
export const HARDWARE_MODULE = gql`
  query GET_HARDWARE_MODULE {
    getHardwareModule
  }
`
// simulation end
