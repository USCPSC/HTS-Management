package gov.cpsc.hts.itds.ui.shared.dto.inbox;

import java.io.Serializable;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * EntrSummary DTO for Inbox UI, working progress
 * 
 * @author cpan
 *
 */
public class EntrySummary extends ItdsUiBaseDto {
	private static final long serialVersionUID = -7817992483277081604L;
	
	// entry
	private long entryId; // internal no UI
	private String entryDate;
	private String entryNumber;
	private int lineNumber;
	private double entryValue;
	private int numberOfLines;
	
	private String bol; // Bill Of Lading
	// private String airwayBill; // future air cargo carriers
	
	private String arrivalDateEstimate;
	private String arrivalDateActual;
	
	// seems one string that has a number first (location code), then
	// company name, postal addr, and then a word "pier" at the final line
	private String locationOfGoods;
	
	// carrier
	private long carrierId; // internal no UI
	private String carrierName;
	private String mot; // Mode of transport: land, air, water
	private String vessel;
	
	// port
	private long foreignPortId; // internal no UI
	private String foreignPortCode; 
	private String foreignPortName;
	
	private long portId; // internal no UI
	private String portCode;
	private String portName;
	
	private long unladingPortId; // internal no UI
	private String unladingPortCode;
	private String unladingPortName;

	private String ftz; // Foreign Trade Zone
	private String ogc; // Office of General Counsel

	private long importerId; // internal no UI
	private long filerId; // internal no UI
	
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
	 * @return the entryDate
	 */
	public String getEntryDate() {
		return entryDate;
	}
	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
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
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the entryValue
	 */
	public double getEntryValue() {
		return entryValue;
	}
	/**
	 * @param entryValue the entryValue to set
	 */
	public void setEntryValue(double entryValue) {
		this.entryValue = entryValue;
	}
	/**
	 * @return the numberOfLines
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}
	/**
	 * @param numberOfLines the numberOfLines to set
	 */
	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	/**
	 * @return the bol
	 */
	public String getBol() {
		return bol;
	}
	/**
	 * @param bol the bol to set
	 */
	public void setBol(String bol) {
		this.bol = bol;
	}
	/**
	 * @return the arrivalDateEstimate
	 */
	public String getArrivalDateEstimate() {
		return arrivalDateEstimate;
	}
	/**
	 * @param arrivalDateEstimate the arrivalDateEstimate to set
	 */
	public void setArrivalDateEstimate(String arrivalDateEstimate) {
		this.arrivalDateEstimate = arrivalDateEstimate;
	}
	/**
	 * @return the arrivalDateActual
	 */
	public String getArrivalDateActual() {
		return arrivalDateActual;
	}
	/**
	 * @param arrivalDateActual the arrivalDateActual to set
	 */
	public void setArrivalDateActual(String arrivalDateActual) {
		this.arrivalDateActual = arrivalDateActual;
	}
	/**
	 * @return the locationOfGoods
	 */
	public String getLocationOfGoods() {
		return locationOfGoods;
	}
	/**
	 * @param locationOfGoods the locationOfGoods to set
	 */
	public void setLocationOfGoods(String locationOfGoods) {
		this.locationOfGoods = locationOfGoods;
	}
	/**
	 * @return the carrierId
	 */
	public long getCarrierId() {
		return carrierId;
	}
	/**
	 * @param carrierId the carrierId to set
	 */
	public void setCarrierId(long carrierId) {
		this.carrierId = carrierId;
	}
	/**
	 * @return the carrierName
	 */
	public String getCarrierName() {
		return carrierName;
	}
	/**
	 * @param carrierName the carrierName to set
	 */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	/**
	 * @return the mot
	 */
	public String getMot() {
		return mot;
	}
	/**
	 * @param mot the mot to set
	 */
	public void setMot(String mot) {
		this.mot = mot;
	}
	/**
	 * @return the vessel
	 */
	public String getVessel() {
		return vessel;
	}
	/**
	 * @param vessel the vessel to set
	 */
	public void setVessel(String vessel) {
		this.vessel = vessel;
	}
	/**
	 * @return the foreignPortId
	 */
	public long getForeignPortId() {
		return foreignPortId;
	}
	/**
	 * @param foreignPortId the foreignPortId to set
	 */
	public void setForeignPortId(long foreignPortId) {
		this.foreignPortId = foreignPortId;
	}
	/**
	 * @return the foreignPortCode
	 */
	public String getForeignPortCode() {
		return foreignPortCode;
	}
	/**
	 * @param foreignPortCode the foreignPortCode to set
	 */
	public void setForeignPortCode(String foreignPortCode) {
		this.foreignPortCode = foreignPortCode;
	}
	/**
	 * @return the foreignPortName
	 */
	public String getForeignPortName() {
		return foreignPortName;
	}
	/**
	 * @param foreignPortName the foreignPortName to set
	 */
	public void setForeignPortName(String foreignPortName) {
		this.foreignPortName = foreignPortName;
	}
	/**
	 * @return the portId
	 */
	public long getPortId() {
		return portId;
	}
	/**
	 * @param portId the portId to set
	 */
	public void setPortId(long portId) {
		this.portId = portId;
	}
	/**
	 * @return the portCode
	 */
	public String getPortCode() {
		return portCode;
	}
	/**
	 * @param portCode the portCode to set
	 */
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}
	/**
	 * @param portName the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}
	/**
	 * @return the unladingPortId
	 */
	public long getUnladingPortId() {
		return unladingPortId;
	}
	/**
	 * @param unladingPortId the unladingPortId to set
	 */
	public void setUnladingPortId(long unladingPortId) {
		this.unladingPortId = unladingPortId;
	}
	/**
	 * @return the unladingPortCode
	 */
	public String getUnladingPortCode() {
		return unladingPortCode;
	}
	/**
	 * @param unladingPortCode the unladingPortCode to set
	 */
	public void setUnladingPortCode(String unladingPortCode) {
		this.unladingPortCode = unladingPortCode;
	}
	/**
	 * @return the unladingPortName
	 */
	public String getUnladingPortName() {
		return unladingPortName;
	}
	/**
	 * @param unladingPortName the unladingPortName to set
	 */
	public void setUnladingPortName(String unladingPortName) {
		this.unladingPortName = unladingPortName;
	}
	/**
	 * @return the ftz
	 */
	public String getFtz() {
		return ftz;
	}
	/**
	 * @param ftz the ftz to set
	 */
	public void setFtz(String ftz) {
		this.ftz = ftz;
	}
	/**
	 * @return the ogc
	 */
	public String getOgc() {
		return ogc;
	}
	/**
	 * @param ogc the ogc to set
	 */
	public void setOgc(String ogc) {
		this.ogc = ogc;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
