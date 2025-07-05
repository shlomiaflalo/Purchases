import React from "react";
import "./ModalWindow.css";

interface ConfirmDialogProps {
    message: string;
    onConfirm: () => void;
    onCancel: () => void;
}

const ConfirmDialog: React.FC<ConfirmDialogProps> = ({message, onConfirm, onCancel}) => {
    return (
        <div className="modal-overlay">
            <div className="modal-box">
                <p>{message}</p>
                <div className="modal-buttons">
                    <button className="yes-button" onClick={onConfirm}>Yes</button>
                    <button className="no-button" onClick={onCancel}>No</button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmDialog;
