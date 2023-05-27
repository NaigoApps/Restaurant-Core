import React from 'react';
import Icon from './Icon';
import Row from './Row';
import Column from './Column';

export default function ({
  kind,
  active,
  size,
  icon,
  text,
  onClick,
  disabled,
}) {
  function getClassName() {
    const classes = ['button'];
    if (kind && !active) {
      classes.push(`is-${kind}`);
    } else if (active) {
      classes.push('is-primary');
    }
    if (size) {
      classes.push(size);
    } else {
      classes.push('lg');
    }
    classes.push('round');
    return classes.join(' ');
  }

  return (
    <button type="button" className={getClassName()} onClick={onClick} disabled={!!disabled}>
      <Column spaced alignCenter>
        {icon && (
          <Row>
            <Column hCenter vCenter>
              <Icon name={icon} />
            </Column>
          </Row>
        )}
        <Row>
          <Column hCenter vCenter>
            {text}
          </Column>
        </Row>
      </Column>
    </button>
  );
}
