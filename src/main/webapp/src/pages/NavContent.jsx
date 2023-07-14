import { Router } from '@reach/router';
import React from 'react';
import CategoryNav from '../navs/CategoryNav';
import DefaultNav from '../navs/DefaultNav';
import DiningTablePageNav from '../navs/DiningTablePageNav';
import DishNav from '../navs/DishNav';
import EveningPageNav from '../navs/EveningPageNav';
import EveningsPageNav from '../navs/EveningsPageNav';
import HomePageNav from '../navs/HomePageNav';

export default function NavContent() {
  return (
    <Router primary={false}>
      <HomePageNav path="/">
        <DefaultNav path="statistics" label="Statistiche" />
        <EveningsPageNav path="evenings">
          <EveningPageNav path=":eveningDate">
            <DiningTablePageNav path=":tableUuid/*" />
          </EveningPageNav>
        </EveningsPageNav>
        <DefaultNav path="configuration" label="Configurazione">
          <DefaultNav path="tables" label="Tavoli" />
          <DefaultNav path="printers" label="Stampanti" />
          <DefaultNav path="waiters" label="Camerieri" />
          <DefaultNav path="menu" label="Menu">
            <CategoryNav path=":categoryUuid">
              <DishNav path=":dishUuid" />
            </CategoryNav>
          </DefaultNav>
          <DefaultNav path="additions" label="Varianti" />
        </DefaultNav>
        <DefaultNav path="settings" label="Impostazioni" />
      </HomePageNav>
    </Router>
  );
}
