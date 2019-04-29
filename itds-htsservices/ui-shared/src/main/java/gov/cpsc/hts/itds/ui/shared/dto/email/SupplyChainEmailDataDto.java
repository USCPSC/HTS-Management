package gov.cpsc.hts.itds.ui.shared.dto.email;

/**
 *
 * @author rzauel
 */
public class SupplyChainEmailDataDto {
    private String entryNo;
    private String lineNo;
    private String workflowStatusDesc;
    private String portId;
    private String portName;

    public SupplyChainEmailDataDto() {
    }

    public SupplyChainEmailDataDto(String entryNo, String lineNo, String workflowStatusDesc, String portId, String portName) {
        this.entryNo = entryNo;
        this.lineNo = lineNo;
        this.workflowStatusDesc = workflowStatusDesc;
        this.portId = portId;
        this.portName = portName;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getWorkflowStatusDesc() {
        return workflowStatusDesc;
    }

    public void setWorkflowStatusDesc(String workflowStatusDesc) {
        this.workflowStatusDesc = workflowStatusDesc;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public String toString() {
        return "SupplyChainEmailDataDto{" + "entryNo=" + entryNo + ", lineNo=" + lineNo + ", workflowStatusDesc=" + workflowStatusDesc + ", portId=" + portId + ", portName=" + portName + '}';
    }
    
}
