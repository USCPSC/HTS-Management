package com.ttw.itds.sharedservice.dto.rules;



import java.io.Serializable;

/**
 * @author hzhao
 *
 */
public class AtomicOperation implements Serializable {

	private static final long serialVersionUID = 6278284861361696745L;
	
	// nationalOp data
	private String nationalOpCode;
	
	// entryline data
	private String importerEin;
	private String consigeeEin;
	private String manufactureId;
	private String htsCode;
	private String portCode;
	private Double value;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getNationalOpCode() {
		return nationalOpCode;
	}
	public void setNationalOpCode(String nationalOpCode) {
		this.nationalOpCode = nationalOpCode;
	}
	public String getImporterEin() {
		return importerEin;
	}
	public String getConsigeeEin() {
		return consigeeEin;
	}
	public String getManufactureId() {
		return manufactureId;
	}
	public String getHtsCode() {
		return htsCode;
	}
	public Double getValue() {
		return value;
	}
	public void setImporterEin(String importerEin) {
		this.importerEin = importerEin;
	}
	public void setConsigeeEin(String consigeeEin) {
		this.consigeeEin = consigeeEin;
	}
	public void setManufactureId(String manufactureId) {
		this.manufactureId = manufactureId;
	}
	public void setHtsCode(String htsCode) {
		this.htsCode = htsCode;
	}
	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	

}
