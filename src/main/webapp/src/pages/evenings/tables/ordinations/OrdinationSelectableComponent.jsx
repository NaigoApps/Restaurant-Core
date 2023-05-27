import React from 'react';
import { formatPrice } from '../../../../utils/Utils';
import Button from '../../../../widgets/Button';
import Column from '../../../../widgets/Column';
import Row from '../../../../widgets/Row';
import Scrollable from '../../../../widgets/Scrollable';

export default function ({
  ordination, selectedPhase, selectedGroup, onSelectGroup,
}) {
  function format(group) {
    const components = [group.dish.name, ...group.additions.map(a => a.name)];
    if (group.notes) {
      components.push(group.notes);
    }
    components.push(`(${group.quantity} x ${formatPrice(group.price)})`);
    return components.join(' ');
  }

  return (
    <Row grow>
      <Column>
        <Scrollable>
          {ordination.orders.map((phaseGroup, phaseIndex) => (
            <Row>
              <Column>
                <Row>
                  <Column>
                    <p className="h6 has-text-centered">{phaseGroup.phase.name}</p>
                  </Column>
                </Row>
                <Row>
                  <Column>
                    {phaseGroup.orders.map((group, groupIndex) => (
                      <Row dense>
                        <Column>
                          <Button
                            active={selectedPhase === phaseIndex && selectedGroup === groupIndex}
                            onClick={() => onSelectGroup(phaseIndex, groupIndex)}
                            text={format(group)}
                          />
                        </Column>
                      </Row>
                    ))}
                  </Column>
                </Row>
              </Column>
            </Row>
          ))}
        </Scrollable>
      </Column>
    </Row>
  );
}
