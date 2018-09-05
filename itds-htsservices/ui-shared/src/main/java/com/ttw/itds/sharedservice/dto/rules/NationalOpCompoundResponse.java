package com.ttw.itds.sharedservice.dto.rules;


import java.util.List;

/**
 * @author hzhao
 */
public class NationalOpCompoundResponse {

	public NationalOpCompoundResponse() {
	}
	
	/**
	 * ZTANG: Used to track the conversation between caller and callee
	 * Whatever caller app send to Ram server, it will be passed back as it is
	 */
	private String transactionId;
	
	/**
	 * Request timestamp in UTC format
	 */
	private String requestTime;	

	/**
	 * Response timestamp in UTC format
	 */
	private String responseTime;
	
	/*
	 * Highest priority national Operation 
	 */
	private NationalOp nationalOp = null;
	

	/*
	 * List of all matched national operations
	 */
	private List<ComputedNationalOpItem> computedItemList;


	public String getTransactionId() {
		return transactionId;
	}


	public String getRequestTime() {
		return requestTime;
	}


	public String getResponseTime() {
		return responseTime;
	}


	public NationalOp getNationalOp() {
		return nationalOp;
	}


	public List<ComputedNationalOpItem> getComputedItemList() {
		return computedItemList;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}


	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}


	public void setNationalOp(NationalOp nationalOp) {
		this.nationalOp = nationalOp;
	}


	public void setComputedItemList(List<ComputedNationalOpItem> computedItemList) {
		this.computedItemList = computedItemList;
	}


}
