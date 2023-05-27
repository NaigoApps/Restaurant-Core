import React from 'react';
import useRemote from '../utils/useRemote';
import DefaultNav from './DefaultNav';

export default function CategoryNav({
  categoryUuid, ...others
}) {
  const [category] = useRemote(`categories/${categoryUuid}`);

  return <DefaultNav label={category && category.name} {...others} />;
}
