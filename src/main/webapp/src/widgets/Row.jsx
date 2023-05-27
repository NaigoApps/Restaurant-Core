import React from 'react';
import classNames from 'classnames';

export default function Row({
  children,
  spaced,
  grow,
  margin,
  relative,
  wrap,
  grid,
  alignCenter,
  justifyCenter,
  justifyBetween,
  justifyAround,
}) {
  return (
    <div className={classNames('is-flex', 'is-flex-direction-row', {
      'is-flex-grow-1': grow,
      'is-justify-content-center': justifyCenter,
      'is-justify-content-space-between': justifyBetween,
      'is-justify-content-space-around': justifyAround,
      'is-align-items-center': alignCenter,
      'm-1': margin === 'small',
      'm-2': margin === 'medium',
      'spaced-row': spaced,
      'is-relative': relative,
      'is-flex-wrap-wrap': wrap,
      'is-flex-basis-0': grid
    })}
    >
      {children}
    </div>
  );
}
