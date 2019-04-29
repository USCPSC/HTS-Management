package gov.cpsc.hts.itds.ui.shared.dto.examlog;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * This DTO class represent ITDS/RAM Exam References.
 * 
 * @author hzhao
 * @since: 1.0.0
 *
 */

public class ExamReference extends ItdsUiBaseDto {
	private static final long serialVersionUID = -7907416154918767267L;

	private String examId;

	private Long referenceId;
	private String referenceType;  // id
	private String referenceTypeName;  // name(description)
	
	private String referenceValue;
	private String status;
	
	private String createUserId;
	private String createTimestamp;
	private String lastUpdateUserId;
	private String lastUpdateTimestamp;

	private String _flag; // CRUD flag
	public String get_flag() {
		return _flag;
	}
	public void set_flag(String _flag) {
		this._flag = _flag;
	}

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

	public Long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	

	/**
	 * @return the referenceType
	 */
	public String getReferenceType() {
		return referenceType;
	}
	/**
	 * @param referenceType the referenceType to set
	 */
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	/**
	 * @return the referenceTypeName
	 */
	public String getReferenceTypeName() {
		return referenceTypeName;
	}


	/**
	 * @param referenceTypeName the referenceTypeName to set
	 */
	public void setReferenceTypeName(String referenceTypeName) {
		this.referenceTypeName = referenceTypeName;
	}


	/**
	 * @return the referenceValue
	 */
	public String getReferenceValue() {
		return referenceValue;
	}
	/**
	 * @param referenceValue the referenceValue to set
	 */
	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
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
}
