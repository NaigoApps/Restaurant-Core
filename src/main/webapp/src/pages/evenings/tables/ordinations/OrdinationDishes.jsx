import { faTimes } from '@fortawesome/free-solid-svg-icons';
import React, { Fragment, useState } from 'react';
import TextEditor from '../../../../inputs/TextEditor';
import useRemote from '../../../../utils/useRemote';
import Button from '../../../../widgets/Button';
import Column from '../../../../widgets/Column';
import Row from '../../../../widgets/Row';
import Scrollable from '../../../../widgets/Scrollable';
import OrdinationDish from './OrdinationDish';

export default function OrdinationDishes({
  categoryUuid,
  ordination,
  onSelectDish,
  ...others
}) {
  const [dishes] = useRemote(`dishes?category=${categoryUuid}`);

  const [search, setSearch] = useState(null);

  if (!dishes) {
    return null;
  }

  const filteredDishes = dishes.filter(dish => !search || dish.name.toLowerCase()
    .includes(search.toLowerCase()));

  return (
    <Fragment>
      <Row>
        <Column grow>
          <TextEditor label="Search" initialValue={search || ''} onConfirm={setSearch}/>
        </Column>
        <Column>
          <Button icon={faTimes} kind="danger" onClick={() => setSearch(null)}/>
        </Column>
      </Row>
      <Scrollable>
        <Row grow>
          <Column grow>
            {filteredDishes.map(dish => (
              <OrdinationDish
                ordination={ordination}
                dish={dish}
                onSelect={onSelectDish}
                {...others}
              />
            ))}
          </Column>
        </Row>
      </Scrollable>
    </Fragment>
  );
}
