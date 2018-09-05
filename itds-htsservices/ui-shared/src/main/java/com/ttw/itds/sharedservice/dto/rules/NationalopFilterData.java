package com.ttw.itds.sharedservice.dto.rules;

import java.io.Serializable;

public class NationalopFilterData implements Serializable  {
	private static final long serialVersionUID = -2877132428721461011L;
	
	private String importerEIN;
	private String consigneeEIN;
	private String manufacturerEIN;
	private String htsCode;
	private String portCode;
	
	public String getImporterEIN() {
		return importerEIN;
	}
	public String getConsigneeEIN() {
		return consigneeEIN;
	}
	public String getManufacturerEIN() {
		return manufacturerEIN;
	}
	public String getHtsCode() {
		return htsCode;
	}
	public String getPortCode() {
		return portCode;
	}
	public void setImporterEIN(String importerEIN) {
		this.importerEIN = importerEIN;
	}
	public void setConsigneeEIN(String consigneeEIN) {
		this.consigneeEIN = consigneeEIN;
	}
	public void setManufacturerEIN(String manufacturerEIN) {
		this.manufacturerEIN = manufacturerEIN;
	}
	public void setHtsCode(String htsCode) {
		this.htsCode = htsCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
	
}
