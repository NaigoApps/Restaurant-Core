import {
  faArchive,
  faCheck,
  faDollarSign,
  faFileExport,
  faHandshake,
  faPen,
  faPlus,
  faTrash,
} from '@fortawesome/free-solid-svg-icons';
import fileDownload from 'js-file-download';
import React, { Fragment, useState } from 'react';
import RemoteSelectInput from '../../../inputs/RemoteSelectInput';
import useNetwork from '../../../utils/useNetwork';
import { formatDiningTable, formatDiningTableBg } from '../../../utils/Utils';
import Button from '../../../widgets/Button';
import Column from '../../../widgets/Column';
import OkCancelDialog from '../../../widgets/OkCancelDialog';
import Row from '../../../widgets/Row';
import BillEditor from './bills/editor/BillEditor';
import DiningTableDataEditor from './DiningTableDataEditor';
import OrdinationEditor from './ordinations/OrdinationEditor';
import MODE from './mode';
import Alert from '../../../widgets/Alert';
import PriceFiller from './bills/editor/PriceFiller';
import OptionsDialog from '../../../widgets/OptionsDialog';
import IntegerInput from '../../../inputs/IntegerInput';
import FloatInput from '../../../inputs/FloatInput';
import { tableOrdersTotal } from '../../../utils/OrdinationUtils';
import IntegerEditor from '../../../inputs/IntegerEditor';
import FloatEditor from '../../../inputs/FloatEditor';

export default function DiningTableActions({
  table,
  refresh,
  setMode,
  navigate,
}) {
  const {
    get,
    post,
    put,
    remove,
  } = useNetwork();

  const [selectingParts, setSelectingParts] = useState(false);
  const [selectedParts, setSelectedParts] = useState(0);
  const [selectedPrice, setSelectedPrice] = useState(0);

  const [choosingBill, setChoosingBill] = useState(false);
  const [closing, setClosing] = useState(false);
  const [archiving, setArchiving] = useState(false);
  const [creating, setCreating] = useState(false);
  const [merging, setMerging] = useState(false);
  const [editing, setEditing] = useState(false);
  const [creatingBill, setCreatingBill] = useState(false);
  const [fixingPrices, setFixingPrices] = useState(false);

  async function makeQuickBill() {
    const zeroOrders = !!table.orders.find(o => o.price === 0);
    if (zeroOrders) {
      setFixingPrices(true);
    } else {
      const result = await post(`bills/quick?table=${table.uuid}`);
      if (result !== null) {
        await refresh();
        setMode(MODE.BILLS);
      }
    }
  }

  async function makePartialBill() {
    const zeroOrders = !!table.orders.find(o => o.price === 0);
    if (zeroOrders) {
      setFixingPrices(true);
    } else {
      const result = await post(`bills/partial?table=${table.uuid}&parts=${selectedParts}&price=${selectedPrice}`);
      if (result !== null) {
        setSelectingParts(false);
        await refresh();
        setMode(MODE.BILLS);
      }
    }
  }

  function createBill() {
    const zeroOrders = !!table.orders.find(o => o.price === 0);
    if (zeroOrders) {
      setFixingPrices(true);
    } else {
      setCreatingBill(true);
      setMode(MODE.BILLS);
    }
  }

  function makeBill(option) {
    setChoosingBill(false);
    if (option && option.id === 'QUICK') {
      makeQuickBill();
    } else if (option && option.id === 'FULL') {
      createBill();
    } else if (option && option.id === 'PARTIAL') {
      setSelectingParts(true);
      setSelectedParts(0);
      setSelectedPrice(0);
    }
  }

  const [mergeDestination, setMergeDestination] = useState(null);

  async function mergeTables() {
    if (mergeDestination) {
      const result = await post(`dining-tables/${table.uuid}/merge/${mergeDestination.uuid}`);
      if (result !== null) {
        setMerging(false);
        navigate(`../${mergeDestination.uuid}`);
      }
    } else {
      setMerging(false);
    }
  }

  async function closeTable(opt) {
    if (opt && opt.id === 'CLOSE') {
      const result = await post(`dining-tables/${table.uuid}/lock`);
      if (result !== null) {
        navigate('..');
      }
    } else if (opt && opt.id === 'ARCHIVE') {
      let result = await post(`dining-tables/${table.uuid}/lock`);
      if (result != null) {
        result = await put(`dining-tables/${table.uuid}/archive`);
        if (result !== null) {
          navigate('..');
        }
      }
    } else {
      setClosing(false);
    }
  }

  async function archiveTable() {
    const result = await put(`dining-tables/${table.uuid}/archive`);
    if (result !== null) {
      setArchiving(false);
      navigate('..');
    }
  }

  async function deleteTable() {
    const result = await remove(`dining-tables/${table.uuid}`);
    if (result !== null) {
      navigate('..');
    }
  }

  async function exportTable() {
    const result = await get(`dining-tables/${table.uuid}/export`);
    if (result !== null) {
      fileDownload(JSON.stringify(result), `${table.uuid}.json`);
    }
  }

  function chooseParts(val) {
    if (val > 0) {
      setSelectedParts(val);

      const coverChargesPrice = table.coverCharges * table.coverCharge;
      const total = tableOrdersTotal(table) + coverChargesPrice;

      setSelectedPrice(parseFloat((total / val).toFixed(2)));
    }
  }

  return (
    <Fragment>
      <Row grow>
        <Column spaced>
          <p className="h4 has-text-centered">Azioni sul tavolo</p>
          <Button
            kind="success"
            icon={faPlus}
            text="Nuova comanda"
            onClick={() => setCreating(true)}
          />
          <Button
            text="Conto"
            kind="success"
            icon={faDollarSign}
            onClick={() => setChoosingBill(true)}
          />
          <Button
            text="Modifica dati"
            kind="info"
            icon={faPen}
            onClick={() => setEditing(true)}
          />
          {/* <Button */}
          {/*  text="Esporta tavolo" */}
          {/*  kind="info" */}
          {/*  icon={faFileExport} */}
          {/*  onClick={() => exportTable()} */}
          {/* /> */}
          <Button
            text="Fondi tavolo"
            kind="warning"
            icon={faHandshake}
            onClick={() => setMerging(true)}
          />
          <Button
            text="Chiudi tavolo"
            kind="warning"
            icon={faCheck}
            onClick={() => setClosing(true)}
          />
          {/* <Button */}
          {/*  text="Archivia tavolo" */}
          {/*  kind="warning" */}
          {/*  icon={faArchive} */}
          {/*  onClick={() => setArchiving(true)} */}
          {/* /> */}
          <Button
            text="Elimina tavolo"
            kind="danger"
            icon={faTrash}
            onClick={() => deleteTable()}
          />
        </Column>
      </Row>
      {choosingBill && (
        <OptionsDialog
          visible
          onSelectOption={makeBill}
          options={[
            {
              id: 'ABORT',
              label: 'Annulla',
            },
            {
              id: 'QUICK',
              label: 'Conto rapido N.F.',
              kind: 'success',
            },
            {
              id: 'FULL',
              label: 'Conto parziale o diviso',
              kind: 'success',
            },
            {
              id: 'PARTIAL',
              label: 'Prezzo fisso',
              kind: 'success',
            },
          ]}
        >
          <p>Creazione conto</p>
        </OptionsDialog>
      )}
      <OkCancelDialog
        visible={selectingParts}
        onCancel={() => setSelectingParts(false)}
        onOk={() => makePartialBill()}
      >
        <Column margin="small">
          <IntegerEditor
            label="Numero conti"
            initialValue={selectedParts}
            onConfirm={(value) => {
              chooseParts(value);
            }}
          />
          <FloatEditor
            label="Prezzo"
            initialValue={selectedPrice}
            onConfirm={(value) => {
              setSelectedPrice(value);
            }}
          />
        </Column>
      </OkCancelDialog>
      {closing && (
        <OptionsDialog
          visible
          onSelectOption={closeTable}
          options={[{
            id: 'ABORT',
            kind: 'info',
            label: 'Annulla',
          }, {
            id: 'CLOSE',
            kind: 'warning',
            label: 'Chiudi',
          }, {
            id: 'ARCHIVE',
            kind: 'danger',
            label: 'Chiudi e archivia',
          }]}
        >
          <p>Chiudere il tavolo?</p>
        </OptionsDialog>
      )}
      {archiving && (
        <OkCancelDialog
          visible
          onOk={archiveTable}
          onCancel={() => setArchiving(false)}
        >
          <p>Archiviare il tavolo?</p>
        </OkCancelDialog>
      )}
      {merging && (
        <OkCancelDialog
          visible
          onOk={mergeTables}
          onCancel={() => setMerging(false)}
        >
          <RemoteSelectInput
            url={`dining-tables/${table.uuid}/mergeTargets`}
            id={dt => dt.uuid}
            text={formatDiningTable}
            bg={formatDiningTableBg}
            onSelectOption={setMergeDestination}
            value={mergeDestination}
          />
        </OkCancelDialog>
      )}
      {creating && (
        <OrdinationEditor
          tableUuid={table.uuid}
          onConfirm={() => {
            setCreating(false);
            refresh();
            setMode(MODE.ORDINATIONS);
          }}
          onCancel={() => setCreating(false)}
        />
      )}
      {editing && (
        <DiningTableDataEditor
          onClose={() => setEditing(false)}
          table={table.uuid}
          refresh={refresh}
        />
      )}
      {creatingBill && (
        <BillEditor
          table={table}
          refresh={refresh}
          onClose={() => setCreatingBill(false)}
        />
      )}
      {fixingPrices && (
        <Alert
          onClose={() => setFixingPrices(false)}
          visible={fixingPrices}
          size="large"
        >
          <PriceFiller table={table} refresh={refresh} onDone={() => setFixingPrices(false)} />
        </Alert>
      )}
    </Fragment>
  );
}
