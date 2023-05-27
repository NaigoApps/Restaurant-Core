import { faAngleDown, faAngleUp } from '@fortawesome/free-solid-svg-icons';
import $ from 'jquery';
import React, { useEffect, useState, useCallback } from 'react';
import { randomUuid } from '../utils/Utils';
import Button from './Button';
import Column from './Column';
import Row from './Row';
import useInterval from '../utils/useInterval';

require('jquery-ui/ui/widgets/draggable');

export default function ({ children }) {
  const [uuid] = useState(`scrollable_${randomUuid()}`);
  const [percent, setPercent] = useState(0);
  const [scrollbarNeeded, setScrollbarNeeded] = useState(false);

  function adjustPercent(value) {
    if (value < 0) {
      return 0;
    }
    if (value > 1) {
      return 1;
    }
    return value;
  }

  const updateScrollbar = useCallback(() => {
    const height = $(`#${uuid}`)
      .height();
    const parentHeight = $(`#${uuid}`)
      .parent()
      .height();
    setScrollbarNeeded(parentHeight < height);
  }, [uuid]);

  const updateComponents = useCallback(() => {
    const height = $(`#${uuid}`)
      .height();
    const parentHeight = $(`#${uuid}`)
      .parent()
      .height();

    const child = $(`#${uuid}`);
    const dot = $(`#${uuid}-dot`);

    if (scrollbarNeeded) {
      child.css('top', `${percent * (parentHeight - height)}px`);
      dot.css('top', `${percent * (dot.parent()
        .height() - dot.height())}px`);
    } else {
      child.css('top', '0');
    }
  }, [percent, scrollbarNeeded, uuid]);

  useEffect(() => {
    updateScrollbar();
  }, [updateScrollbar]);

  useEffect(() => {
    updateComponents();
  }, [updateComponents]);

  useInterval(updateScrollbar, 1000);
  useInterval(updateComponents, 1000);

  function moveUp() {
    const height = $(`#${uuid}`)
      .height();
    const parentHeight = $(`#${uuid}`)
      .parent()
      .height();
    setPercent(p => adjustPercent(p + 100 / (parentHeight - height)));
  }

  function moveDown() {
    const height = $(`#${uuid}`)
      .height();
    const parentHeight = $(`#${uuid}`)
      .parent()
      .height();
    setPercent(p => adjustPercent(p - 100 / (parentHeight - height)));
  }

  return (
    <Row grow>
      <Column grow>
        <div className="scrollable-container">
          <div id={uuid} className="scrollable-content">
            {children}
          </div>
        </div>
      </Column>
      <Column>
        <Button
          icon={faAngleUp}
          onClick={moveUp}
          disabled={!scrollbarNeeded || percent === 0}
        />
        <Row grow relative>
          {scrollbarNeeded && (
            <div id={`${uuid}-dot`} className="scrollable-dot"/>
          )}
        </Row>
        <Button
          icon={faAngleDown}
          onClick={moveDown}
          disabled={!scrollbarNeeded || percent === 1}
        />
      </Column>
    </Row>
  );
}
