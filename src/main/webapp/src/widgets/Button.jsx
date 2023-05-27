import classNames from 'classnames';
import React, { Fragment, useState } from 'react';
import Icon from './Icon';

export const SIZES = {
  XXSMALL: 'xxs',
  XSMALL: 'xs',
  SMALL: 'sm',
  MEDIUM: 'md',
  HUGE: 'lg',
  SPACE: 'space',
};

export default function Button({
  icon,
  large,
  size,
  text,
  kind,
  invisible,
  active,
  disabled,
  onClick,
}) {
  const [loading, setLoading] = useState(false);

  let iconComponent = null;
  if (icon) {
    iconComponent = <Icon name={icon} />;
  }

  let bg;
  if (invisible) {
    bg = 'invisible';
  } else if (active) {
    bg = 'primary';
  } else {
    bg = kind || 'secondary';
  }


  async function handleClick() {
    setLoading(true);
    try {
      await onClick();
    } catch (err) {
      setLoading(false);
      throw err;
    }
    setLoading(false);
  }

  return (
    <Fragment>
      <button
        type="button"
        className={classNames(`button is-${bg} has-background-${bg}`, {
          'is-large': large,
          xxs: size === SIZES.XXSMALL,
          xs: size === SIZES.XSMALL,
          sm: size === SIZES.SMALL,
          md: size === SIZES.MEDIUM,
          lg: size === SIZES.HUGE,
          space: size === SIZES.SPACE,
          'is-loading': loading,
        })}
        onMouseUp={handleClick}
        disabled={!!disabled}
      >
        {iconComponent && <span className="icon">{iconComponent}</span>}
        {text && <span>{text}</span>}
      </button>
    </Fragment>
  );
}
