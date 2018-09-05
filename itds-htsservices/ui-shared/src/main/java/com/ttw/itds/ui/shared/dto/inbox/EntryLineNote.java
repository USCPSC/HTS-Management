/**
 * 
 */
package com.ttw.itds.ui.shared.dto.inbox;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 * @since 1.0.6
 *
 */
public class EntryLineNote extends ItdsUiBaseDto {

	private static final long serialVersionUID = -554458968653917245L;

	private Long id;
	private Long cbpEntryId;
	private Long cbpEntryLineId;
	private Long cpscEntryLineId;

	private String entryNumber;
	private Integer entryLineNumber;
	private String note;

	private String status;

	private String createUserId;
	private String createTimestamp;
	private String lastUpdateUserId;
	private String lastUpdateTimestamp;

	
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
	 * @return the cbpEntryId
	 */
	public Long getCbpEntryId() {
		return cbpEntryId;
	}
	/**
	 * @param cbpEntryId the cbpEntryId to set
	 */
	public void setCbpEntryId(Long cbpEntryId) {
		this.cbpEntryId = cbpEntryId;
	}
	/**
	 * @return the cbpEntryLineId
	 */
	public Long getCbpEntryLineId() {
		return cbpEntryLineId;
	}
	/**
	 * @param cbpEntryLineId the cbpEntryLineId to set
	 */
	public void setCbpEntryLineId(Long cbpEntryLineId) {
		this.cbpEntryLineId = cbpEntryLineId;
	}
	/**
	 * @return the cpscEntryLineId
	 */
	public Long getCpscEntryLineId() {
		return cpscEntryLineId;
	}
	/**
	 * @param cpscEntryLineId the cpscEntryLineId to set
	 */
	public void setCpscEntryLineId(Long cpscEntryLineId) {
		this.cpscEntryLineId = cpscEntryLineId;
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
	 * @return the entryLineNumber
	 */
	public Integer getEntryLineNumber() {
		return entryLineNumber;
	}
	/**
	 * @param entryLineNumber the entryLineNumber to set
	 */
	public void setEntryLineNumber(Integer entryLineNumber) {
		this.entryLineNumber = entryLineNumber;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
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
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
