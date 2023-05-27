import Axios from 'axios';
import {
  useCallback, useContext, useEffect, useState,
} from 'react';
import AppContext from '../ApplicationContext';

export const appUrl = '/rest/';

function getOrDefault(value, defaultValue) {
  return value !== undefined ? value : defaultValue;
}

export function url(res) {
  return appUrl + res;
}

export default function (resource, config) {
  const { notifyError, startLoading, stopLoading } = useContext(AppContext);

  const [data, setData] = useState(
    config ? getOrDefault(config.default, null) : null,
  );

  const refresh = useCallback(async () => {
    try {
      startLoading();
      const response = await Axios.get(appUrl + resource);
      setData(response.data);
      stopLoading();
    } catch (err) {
      stopLoading();
      if (err.response && err.response.data) {
        console.log('Error data', err.response);
        notifyError(err.response.data);
      } else {
        notifyError('Errore sconosciuto');
      }
    }
  }, [notifyError, resource, startLoading, stopLoading]);

  const autoLoad = !config || getOrDefault(config.auto, true);

  useEffect(() => {
    if (resource && autoLoad) {
      refresh();
    }
  }, [autoLoad, refresh, resource]);

  return [data, refresh];
}
