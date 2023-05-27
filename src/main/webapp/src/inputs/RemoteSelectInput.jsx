import React, { useEffect } from 'react';
import useRemote from '../utils/useRemote';
import SelectInput from './SelectInput';

export default function RemoteSelectInput({
  url, ...others
}) {
  const [data, refresh] = useRemote(url);

  useEffect(() => { refresh(); }, [url, refresh]);

  return (
    <SelectInput options={data || []} {...others} />
  );
}
