
package gov.cpsc.hts.itds.ui.shared.dto.examlog;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;
/**
 * This DTO class represent ITDS/RAM exam record.
 * 
 * @author hzhao
 * @since: 1.0.0
 *
 */
public class Exam extends ItdsUiBaseDto {

	private static final long serialVersionUID = 1L;

	private String examId;
	private String examDate;
	private String investigator;
	private String investigatorFirstName;
	private String investigatorLastName;
	private String investigatorFormatted;
	private String investigator2;
	private String investigator2FirstName;
	private String investigator2LastName;
	private String importerName;
	private String importerNumber;
	private String entryNumber;  //Entry
	private String countryOfOrigin;
	private String portName;
	
	private String portCode; // added 3/15/2017 for examlog export
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

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * @return the entry
	 */
	public String getEntryNumber() {
		return entryNumber;
	}
	/**
	 * @param entry the entry to set
	 */
	public void setEntryNumber(String entry) {
		this.entryNumber = entry;
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

	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
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


	public String getInvestigatorFormatted() {
		return investigatorFormatted;
	}


	public void setInvestigatorFormatted(String investigatorFormatted) {
		this.investigatorFormatted = investigatorFormatted;
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


	public String getInvestigator2FirstName() {
		return investigator2FirstName;
	}


	public void setInvestigator2FirstName(String investigator2FirstName) {
		this.investigator2FirstName = investigator2FirstName;
	}


	public String getInvestigator2LastName() {
		return investigator2LastName;
	}


	public void setInvestigator2LastName(String investigator2LastName) {
		this.investigator2LastName = investigator2LastName;
	}

}
