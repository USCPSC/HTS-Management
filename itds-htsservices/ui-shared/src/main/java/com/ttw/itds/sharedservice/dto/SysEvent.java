package com.ttw.itds.sharedservice.dto;

import java.io.Serializable;

/**
 * @author zlt633
 * @generated at : Apr 16, 2016 - 8:51:10 PM
 * @since: 1.0.0
 */
public class SysEvent implements Serializable{

	private static final long serialVersionUID = 2703036055408886287L;
	
	public SysEvent() {	}
	
	public SysEvent(String userIdentifier) {
		super();
		this.userIdentifier = userIdentifier;
	}
	
	public SysEvent(String appName, String appVersion) {
		super();
		this.appName = appName;
		this.appVersion = appVersion;
	}
	
	public SysEvent(String userIdentifier, String appName, String appVersion) {
		super();
		this.userIdentifier = userIdentifier;
		this.appName = appName;
		this.appVersion = appVersion;
	}


	private String userIdentifier;
	public String getUserIdentifier() {	return userIdentifier;	}
	public void setUserIdentifier(String userIdentifier) {	this.userIdentifier = userIdentifier;	}
	
	private String appName;
	public String getAppName() {return appName;	}
	public void setAppName(String appName) {	this.appName = appName;	}
	
	private String appVersion;
	public String getAppVersion() {	return appVersion;	}
	public void setAppVersion(String appVersion) {	this.appVersion = appVersion;	}

	private String action;
	public String getAction() {	return action;	}
	public void setAction(String action) {	this.action = action;	}

	private String eventName;
	public String getEventName() {	return eventName;	}
	public void setEventName(String eventName) {  this.eventName = eventName;	}

	private String eventDescription;
	public String getEventDescription() {	return eventDescription;	}
	public void setEventDescription(String eventDescription) {	this.eventDescription = eventDescription;	}

	private String eventTimeStamp;
	public String getEventTimeStamp() {	return eventTimeStamp;	}
	public void setEventTimeStamp(String eventTimeStamp) {	this.eventTimeStamp = eventTimeStamp;	}




}
