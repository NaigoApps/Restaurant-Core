import React, {useContext} from 'react';
import Column from '../../../widgets/Column';
import Row from '../../../widgets/Row';
import DiningTableActions from './DiningTableActions';
import useRemote from '../../../utils/useRemote';
import DiningTableReview from './DiningTableReview';
import DiningTableOrdinations from './ordinations/DiningTableOrdinations';
import DiningTableBills from './bills/DiningTableBills';
import MODE from './mode';
import AppContext from '../../../ApplicationContext';
import SockJsClient from "react-stomp";

export default function DiningTablePage({
  tableUuid,
  navigate
}) {
  const [table, refresh] = useRemote(`dining-tables/${tableUuid}`);

  const {
    mode,
    setMode
  } = useContext(AppContext);

  return table && (
      <>
        <SockJsClient
            url={'http://localhost:8080/restaurant'}
            topics={[`/topic/dining-tables/${tableUuid}`]}
            onConnect={() => console.log("Connected to dining table ws")}
            onDisconnect={() => console.log("Disconnected from dining table ws")}
            onMessage={() => {
              console.log("New ordination detected")
              refresh();
            }}
        />
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
      </>
  );
}
