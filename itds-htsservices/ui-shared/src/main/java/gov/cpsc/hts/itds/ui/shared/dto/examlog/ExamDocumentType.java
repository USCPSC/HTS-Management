package gov.cpsc.hts.itds.ui.shared.dto.examlog;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * This DTO class represent ITDS/RAM Exam Document Type record.
 * 
 * @author hzhao
 * @since: 1.0.4
 *
 */
public class ExamDocumentType extends ItdsUiBaseDto {
/**
	 * 
	 */
	private static final long serialVersionUID = 7969375047646400487L;
	private String documentType;
	private String documentTypeName;

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType
	 *            the documentType to set
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	/**
	 * @return the documentTypeName
	 */
	public String getDocumentTypeName() {
		return documentTypeName;
	}

	/**
	 * @param documentTypeName the documentTypeName to set
	 */
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}


}
