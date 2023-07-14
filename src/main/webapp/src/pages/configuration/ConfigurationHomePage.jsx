import {
  faCarrot,
  faPeopleCarry,
  faPlus,
  faPrint,
  faTable,
} from '@fortawesome/free-solid-svg-icons';
import React, { Fragment } from 'react';
import screenfull from 'screenfull';
import { faUser } from '@fortawesome/free-solid-svg-icons/faUser';
import Column from '../../widgets/Column';
import RoundButton from '../../widgets/RoundButton';
import Row from '../../widgets/Row';

export default function ({ navigate }) {
  return (
    <Fragment>
      <Row grow justifyAround>
        <Column>
          <RoundButton
            text="Tavoli"
            icon={faTable}
            onClick={() => {
              navigate('tables');
              screenfull.request();
            }}
            size="xl"
            vertical
            fill
          />
        </Column>
        <Column>
          <RoundButton
            text="Menu"
            icon={faCarrot}
            onClick={() => {
              navigate('menu');
              screenfull.request();
            }}
            size="xxl"
            vertical
            fill
          />
        </Column>
        <Column>
          <RoundButton
            text="Camerieri"
            icon={faPeopleCarry}
            onClick={() => {
              navigate('waiters');
              screenfull.request();
            }}
            size="xl"
            vertical
            fill
          />
        </Column>
      </Row>
      <Row grow justifyAround>
        <Column>
          <RoundButton
            text="Varianti"
            icon={faPlus}
            onClick={() => {
              navigate('additions');
              screenfull.request();
            }}
            size="xl"
            vertical
            fill
          />
        </Column>
        <Column>
          <RoundButton
            text="Stampanti"
            icon={faPrint}
            onClick={() => {
              navigate('printers');
              screenfull.request();
            }}
            size="xl"
            vertical
            fill
          />
        </Column>
      </Row>
    </Fragment>
  );
}
