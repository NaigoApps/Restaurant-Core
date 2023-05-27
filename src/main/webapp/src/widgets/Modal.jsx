import classNames from 'classnames';
import React from 'react';

export default function Modal({
  visible, size, children, onClose,
}) {
  return visible && (
    <div className="modal is-active">
      <div className="modal-background" onMouseUp={onClose} />
      <div className={classNames('modal-content', {
        'is-large': size === 'lg',
        'is-medium': size === 'md',
      })}
      >
        <div className="box">
          {children}
        </div>
      </div>
      <button
        type="button"
        onMouseUp={onClose}
        className="modal-close is-large"
        aria-label="close"
      />
    </div>
  );
}
