import React, { useEffect } from 'react';
import useRemote from '../utils/useRemote';
import SelectEditor from './SelectEditor';

export default function RemoteSelectEditor({
  url, ...others
}) {
  const [data, refresh] = useRemote(url);

  useEffect(() => { refresh(); }, [url, refresh]);

  return (
    <SelectEditor options={data || []} {...others} />
  );
}
