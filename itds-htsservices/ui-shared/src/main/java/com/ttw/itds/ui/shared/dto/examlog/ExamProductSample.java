/**
 * 
 */
package com.ttw.itds.ui.shared.dto.examlog;

import java.util.List;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * This DTO class represent ITDS/RAM exam Product/Sample record.
 * 
 * @author hzhao
 * @since: 1.0.0
 *
 */

public class ExamProductSample extends ItdsUiBaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8869151546309603773L;

	private String examId;
	private Long productSampleId;
	private String entryLineNumber;
	private String sampleNumber;
	private String dispositionCode;
	private String dispositionDescription;
	private String dispositionDate;
	private String dispositionForm;
	private String additionalActionRequested;
	private String statuteCode;
	private String storageLocation;
	private String numberProductsScreened;
	private String numberModelsScreened;
	private String factCbpEntryId;
	private String factCbpEntrySummaryId;
	private String factCbpEntrySummaryTableName;
	private String itemModel;
	private String productDescription;
	private Long manufacturerId;
	private String mid;
	private String manufacturerName;
	private String hts;
	private String status;
	private String remarks;
	
	private List<ExamReadingItem> readingItems; // a sample can have many readings
	private List<ExamProductSampleReason> reasons;  // associate with the reasons
	
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

	public ExamProductSample() {
		super();
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

	public Long getProductSampleId() {
		return productSampleId;
	}
	public void setProductSampleId(Long productSampleId) {
		this.productSampleId = productSampleId;
	}


	/**
	 * @return the entryLineNumber
	 */
	public String getEntryLineNumber() {
		return entryLineNumber;
	}

	/**
	 * @param entryLineNumber
	 *            the entryLineNumber to set
	 */
	public void setEntryLineNumber(String entryLineNumber) {
		this.entryLineNumber = entryLineNumber;
	}

	/**
	 * @return the sampleNumber
	 */
	public String getSampleNumber() {
		return sampleNumber;
	}

	/**
	 * @param sampleNumber
	 *            the sampleNumber to set
	 */
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	/**
	 * @return the dispositionCode
	 */
	public String getDispositionCode() {
		return dispositionCode;
	}

	/**
	 * @param dispositionCode
	 *            the dispositionCode to set
	 */
	public void setDispositionCode(String dispositionCode) {
		this.dispositionCode = dispositionCode;
	}

	/**
	 * @return the dispositionDescription
	 */
	public String getDispositionDescription() {
		return dispositionDescription;
	}

	/**
	 * @param dispositionDescription the dispositionDescription to set
	 */
	public void setDispositionDescription(String dispositionDescription) {
		this.dispositionDescription = dispositionDescription;
	}

	/**
	 * @return the dispositionDate
	 */
	public String getDispositionDate() {
		return dispositionDate;
	}

	/**
	 * @param dispositionDate
	 *            the dispositionDate to set
	 */
	public void setDispositionDate(String dispositionDate) {
		this.dispositionDate = dispositionDate;
	}

	/**
	 * @return the dispositionForm
	 */
	public String getDispositionForm() {
		return dispositionForm;
	}

	/**
	 * @param dispositionForm
	 *            the dispositionForm to set
	 */
	public void setDispositionForm(String dispositionForm) {
		this.dispositionForm = dispositionForm;
	}

	/**
	 * @return the additionalActionRequested
	 */
	public String getAdditionalActionRequested() {
		return additionalActionRequested;
	}

	/**
	 * @param additionalActionRequested
	 *            the additionalActionRequested to set
	 */
	public void setAdditionalActionRequested(String additionalActionRequested) {
		this.additionalActionRequested = additionalActionRequested;
	}

	/**
	 * @return the statuteCode
	 */
	public String getStatuteCode() {
		return statuteCode;
	}

	/**
	 * @param statuteCode
	 *            the statuteCode to set
	 */
	public void setStatuteCode(String statuteCode) {
		this.statuteCode = statuteCode;
	}

	/**
	 * @return the storageLocation
	 */
	public String getStorageLocation() {
		return storageLocation;
	}

	/**
	 * @param storageLocation
	 *            the storageLocation to set
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	/**
	 * @return the numberProductsScreened
	 */
	public String getNumberProductsScreened() {
		return numberProductsScreened;
	}

	/**
	 * @param numberProductsScreened
	 *            the numberProductsScreened to set
	 */
	public void setNumberProductsScreened(String numberProductsScreened) {
		this.numberProductsScreened = numberProductsScreened;
	}

	/**
	 * @return the numberModelsScreened
	 */
	public String getNumberModelsScreened() {
		return numberModelsScreened;
	}

	/**
	 * @param numberModelsScreened
	 *            the numberModelsScreened to set
	 */
	public void setNumberModelsScreened(String numberModelsScreened) {
		this.numberModelsScreened = numberModelsScreened;
	}

	/**
	 * @return the factCbpEntryId
	 */
	public String getFactCbpEntryId() {
		return factCbpEntryId;
	}

	/**
	 * @param factCbpEntryId
	 *            the factCbpEntryId to set
	 */
	public void setFactCbpEntryId(String factCbpEntryId) {
		this.factCbpEntryId = factCbpEntryId;
	}

	/**
	 * @return the factCbpEntrySummaryId
	 */
	public String getFactCbpEntrySummaryId() {
		return factCbpEntrySummaryId;
	}

	/**
	 * @param factCbpEntrySummaryId
	 *            the factCbpEntrySummaryId to set
	 */
	public void setFactCbpEntrySummaryId(String factCbpEntrySummaryId) {
		this.factCbpEntrySummaryId = factCbpEntrySummaryId;
	}

	/**
	 * @return the factCbpEntrySummaryTableName
	 */
	public String getFactCbpEntrySummaryTableName() {
		return factCbpEntrySummaryTableName;
	}

	/**
	 * @param factCbpEntrySummaryTableName
	 *            the factCbpEntrySummaryTableName to set
	 */
	public void setFactCbpEntrySummaryTableName(String factCbpEntrySummaryTableName) {
		this.factCbpEntrySummaryTableName = factCbpEntrySummaryTableName;
	}

	/**
	 * @return the itemModel
	 */
	public String getItemModel() {
		return itemModel;
	}

	/**
	 * @param itemModel
	 *            the itemModel to set
	 */
	public void setItemModel(String itemModel) {
		this.itemModel = itemModel;
	}

	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}

	/**
	 * @param productDescription
	 *            the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * @return the manufacturerId
	 */
	public Long getManufacturerId() {
		return manufacturerId;
	}

	/**
	 * @param manufacturerId
	 *            the manufacturerId to set
	 */
	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}

	/**
	 * @param mid
	 *            the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}

	/**
	 * @return the manufacturerName
	 */
	public String getManufacturerName() {
		return manufacturerName;
	}

	/**
	 * @param manufacturerName
	 *            the manufacturerName to set
	 */
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	
	/**
	 * @return the hts
	 */
	public String getHts() {
		return hts;
	}

	/**
	 * @param hts
	 *            the hts to set
	 */
	public void setHts(String hts) {
		this.hts = hts;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks
	 *            the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	/**
	 * @return the readings
	 */
	public List<ExamReadingItem> getReadingItems() {
		return readingItems;
	}

	/**
	 * @param readings the readings to set
	 */
	public void setReadingItems(List<ExamReadingItem> readingItems) {
		this.readingItems = readingItems;
	}

	
	public List<ExamProductSampleReason> getReasons() {
		return reasons;
	}
	public void setReasons(List<ExamProductSampleReason> reasons) {
		this.reasons = reasons;
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

}