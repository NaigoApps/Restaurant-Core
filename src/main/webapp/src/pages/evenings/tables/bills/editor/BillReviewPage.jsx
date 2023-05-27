import numeral from 'numeral';
import React, { useState, useEffect, useCallback } from 'react';
import FloatEditor from '../../../../../inputs/FloatEditor';
import IntegerEditor from '../../../../../inputs/IntegerEditor';
import { formatGroup } from '../../../../../utils/Utils';
import Column from '../../../../../widgets/Column';
import Row from '../../../../../widgets/Row';
import Scrollable from '../../../../../widgets/Scrollable';

export default function BillReviewPage({
  table,
  selectedCcs,
  finalTotal,
  setFinalTotal,
  availableOrders,
}) {
  const [bills, setBills] = useState(1);

  async function updateBillsNumber(n) {
    const newStringTotal = numeral(finalTotal / n)
      .format('0.00');
    const newTotal = numeral(newStringTotal)
      .value() * n;
    setFinalTotal(newTotal);
    setBills(n);
  }

  const estimatedTotal = useCallback(() => table.coverCharge * selectedCcs
    + availableOrders.map(group => group.selected * group.price)
      .reduce((o1, o2) => o1 + o2, 0), [availableOrders, selectedCcs, table.coverCharge]);

  useEffect(() => {
    setFinalTotal(estimatedTotal());
  }, [estimatedTotal, setFinalTotal]);

  return (
    <Row grow>
      <Column grow>
        <Row grow>
          <Column grow>
            <Row grow>
              <Column grow>
                <Scrollable>
                  <Row grow>
                    <Column grow>
                      <Row justifyBetween>
                        <div>
                          {`${selectedCcs} COPERTI`}
                        </div>
                        <div>
                          {numeral(selectedCcs * table.coverCharge)
                            .format('$0.00')}
                        </div>
                      </Row>
                    </Column>
                  </Row>
                  {availableOrders
                    .filter(group => group.selected > 0)
                    .map(group => ({
                      ...group,
                      quantity: group.selected,
                    }))
                    .map(grp => (
                      <Row grow>
                        <Column grow>
                          <Row justifyBetween>
                            <div>
                              {formatGroup(grp)}
                            </div>
                            <div>
                              {numeral(grp.quantity * grp.price)
                                .format('$0.00')}
                            </div>
                          </Row>
                        </Column>
                      </Row>
                    ))}
                </Scrollable>
              </Column>
            </Row>
            <Row>
              <Column grow>
                <Row justifyBetween>
                  <div>
                    TOTALE PROVVISORIO:
                  </div>
                  <div>
                    {numeral(estimatedTotal())
                      .format('$0.00')}
                  </div>
                </Row>
              </Column>
            </Row>
            <Row>
              <Column grow>
                <Row justifyBetween>
                  <div>
                    TOTALE DEFINITIVO:
                  </div>
                  <div>
                    {numeral(finalTotal)
                      .format('$0.00')}
                  </div>
                </Row>
              </Column>
            </Row>
          </Column>
        </Row>
      </Column>
      <Column grow>
        <Row>
          <Column>
            <Row>
              <Column>
                <IntegerEditor
                  kind="info"
                  label="Numero di parti"
                  initialValue={bills}
                  onConfirm={updateBillsNumber}
                />
              </Column>
            </Row>
            <Row>
              <Column>
                <FloatEditor
                  kind="info"
                  label={(bills > 1) ? 'Totale per parte' : 'Totale definitivo'}
                  initialValue={finalTotal / bills}
                  onConfirm={value => setFinalTotal(bills * value)}
                  currency
                />
              </Column>
            </Row>
          </Column>
        </Row>
      </Column>
    </Row>
  );
}
