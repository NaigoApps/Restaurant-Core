import $ from 'jquery';
import React, { useEffect, useState } from 'react';
import { randomUuid } from '../utils/Utils';

require('jquery-ui');
require('jquery-ui/ui/core');
require('jquery-ui/ui/widgets/draggable');

const DOT_SIZE = 40;
const DOT_MARGIN = 2;

export default function ({ percent, visible, onScroll }) {
  const [uuid] = useState(`scroll-bar-${randomUuid()}`);

  useEffect(() => {
    const scrollBarComponent = $(`#${uuid}`);
    const height = scrollBarComponent.parent().height();
    scrollBarComponent.css('top', percent * (height - DOT_SIZE - 2 * DOT_MARGIN) + DOT_MARGIN);

    $(`#${uuid}`).draggable({
      axis: 'y',
      drag(event, ui) {
        const cmp = ui;
        cmp.position.top = Math.max(DOT_MARGIN, ui.position.top);
        cmp.position.top = Math.min(ui.position.top, ui.helper.parent().height() - ui.helper.height() - DOT_MARGIN);

        onScroll(ui.position.top / (ui.helper.parent().height() - ui.helper.height()));
      },
    });
  }, [onScroll, percent, uuid]);

  useEffect(() => {
    const scrollBarComponent = $(`#${uuid}`);
    const height = scrollBarComponent.parent().height();
    scrollBarComponent.css('top', percent * (height - DOT_SIZE - 2 * DOT_MARGIN) + DOT_MARGIN);
  }, [percent, uuid]);

  return (
    <div className="ml-auto scrollbar">
      <div id={uuid} className="scrollbar-dot" hidden={!visible} />
    </div>
  );
}
