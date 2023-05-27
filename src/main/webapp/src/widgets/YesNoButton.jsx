import React, { Fragment, useState } from 'react';
import Button from './Button';
import OkCancelDialog from './OkCancelDialog';

export default function YesNoButton({
  message, yesText, noText, onYes, onNo, ...others
}) {
  const [dialogVisible, setDialogVisible] = useState(false);

  return (
    <Fragment>
      <Button {...others} onClick={() => setDialogVisible(true)} />
      {message && (
        <OkCancelDialog
          okText={yesText || 'Sì'}
          cancelText={noText || 'No'}
          onOk={() => {
            setDialogVisible(false);
            onYes();
          }}
          onCancel={() => {
            setDialogVisible(false);
            onNo();
          }}
          visible={dialogVisible}
        >
          <p>{message}</p>
        </OkCancelDialog>
      )}
    </Fragment>
  );
}
