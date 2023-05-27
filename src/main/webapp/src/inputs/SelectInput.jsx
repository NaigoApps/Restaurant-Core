import React, { useEffect, useState } from 'react';
import { distribute, randomUuid } from '../utils/Utils';
import Button from '../widgets/Button';
import ButtonGroup from '../widgets/ButtonGroup';
import Column from '../widgets/Column';
import ColumnSpace from '../widgets/ColumnSpace';
import Row from '../widgets/Row';

export default function SelectInput({
  id,
  text,
  bg = () => 'secondary',
  options,
  rows,
  cols,
  multiSelect,
  value,
  alwaysShowPages,
  onSelectOption,
}) {
  const [page, setPage] = useState(0);

  useEffect(() => {
    setPage(0);
  }, [options]);

  function isSelected(v) {
    if (!value) {
      return false;
    }
    if (!multiSelect) {
      return id(value) === id(v);
    }
    return !!value.find(val => id(val) === id(v));
  }

  function buildPageButtons(groups, currentPage) {
    if (groups.length > 1 || alwaysShowPages) {
      const btns = [];
      groups.forEach((group, index) => {
        btns.push(
          <Button
            key={btns.length}
            active={currentPage === index}
            onClick={() => setPage(index)}
            text={index + 1}
          />,
        );
      });
      return (
        <Row margin={null}>
          <Column grow>
            <ButtonGroup>{btns}</ButtonGroup>
          </Column>
        </Row>
      );
    }
    return null;
  }

  const rs = rows || 3;
  const cs = cols || 3;
  const pageSize = rs * cs;

  let optionsList;

  optionsList = distribute(options, pageSize);
  const currentPage = Math.min(page, optionsList.length - 1);

  const pageButtons = buildPageButtons(optionsList, currentPage);

  optionsList = optionsList[currentPage];
  optionsList = distribute(optionsList, cs);
  optionsList = optionsList.map((row, rowIndex) => {
    const buttons = row.map((option) => {
      // FIXME
      const color = option.color || 'secondary';

      return (
        <Column key={id(option)} grow grid>
          <Button
            type={bg}
            color={color}
            active={isSelected(option)}
            kind={bg(option)}
            text={text(option)}
            onClick={() => onSelectOption(option)}
          />
        </Column>
      );
    });

    while (buttons.length < cs) {
      buttons.push(<ColumnSpace key={buttons.length} grow grid />);
    }
    return <Row margin={null} key={randomUuid()} grow wrap spaced>{buttons}</Row>;
  });

  while (optionsList.length < rs) {
    optionsList.push(
      <Row margin={null} key={optionsList.length} grow>
        <ColumnSpace />
      </Row>,
    );
  }

  return (
    <Row grow>
      <Column grow spaced>
        <Row>
          <Column grow spaced>{optionsList}</Column>
        </Row>
        {pageButtons}
      </Column>
    </Row>
  );
}
