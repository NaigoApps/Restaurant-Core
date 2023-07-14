import { Router } from '@reach/router';
import React, { useCallback, useState } from 'react';
import AppContext from './ApplicationContext';
import AdditionsConfigurationPage from './pages/configuration/additions/AdditionsConfigurationPage';
import ConfigurationHomePage from './pages/configuration/ConfigurationHomePage';
import ConfigurationPage from './pages/configuration/ConfigurationPage';
import CategoriesConfigurationSection from './pages/configuration/menu/CategoriesConfigurationSection';
import CategoryEditor from './pages/configuration/menu/CategoryEditor';
import MenuConfigurationPage from './pages/configuration/menu/MenuConfigurationPage';
import PrintersConfigurationPage from './pages/configuration/printers/PrintersConfigurationPage';
import TablesConfigurationPage from './pages/configuration/tables/TablesConfigurationPage';
import WaitersConfigurationPage from './pages/configuration/waiters/WaitersConfigurationPage';
import DiningTablesListPage from './pages/evenings/DiningTablesListPage';
import EveningPage from './pages/evenings/EveningPage';
import EveningSelectionPage from './pages/evenings/EveningSelectionPage';
import EveningsPage from './pages/evenings/EveningsPage';
import DiningTablePage from './pages/evenings/tables/DiningTablePage';
import HomePage from './pages/home/HomePage';
import Main from './pages/Main';
import NotFound from './pages/NotFound';
import SettingsPage from './pages/settings/SettingsPage';
import MODE from './pages/evenings/tables/mode';
import StatisticsPage from './pages/statistics/StatisticsPage';

export default function App() {
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(0);

  const [diningTable, setDiningTable] = useState(null);
  const [mode, setMode] = useState(MODE.REVIEW);

  return (
    <AppContext.Provider
      value={{
        error,
        notifyError: useCallback(err => setError(err), []),
        clearError: useCallback(() => setError(null), []),
        loading: loading > 0,
        startLoading: useCallback(() => setLoading(l => l + 1), []),
        stopLoading: useCallback(() => setLoading(l => l - 1), []),
        diningTable,
        setDiningTable: useCallback(dt => setDiningTable(dt), []),
        mode,
        setMode: useCallback(m => setMode(m), []),
      }}
    >
      <Router>
        <Main path="/">
          <HomePage path="/" />
          <StatisticsPage path="statistics" />
          <EveningsPage path="evenings">
            <EveningSelectionPage path="/" />
            <EveningPage path=":eveningDate">
              <DiningTablesListPage path="/" />
              <DiningTablePage path=":tableUuid" />
            </EveningPage>
          </EveningsPage>
          <ConfigurationPage path="configuration">
            <ConfigurationHomePage path="/" />
            <PrintersConfigurationPage path="printers" />
            <WaitersConfigurationPage path="waiters" />
            <TablesConfigurationPage path="tables" />
            <MenuConfigurationPage path="menu">
              <CategoriesConfigurationSection path="/" />
              <CategoryEditor path=":categoryUuid" />
            </MenuConfigurationPage>
            <AdditionsConfigurationPage path="additions" />
          </ConfigurationPage>
          <SettingsPage path="settings" />
        </Main>
        <NotFound default />
      </Router>
    </AppContext.Provider>
  );
}
