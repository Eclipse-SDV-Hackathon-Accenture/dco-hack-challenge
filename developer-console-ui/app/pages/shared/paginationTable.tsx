import { Button } from '@dco/sdv-ui'
import React, { useState, useEffect } from 'react'
import styles from './styles.module.css'

export const Pagination = ({ pageChangeHandler, totalRows, rowsPerPage }: any) => {
  // Calculating max number of pages
  const noOfPages = Math.ceil(totalRows / rowsPerPage)||0

  // Creating an array with length equal to no.of pages
  const pagesArr = [...new Array(noOfPages)]

  // State variable to hold the current page. This value is
  // passed to the callback provided by the parent
  const [currentPage, setCurrentPage] = useState(1)

  // Navigation arrows enable/disable state
  const [canGoBack, setCanGoBack] = useState(false)
  const [canGoNext, setCanGoNext] = useState(true)

  // Onclick handlers for the butons
  const onNextPage = () => setCurrentPage(currentPage + 1)
  const onPrevPage = () => setCurrentPage(currentPage - 1)
  const onPageSelect = (pageNo: any) => setCurrentPage(pageNo)

  // Disable previous and next buttons in the first and last page
  // respectively
  useEffect(() => {
    if (noOfPages === currentPage) {
      setCanGoNext(false)
    } else {
      setCanGoNext(true)
    }
    if (currentPage === 1) {
      setCanGoBack(false)
    } else {
      setCanGoBack(true)
    }
  }, [noOfPages, currentPage])

  // To set the starting index of the page
  useEffect(() => {
    pageChangeHandler(currentPage)
  }, [currentPage])

  return (
    <>
      {noOfPages > 1 ? (
        <div className={styles.s}>
          <div className={styles.pagebuttons}>
            <Button  variant='naked' data-testid="btn1" onClick={onPrevPage} disabled={!canGoBack}>
              &#8249;
            </Button>
            {pagesArr.map((num, index) => (
              <Button key={index} 
                variant='naked'  
                onClick={() => onPageSelect(index + 1)}
                className={`${styles.pageBtn}  ${index + 1 === currentPage ? styles.activeBtn : ''}`}
              >
                {index + 1}
                {pagesArr[index]}
              </Button>
            ))}
            <Button data-testid="btn" variant='naked' onClick={onNextPage} disabled={!canGoNext}>
              &#8250;
            </Button>
          </div>
        </div>
      ) : null}
    </>
  )
}

export default Pagination
