/**
 * 
 */
package gov.cpsc.hts.itds.ui.shared.dto.user;

import java.util.List;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 *
 */
public class User extends ItdsUiBaseDto {

    private String username;
    private String lastLoginTimestamp;
    private String lastLogoutTimestamp; // logout or session timeout
    private String inboxRowsShown;
    private String email;
    private String exisLocation;
    private String exisSupervisor;
    private String firstName;
    private String lastName;
    private String orgCode;
    private List<String> portCodeList;
    private List<String> roleList;

    /**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the lastLoginTimestamp
	 */
	public String getLastLoginTimestamp() {
		return lastLoginTimestamp;
	}
	/**
	 * @param lastLoginTimestamp the lastLoginTimestamp to set
	 */
	public void setLastLoginTimestamp(String lastLoginTimestamp) {
		this.lastLoginTimestamp = lastLoginTimestamp;
	}
	/**
	 * @return the lastLogoutTimestamp
	 */
	public String getLastLogoutTimestamp() {
		return lastLogoutTimestamp;
	}
	/**
	 * @param lastLogoutTimestamp the lastLogoutTimestamp to set
	 */
	public void setLastLogoutTimestamp(String lastLogoutTimestamp) {
		this.lastLogoutTimestamp = lastLogoutTimestamp;
	}
	/**
	 * @return the inboxRowsShown
	 */
	public String getInboxRowsShown() {
		return inboxRowsShown;
	}
	/**
	 * @param inboxRowsShown the inboxRowsShown to set
	 */
	public void setInboxRowsShown(String inboxRowsShown) {
		this.inboxRowsShown = inboxRowsShown;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the exisLocation
	 */
	public String getExisLocation() {
		return exisLocation;
	}
	/**
	 * @param exisLocation the exisLocation to set
	 */
	public void setExisLocation(String exisLocation) {
		this.exisLocation = exisLocation;
	}
	/**
	 * @return the exisSupervisor
	 */
	public String getExisSupervisor() {
		return exisSupervisor;
	}
	/**
	 * @param exisSupervisor the exisSupervisor to set
	 */
	public void setExisSupervisor(String exisSupervisor) {
		this.exisSupervisor = exisSupervisor;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}
	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	/**
	 * @return the portCodeList
	 */
	public List<String> getPortCodeList() {
		return portCodeList;
	}
	/**
	 * @param portCodeList the portCodeList to set
	 */
	public void setPortCodeList(List<String> portCodeList) {
		this.portCodeList = portCodeList;
	}
	/**
	 * @return the roleList
	 */
	public List<String> getRoleList() {
		return roleList;
	}
	/**
	 * @param roleList the roleList to set
	 */
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}
    
    
	}