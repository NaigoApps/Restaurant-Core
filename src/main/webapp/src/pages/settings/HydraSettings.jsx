import React, { Fragment, useState } from 'react';
import IntegerEditor from '../../inputs/IntegerEditor';
import TextEditor from '../../inputs/TextEditor';
import useNetwork from '../../utils/useNetwork';
import useRemote from '../../utils/useRemote';
import Column from '../../widgets/Column';
import Row from '../../widgets/Row';
import Button from '../../widgets/Button';
import FiscalPrinter from '../home/FiscalPrinter';
import Alert from '../../widgets/Alert';

export default function HydraSettings() {
  const [settings, refresh] = useRemote('settings');

  const {
    put,
    post
  } = useNetwork();

  const [printerModalVisible, setPrinterModalVisible] = useState(false);

  async function updateSetting(name, value) {
    await put(`settings/${name}`, value);
    await refresh();
  }

  async function testPrint() {
    await post('printers/fiscal/test-print');
  }

  if (!settings) {
    return null;
  }

  return (
    <Column spaced>
      <Row spaced>
        <Column grow grid>
          <TextEditor
            label="Indirizzo stampante"
            initialValue={settings.fiscalPrinterAddress}
            onConfirm={value => updateSetting('fiscal-printer-address',
              value)}
          />
        </Column>
        <Column grow grid>
          <IntegerEditor
            label="Porta stampante"
            initialValue={settings.fiscalPrinterPort}
            onConfirm={value => updateSetting('fiscal-printer-port', value)}
          />
        </Column>
      </Row>
      <Row spaced>
        <Column grow grid>
          <Button
            text="Test stampa"
            onClick={testPrint}
          />
        </Column>
        <Column grow grid>
          <Button
            text="Stato stampante"
            onClick={() => setPrinterModalVisible(true)}
          />
        </Column>
        <Alert
          onClose={() => setPrinterModalVisible(false)}
          visible={printerModalVisible}
          large
        >
          <FiscalPrinter/>
        </Alert>
      </Row>
    </Column>
  );
}
