import React, { Fragment, useState } from 'react';
import BaseSettings from './BaseSettings';
import HydraSettings from './HydraSettings';

export default function SettingsPage() {
  const [page, setPage] = useState('base');

  return (
    <Fragment>
      <div className="tabs">
        <ul>
          <li className={page === 'base' && 'is-active'}>
            <a onClick={() => setPage('base')}>Impostazioni di base</a>
          </li>
          <li className={page === 'hydra' && 'is-active'}>
            <a onClick={() => setPage('hydra')}>Stampante fiscale</a>
          </li>
        </ul>
      </div>
      {page === 'base' && <BaseSettings />}
      {page === 'hydra' && <HydraSettings />}
    </Fragment>
  );
}
