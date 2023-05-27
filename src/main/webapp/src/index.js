import React from 'react';
import ReactDOM from 'react-dom';
import numeral from 'numeral';
import App from './App';
import * as serviceWorker from './serviceWorker';

require('./index.scss');

// load a locale
numeral.register('locale', 'it', {
  delimiters: {
    thousands: '\'',
    decimal: ',',
  },
  abbreviations: {
    thousand: 'k',
    million: 'm',
    billion: 'b',
    trillion: 't',
  },
  ordinal(number) {
    return number === 1 ? 'st' : 'th';
  },
  currency: {
    symbol: '€',
  },
});

// switch between locales
numeral.locale('it');


ReactDOM.render(<App />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
