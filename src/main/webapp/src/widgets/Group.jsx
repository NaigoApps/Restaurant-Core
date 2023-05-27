import classNames from 'classnames';
import React from 'react';
import { formatPrice, formatGroup } from '../utils/Utils';

export default function Group({ group }) {
  return (
    <div className={classNames(`has-text-${(group.dish && group.dish.color) || 'secondary'}`, {
      'is-blink': group.price === 0,
    })}
    >
      <div className="is-pulled-left">
        {`${formatGroup(group)}`}
      </div>
      <div className="is-pulled-right">
        {formatPrice(group.quantity * group.price)}
      </div>
    </div>
  );
}
