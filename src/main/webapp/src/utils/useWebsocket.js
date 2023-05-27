import {useEffect, useState} from 'react';
import SockJS from 'sockjs-client';

const Stomp = require('stompjs');

export default function useWebsocket(url, onMessage) {
  const [stompClient, setStompClient] = useState(null);

  useEffect(() => {
    if (!stompClient) {
      const s = new SockJS('/restaurant');

      const client = Stomp.over(s);
      client.connect({}, () => {
        client.subscribe(`/topic/${url}`, onMessage);
      });

      setStompClient(client);
    }
    return () => {
      if (stompClient != null) {
        try {
          stompClient.disconnect();
        } catch (e) {
          console.log(e);
        }
        setStompClient(null);
      }
    };
  }, [onMessage, stompClient, url]);
}
