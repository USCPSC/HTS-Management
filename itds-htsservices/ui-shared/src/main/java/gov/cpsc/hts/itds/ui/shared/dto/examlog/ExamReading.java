/**
 * 
 */
package gov.cpsc.hts.itds.ui.shared.dto.examlog;

import java.util.Date;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 * @since 1.0.3
 *
 */
public class ExamReading extends ItdsUiBaseDto{

	private static final long serialVersionUID = -8332582205905624735L;
	private Long id;
	private Long examReadingItemId;
	private String readingType;
	private String readingTypeDescription;
	private Integer readingValue;
	private String status;
	private Integer indicator;
	
	private Date createTimestamp;
	private String createUserId;
	private Date lastUpdateTimestamp;
	private String lastUpdateUserId;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the examReadingItemId
	 */
	public Long getExamReadingItemId() {
		return examReadingItemId;
	}
	/**
	 * @param examReadingItemId the examReadingItemId to set
	 */
	public void setExamReadingItemId(Long examReadingItemId) {
		this.examReadingItemId = examReadingItemId;
	}
	/**
	 * @return the readingType
	 */
	public String getReadingType() {
		return readingType;
	}
	/**
	 * @param readingType the readingType to set
	 */
	public void setReadingType(String readingType) {
		this.readingType = readingType;
	}
	/**
	 * @return the readingValue
	 */
	public Integer getReadingValue() {
		return readingValue;
	}
	/**
	 * @param readingValue the readingValue to set
	 */
	public void setReadingValue(Integer readingValue) {
		this.readingValue = readingValue;
	}
	/**
	 * @return the readingTypeDescription
	 */
	public String getReadingTypeDescription() {
		return readingTypeDescription;
	}
	/**
	 * @param readingTypeDescription the readingTypeDescription to set
	 */
	public void setReadingTypeDescription(String readingTypeDescription) {
		this.readingTypeDescription = readingTypeDescription;
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
	 * @return the indicator
	 */
	public Integer getIndicator() {
		return indicator;
	}
	/**
	 * @param indicator the indicator to set
	 */
	public void setIndicator(Integer indicator) {
		this.indicator = indicator;
	}
	/**
	 * @return the createTimestamp
	 */
	public Date getCreateTimestamp() {
		return createTimestamp;
	}
	/**
	 * @param createTimestamp the createTimestamp to set
	 */
	public void setCreateTimestamp(Date createTimestamp) {
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
	public Date getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	/**
	 * @param lastUpdateTimestamp the lastUpdateTimestamp to set
	 */
	public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
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
