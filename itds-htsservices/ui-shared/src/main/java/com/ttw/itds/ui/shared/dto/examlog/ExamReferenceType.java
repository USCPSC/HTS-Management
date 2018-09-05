package com.ttw.itds.ui.shared.dto.examlog;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * This DTO class represent ITDS/RAM Exam Reference Type record.
 * 
 * @author hzhao
 * @since: 1.0.4
 *
 */
public class ExamReferenceType extends ItdsUiBaseDto {
	private static final long serialVersionUID = 5513412822763067487L;
	private String referenceType;
	private String referenceTypeName;

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the referenceType
	 */
	public String getReferenceType() {
		return referenceType;
	}

	/**
	 * @param referenceType
	 *            the referenceType to set
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


}
