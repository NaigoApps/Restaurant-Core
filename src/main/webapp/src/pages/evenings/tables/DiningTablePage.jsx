import React, { useContext, useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import Column from '../../../widgets/Column';
import Row from '../../../widgets/Row';
import DiningTableActions from './DiningTableActions';
import useRemote from '../../../utils/useRemote';
import DiningTableReview from './DiningTableReview';
import DiningTableOrdinations from './ordinations/DiningTableOrdinations';
import DiningTableBills from './bills/DiningTableBills';
import MODE from './mode';
import AppContext from '../../../ApplicationContext';

export default function DiningTablePage({
  tableUuid,
  navigate
}) {
  const [table, refresh] = useRemote(`dining-tables/${tableUuid}`);

  const {
    mode,
    setMode
  } = useContext(AppContext);

  useEffect(() => {
    const sock = new SockJS('/restaurant');
    const client = Stomp.over(sock);
    client.connect({}, () => client
      .subscribe(`/topic/dining-tables/${tableUuid}`, () => refresh()));
    return () => client.disconnect();
  }, [refresh, tableUuid]);

  return table && (
    <Row grow spaced>
      <Column grow>
        {mode === MODE.REVIEW && <DiningTableReview table={table}/>}
        {mode === MODE.ORDINATIONS && <DiningTableOrdinations table={table} refresh={refresh}/>}
        {mode === MODE.BILLS && <DiningTableBills table={table} refresh={refresh}/>}
      </Column>
      <Column>
        <DiningTableActions
          navigate={navigate}
          setMode={setMode}
          table={table}
          refresh={refresh}
        />
      </Column>
    </Row>
  );
}
