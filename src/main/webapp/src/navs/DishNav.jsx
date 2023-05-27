import React from 'react';
import useRemote from '../utils/useRemote';
import DefaultNav from './DefaultNav';

export default function DishNav({
  dishUuid, ...others
}) {
  const [dish] = useRemote(`dishes/${dishUuid}`);

  return <DefaultNav label={dish && dish.name} {...others} />;
}
