package com.ttw.itds.ui.shared.dto.inbox;

import java.io.Serializable;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author cpan
 * To display entry line details when expanded
 *
 */
public class EntryLineDetails extends ItdsUiBaseDto {
	private static final long serialVersionUID = -7817992483277081604L;

	// entry
	private String entryDate;
	private String entryNumber;
	private Integer lineNumber;
	private Double entryValue;
	private String investigatorFirstName;
	private String investigatorLastName;
	private String htsDescription;

	private Integer numberOfLines;
	
//	private String bol; // Bill Of Lading
	// private String airwayBill; // future air cargo carriers
	
	private String billOfLading;
	private String inBondNumber;
	private String containerNumber;
	
	private String arrivalDateEstimate;
	private String arrivalDateActual;
	
	private String locationOfGoods;
	
	// carrier
	private String carrierName;
	private String mot; // Mode of transport: land, air, water description
	private String vessel;  // vessel name
	
	// port
//	private String foreignPortCode; 
	private String foreignPortName;
	
	private String portCode; // no display
	private String portName; // port of entry
	
	private String unladingPortCode; // no display
	private String unladingPortName; // port of unlading

	private String ftz; // Foreign Trade Zone
	private String ogc; // Office of General Counsel

	private Long importerId; // internal no UI
	private Long filerId; // internal no UI
	private Long consigneeId; // internal no UI
	private Long manufacturerId; // internal no UI
	
	
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getEntryNumber() {
		return entryNumber;
	}
	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	public Double getEntryValue() {
		return entryValue;
	}
	public void setEntryValue(Double entryValue) {
		this.entryValue = entryValue;
	}
	public String getInvestigatorFirstName() {
		return investigatorFirstName;
	}
	public void setInvestigatorFirstName(String investigatorFirstName) {
		this.investigatorFirstName = investigatorFirstName;
	}
	public String getInvestigatorLastName() {
		return investigatorLastName;
	}
	public void setInvestigatorLastName(String investigatorLastName) {
		this.investigatorLastName = investigatorLastName;
	}
	public String getHtsDescription() {
		return htsDescription;
	}
	public void setHtsDescription(String htsDescription) {
		this.htsDescription = htsDescription;
	}
	public Integer getNumberOfLines() {
		return numberOfLines;
	}
	public void setNumberOfLines(Integer numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	
	public String getBillOfLading() {
		return billOfLading;
	}
	public void setBillOfLading(String billOfLading) {
		this.billOfLading = billOfLading;
	}
	
	public String getInBondNumber() {
		return inBondNumber;
	}
	public void setInBondNumber(String inBondNumber) {
		this.inBondNumber = inBondNumber;
	}
	
	public String getContainerNumber() {
		return containerNumber;
	}
	public void setContainerNumber(String containerNumber) {
		this.containerNumber = containerNumber;
	}
	
	public String getArrivalDateEstimate() {
		return arrivalDateEstimate;
	}
	public void setArrivalDateEstimate(String arrivalDateEstimate) {
		this.arrivalDateEstimate = arrivalDateEstimate;
	}
	public String getArrivalDateActual() {
		return arrivalDateActual;
	}
	public void setArrivalDateActual(String arrivalDateActual) {
		this.arrivalDateActual = arrivalDateActual;
	}
	public String getLocationOfGoods() {
		return locationOfGoods;
	}
	public void setLocationOfGoods(String locationOfGoods) {
		this.locationOfGoods = locationOfGoods;
	}
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	public String getMot() {
		return mot;
	}
	public void setMot(String mot) {
		this.mot = mot;
	}
	public String getVessel() {
		return vessel;
	}
	public void setVessel(String vessel) {
		this.vessel = vessel;
	}
	public String getForeignPortName() {
		return foreignPortName;
	}
	public void setForeignPortName(String foreignPortName) {
		this.foreignPortName = foreignPortName;
	}
	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public String getUnladingPortCode() {
		return unladingPortCode;
	}
	public void setUnladingPortCode(String unladingPortCode) {
		this.unladingPortCode = unladingPortCode;
	}
	public String getUnladingPortName() {
		return unladingPortName;
	}
	public void setUnladingPortName(String unladingPortName) {
		this.unladingPortName = unladingPortName;
	}
	public String getFtz() {
		return ftz;
	}
	public void setFtz(String ftz) {
		this.ftz = ftz;
	}
	public String getOgc() {
		return ogc;
	}
	public void setOgc(String ogc) {
		this.ogc = ogc;
	}
	public Long getImporterId() {
		return importerId;
	}
	public void setImporterId(Long importerId) {
		this.importerId = importerId;
	}
	public Long getFilerId() {
		return filerId;
	}
	public void setFilerId(Long filerId) {
		this.filerId = filerId;
	}
	public Long getConsigneeId() {
		return consigneeId;
	}
	public void setConsigneeId(Long consigneeId) {
		this.consigneeId = consigneeId;
	}
	public Long getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}


}
