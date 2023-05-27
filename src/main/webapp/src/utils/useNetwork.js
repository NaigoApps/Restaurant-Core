import { useCallback, useContext } from 'react';
import Axios from 'axios';
import AppContext from '../ApplicationContext';

export const appUrl = '/rest/';

const config = {
  headers: {
    'Content-Type': 'application/json',
  },
};

export default function () {
  const { notifyError, startLoading, stopLoading } = useContext(AppContext);

  const remoteCall = useCallback(
    async (call) => {
      try {
        startLoading();
        const response = await call();
        stopLoading();
        return response.data;
      } catch (err) {
        stopLoading();
        if (err.response && err.response.data) {
          notifyError(err.response.data);
        } else {
          notifyError('Errore sul server');
        }
      }
      return null;
    },
    [notifyError, startLoading, stopLoading],
  );

  const get = useCallback(res => remoteCall(() => Axios.get(appUrl + res)), [
    remoteCall,
  ]);
  const put = useCallback(
    (res, data) => remoteCall(() => Axios.put(appUrl + res, data, config)),
    [remoteCall],
  );
  const post = useCallback(
    (res, data) => remoteCall(() => Axios.post(appUrl + res, data)),
    [remoteCall],
  );
  const remove = useCallback(
    res => remoteCall(() => Axios.delete(appUrl + res)),
    [remoteCall],
  );

  return {
    get,
    put,
    post,
    remove,
  };
}
