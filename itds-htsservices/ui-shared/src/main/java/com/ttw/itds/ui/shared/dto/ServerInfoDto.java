package com.ttw.itds.ui.shared.dto;

public class ServerInfoDto {

	private String serverName;	
	private String serverVersion;
	private String minutesBeforeSessionTimeoutWarning;
	private String minutesAfterSessionTimeoutWarning;
	private String ifsViewSampleUrl;
	private String ifsCreateSampleUrl;
	private String ruleEngineUiUrl;
	private String ramApiUrlPrefix;
	private String ramApiUrlListNationalOpsPartial;
	
	public ServerInfoDto() {
		super();
	}
	public String getServerName() {	return serverName;	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerVersion() {	return serverVersion;	}
	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public String getMinutesBeforeSessionTimeoutWarning() { return minutesBeforeSessionTimeoutWarning; }
	public void setMinutesBeforeSessionTimeoutWarning(String minutesBeforeSessionTimeoutWarning) { this.minutesBeforeSessionTimeoutWarning = minutesBeforeSessionTimeoutWarning; }
	public String getMinutesAfterSessionTimeoutWarning() { return minutesAfterSessionTimeoutWarning; }
	public void setMinutesAfterSessionTimeoutWarning(String minutesAfterSessionTimeoutWarning) { this.minutesAfterSessionTimeoutWarning = minutesAfterSessionTimeoutWarning; }


	/**
	 * @return the ifsViewSampleUrl
	 */
	public String getIfsViewSampleUrl() {
		return ifsViewSampleUrl;
	}
	/**
	 * @param ifsViewSampleUrl the ifsViewSampleUrl to set
	 */
	public void setIfsViewSampleUrl(String ifsViewSampleUrl) {
		this.ifsViewSampleUrl = ifsViewSampleUrl;
	}
	/**
	 * @return the ifsCreateSampleUrl
	 */
	public String getIfsCreateSampleUrl() {
		return ifsCreateSampleUrl;
	}
	/**
	 * @param ifsCreateSampleUrl the ifsCreateSampleUrl to set
	 */
	public void setIfsCreateSampleUrl(String ifsCreateSampleUrl) {
		this.ifsCreateSampleUrl = ifsCreateSampleUrl;
	}
	/**
	 * @return the ruleEngineUiUrl
	 */
	public String getRuleEngineUiUrl() {
		return ruleEngineUiUrl;
	}
	/**
	 * @param ruleEngineUiUrl the ruleEngineUiUrl to set
	 */
	public void setRuleEngineUiUrl(String ruleEngineUiUrl) {
		this.ruleEngineUiUrl = ruleEngineUiUrl;
	}
	public String getRamApiUrlPrefix() {
		return ramApiUrlPrefix;
	}
	public String getRamApiUrlListNationalOpsPartial() {
		return ramApiUrlListNationalOpsPartial;
	}
	public void setRamApiUrlPrefix(String ramApiUrlPrefix) {
		this.ramApiUrlPrefix = ramApiUrlPrefix;
	}
	public void setRamApiUrlListNationalOpsPartial(String ramApiUrlListNationalOpsPartial) {
		this.ramApiUrlListNationalOpsPartial = ramApiUrlListNationalOpsPartial;
	}

}
