package com.ttw.itds.ui.shared.dto.email;

/**
 * Created by dlauber on 5/27/16.
 */
public class ReconRowDto {
    private String entryNumber;
    private String portCode;
    private String operation;
    private String oldStatus;
    private String newStatus;

    public ReconRowDto() {}

    public ReconRowDto(String entryNumber, String portCode, String operation, String oldStatus, String newStatus) {
        this.entryNumber = entryNumber;
        this.portCode = portCode;
        this.operation = operation;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public String toString() {
        return "ReconRowDto{" + "entryNumber=" + entryNumber + ", portCode=" + portCode + ", operation=" + operation + ", oldStatus=" + oldStatus + ", newStatus=" + newStatus + '}';
    }
}
