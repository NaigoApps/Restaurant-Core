import { useState, useEffect, useCallback } from 'react';
import useRemote from './useRemote';
import useNetwork from './useNetwork';

export default function useEditor({
  url, uuid, initialValue,
}) {
  const { put, post } = useNetwork();

  const [entity, setEntity] = useState(null);

  const confirm = useCallback(async () => {
    if (uuid) {
      await put(`${url}/${uuid}`, entity);
    } else {
      await post(url, entity);
    }
  }, [entity, post, put, url, uuid]);

  const [loadedEntity] = useRemote(`${url}/${uuid}`, {
    auto: !!uuid,
    default: initialValue,
  });

  useEffect(() => {
    setEntity(loadedEntity);
  }, [loadedEntity]);

  return [entity, setEntity, confirm];
}
