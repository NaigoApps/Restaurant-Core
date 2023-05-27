import React, { Fragment, useState } from 'react';
import Button from './Button';
import OptionsDialog from './OptionsDialog';

export default function OptionsButton({
  message,
  options,
  ...others
}) {
  const [dialogVisible, setDialogVisible] = useState(false);

  return (
    <Fragment>
      <Button
        {...others}
        onClick={() => setDialogVisible(true)}
      />
      {message && (
        <OptionsDialog
          options={options}
          onSelectOption={(opt) => {
            setDialogVisible(false);
            opt.onClick();
          }}
          onCancel={() => setDialogVisible(false)}
          visible={dialogVisible}
        >
          <p>{message}</p>
        </OptionsDialog>
      )}
    </Fragment>
  );
}
