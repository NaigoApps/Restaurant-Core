import React, { Fragment } from 'react';
import { formatPrice } from '../../../../utils/Utils';
import Column from '../../../../widgets/Column';
import Row from '../../../../widgets/Row';

function format(group) {
  const components = [
    group.quantity,
    group.dish.name,
    ...group.additions.map(a => a.name),
  ];
  if (group.notes) {
    components.push(group.notes);
  }
  return components.join(' ');
}

export default function ({ ordination }) {
  if (!ordination) {
    return null;
  }

  return (
    <Fragment>
      {ordination.orders.map(phaseGroup => (
        <Fragment key={phaseGroup.phase.name}>
          <Row>
            <Column grow>
              <p className="h6">
                {phaseGroup.phase.name}
              </p>
              {phaseGroup.orders.map(group => (
                <Row justifyBetween key={group.dish.name}>
                  <div>
                    {format(group)}
                  </div>
                  <div>
                    {formatPrice(
                      group.quantity * group.price,
                    )}
                  </div>
                </Row>
              ))}
            </Column>
          </Row>
        </Fragment>
      ))}
    </Fragment>
  );
}
