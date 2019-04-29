package gov.cpsc.hts.itds.sharedservice.dto.audit;

import java.io.Serializable;
import java.util.Date;

import gov.cpsc.hts.itds.sharedservice.dto.audit.AuditEnums.EventActionEnum;
import gov.cpsc.hts.itds.sharedservice.dto.audit.AuditEnums.NetworkAccessPointEnum;
import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author zlt633
 * @generated at : Dec 30, 2015 - 9:54:42 PM
 * @since: 1.0.0
 */
public class AuditRecord implements Serializable{

	private static final long serialVersionUID = 6492770209471042662L;
	
	public AuditRecord() {
		super();
	}
	
	
	/**
	 * ZTANG:
	 * Here userId means either one of the followings
	 * 1. login user's user name, a person's identifier. for example jsmaith
	 * 2. access credential of the caller application if it is system-to-system integration, 
	 * 		for example: goldenstar-app-from-BUZZ-com
	 */
	private String userId;
	public String getUserId() {	return userId;	}
	public void setUserId(String userId) {	this.userId = userId;	}

	/**
	 * ZTANG: formated Date object, see test case for detail
	 */
	private String eventTimeStamp;
	public String getEventTimeStamp() {	return eventTimeStamp;	}
	public void setEventTimeStamp(String eventTimeStamp) {  this.eventTimeStamp = eventTimeStamp;	}
	
	private EventActionEnum eventAction;
	public EventActionEnum getEventAction() {	return eventAction;	}
	public void setEventAction(EventActionEnum eventAction) {
		this.eventAction = eventAction;
	}
	
	
	/**
	 * ZTANG: generator is the application which generate this audit record
	 * Refer to itds-ui-appinfo.properties's 
	 * server_name=ITDS-UI-Web
	 * server_version=1.0.0
	 * In this case: appName is ITDS-UI-Web; and appVersion is: 1.0.0
	 */
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
	
	/**
	 * ZTANG: for network access: common way is capture IP address
	 * For example: 
	 * networkAccessPointEnum = IP_ADDRESS
	 */
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
	
	
	/**
	 * ZTANG: a full URI is http(s)://www.google.com/user/createUser
	 * contextPath : "user/createUser"
	 */
	private String queryContextPath;
	public String getQueryContextPath() {return queryContextPath;	}
	public void setQueryContextPath(String queryContextPath) {
		this.queryContextPath = queryContextPath;
	}
	
	/**
	 * ZTANG:
	 * sample : "key1=foo;key2=bazz"
	 */
	private String queryParameter;
	public String getQueryParameter() {	return queryParameter;	}
	public void setQueryParameter(String queryParameter) {
		this.queryParameter = queryParameter;
	}

}
