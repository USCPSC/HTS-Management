package com.ttw.itds.sharedservice.dto.audit;

import java.io.Serializable;
import java.util.Date;

import com.ttw.itds.sharedservice.dto.audit.AuditEnums.EventActionEnum;
import com.ttw.itds.sharedservice.dto.audit.AuditEnums.NetworkAccessPointEnum;
import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

public class AuditRecord implements Serializable{

	private static final long serialVersionUID = 6492770209471042662L;
	
	public AuditRecord() {
		super();
	}
	
	private String userId;
	public String getUserId() {	return userId;	}
	public void setUserId(String userId) {	this.userId = userId;	}

	private String eventTimeStamp;
	public String getEventTimeStamp() {	return eventTimeStamp;	}
	public void setEventTimeStamp(String eventTimeStamp) {  this.eventTimeStamp = eventTimeStamp;	}
	
	private EventActionEnum eventAction;
	public EventActionEnum getEventAction() {	return eventAction;	}
	public void setEventAction(EventActionEnum eventAction) {
		this.eventAction = eventAction;
	}
	
	
	private String generatorAppName;
	public String getGeneratorAppName() {	return generatorAppName;	}
	public void setGeneratorAppName(String generatorAppName) {
		this.generatorAppName = generatorAppName;
	}
	
	private String generatorAppVersion;
	public String getGeneratorAppVersion() {  return generatorAppVersion;	}
	public void setGeneratorAppVersion(String generatorAppVersion) {
		this.generatorAppVersion = generatorAppVersion;
	}
	
	private NetworkAccessPointEnum networkAccessPointEnum;
	public NetworkAccessPointEnum getNetworkAccessPointEnum() {	return networkAccessPointEnum;	}
	public void setNetworkAccessPointEnum(NetworkAccessPointEnum networkAccessPointEnum) {
		this.networkAccessPointEnum = networkAccessPointEnum;
	}

	private String networkAccessAddress;
	public String getNetworkAccessAddress() {	return networkAccessAddress;	}
	public void setNetworkAccessAddress(String networkAccessAddress) {
		this.networkAccessAddress = networkAccessAddress;
	}
	
	
	private String queryContextPath;
	public String getQueryContextPath() {return queryContextPath;	}
	public void setQueryContextPath(String queryContextPath) {
		this.queryContextPath = queryContextPath;
	}
	
	private String queryParameter;
	public String getQueryParameter() {	return queryParameter;	}
	public void setQueryParameter(String queryParameter) {
		this.queryParameter = queryParameter;
	}

}
