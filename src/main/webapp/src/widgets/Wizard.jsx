import React from 'react';
import { faCaretLeft, faCaretRight } from '@fortawesome/free-solid-svg-icons';
import Row from './Row';
import Column from './Column';
import Button from './Button';

export default function Wizard({ children, wizard }) {
  const pages = React.Children.map(children, child => React.cloneElement(child));

  return (
    <Row grow>
      <Column>
        {React.Children.toArray(pages)[wizard.currentPage]}
        <Row>
          {wizard.annullable
            && (
            <Column>
              <Button
                text="Annulla"
                kind="danger"
                onClick={wizard.abort}
              />
            </Column>
            )}
          {wizard.hasPrevious && (
            <Column>
              <Button
                text="Indietro"
                kind="info"
                onClick={wizard.previous}
              />
            </Column>
          )}
          {wizard.hasNext
            && (
            <Column>
              <Button
                text="Avanti"
                kind="info"
                onClick={wizard.next}
              />
            </Column>
            )}
          {wizard.isValid
              && (
              <Column>
                <Button
                  text="Conferma"
                  kind="success"
                  onClick={wizard.confirm}
                />
              </Column>
              )}
        </Row>
      </Column>
    </Row>
  );
}
