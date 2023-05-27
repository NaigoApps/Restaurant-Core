import React from 'react';
import Column from '../../../widgets/Column';
import Row from '../../../widgets/Row';
import LocationsConfigurationSection from './LocationsConfigurationSection';
import PrintersConfigurationSection from './PrintersConfigurationSection';

export default function PrintersConfigurationPage() {
  return (
    <Row grow spaced>
      <Column grow>
        <LocationsConfigurationSection />
      </Column>
      <Column grow>
        <PrintersConfigurationSection />
      </Column>
    </Row>
  );
}
