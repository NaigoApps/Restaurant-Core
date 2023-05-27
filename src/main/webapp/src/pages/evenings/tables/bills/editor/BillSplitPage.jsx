import numeral from 'numeral';
import React, { Fragment } from 'react';
import {
  faCaretLeft,
  faCaretRight,
  faMinus,
  faPlus,
  faStepBackward, faStepForward,
} from '@fortawesome/free-solid-svg-icons';
import { formatGroup } from '../../../../../utils/Utils';
import Column from '../../../../../widgets/Column';
import Row from '../../../../../widgets/Row';
import SplitModule from './SplitModule';
import Wrap from '../../../../../widgets/Wrap';
import Button from '../../../../../widgets/Button';

export default function BillSplitPage({
  selectedCcs,
  setSelectedCcs,
  availableCcs,
  availableOrders,
  setAvailableOrders,
  table,
}) {
  function editCoverCharges(n) {
    setSelectedCcs(old => old + n);
  }

  function closeAll() {
    setAvailableOrders(oldGroups => oldGroups.map(group => ({
      ...group,
      selected: group.quantity,
    })));
    setSelectedCcs(availableCcs);
  }

  async function openAll() {
    setAvailableOrders(oldGroups => oldGroups.map(group => ({
      ...group,
      selected: 0,
    })));
    setSelectedCcs(0);
  }

  function editGroup(group, quantity) {
    setAvailableOrders(oldOrders => oldOrders.map(g => ({
      ...g,
      selected: g.id === group.id ? g.selected + quantity : g.selected,
    })));
  }

  function remainingTotal() {
    return (availableCcs - selectedCcs) * table.coverCharge
      + availableOrders
        .map(group => (group.quantity - group.selected) * group.price)
        .reduce((p1, p2) => p1 + p2, 0);
  }

  function selectedTotal() {
    return selectedCcs * table.coverCharge
      + availableOrders
        .map(group => group.selected * group.price)
        .reduce((p1, p2) => p1 + p2, 0);
  }

  return (
    <Fragment>
      <Row justifyBetween>
        <Row justifyBetween>
          <Column grow justifyCenter>
            <p className="one-line">{`COPERTI RIMASTI: ${availableCcs - selectedCcs}`}</p>
          </Column>
          {availableCcs - selectedCcs > 0 && (
            <Column>
              <Row>
                <Column>
                  <Button
                    icon={faCaretRight}
                    onClick={() => editCoverCharges(1)}
                  />
                </Column>
                <Column>
                  <Button
                    icon={faStepForward}
                    onClick={() => editCoverCharges(availableCcs)}
                  />
                </Column>
              </Row>
            </Column>
          )}
        </Row>
        <Row justifyBetween>
          {(selectedCcs > 0) && (
            <Column>
              <Row>
                <Column>
                  <Button
                    icon={faStepBackward}
                    onClick={() => editCoverCharges(-selectedCcs)}
                  />
                </Column>
                <Column>
                  <Button
                    icon={faCaretLeft}
                    onClick={() => editCoverCharges(-1)}
                  />
                </Column>
              </Row>
            </Column>
          )}
          <Column grow justifyCenter>
            <p className="one-line">{`COPERTI SELEZIONATI: ${selectedCcs}`}</p>
          </Column>
        </Row>
      </Row>
      <Row grow>
        <Column grow>
          <SplitModule
            items={availableOrders
              .map(group => ({
                ...group,
                quantity: group.quantity - group.selected,
              }))
              .filter(group => group.quantity > 0)}
            display={group => (
              <Row justifyBetween>
                <div>
                  {formatGroup(group)}
                </div>
                <div>
                  {numeral(group.price * group.quantity)
                    .format('$0.00')}
                </div>
              </Row>
            )}
            f={group => editGroup(group, 1)}
            ff={group => editGroup(group, group.quantity - group.selected)}
            fff={closeAll}
          />
          <Wrap>
            {`TOTALE RIMANENTE: ${numeral(remainingTotal())
              .format('$0.00')}`}
          </Wrap>
        </Column>
        <Column grow>
          <SplitModule
            items={availableOrders
              .map(group => ({
                ...group,
                quantity: group.selected,
              }))
              .filter(group => group.quantity > 0)}
            display={group => (
              <Row justifyBetween>
                <div>
                  {formatGroup(group)}
                </div>
                <div>
                  {numeral(group.price * group.quantity)
                    .format('$0.00')}
                </div>
              </Row>
            )}
            b={group => editGroup(group, -1)}
            bb={group => editGroup(group, -group.selected)}
            bbb={openAll}
          />
          <Wrap>
            {`TOTALE CONTO: ${numeral(selectedTotal())
              .format('$0.00')}`}
          </Wrap>
        </Column>
      </Row>
    </Fragment>
  );
}
