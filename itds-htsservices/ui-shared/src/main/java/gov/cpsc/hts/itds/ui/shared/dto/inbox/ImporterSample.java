package gov.cpsc.hts.itds.ui.shared.dto.inbox;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

public class ImporterSample extends ItdsUiBaseDto {

	private static final long serialVersionUID = -2648430994943935907L;

	private String entryNo;

	private String examDate;

	private String sampleNumber;

	private String productName;

	private String importerNumber;
	
	private String customAction;

	private String status;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEntryNo() {
		return entryNo;
	}

	public String getExamDate() {
		return examDate;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public String getProductName() {
		return productName;
	}

	public String getImporterNumber() {
		return importerNumber;
	}

	public String getCustomAction() {
		return customAction;
	}

	public String getStatus() {
		return status;
	}

	public void setEntryNo(String entryNo) {
		this.entryNo = entryNo;
	}

	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setImporterNumber(String importerNumber) {
		this.importerNumber = importerNumber;
	}

	public void setCustomAction(String customAction) {
		this.customAction = customAction;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}