/**
 * @author hzhao
 * @since 1.0.5
 *
 */
package gov.cpsc.hts.itds.ui.shared.dto.ref;

import gov.cpsc.hts.itds.ui.shared.dto.ItdsUiBaseDto;

public class Importer extends ItdsUiBaseDto {

	private static final long serialVersionUID = -1373621769175526058L;
	private String importerNumber;
	private String importerName;
	private String pocName;
	private String pocPhone;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zipCode;

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
	 * @return the importerNumber
	 */
	public String getImporterNumber() {
		return importerNumber;
	}

	/**
	 * @param importerNumber
	 *            the importerNumber to set
	 */
	public void setImporterNumber(String importerNumber) {
		this.importerNumber = importerNumber;
	}

	/**
	 * @return the importerName
	 */
	public String getImporterName() {
		return importerName;
	}

	/**
	 * @param importerName
	 *            the importerName to set
	 */
	public void setImporterName(String importerName) {
		this.importerName = importerName;
	}

	/**
	 * @return the pocName
	 */
	public String getPocName() {
		return pocName;
	}

	/**
	 * @param pocName
	 *            the pocName to set
	 */
	public void setPocName(String pocName) {
		this.pocName = pocName;
	}

	/**
	 * @return the pocPhone
	 */
	public String getPocPhone() {
		return pocPhone;
	}

	/**
	 * @param pocPhone
	 *            the pocPhone to set
	 */
	public void setPocPhone(String pocPhone) {
		this.pocPhone = pocPhone;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @param addressLine1
	 *            the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @param addressLine2
	 *            the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId
	 *            the createUserId to set
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
	 * @param createTimestamp
	 *            the createTimestamp to set
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
	 * @param lastUpdateUserId
	 *            the lastUpdateUserId to set
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
	 * @param lastUpdateTimestamp
	 *            the lastUpdateTimestamp to set
	 */
	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

}
