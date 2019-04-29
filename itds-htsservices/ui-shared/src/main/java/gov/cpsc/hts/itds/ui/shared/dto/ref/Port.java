/**
 * 
 */
package gov.cpsc.hts.itds.ui.shared.dto.ref;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 * @since 1.0.5
 */
public class Port extends ItdsUiBaseDto {

	private static final long serialVersionUID = 8118380774171743704L;

	private String portCode;
	private String portName;
	private String state;

	private String createUserId;
	private String createTimestamp;
	private String lastUpdateUserId;
	private String lastUpdateTimestamp;
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the portCode
	 */
	public String getPortCode() {
		return portCode;
	}
	/**
	 * @param portCode the portCode to set
	 */
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}
	/**
	 * @param portName the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * @return the createTimestamp
	 */
	public String getCreateTimestamp() {
		return createTimestamp;
	}
	/**
	 * @param createTimestamp the createTimestamp to set
	 */
	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	/**
	 * @return the lastUpdateUserId
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}
	/**
	 * @param lastUpdateUserId the lastUpdateUserId to set
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	/**
	 * @return the lastUpdateTimestamp
	 */
	public String getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	/**
	 * @param lastUpdateTimestamp the lastUpdateTimestamp to set
	 */
	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

}
