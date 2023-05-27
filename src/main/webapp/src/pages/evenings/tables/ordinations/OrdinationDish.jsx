import { faEllipsisH, faMinus, faPlus } from '@fortawesome/free-solid-svg-icons';
import React from 'react';
import { dishOrders } from '../../../../utils/OrdinationUtils';
import Button from '../../../../widgets/Button';
import Column from '../../../../widgets/Column';
import Row from '../../../../widgets/Row';
import { formatGroup } from '../../../../utils/Utils';

export default function OrdinationDish({
  ordination,
  dish,
  onSelect,
  onCustomize,
  onLess,
  onMore,
}) {
  const dishes = dishOrders(ordination, dish.uuid);

  return (
    <div className="panel">
      <p className="panel-heading">
        <Row>
          <Column grow>
            {dish.name}
          </Column>
          <Column>
            <Button icon={faPlus} onClick={() => onSelect(dish)}/>
          </Column>
          <Column>
            <Button icon={faEllipsisH} onClick={() => onCustomize(dish)}/>
          </Column>
        </Row>
      </p>
      {dishes.length > 0
      && dishes.map((item) => {
        const text = `${item.phase.name}: x ${formatGroup(item.group, {
          withQuantity: true,
          withName: false
        })}`;
        return (
          <div className="panel-block">
            <Column grow>
              {text}
            </Column>
            <Column>
              <Row>
                <Column>
                  <Button icon={faMinus} kind="danger"
                          onClick={() => onLess(item.phaseIndex, item.groupIndex)}/>
                </Column>
                <Column>
                  <Button icon={faPlus} kind="success"
                          onClick={() => onMore(item.phaseIndex, item.groupIndex)}/>
                </Column>
              </Row>
            </Column>
          </div>
        );
      })
      }
    </div>
  );
}
