package com.ttw.itds.ui.shared.dto.examlog;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * This DTO class represent ITDS/RAM Exam Contact record.
 * 
 * @author hzhao
 * @since: 1.0.0
 *
 */

public class ExamContact extends ItdsUiBaseDto {

	private static final long serialVersionUID = 5979984925755843683L;
	private String _flag; // CRUD flag
	public String get_flag() {
		return _flag;
	}
	public void set_flag(String _flag) {
		this._flag = _flag;
	}

	public ExamContact() {
		super();
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String examId;
	private Long contactId;

	/*
	 * Type - CBP Officer, CBP Port Director, Import Broker, Specialist, or Importer
	 */
	private String contactType;  // code
	private String contactTypeName;  // name(description)
	private String name;
	private String phoneNumber;
	private String email;
	private String fax;
	
	private String createUserId;
	private String createTimestamp;
	private String lastUpdateUserId;
	private String lastUpdateTimestamp;

	/**
	 * @return the examId
	 */
	public String getExamId() {
		return examId;
	}

	/**
	 * @param examId
	 *            the examId to set
	 */
	public void setExamId(String examId) {
		this.examId = examId;
	}

	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long id) {
		this.contactId = id;
	}

	/**
	 * @return the contactType
	 */
	public String getContactType() {
		return contactType;
	}

	/**
	 * @param contactType
	 *            the contactType to set
	 */
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	/**
	 * @return the contactTypeName
	 */
	public String getContactTypeName() {
		return contactTypeName;
	}

	/**
	 * @param contactTypeName the contactTypeName to set
	 */
	public void setContactTypeName(String contactTypeName) {
		this.contactTypeName = contactTypeName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId
	 *            the createUserId to set
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
	 * @param createTimestamp
	 *            the createTimestamp to set
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
	 * @param lastUpdateUserId
	 *            the lastUpdateUserId to set
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
	 * @param lastUpdateTimestamp
	 *            the lastUpdateTimestamp to set
	 */
	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}
	/**
	 * 
	 */

}