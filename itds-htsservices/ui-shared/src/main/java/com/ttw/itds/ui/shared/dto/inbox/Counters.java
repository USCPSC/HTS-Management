package com.ttw.itds.ui.shared.dto.inbox;

import java.io.Serializable;

import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;
/**
 * Counters DTO for Inbox UI
 * 
 * @author hzhao
 *
 */
public class Counters extends ItdsUiBaseDto {

	private static final long serialVersionUID = 6110791399204745438L;
	private Integer redCount;
	private Integer newSinceLastLogout;
	/**
	 * @return the redCount
	 */
	public Integer getRedCount() {
		return redCount;
	}
	/**
	 * @param redCount the redCount to set
	 */
	public void setRedCount(Integer redCount) {
		this.redCount = redCount;
	}
	/**
	 * @return the newSinceLastLogout
	 */
	public Integer getNewSinceLastLogout() {
		return newSinceLastLogout;
	}
	/**
	 * @param newSinceLastLogout the newSinceLastLogout to set
	 */
	public void setNewSinceLastLogout(Integer newSinceLastLogout) {
		this.newSinceLastLogout = newSinceLastLogout;
	}
	

}
