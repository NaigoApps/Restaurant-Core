import { faCalendar, faCogs, faPen } from '@fortawesome/free-solid-svg-icons';
import React from 'react';
import screenfull from 'screenfull';
import { faBolt } from '@fortawesome/free-solid-svg-icons/faBolt';
import Column from '../../widgets/Column';
import RoundButton from '../../widgets/RoundButton';
import Row from '../../widgets/Row';

export default function ({ navigate }) {
  return (
    <Column grow>
      <Row justifyAround>
        <RoundButton
          text="Serate"
          icon={faCalendar}
          onClick={() => {
            navigate('evenings');
            screenfull.request();
          }}
          size="xxl"
          vertical
          fill
        />
      </Row>
      <Row grow justifyAround>
        <RoundButton
          text="Impostazioni"
          icon={faCogs}
          onClick={() => navigate('settings')}
          size="xl"
          vertical
          fill
        />
        <RoundButton
          text="Statistiche"
          icon={faBolt}
          onClick={() => navigate('statistics')}
          size="xl"
          vertical
          fill
        />
        <RoundButton
          text="Configurazione"
          icon={faPen}
          onClick={() => navigate('configuration')}
          size="xl"
          vertical
          fill
        />
      </Row>
    </Column>
  );
}
