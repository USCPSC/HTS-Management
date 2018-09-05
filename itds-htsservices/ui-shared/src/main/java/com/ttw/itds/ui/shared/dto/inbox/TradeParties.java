package com.ttw.itds.ui.shared.dto.inbox;

import java.io.Serializable;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * TradeParties DTO for Inbox UI, working progress
 * 
 * @author cpan
 *
 */
public class TradeParties extends ItdsUiBaseDto {
	private static final long serialVersionUID = 1682507884236281928L;
	
	// entry
	private long entryId; // internal no UI
	private String entryNumber;
	private int entryLineNumber;
	
	// importer
	private long importerId; // internal no UI
	private String importerNumber;
	private String importerName;
	private String importerAddressLine1;
	private String importerAddressLine2;
	private String importerCity; // Bethesda
	private String importerStateCode; // MD
	private String importerZipCode; // 20187-0128
	
	private long manufacturerId; // internal no UI
	private String mid; // manufacturer ID
	private String manufacturerName;
	private String manufacturerAddress;
	private String manufacturerCity;
	private String manufacturerCountry;
	
	// filer
	private long filerId; // internal no UI
	private String filerName;
	private String filerPointOfContact; // First name + Last name
	private String filerPhoneOfContact; // POC phone number
	private String filerAddressLine1;
	private String filerAddressLine2;
	private String filerCity; // "Bethesda"
	private String filerStateCode; // "MD"
	private String filerZipCode; // "20187-0128"

	// consignee
	private long consigneeId;
	private String consigneeName;
	private String ein; // EIN
	private String consigneeNumber;
	private String consigneeAddressLine1;
	private String consigneeAddressLine2;
	private String consigneeCity; // "Bethesda"
	private String consigneeStateCode; // "MD"
	private String consigneeZipCode; // "20187-0128"
	
	/**
	 * @return the entryId
	 */
	public long getEntryId() {
		return entryId;
	}
	/**
	 * @param entryId the entryId to set
	 */
	public void setEntryId(long entryId) {
		this.entryId = entryId;
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
	public int getEntryLineNumber() {
		return entryLineNumber;
	}
	/**
	 * @param entryLineNumber the entryLineNumber to set
	 */
	public void setEntryLineNumber(int entryLineNumber) {
		this.entryLineNumber = entryLineNumber;
	}
	/**
	 * @return the importerId
	 */
	public long getImporterId() {
		return importerId;
	}
	/**
	 * @param importerId the importerId to set
	 */
	public void setImporterId(long importerId) {
		this.importerId = importerId;
	}
	/**
	 * @return the importerNumber
	 */
	public String getImporterNumber() {
		return importerNumber;
	}
	/**
	 * @param importerNumber the importerNumber to set
	 */
	public void setImporterNumber(String importerNumber) {
		this.importerNumber = importerNumber;
	}
	/**
	 * @return the importerName
	 */
	public String getImporterName() {
		return importerName;
	}
	/**
	 * @param importerName the importerName to set
	 */
	public void setImporterName(String importerName) {
		this.importerName = importerName;
	}
	/**
	 * @return the importerAddressLine1
	 */
	public String getImporterAddressLine1() {
		return importerAddressLine1;
	}
	/**
	 * @param importerAddressLine1 the importerAddressLine1 to set
	 */
	public void setImporterAddressLine1(String importerAddressLine1) {
		this.importerAddressLine1 = importerAddressLine1;
	}
	/**
	 * @return the importerAddressLine2
	 */
	public String getImporterAddressLine2() {
		return importerAddressLine2;
	}
	/**
	 * @param importerAddressLine2 the importerAddressLine2 to set
	 */
	public void setImporterAddressLine2(String importerAddressLine2) {
		this.importerAddressLine2 = importerAddressLine2;
	}
	/**
	 * @return the importerCity
	 */
	public String getImporterCity() {
		return importerCity;
	}
	/**
	 * @param importerCity the importerCity to set
	 */
	public void setImporterCity(String importerCity) {
		this.importerCity = importerCity;
	}
	/**
	 * @return the importerStateCode
	 */
	public String getImporterStateCode() {
		return importerStateCode;
	}
	/**
	 * @param importerStateCode the importerStateCode to set
	 */
	public void setImporterStateCode(String importerStateCode) {
		this.importerStateCode = importerStateCode;
	}
	/**
	 * @return the importerZipCode
	 */
	public String getImporterZipCode() {
		return importerZipCode;
	}
	/**
	 * @param importerZipCode the importerZipCode to set
	 */
	public void setImporterZipCode(String importerZipCode) {
		this.importerZipCode = importerZipCode;
	}
	/**
	 * @return the manufacturerId
	 */
	public long getManufacturerId() {
		return manufacturerId;
	}
	/**
	 * @param manufacturerId the manufacturerId to set
	 */
	public void setManufacturerId(long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}
	/**
	 * @param mid the mid to set
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
	 * @param manufacturerName the manufacturerName to set
	 */
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	/**
	 * @return the manufacturerAddress
	 */
	public String getManufacturerAddress() {
		return manufacturerAddress;
	}
	/**
	 * @param manufacturerAddress the manufacturerAddress to set
	 */
	public void setManufacturerAddress(String manufacturerAddress) {
		this.manufacturerAddress = manufacturerAddress;
	}
	/**
	 * @return the manufacturerCity
	 */
	public String getManufacturerCity() {
		return manufacturerCity;
	}
	/**
	 * @param manufacturerCity the manufacturerCity to set
	 */
	public void setManufacturerCity(String manufacturerCity) {
		this.manufacturerCity = manufacturerCity;
	}
	/**
	 * @return the manufacturerCountry
	 */
	public String getManufacturerCountry() {
		return manufacturerCountry;
	}
	/**
	 * @param manufacturerCountry the manufacturerCountry to set
	 */
	public void setManufacturerCountry(String manufacturerCountry) {
		this.manufacturerCountry = manufacturerCountry;
	}
	/**
	 * @return the filerId
	 */
	public long getFilerId() {
		return filerId;
	}
	/**
	 * @param filerId the filerId to set
	 */
	public void setFilerId(long filerId) {
		this.filerId = filerId;
	}
	/**
	 * @return the filerName
	 */
	public String getFilerName() {
		return filerName;
	}
	/**
	 * @param filerName the filerName to set
	 */
	public void setFilerName(String filerName) {
		this.filerName = filerName;
	}
	/**
	 * @return the filerPointOfContact
	 */
	public String getFilerPointOfContact() {
		return filerPointOfContact;
	}
	/**
	 * @param filerPointOfContact the filerPointOfContact to set
	 */
	public void setFilerPointOfContact(String filerPointOfContact) {
		this.filerPointOfContact = filerPointOfContact;
	}
	/**
	 * @return the filerPhoneOfContact
	 */
	public String getFilerPhoneOfContact() {
		return filerPhoneOfContact;
	}
	/**
	 * @param filerPhoneOfContact the filerPhoneOfContact to set
	 */
	public void setFilerPhoneOfContact(String filerPhoneOfContact) {
		this.filerPhoneOfContact = filerPhoneOfContact;
	}
	/**
	 * @return the filerAddressLine1
	 */
	public String getFilerAddressLine1() {
		return filerAddressLine1;
	}
	/**
	 * @param filerAddressLine1 the filerAddressLine1 to set
	 */
	public void setFilerAddressLine1(String filerAddressLine1) {
		this.filerAddressLine1 = filerAddressLine1;
	}
	/**
	 * @return the filerAddressLine2
	 */
	public String getFilerAddressLine2() {
		return filerAddressLine2;
	}
	/**
	 * @param filerAddressLine2 the filerAddressLine2 to set
	 */
	public void setFilerAddressLine2(String filerAddressLine2) {
		this.filerAddressLine2 = filerAddressLine2;
	}
	/**
	 * @return the filerCity
	 */
	public String getFilerCity() {
		return filerCity;
	}
	/**
	 * @param filerCity the filerCity to set
	 */
	public void setFilerCity(String filerCity) {
		this.filerCity = filerCity;
	}
	/**
	 * @return the filerStateCode
	 */
	public String getFilerStateCode() {
		return filerStateCode;
	}
	/**
	 * @param filerStateCode the filerStateCode to set
	 */
	public void setFilerStateCode(String filerStateCode) {
		this.filerStateCode = filerStateCode;
	}
	/**
	 * @return the filerZipCode
	 */
	public String getFilerZipCode() {
		return filerZipCode;
	}
	/**
	 * @param filerZipCode the filerZipCode to set
	 */
	public void setFilerZipCode(String filerZipCode) {
		this.filerZipCode = filerZipCode;
	}
	/**
	 * @return the consigneeId
	 */
	public long getConsigneeId() {
		return consigneeId;
	}
	/**
	 * @param consigneeId the consigneeId to set
	 */
	public void setConsigneeId(long consigneeId) {
		this.consigneeId = consigneeId;
	}
	/**
	 * @return the consigneeName
	 */
	public String getConsigneeName() {
		return consigneeName;
	}
	/**
	 * @param consigneeName the consigneeName to set
	 */
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	/**
	 * @return the ein
	 */
	public String getEin() {
		return ein;
	}
	/**
	 * @param ein the ein to set
	 */
	public void setEin(String ein) {
		this.ein = ein;
	}
	/**
	 * @return the consigneeNumber
	 */
	public String getConsigneeNumber() {
		return consigneeNumber;
	}
	/**
	 * @param consigneeNumber the consigneeNumber to set
	 */
	public void setConsigneeNumber(String consigneeNumber) {
		this.consigneeNumber = consigneeNumber;
	}
	/**
	 * @return the consigneeAddressLine1
	 */
	public String getConsigneeAddressLine1() {
		return consigneeAddressLine1;
	}
	/**
	 * @param consigneeAddressLine1 the consigneeAddressLine1 to set
	 */
	public void setConsigneeAddressLine1(String consigneeAddressLine1) {
		this.consigneeAddressLine1 = consigneeAddressLine1;
	}
	/**
	 * @return the consigneeAddressLine2
	 */
	public String getConsigneeAddressLine2() {
		return consigneeAddressLine2;
	}
	/**
	 * @param consigneeAddressLine2 the consigneeAddressLine2 to set
	 */
	public void setConsigneeAddressLine2(String consigneeAddressLine2) {
		this.consigneeAddressLine2 = consigneeAddressLine2;
	}
	/**
	 * @return the consigneeCity
	 */
	public String getConsigneeCity() {
		return consigneeCity;
	}
	/**
	 * @param consigneeCity the consigneeCity to set
	 */
	public void setConsigneeCity(String consigneeCity) {
		this.consigneeCity = consigneeCity;
	}
	/**
	 * @return the consigneeStateCode
	 */
	public String getConsigneeStateCode() {
		return consigneeStateCode;
	}
	/**
	 * @param consigneeStateCode the consigneeStateCode to set
	 */
	public void setConsigneeStateCode(String consigneeStateCode) {
		this.consigneeStateCode = consigneeStateCode;
	}
	/**
	 * @return the consigneeZipCode
	 */
	public String getConsigneeZipCode() {
		return consigneeZipCode;
	}
	/**
	 * @param consigneeZipCode the consigneeZipCode to set
	 */
	public void setConsigneeZipCode(String consigneeZipCode) {
		this.consigneeZipCode = consigneeZipCode;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
