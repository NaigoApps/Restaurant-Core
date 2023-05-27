import {
  faBars, faPen, faPrint, faTimes, faTrash,
} from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState } from 'react';
import useNetwork from '../../../../utils/useNetwork';
import useRemote from '../../../../utils/useRemote';
import { beautifyTime, formatDiningTable } from '../../../../utils/Utils';
import Alert from '../../../../widgets/Alert';
import Button from '../../../../widgets/Button';
import Column from '../../../../widgets/Column';
import ConfirmButton from '../../../../widgets/ConfirmButton';
import Row from '../../../../widgets/Row';
import Scrollable from '../../../../widgets/Scrollable';
import OrdinationEditor from './OrdinationEditor';
import OrdinationReview from './OrdinationReview';

export default function DiningTableOrdinations({
  table,
  refresh,
}) {
  const {
    put,
    post,
    remove
  } = useNetwork();

  const [settings] = useRemote('settings');

  const [selectedOrdination, setSelectedOrdination] = useState(null);
  const [menuOrdination, setMenuOrdination] = useState(null);

  async function printOrdination(ordination) {
    const result = await post(
      `dining-tables/${table.uuid}/ordinations/${ordination.uuid}/print`,
    );
    if (result !== null) {
      refresh();
      setMenuOrdination(null);
    }
  }

  async function abortOrdination(ordination) {
    const result = await put(
      `dining-tables/${table.uuid}/ordinations/${ordination.uuid}/abort`,
    );
    if (result !== null) {
      refresh();
      setMenuOrdination(null);
    }
  }

  async function deleteOrdination(ordination) {
    const result = await remove(
      `dining-tables/${table.uuid}/ordinations/${ordination.uuid}`,
    );
    if (result !== null) {
      refresh();
      setMenuOrdination(null);
    }
  }

  if (!table || !settings) {
    return null;
  }

  return (
    <Fragment>
      <Row grow>
        <Column grow>
          <Row>
            <Column grow>
              <p className="h4 has-text-centered">{formatDiningTable(table)}</p>
            </Column>
          </Row>
          <Row grow>
            <Column grow>
              <Scrollable>
                <Row>
                  <Column grow>
                    {settings.coverCharges
                    && `${table.coverCharges} ${
                      table.coverCharges === 1 ? 'COPERTO' : 'COPERTI'
                    }`}
                    {table.ordinations.map(ordination => (
                      <Row key={ordination.uuid}>
                        <Column grow>
                          <div className="card">
                            <header className="card-header">
                              <p className="card-header-title h4">
                                {`Comanda delle ${beautifyTime(
                                  ordination.creationTime,
                                )}`}
                              </p>
                              <Row alignCenter>
                                {ordination.dirty && (
                                  <Column grow>
                                    <Button
                                      icon={faPrint}
                                      large
                                      kind="warning"
                                      onClick={() => printOrdination(ordination)}
                                    />
                                  </Column>
                                )}
                                <Column grow>
                                  <Button
                                    icon={faBars}
                                    large
                                    onClick={() => setMenuOrdination(ordination)}
                                  />
                                </Column>
                                {menuOrdination === ordination && (
                                  <Alert
                                    onClose={() => setMenuOrdination(null)}
                                    visible
                                  >
                                    <Column spaced>
                                      <Row>
                                        <Column grow>
                                          <Button
                                            text="Modifica comanda"
                                            icon={faPen}
                                            onClick={() => {
                                              setSelectedOrdination(ordination);
                                              setMenuOrdination(null);
                                            }}
                                          />
                                        </Column>
                                      </Row>
                                      <Row>
                                        <Column grow>
                                          <Button
                                            text="Stampa comanda"
                                            icon={faPrint}
                                            kind={
                                              ordination.dirty
                                                ? 'warning'
                                                : 'secondary'
                                            }
                                            onClick={() => printOrdination(ordination)
                                            }
                                          />
                                        </Column>
                                      </Row>
                                      <Row>
                                        <Column grow>
                                          <ConfirmButton
                                            icon={faTimes}
                                            text="Stampa annullamento"
                                            kind="danger"
                                            onClick={() => abortOrdination(ordination)
                                            }
                                            confirmMessage="Stampare annullamento comanda?"
                                          />
                                        </Column>
                                      </Row>
                                      <Row>
                                        <Column grow>
                                          <ConfirmButton
                                            icon={faTrash}
                                            text="Elimina comanda"
                                            kind="danger"
                                            onClick={() => deleteOrdination(ordination)
                                            }
                                            confirmMessage="Eliminare la comanda?"
                                          />
                                        </Column>
                                      </Row>
                                    </Column>
                                  </Alert>
                                )}
                              </Row>
                            </header>
                            <div className="card-content">
                              <OrdinationReview ordination={ordination}/>
                            </div>
                          </div>
                        </Column>
                      </Row>
                    ))}
                  </Column>
                </Row>
              </Scrollable>
              {selectedOrdination && (
                <OrdinationEditor
                  initialOrdination={selectedOrdination}
                  tableUuid={table.uuid}
                  onConfirm={() => {
                    setSelectedOrdination(null);
                    refresh();
                  }}
                  onCancel={() => setSelectedOrdination(null)}
                />
              )}
            </Column>
          </Row>
        </Column>
      </Row>
    </Fragment>
  );
}
