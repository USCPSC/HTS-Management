/**
 * 
 */
package com.ttw.itds.ui.shared.dto.examlog;

import java.util.List;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;
import com.ttw.itds.ui.shared.dto.inbox.EntryLine;
import com.ttw.itds.ui.shared.dto.*;

/**
 * @author hzhao
 *
 */
public class ExamDetail extends ItdsUiBaseDto {

	private String examId;
	private String examDate;
	private String investigator;
	private String investigatorFormatted;
	private String investigator2;
	private String importerName;
	private String importerNumber;
	private String importerAddressLine1;
	private String importerAddressLine2;
	private String importerAddressCity;
	private String importerAddressState;
	private String importerAddressZip;
	private String entryNumber;  //Entry
	private String countryOfOrigin;
	private String portName;
	private String sampleNumber;
	private String numOfProductsScreened;
	private String productDescription;
	private String reasonsForScreening;
	private String lead;
	private String cadmium;
	private String ftirPhthalates;
	private String cbpActionRequested;
	private String dispositionDate;
	private String remarks;
	private String activityTime;
	private String travelTime;
	private String declinedIndicator;
	private String modelItemNumber;
	private String labelMissingIncomplete;
	private String brokerImporterNotified;
	
	private String portCode;  // Port Code
	private String ifsAssignmentId;
	private String operation;
	
	private String createUserId;
	private String createTimestamp;
	private String lastUpdateUserId;
	private String lastUpdateTimestamp;

	private List<ExamProductSample> examSamples;
	private List<ExamContact> examContacts;
	private List<ExamReference> examReferences;
	private List<ExamDocument> examDocuments;
	private List<EntryLine> examEntryLines;
	
	private String _flag; // CRUD flag
	public String get_flag() {
		return _flag;
	}
	public void set_flag(String _flag) {
		this._flag = _flag;
	}

	/**
	 * @return the examId
	 */
	public String getExamId() {
		return examId;
	}

	/**
	 * @param examId the examId to set
	 */
	public void setExamId(String examId) {
		this.examId = examId;
	}

	/**
	 * @return the examDate
	 */
	public String getExamDate() {
		return examDate;
	}

	/**
	 * @param examDate the examDate to set
	 */
	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}

	/**
	 * @return the investigator
	 */
	public String getInvestigator() {
		return investigator;
	}

	/**
	 * @param investigator the investigator to set
	 */
	public void setInvestigator(String investigator) {
		this.investigator = investigator;
	}

	/**
	 * @return the investigator2
	 */
	public String getInvestigator2() {
		return investigator2;
	}
	/**
	 * @param investigator2 the investigator2 to set
	 */
	public void setInvestigator2(String investigator2) {
		this.investigator2 = investigator2;
	}
	/**
	 * @return the importerName
	 */
	public String getImporterName() {
		return importerName;
	}

	/**
	 * @param importerName the importerName to set
	 */
	public void setImporterName(String importerName) {
		this.importerName = importerName;
	}

	/**
	 * @return the importerNumber
	 */
	public String getImporterNumber() {
		return importerNumber;
	}

	/**
	 * @param importerNumber the importerNumber to set
	 */
	public void setImporterNumber(String importerNumber) {
		this.importerNumber = importerNumber;
	}

	/**
	 * @return the importerAddressLine1
	 */
	public String getImporterAddressLine1() {
		return importerAddressLine1;
	}

	/**
	 * @param importerAddressLine1 the importerAddressLine1 to set
	 */
	public void setImporterAddressLine1(String importerAddressLine1) {
		this.importerAddressLine1 = importerAddressLine1;
	}

	/**
	 * @return the importerAddressLine2
	 */
	public String getImporterAddressLine2() {
		return importerAddressLine2;
	}

	/**
	 * @param importerAddressLine2 the importerAddressLine2 to set
	 */
	public void setImporterAddressLine2(String importerAddressLine2) {
		this.importerAddressLine2 = importerAddressLine2;
	}

	/**
	 * @return the importerAddressCity
	 */
	public String getImporterAddressCity() {
		return importerAddressCity;
	}

	/**
	 * @param importerAddressCity the importerAddressCity to set
	 */
	public void setImporterAddressCity(String importerAddressCity) {
		this.importerAddressCity = importerAddressCity;
	}

	/**
	 * @return the importerAddressState
	 */
	public String getImporterAddressState() {
		return importerAddressState;
	}

	/**
	 * @param importerAddressState the importerAddressState to set
	 */
	public void setImporterAddressState(String importerAddressState) {
		this.importerAddressState = importerAddressState;
	}

	/**
	 * @return the importerAddressZip
	 */
	public String getImporterAddressZip() {
		return importerAddressZip;
	}

	/**
	 * @param importerAddressZip the importerAddressZip to set
	 */
	public void setImporterAddressZip(String importerAddressZip) {
		this.importerAddressZip = importerAddressZip;
	}

	/**
	 * @return the entryNumber
	 */
	public String getEntryNumber() {
		return entryNumber;
	}

	/**
	 * @param entryNumber the entryNumber to set
	 */
	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}

	/**
	 * @return the countryOfOrigin
	 */
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	/**
	 * @param countryOfOrigin the countryOfOrigin to set
	 */
	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}

	/**
	 * @param portName the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}

	/**
	 * @return the sampleNumber
	 */
	public String getSampleNumber() {
		return sampleNumber;
	}

	/**
	 * @param sampleNumber the sampleNumber to set
	 */
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	/**
	 * @return the numOfProductsScreened
	 */
	public String getNumOfProductsScreened() {
		return numOfProductsScreened;
	}

	/**
	 * @param numOfProductsScreened the numOfProductsScreened to set
	 */
	public void setNumOfProductsScreened(String numOfProductsScreened) {
		this.numOfProductsScreened = numOfProductsScreened;
	}

	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}

	/**
	 * @param productDescription the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * @return the reasonsForScreening
	 */
	public String getReasonsForScreening() {
		return reasonsForScreening;
	}

	/**
	 * @param reasonsForScreening the reasonsForScreening to set
	 */
	public void setReasonsForScreening(String reasonsForScreening) {
		this.reasonsForScreening = reasonsForScreening;
	}

	/**
	 * @return the lead
	 */
	public String getLead() {
		return lead;
	}

	/**
	 * @param lead the lead to set
	 */
	public void setLead(String lead) {
		this.lead = lead;
	}

	/**
	 * @return the cadmium
	 */
	public String getCadmium() {
		return cadmium;
	}

	/**
	 * @param cadmium the cadmium to set
	 */
	public void setCadmium(String cadmium) {
		this.cadmium = cadmium;
	}

	/**
	 * @return the ftirPhthalates
	 */
	public String getFtirPhthalates() {
		return ftirPhthalates;
	}

	/**
	 * @param ftirPhthalates the ftirPhthalates to set
	 */
	public void setFtirPhthalates(String ftirPhthalates) {
		this.ftirPhthalates = ftirPhthalates;
	}

	/**
	 * @return the cbpActionRequested
	 */
	public String getCbpActionRequested() {
		return cbpActionRequested;
	}

	/**
	 * @param cbpActionRequested the cbpActionRequested to set
	 */
	public void setCbpActionRequested(String cbpActionRequested) {
		this.cbpActionRequested = cbpActionRequested;
	}

	/**
	 * @return the dispositionDate
	 */
	public String getDispositionDate() {
		return dispositionDate;
	}

	/**
	 * @param dispositionDate the dispositionDate to set
	 */
	public void setDispositionDate(String dispositionDate) {
		this.dispositionDate = dispositionDate;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the activityTime
	 */
	public String getActivityTime() {
		return activityTime;
	}

	/**
	 * @param activityTime the activityTime to set
	 */
	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	/**
	 * @return the travelTime
	 */
	public String getTravelTime() {
		return travelTime;
	}

	/**
	 * @param travelTime the travelTime to set
	 */
	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	/**
	 * @return the declinedIndicator
	 */
	public String getDeclinedIndicator() {
		return declinedIndicator;
	}

	/**
	 * @param declinedIndicator the declinedIndicator to set
	 */
	public void setDeclinedIndicator(String declinedIndicator) {
		this.declinedIndicator = declinedIndicator;
	}

	/**
	 * @return the modelItemNumber
	 */
	public String getModelItemNumber() {
		return modelItemNumber;
	}

	/**
	 * @param modelItemNumber the modelItemNumber to set
	 */
	public void setModelItemNumber(String modelItemNumber) {
		this.modelItemNumber = modelItemNumber;
	}

	/**
	 * @return the labelMissingIncomplete
	 */
	public String getLabelMissingIncomplete() {
		return labelMissingIncomplete;
	}

	/**
	 * @param labelMissingIncomplete the labelMissingIncomplete to set
	 */
	public void setLabelMissingIncomplete(String labelMissingIncomplete) {
		this.labelMissingIncomplete = labelMissingIncomplete;
	}

	/**
	 * @return the brokerImporterNotified
	 */
	public String getBrokerImporterNotified() {
		return brokerImporterNotified;
	}

	/**
	 * @param brokerImporterNotified the brokerImporterNotified to set
	 */
	public void setBrokerImporterNotified(String brokerImporterNotified) {
		this.brokerImporterNotified = brokerImporterNotified;
	}

	/**
	 * @return the portCode
	 */
	public String getPortCode() {
		return portCode;
	}

	/**
	 * @param portCode the portCode to set
	 */
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	/**
	 * @return the examSamples
	 */
	public List<ExamProductSample> getExamSamples() {
		return examSamples;
	}

	/**
	 * @param examSamples the examSamples to set
	 */
	public void setExamSamples(List<ExamProductSample> examSamples) {
		this.examSamples = examSamples;
	}

	/**
	 * @return the examContacts
	 */
	public List<ExamContact> getExamContacts() {
		return examContacts;
	}

	/**
	 * @param examContacts the examContacts to set
	 */
	public void setExamContacts(List<ExamContact> examContacts) {
		this.examContacts = examContacts;
	}

	/**
	 * @return the examReferences
	 */
	public List<ExamReference> getExamReferences() {
		return examReferences;
	}

	/**
	 * @param examReferences the examReferences to set
	 */
	public void setExamReferences(List<ExamReference> examReferences) {
		this.examReferences = examReferences;
	}

	/**
	 * @return the examDocs
	 */
	public List<ExamDocument> getExamDocuments() {
		return examDocuments;
	}

	/**
	 * @param examDocs the examDocs to set
	 */
	public void setExamDocuments(List<ExamDocument> examDocs) {
		this.examDocuments = examDocs;
	}

	/**
	 * @return the examEntryLines
	 */
	public List<EntryLine> getExamEntryLines() {
		return examEntryLines;
	}

	/**
	 * @param examEntryLines the examEntryLines to set
	 */
	public void setExamEntryLines(List<EntryLine> examEntryLines) {
		this.examEntryLines = examEntryLines;
	}

	/**
	 * @return the ifsAssignmentId
	 */
	public String getIfsAssignmentId() {
		return ifsAssignmentId;
	}

	/**
	 * @param ifsAssignmentId the ifsAssignmentId to set
	 */
	public void setIfsAssignmentId(String ifsAssignmentId) {
		this.ifsAssignmentId = ifsAssignmentId;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * @return the createTimestamp
	 */
	public String getCreateTimestamp() {
		return createTimestamp;
	}
	/**
	 * @param createTimestamp the createTimestamp to set
	 */
	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	/**
	 * @return the lastUpdateUserId
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}
	/**
	 * @param lastUpdateUserId the lastUpdateUserId to set
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	/**
	 * @return the lastUpdateTimestamp
	 */
	public String getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	/**
	 * @param lastUpdateTimestamp the lastUpdateTimestamp to set
	 */
	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}
	
	public String getInvestigatorFormatted() {
		return investigatorFormatted;
	}


	public void setInvestigatorFormatted(String investigatorFormatted) {
		this.investigatorFormatted = investigatorFormatted;
	}

	
	
}
