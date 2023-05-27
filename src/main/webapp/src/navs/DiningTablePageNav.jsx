import React, { Fragment, useContext } from 'react';
import Button from '../widgets/Button';
import ButtonGroup from '../widgets/ButtonGroup';
import AppContext from '../ApplicationContext';
import MODE from '../pages/evenings/tables/mode';

export default function DiningTablePageNav({
  children,
}) {
  const { mode, setMode } = useContext(AppContext);

  return (
    <Fragment>
      <ButtonGroup>
        <Button
          text="Riepilogo"
          kind="info"
          onClick={() => setMode(MODE.REVIEW)}
          active={mode === MODE.REVIEW}
          disabled={mode === MODE.REVIEW}
        />
        <Button
          text="Comande"
          kind="info"
          onClick={() => setMode(MODE.ORDINATIONS)}
          active={mode === MODE.ORDINATIONS}
          disabled={mode === MODE.ORDINATIONS}
        />
        <Button
          text="Conti"
          kind="info"
          onClick={() => setMode(MODE.BILLS)}
          active={mode === MODE.BILLS}
          disabled={mode === MODE.BILLS}
        />
      </ButtonGroup>
      {children}
    </Fragment>
  );
}
