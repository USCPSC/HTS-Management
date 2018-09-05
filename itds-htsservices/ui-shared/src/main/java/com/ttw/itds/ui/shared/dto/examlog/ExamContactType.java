package com.ttw.itds.ui.shared.dto.examlog;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * This DTO class represent ITDS/RAM Exam Disposition Type record.
 * 
 * @author hzhao
 * @since: 1.0.4
 *
 */
public class ExamContactType extends ItdsUiBaseDto {

	private static final long serialVersionUID = 5868073484347080842L;

	private String contactType;
	private String contactTypeName;

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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


}
