package com.ttw.itds.ui.shared.dto.examlog;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * This DTO class represent ITDS/RAM Exam Documents.
 * 
 * @author hzhao
 * @since: 1.0.0
 *
 */

public class ExamDocument extends ItdsUiBaseDto {

	private static final long serialVersionUID = -6551581493897719309L;

	private String examId;
	private Long documentId;
	private String documentName;
	private String documentNameOnServer; // the temp doc file name on server, timestamped, unique and no special characters
	private String documentType;  // document type code
	private String documentTypeName;  // document type name
	private String documentDate;
	private String createUserId;

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
	 * @param examId
	 *            the examId to set
	 */
	public void setExamId(String examId) {
		this.examId = examId;
	}

	/**
	 * @return the documentId
	 */
	public Long getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the documentName
	 */
	public String getDocumentName() {
		return documentName;
	}

	/**
	 * @param documentName
	 *            the documentName to set
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
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

	/**
	 * @return the documentDate
	 */
	public String getDocumentDate() {
		return documentDate;
	}

	/**
	 * @param documentDate
	 *            the documentDate to set
	 */
	public void setDocumentDate(String documentDate) {
		this.documentDate = documentDate;
	}
	/**
	 * @return the documentNameOnServer
	 */
	public String getDocumentNameOnServer() {
		return documentNameOnServer;
	}
	/**
	 * @param documentNameOnServer the documentNameOnServer to set
	 */
	public void setDocumentNameOnServer(String documentNameOnServer) {
		this.documentNameOnServer = documentNameOnServer;
	}

	public String getCreateUserId() {    
		return createUserId;    
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;	
	}
	
}