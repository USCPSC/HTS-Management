package com.ttw.itds.ui.shared.dto.examlog;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;
/**
 * This DTO class represent ITDS/RAM Exam Reason For Screening record.
 * 
 * @author hzhao
 * @since: 1.0.3
 *
 */
public class ExamProductSampleReason extends ItdsUiBaseDto {

	private static final long serialVersionUID = -6492490138797267628L;

	private Long productSampleId;
	private String reasonCode;
	private String reasonName;
	private String reasonType;  // 'S': sample reason; NULL: regular screening reason

	private String createTimestamp;
	private String createUserId;
	private String lastUpdateTimestamp;
	private String lastUpdateUserId;

	
	/**
	 * @return the productSampleId
	 */
	public Long getProductSampleId() {
		return productSampleId;
	}
	/**
	 * @param productSampleId the productSampleId to set
	 */
	public void setProductSampleId(Long productSampleId) {
		this.productSampleId = productSampleId;
	}
	/**
	 * @return the reasonCode
	 */
	public String getReasonCode() {
		return reasonCode;
	}
	/**
	 * @param reasonCode the reasonCode to set
	 */
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	/**
	 * @return the reasonName
	 */
	public String getReasonName() {
		return reasonName;
	}
	/**
	 * @param reasonName the reasonName to set
	 */
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	/**
	 * @return the reasonType
	 */
	public String getReasonType() {
		return reasonType;
	}
	/**
	 * @param reasonType the reasonType to set
	 */
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

