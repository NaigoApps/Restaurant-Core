import moment from 'moment';
import React from 'react';
import Calendar from '../../widgets/Calendar';

export default function ({ navigate }) {
  function selectEvening(date) {
    navigate(`${moment(date).format('YYYY-MM-DD')}`);
  }

  return (
    <Calendar initialDate={new Date()} onChange={d => selectEvening(d)} />
  );
}
