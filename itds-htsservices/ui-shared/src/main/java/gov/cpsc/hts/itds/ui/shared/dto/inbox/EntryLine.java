package gov.cpsc.hts.itds.ui.shared.dto.inbox;

import java.io.Serializable;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;
/**
 * EntrLine DTO for Inbox UI
 * 
 * @author cpan
 * for the list of entry line displayed of Inbox
 *
 */
public class EntryLine extends ItdsUiBaseDto {
	private static final long serialVersionUID = 3428908835642100790L;
	
	private String entryNumber;
	private String entryLineNumber;
	private String htsNumber;
	private String importerName;
	private String importerNumber;
	private String manufacturerName;
	private String billOfLading;
	private String inBondNumber;
	private String containerNumber;
	private String filerCode;
	private String portCode;
	private String portName;
	private String releaseDate;
	private String arrivalDate;
	private int ramScore;
	private String ramColor;
	private String ramScoreVersion;
	private String nationalOp;
	private String investigatorFirstName;
	private String investigatorLastName;
	private String workflowStatus;
	private Integer workflowStatusStack;
	private Long ogcReferral;
	private String ogcReferralStatus;
	private boolean hasExam;
	private String lastUpdated;
	// added for Inbox search functions
	private String mid;
	private String filerName; // filer code in the back end
	private String consigneeNumber;
	private String consigneeName;
	private String investigatorUsername;
	
	private String _flag; // CRUD flag
	public String get_flag() {
		return _flag;
	}
	public void set_flag(String _flag) {
		this._flag = _flag;
	}
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
	public String getHtsNumber() {
		return htsNumber;
	}
	public void setHtsNumber(String htsNumber) {
		this.htsNumber = htsNumber;
	}
	public String getImporterName() {
		return importerName;
	}
	public void setImporterName(String importerName) {
		this.importerName = importerName;
	}
	public String getImporterNumber() {
		return importerNumber;
	}
	public void setImporterNumber(String importerNumber) {
		this.importerNumber = importerNumber;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getBillOfLading() {
		return billOfLading;
	}
	public void setBillOfLading(String billOfLading) {
		this.billOfLading = billOfLading;
	}
	public String getInBondNumber() {
		return inBondNumber;
	}
	public void setInBondNumber(String inBondNumber) {
		this.inBondNumber = inBondNumber;
	}
	public String getContainerNumber() {
		return containerNumber;
	}
	public void setContainerNumber(String containerNumber) {
		this.containerNumber = containerNumber;
	}
	public String getFilerCode() {
		return filerCode;
	}
	public void setFilerCode(String filerCode) {
		this.filerCode = filerCode;
	}
	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public int getRamScore() {
		return ramScore;
	}
	public void setRamScore(int ramScore) {
		this.ramScore = ramScore;
	}
	public String getRamColor() {
		return ramColor;
	}
	public void setRamColor(String ramColor) {
		this.ramColor = ramColor;
	}
	public String getRamScoreVersion() {
		return ramScoreVersion;
	}
	public void setRamScoreVersion(String ramScoreVersion) {
		this.ramScoreVersion = ramScoreVersion;
	}
	public String getNationalOp() {
		return nationalOp;
	}
	public void setNationalOp(String nationalOp) {
		this.nationalOp = nationalOp;
	}
	public String getInvestigatorFirstName() {
		return investigatorFirstName;
	}
	public void setInvestigatorFirstName(String investigatorFirstName) {
		this.investigatorFirstName = investigatorFirstName;
	}
	public String getInvestigatorLastName() {
		return investigatorLastName;
	}
	public void setInvestigatorLastName(String investigatorLastName) {
		this.investigatorLastName = investigatorLastName;
	}
	public String getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public Integer getWorkflowStatusStack() {
		return workflowStatusStack;
	}
	public void setWorkflowStatusStack(Integer workflowStatusStack) {
		this.workflowStatusStack = workflowStatusStack;
	}
	public Long getOgcReferral() {
		return ogcReferral;
	}
	public void setOgcReferral(Long ogcReferral) {
		this.ogcReferral = ogcReferral;
	}
	public boolean isHasExam() {
		return hasExam;
	}
	public void setHasExam(boolean hasExam) {
		this.hasExam = hasExam;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getFilerName() {
		return filerName;
	}
	public void setFilerName(String filerName) {
		this.filerName = filerName;
	}
	public String getConsigneeNumber() {
		return consigneeNumber;
	}
	public void setConsigneeNumber(String consigneeNumber) {
		this.consigneeNumber = consigneeNumber;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getInvestigatorUsername() {
		return investigatorUsername;
	}
	public void setInvestigatorUsername(String investigatorUsername) {
		this.investigatorUsername = investigatorUsername;
	}
	public String getOgcReferralStatus() {
		return ogcReferralStatus;
	}
	public void setOgcReferralStatus(String ogcReferralStatus) {
		this.ogcReferralStatus = ogcReferralStatus;
	}
	public String getLastUpdated() { return lastUpdated; }
	public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
}
