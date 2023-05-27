import React, { Fragment, useState } from 'react';
import Button from './Button';
import OkCancelDialog from './OkCancelDialog';

export default function ConfirmButton({
  confirmMessage,
  onClick,
  ...others
}) {
  const [dialogVisible, setDialogVisible] = useState(false);

  return (
    <Fragment>
      <Button
        {...others}
        onClick={() => setDialogVisible(true)}
      />
      {confirmMessage && (
      <OkCancelDialog
        onOk={() => {
          setDialogVisible(false);
          onClick();
        }}
        onCancel={() => setDialogVisible(false)}
        visible={dialogVisible}
      >
        <p>{confirmMessage}</p>
      </OkCancelDialog>
      )}
    </Fragment>
  );
}
