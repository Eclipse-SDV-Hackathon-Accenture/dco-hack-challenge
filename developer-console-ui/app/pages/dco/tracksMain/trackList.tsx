import { Box, Check, Icon, Table, Toast } from '@dco/sdv-ui'
import { useStoreActions } from 'easy-peasy'
import { useEffect, useState } from 'react'
import { deleteTrackFun, getTrackData, onClickDeleteTrack, onClickTrackColumn, Searchval, Selectedtrack, setToastMessageForDeleteTrack, trackRowData } from '../../../services/functionTrack.service'
import CounterWithToolTip from '../../shared/counterWithToolTip'
import ConditionalAlertBtn from '../../shared/conditionalAlertBtn'
import { useMutation } from '@apollo/client'
import { DELETE_TRACK } from '../../../services/queries'
import BoxToast from '../../../components/layout/boxToast'
import { Pagination } from '../../shared/paginationTable'

// track table on listing page
const TrackList = ({ path }: any) => {
    const setSelectedtrack = useStoreActions((actions: any) => actions.setSelectedtrack)
    const theArray = Selectedtrack();
    const setTid = useStoreActions((actions: any) => actions.setTid)
    const setTname = useStoreActions((actions: any) => actions.setTname)
    const setCount = useStoreActions((actions: any) => actions.setCount)
    const [isShowAlert, setShowAlert] = useState(false);
    const [deleteTrackId, setdeleteTrackId] = useState('');
    const [isToastOpen, setToastOpen] = useState(false);
    const [toastMsg, setToastMsg] = useState<string>('')
    const [currentPage, setCurrentPage] = useState(1)
    const searchval = Searchval()
    const [deleteTrack] = useMutation(DELETE_TRACK, {
        onCompleted(data2) {
            setToastMessageForDeleteTrack(data2, setToastMsg, setToastOpen, 'success');
        },
        onError(err) {
            setToastMessageForDeleteTrack(JSON.parse(JSON.stringify(err)), setToastMsg, setToastOpen, 'fail');
        }
    });
    const columns = [
        {
            Header: '',
            accessor: 'check',
            hidden: path == 'sim' ? false : true,
            formatter: (value: any, cell: any) => {
                return (<Check
                    checked={theArray?.find((e: any) => {
                        if (e.id == cell.row.values.trackID && e.checked) {
                            return true
                        } else { return false }
                    })}
                    onChange={(e: any, val: any) => {
                        theArray.map((v: any) => {
                            if (v?.id == cell.row.values.trackID) {
                                v.checked = e.target.checked
                            } else {
                                theArray.push({ id: cell.row.values.trackID, checked: e.target.checked })
                            }
                        })
                        let newArr = [...new Map(theArray.map((s: any) => [s['id'], s])).values()]
                        setSelectedtrack(newArr)
                    }}
                />)
            }
        },
        {
            Header: 'Track ID',
            accessor: 'trackID',
            valign: 'top',
        },
        {
            Header: 'Track Name',
            accessor: 'trackName',
            hidden: path == 'sim' ? true : false,
            onClick: (e: any, index: any) => {
                onClickTrackColumn(index, setTname, setTid)
            },
        },
        {
            Header: 'Track Name',
            accessor: 'trackNameSim',
            hidden: path == 'sim' ? false : true,

        },
        {
            Header: 'Track Status',
            accessor: 'trackStatus',
        },
        {
            Header: 'Track Type',
            accessor: 'trackType',
        },
        {
            Header: 'Number of vehicles',
            accessor: 'numberofvehicles',
        },
        {
            Header: 'Country',
            accessor: 'country',
            formatter: (value: any, cell: any) => {
                return <CounterWithToolTip toolTipVal={cell?.row?.values?.country}></CounterWithToolTip>
            },
        },
        {
            Header: ' ',
            accessor: 'delete',
            hidden: path == 'sim' ? true : false,
            formatter: (value: any, cell: any) => {
                return <Icon key={value} style={{ cursor: 'pointer' }} name='delete' data-testid='delete-btn' onClick={() => { deleteTrackFun(setShowAlert, setdeleteTrackId, isShowAlert, cell.row.values.trackID) }} />
            }
        },
    ]
    const [pageData, setPageData] = useState({
        rowData: [],
        isLoading: false,
        totalPages: 0,
        totalTracks: 0,
    })

    useEffect(() => {
        setPageData((prevState) => ({
            ...prevState,
            rowData: [],
            isLoading: true,
        }))
        getTrackData(currentPage, searchval).then((info) => {
            setPageData({
                isLoading: false,
                rowData: trackRowData(info) as any,
                totalPages: info?.data?.searchTrackByPattern?.pages,
                totalTracks: info?.data?.searchTrackByPattern?.total,
            })
            setCount(info?.data?.searchTrackByPattern?.total);
        })
    }, [currentPage, searchval])
    return (
        <>
              {/* @ts-ignore */}
            <Table columns={columns} data={pageData.rowData}
            />
            <ConditionalAlertBtn show={isShowAlert} onClose={setShowAlert} respectiveId={deleteTrackId} mutationLoad={deleteTrack} popupMsg={'Are you sure you want to delete this track?'} popupState={'Warning!'} respectiveFun={onClickDeleteTrack} yes={'Yes'} no={'No'}></ConditionalAlertBtn>
            <Box align='right' padding='small'>
                <Pagination totalRows={pageData.totalTracks} pageChangeHandler={setCurrentPage} rowsPerPage={10} />
            </Box>
            {/* Toast message for deleting a track */}
            {<Toast show={isToastOpen}>
                <div>
                    <BoxToast toastMsg={toastMsg} />
                </div>
            </Toast>}
        </>
    )
}

export default TrackList
