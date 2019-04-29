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
@Table(name = "AUDIT_RECORD")
public class AuditRecordEntity extends BaseItdsEntity {

	private static final long serialVersionUID = -2682210930495806627L;
	public AuditRecordEntity() {	}

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

	@Column(name = "Event_Action")
	private String eventAction;
	public String getEventAction() {	return eventAction;	}
	public void setEventAction(String eventAction) { this.eventAction = eventAction;	}
	
	
	@Column(name = "Event_TimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventTimeStamp;
	public Date getEventTimeStamp() {	return eventTimeStamp;	}
	public void setEventTimeStamp(Date eventTimeStamp) {
		this.eventTimeStamp = eventTimeStamp;
	}
	
	@Column(name = "Network_Access_Point")
	private String networkAccessPoint;
	public String getNetworkAccessPoint() {	return networkAccessPoint;	}
	public void setNetworkAccessPoint(String networkAccessPoint) {	this.networkAccessPoint = networkAccessPoint;	}
	
	@Column(name = "Network_Access_Address")
	private String networkAccessAddress;
	public String getNetworkAccessAddress() {	return networkAccessAddress;	}
	public void setNetworkAccessAddress(String networkAccessAddress) {
		this.networkAccessAddress = networkAccessAddress;
	}
	
	@Column(name = "Query_ContextPath")
	private String queryContextPath;
	public String getQueryContextPath() {return queryContextPath;	}
	public void setQueryContextPath(String queryContextPath) {
		this.queryContextPath = queryContextPath;
	}
	
	@Column(name = "Query_Parameter")
	private String queryParameter;
	public String getQueryParameter() {	return queryParameter;	}
	public void setQueryParameter(String queryParameter) {	this.queryParameter = queryParameter;	}
}
