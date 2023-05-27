import { useState, useCallback } from 'react';

export default function useWizard({ pages, skip, isValid }) {
  let initialPage = 0;
  while (skip(initialPage) && initialPage < pages) {
    initialPage += 1;
  }

  const [currentPage, setCurrentPage] = useState(initialPage);

  let nextPage = currentPage;
  while (skip(nextPage) && nextPage < pages) {
    nextPage += 1;
  }
  if (skip(nextPage) || nextPage === currentPage) {
    nextPage = null;
  }

  let previousPage = currentPage;
  while (skip(previousPage) && previousPage > 0) {
    previousPage -= 1;
  }
  if (skip(previousPage) || previousPage === currentPage) {
    previousPage = null;
  }

  const next = useCallback(() => {
    if (nextPage !== null) {
      setCurrentPage(nextPage);
    }
  }, [nextPage]);

  const previous = useCallback(() => {
    if (previousPage !== null) {
      setCurrentPage(previousPage);
    }
  }, [previousPage]);


  return {
    currentPage,
    next,
    previous,
    hasPrevious: previousPage !== null,
    hasNext: nextPage !== null,
    isValid,
  };
}
