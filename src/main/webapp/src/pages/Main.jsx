import { faCompress, faExpand, faLockOpen } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useContext, useState } from 'react';
import screenfull from 'screenfull';
import AppContext from '../ApplicationContext';
import TextInput from '../inputs/TextInput';
import useNetwork from '../utils/useNetwork';
import Alert from '../widgets/Alert';
import Button from '../widgets/Button';
import Column from '../widgets/Column';
import Loading from '../widgets/Loading';
import Modal from '../widgets/Modal';
import OkCancelDialog from '../widgets/OkCancelDialog';
import Row from '../widgets/Row';
import FiscalPrinter from './home/FiscalPrinter';
import NavContent from './NavContent';

export default function Main({
  navigate,
  children,
  buttons,
}) {
  const { get } = useNetwork();
  const data = useContext(AppContext);

  const [showPrinterDialog, setShowPrinterDialog] = useState(false);

  const [showCashPassword, setShowCashPassword] = useState(false);
  const [cashPassword, setCashPassword] = useState('');

  async function openCash() {
    await get(`printers/fiscal/open?password=${cashPassword}`);
    setCashPassword('');
    setShowCashPassword(false);
  }

  return (
    <Fragment>
      <nav className="navbar" role="navigation" aria-label="main navigation">
        <div className="navbar-menu">
          <div className="navbar-start">
            <div className="navbar-item">
              <div className="buttons">
                <NavContent />
              </div>
            </div>
          </div>

          <div className="navbar-end">
            <div className="navbar-item">
              <div className="buttons">
                <Button
                  icon={faLockOpen}
                  onClick={() => setShowCashPassword(true)}
                />
                <Button icon={faExpand} onClick={() => screenfull.request()} />
                <Button icon={faCompress} onClick={() => screenfull.exit()} />
              </div>
            </div>
          </div>
        </div>
      </nav>
      <div className="container is-fluid">
        {data.loading && <Loading loading />}
        {children}
      </div>
      {showCashPassword && (
        <OkCancelDialog
          onCancel={() => setShowCashPassword(false)}
          onOk={openCash}
          visible
        >
          <TextInput value={cashPassword} onChange={setCashPassword} password />
        </OkCancelDialog>
      )}
      {showPrinterDialog && (
        <Alert onClose={() => setShowPrinterDialog(false)} visible>
          <FiscalPrinter />
        </Alert>
      )}
      {data.error && (
        <Modal visible>
          <p className="h5 has-text-danger">{data.error}</p>
          <Row>
            <Column>
              <Button text="Chiudi" onClick={data.clearError} />
            </Column>
          </Row>
        </Modal>
      )}
    </Fragment>
  );
}
