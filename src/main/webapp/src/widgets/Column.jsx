import React from 'react';
import classNames from 'classnames';

export default function Column({
  spaced,
  grow,
  margin,
  relative,
  wrap,
  grid,
  justifyCenter,
  justifyStretch,
  alignCenter,
  children,
}) {
  return (
    <div className={classNames('is-flex', 'is-flex-direction-column', {
      'is-flex-grow-1': grow,
      'is-justify-content-center': justifyCenter,
      'is-justify-content-stretch': justifyStretch,
      'is-align-items-center': alignCenter,
      'm-1': margin === 'small',
      'm-2': margin === 'medium',
      'is-relative': relative,
      'is-flex-wrap-wrap': wrap,
      'is-flex-basis-0': grid,
      'spaced-column': spaced,
    })}
    >
      {children}
    </div>
  );
}
