package gov.cpsc.hts.itds.ui.shared.dto.examlog;


/**
 * This DTO class represent ITDS/RAM Exam Disposition Type record.
 * 
 * @author hzhao
 * @since: 1.0.3
 *
 */
public class ExamDispositionType {
	
	private String dispositionCode;
	private String dispositionDescription;
	private String dispositionDate;
	/**
	 * @return the dispositionCode
	 */
	public String getDispositionCode() {
		return dispositionCode;
	}
	/**
	 * @param dispositionCode the dispositionCode to set
	 */
	public void setDispositionCode(String dispositionCode) {
		this.dispositionCode = dispositionCode;
	}
	/**
	 * @return the dispositionDescription
	 */
	public String getDispositionDescription() {
		return dispositionDescription;
	}
	/**
	 * @param dispositionDescription the dispositionDescription to set
	 */
	public void setDispositionDescription(String dispositionDescription) {
		this.dispositionDescription = dispositionDescription;
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

}
