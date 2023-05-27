import React, { Component } from 'react';
import Modal from './modal/Modal';
import { uuid } from '../utils/Utils';
import Button from './Button';

export default class ConfirmModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uuid: 'em_' + uuid(),
    };
  }

  render() {
    return (
      <Modal visible={this.props.visible}>
        <div className="modal-header">
          <h4 className="modal-title text-danger text-center">
            {this.props.message || 'Procedere?'}
          </h4>
        </div>
        <div className="modal-footer">
          <Button
            type={this.props.confirmType || 'success'}
            text="Sì"
            commitAction={this.props.confirmAction}
          />
          <Button
            type={this.props.abortType || 'danger'}
            text="No"
            commitAction={this.props.abortAction}
          />
        </div>
      </Modal>
    );
  }
}
