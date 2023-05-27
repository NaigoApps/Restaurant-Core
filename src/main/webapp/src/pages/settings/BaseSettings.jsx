import React, { Fragment } from 'react';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import BooleanInput from '../../inputs/BooleanInput';
import RemoteSelectEditor from '../../inputs/RemoteSelectEditor';
import TextEditor from '../../inputs/TextEditor';
import useNetwork from '../../utils/useNetwork';
import useRemote from '../../utils/useRemote';
import Column from '../../widgets/Column';
import Row from '../../widgets/Row';
import Wrap from '../../widgets/Wrap';
import Button from '../../widgets/Button';

export default function BaseSettings() {
  const [settings, refresh] = useRemote('settings');

  const { put } = useNetwork();

  let headers = [];
  if (settings && settings.billHeader) {
    headers = settings.billHeader.split('\n');
  }

  async function updateSetting(name, value) {
    await put(`settings/${name}`, value);
    await refresh();
  }

  async function deleteHeader(index) {
    const newHeaders = [...headers];
    newHeaders.splice(index, 1);
    await put('settings/billHeader', { value: newHeaders.join('\n') });
    await refresh();
  }

  async function updateHeader(index, value) {
    const newHeaders = [...headers];
    if (index >= newHeaders.length) {
      newHeaders.push(value || ' ');
    } else {
      newHeaders[index] = value || ' ';
    }
    await put('settings/billHeader', { value: newHeaders.join('\n') });
    await refresh();
  }

  if (!settings) {
    return null;
  }

  return (
    <Column spaced>
      <RemoteSelectEditor
        url="printers"
        label="Stampante preconti"
        value={settings.mainPrinter}
        id={v => v.uuid}
        text={v => v.name}
        onSelectOption={value => updateSetting('mainPrinter', value)}
      />
      <TextEditor
        label="Password cassa"
        initialValue={settings.cashPassword}
        onConfirm={value => updateSetting('cashPassword', { value })}
        password
      />
      <BooleanInput
        label="Sintetizza comande"
        value={settings.shrinkOrdinations}
        onConfirm={value => updateSetting('shrink-ordination', { value })}
      />
      {headers.map((header, index) => (
        <Row>
          <Column>
            <TextEditor
              initialValue={header}
              label={`Intestazione preconto ${index + 1}`}
              onConfirm={value => updateHeader(index, value)}
            />
          </Column>
          <Column auto>
            <Button kind="danger" icon={faTrash} onClick={() => deleteHeader(index)}/>
          </Column>
        </Row>
      ))}
      <TextEditor
        initialValue=""
        label={`Intestazione preconto ${headers.length + 1}`}
        onConfirm={value => updateHeader(headers.length, value)}
      />
    </Column>
  );
}
