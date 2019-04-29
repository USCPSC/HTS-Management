package gov.cpsc.hts.itds.ui.shared.dto.inbox;

import java.io.Serializable;
import java.util.Date;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;
/**
 * CpscEntrLineHist DTO for Inbox UI
 * 
 * @author cpan
 * for the list of entry line displayed of Inbox
 *
 */
public class CpscEntryLineHist extends ItdsUiBaseDto {
	private static final long serialVersionUID = 3428908835642100790L;
	
//	private int ramScore;
//	private String ramColor;
//	private String ramScoreVersion;
//	private String lastScoreCalcCaller;
//	private Date lastScoreCalcTimestamp;
		
//	private String nationalOp;
//	private Long ogcReferral;
//	private String ogcReferralStatus;
//	private boolean hasExam;
//	
//	private String investigatorId1st;
//		private String investigatorLastName1st;
//		private String investigatorFirstName1st;
//	private String investigatorId2nd;
//		private String investigatorLastName2nd;
//		private String investigatorFirstName2nd;

	private Long id;
	private String entryNumber;
	private String entryLineNumber;
	
	private String workflowStatusCode;
	private String workflowStatus;
	private String previousWorkflowStatusCode;
	private String previousWorkflowStatus;	
		
	private Integer workflowStatusStack;
	private String dataLastUpdateTimestamp;
	private String dataLastUpdateUserId;
	private String lastUpdateTimeStamp;
	private String lastUpdateUserId;
		private String lastUpdateUserLastName;
		private String lastUpdateUserFirstName;
		
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getEntryNumber() {
		return entryNumber;
	}
	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}
	public String getEntryLineNumber() {
		return entryLineNumber;
	}
	public void setEntryLineNumber(String entryLineNumber) {
		this.entryLineNumber = entryLineNumber;
	}
	public String getWorkflowStatusCode() {
		return workflowStatusCode;
	}
	public void setWorkflowStatusCode(String workflowStatusCode) {
		this.workflowStatusCode = workflowStatusCode;
	}
	public String getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public Integer getWorkflowStatusStack() { return workflowStatusStack; }
	public void setWorkflowStatusStack(Integer workflowStatusStack) { this.workflowStatusStack = workflowStatusStack; }
	public String getDataLastUpdateTimestamp() {
		return dataLastUpdateTimestamp;
	}
	public void setDataLastUpdateTimestamp(String dataLastUpdateTimestamp) {
		this.dataLastUpdateTimestamp = dataLastUpdateTimestamp;
	}
	public String getDataLastUpdateUserId() {
		return dataLastUpdateUserId;
	}
	public void setDataLastUpdateUserId(String dataLastUpdateUserId) {
		this.dataLastUpdateUserId = dataLastUpdateUserId;
	}
	public String getLastUpdateUserLastName() {
		return lastUpdateUserLastName;
	}
	public void setLastUpdateUserLastName(String lastUpdateUserLastName) {
		this.lastUpdateUserLastName = lastUpdateUserLastName;
	}
	public String getLastUpdateUserFirstName() {
		return lastUpdateUserFirstName;
	}
	public void setLastUpdateUserFirstName(String lastUpdateUserFirstName) {
		this.lastUpdateUserFirstName = lastUpdateUserFirstName;
	}

    public String getPreviousWorkflowStatusCode() {
		return previousWorkflowStatusCode;
	}
	public void setPreviousWorkflowStatusCode(String previousWorkflowStatusCode) {
		this.previousWorkflowStatusCode = previousWorkflowStatusCode;
	}
	public String getPreviousWorkflowStatus() {
		return previousWorkflowStatus;
	}
	public void setPreviousWorkflowStatus(String previousWorkflowStatus) {
		this.previousWorkflowStatus = previousWorkflowStatus;
	}
	public String getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}
	public void setLastUpdateTimeStamp(String lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	@Override
    public String toString() {
        return "CpscEntryLineHist{" + "id=" + id + ", entryNumber=" + entryNumber + ", entryLineNumber=" + entryLineNumber + ", workflowStatusCode=" + workflowStatusCode + ", workflowStatus=" + workflowStatus + ", workflowStatusStack=" + workflowStatusStack + ", dataLastUpdateTimestamp=" + dataLastUpdateTimestamp + ", dataLastUpdateUserId=" + dataLastUpdateUserId + ", lastUpdateUserLastName=" + lastUpdateUserLastName + ", lastUpdateUserFirstName=" + lastUpdateUserFirstName + '}';
    }

}
