package gov.cpsc.hts.itds.ui.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SYS_EVENT")
public class SysEventEntity extends BaseItdsEntity{

	private static final long serialVersionUID = 2962193508310586393L;
	public SysEventEntity() {
	}
	
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	public Long getId() {	return id;	}
	public void setId(Long id) {	this.id = id;	}

	@Column(name = "User_Identifier")
	private String userIdentifier;
	public String getUserIdentifier() {	return userIdentifier;	}
	public void setUserIdentifier(String userIdentifier) {	this.userIdentifier = userIdentifier;	}
	
	@Column(name = "App_Name")
	private String appName;
	public String getAppName() {return appName;	}
	public void setAppName(String appName) {	this.appName = appName;	}
	
	@Column(name = "App_Version")
	private String appVersion;
	public String getAppVersion() {	return appVersion;	}
	public void setAppVersion(String appVersion) {	this.appVersion = appVersion;	}

	@Column(name = "Action_Name")
	private String action;
	public String getAction() {	return action;	}
	public void setAction(String action) {	this.action = action;	}

	@Column(name = "Event_Name")
	private String eventName;
	public String getEventName() {	return eventName;	}
	public void setEventName(String eventName) {  this.eventName = eventName;	}

	@Column(name = "Event_Description")
	private String eventDescription;
	public String getEventDescription() {	return eventDescription;	}
	public void setEventDescription(String eventDescription) {	this.eventDescription = eventDescription;	}

	@Column(name = "Event_TimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventTimeStamp;
	public Date getEventTimeStamp() {	return eventTimeStamp;	}
	public void setEventTimeStamp(Date eventTimeStamp) {
		this.eventTimeStamp = eventTimeStamp;
	}

}
