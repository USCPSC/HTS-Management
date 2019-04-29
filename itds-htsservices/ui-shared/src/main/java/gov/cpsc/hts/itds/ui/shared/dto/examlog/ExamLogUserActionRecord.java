package gov.cpsc.hts.itds.ui.shared.dto.examlog;

import java.io.Serializable;

public class ExamLogUserActionRecord implements Serializable {

	private static final long serialVersionUID = 4644922657199657844L;

	private String examId;
	private String entryNumber;
	private String importerNumber;
	private String importerName;
	private String lastUpdateTime;
	private String actionSourceTable;
	private String examDate;
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getEntryNumber() {
		return entryNumber;
	}
	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}
	public String getImporterNumber() {
		return importerNumber;
	}
	public void setImporterNumber(String importerNumber) {
		this.importerNumber = importerNumber;
	}
	public String getImporterName() {
		return importerName;
	}
	public void setImporterName(String importerName) {
		this.importerName = importerName;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getActionSourceTable() {
		return actionSourceTable;
	}
	public void setActionSourceTable(String actionSourceTable) {
		this.actionSourceTable = actionSourceTable;
	}
	
	public String getExamDate() {
		return examDate;
	}
	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}
	
	
}
