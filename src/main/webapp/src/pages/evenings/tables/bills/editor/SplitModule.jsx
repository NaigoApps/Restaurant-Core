import React from 'react';
import {
  faCaretLeft,
  faCaretRight,
  faFastBackward,
  faFastForward,
  faStepBackward,
  faStepForward,
} from '@fortawesome/free-solid-svg-icons';
import Row from '../../../../../widgets/Row';
import Column from '../../../../../widgets/Column';
import Button from '../../../../../widgets/Button';
import Scrollable from '../../../../../widgets/Scrollable';


export default function SplitModule({
  items,
  f,
  ff,
  fff,
  b,
  bb,
  bbb,
  display,
}) {
  return (
    <Row grow>
      <Column grow>
        <Row grow>
          <Column grow>
            <Scrollable>
              {items.map(item => (
                <Row justifyBetween grow>
                  {(b || bb) && (
                    <Column>
                      <Row>
                        <Column>
                          <Button
                            icon={faStepBackward}
                            onClick={() => bb(item)}
                          />
                        </Column>
                        <Column>
                          <Button
                            icon={faCaretLeft}
                            onClick={() => b(item)}
                          />
                        </Column>
                      </Row>
                    </Column>
                  )}
                  <Column grow justifyCenter>
                    {display(item)}
                  </Column>
                  {(f || ff) && (
                    <Column>
                      <Row>
                        <Column>
                          <Button
                            icon={faCaretRight}
                            onClick={() => f(item)}
                          />
                        </Column>
                        <Column>
                          <Button
                            icon={faStepForward}
                            onClick={() => ff(item)}
                          />
                        </Column>
                      </Row>
                    </Column>
                  )}
                </Row>
              ))}
            </Scrollable>
          </Column>
        </Row>
        <Row>
          {bbb && (
            <Column>
              <Button icon={faFastBackward} onClick={() => bbb()} />
            </Column>
          )}
          {fff && (
            <Column>
              <Button icon={faFastForward} onClick={() => fff()} />
            </Column>
          )}
        </Row>
      </Column>
    </Row>
  );
}
