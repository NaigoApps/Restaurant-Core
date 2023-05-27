import React, { Fragment, useEffect } from 'react';
import RemoteSelectInput from '../../../../inputs/RemoteSelectInput';
import SelectEditor from '../../../../inputs/SelectEditor';
import useRemote from '../../../../utils/useRemote';
import Button from '../../../../widgets/Button';
import Column from '../../../../widgets/Column';
import Row from '../../../../widgets/Row';
import OrdinationDishes from './OrdinationDishes';

export default function ({
  ordination,
  category,
  onSelectCategory,
  phase,
  onSelectPhase,
  quantity,
  onSelectQuantity,
  onSelectDish,
  ...others
}) {
  const [phases] = useRemote('phases');

  useEffect(() => {
    if (phases && !phase) {
      onSelectPhase(phases[0]);
    }
  }, [onSelectPhase, phase, phases]);

  return (
    <Fragment>
      <Row grow>
        <Column grow>
          <Row>
            {category && (
              <Column grow>
                <Button
                  text="Categorie"
                  active={!category}
                  disabled={!category}
                  onClick={() => onSelectCategory(null)}
                />
              </Column>
            )}
            <Column grow>
              <SelectEditor
                label="Portata"
                kind="info"
                rows={2}
                cols={2}
                options={phases}
                id={p => p.uuid}
                text={p => p.name}
                value={phase}
                onSelectOption={onSelectPhase}
              />
            </Column>
          </Row>
          <Row grow>
            <Column grow>
              <Row>
                <Column grow>
                  <Row>
                    {!category && (
                      <Column grow>
                        <p className="h5 has-text-centered">Categorie</p>
                      </Column>
                    )}
                    {category && (
                      <Column grow>
                        <p className="h5 has-text-centered">{category.name}</p>
                      </Column>
                    )}

                  </Row>
                </Column>
              </Row>
              <Row grow>
                <Column grow>
                  {!category ? (
                    <RemoteSelectInput
                      url="categories"
                      rows={6}
                      cols={2}
                      id={cat => cat.uuid}
                      text={cat => cat.name}
                      value={category}
                      onSelectOption={onSelectCategory}
                      onDeselect={() => {
                      }}
                      alwaysShowPages
                    />
                  ) : (
                    <OrdinationDishes
                      ordination={ordination}
                      categoryUuid={category.uuid}
                      onSelectDish={onSelectDish}
                      {...others}
                    />
                  )}
                </Column>
              </Row>
            </Column>
          </Row>
        </Column>
      </Row>
    </Fragment>
  );
}
