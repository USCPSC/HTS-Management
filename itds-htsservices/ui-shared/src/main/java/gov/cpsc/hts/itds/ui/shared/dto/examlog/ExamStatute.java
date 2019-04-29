/**
 * 
 */
package gov.cpsc.hts.itds.ui.shared.dto.examlog;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 * @since: 1.0.3
 *
 */
public class ExamStatute extends ItdsUiBaseDto {

	private static final long serialVersionUID = -7757418488973706507L;

	private String statuteCode;
	private String statuteDescription;
	private String status;
	/**
	 * @return the statuteCode
	 */
	public String getStatuteCode() {
		return statuteCode;
	}
	/**
	 * @param statuteCode the statuteCode to set
	 */
	public void setStatuteCode(String statuteCode) {
		this.statuteCode = statuteCode;
	}
	/**
	 * @return the statuteDescription
	 */
	public String getStatuteDescription() {
		return statuteDescription;
	}
	/**
	 * @param statuteDescription the statuteDescription to set
	 */
	public void setStatuteDescription(String statuteDescription) {
		this.statuteDescription = statuteDescription;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
