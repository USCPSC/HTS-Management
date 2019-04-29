package gov.cpsc.hts.itds.sharedservice.dto.rules;

import java.util.List;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author zlt633
 * @generated at : Jun 5, 2016 - 11:26:34 PM
 * @since: 1.0.0
 */
public class RamCompoundResponse extends ItdsUiBaseDto {
	private static final long serialVersionUID = 2796662223836116924L;
	public RamCompoundResponse() {
	}
	
	/**
	 * ZTANG: Used to track the conversation between caller and callee
	 * Whatever caller app send to Ram server, it will be passed back as it is
	 */
	private String transactionId;
	public String getTransactionId() {	return transactionId;	}
	public void setTransactionId(String transactionId) {	this.transactionId = transactionId;	}
	
	/**
	 * ZTANG: Request timestamp in UTC format
	 */
	private String requestTime;	
	public String getRequestTime() {	return requestTime;	}
	public void setRequestTime(String requestTime) {	this.requestTime = requestTime;	}

	/**
	 * ZTANG: Response timestamp in UTC format
	 */
	private String responseTime;
	public String getResponseTime() {	return responseTime;	}
	public void setResponseTime(String responseTime) {	this.responseTime = responseTime;	}
	
	/**
	 * ZTANG: importerEin is optional for calculation, but
	 * could be required for trace importer's Risk score history
	 * it will be passed back as it is
	 */
	private String importerEin;
	public String getImporterEin() { return importerEin;	}
	public void setImporterEin(String importerEin) {	this.importerEin = importerEin;	}
	
	
	/**
	 * ZTANG: entryIdentifier is optional for calculation, but
	 * could be required for trace history of CBP entry, invoke from Entry
	 * it will be passed back as it is
	 */
	
	private String entryIdentifier;
	public String getEntryIdentifier() {	return entryIdentifier;	}
	public void setEntryIdentifier(String entryIdentifier) {this.entryIdentifier = entryIdentifier;	}
	
	
	/**
	 * ZTANG: htsCode is optional for calculation, but
	 * could be required for trace history of product with assigned Hts code
	 * it will be passed back as it is
	 */
	private String htsCode;
	public String getHtsCode() {	return htsCode;	}
	public void setHtsCode(String htsCode) { this.htsCode = htsCode;	}
	
	
	private Double totalScore = null;
	public Double getTotalScore() {	return totalScore; 	}
	public void setTotalScore(Double totalScore) {	this.totalScore = totalScore;	}
	
	
	private List<ScoredItem> scoredItemList;
	public List<ScoredItem> getScoredItemList() {	return scoredItemList;	}
	public void setScoredItemList(List<ScoredItem> scoredItemList) {
		this.scoredItemList = scoredItemList;
	}


}
