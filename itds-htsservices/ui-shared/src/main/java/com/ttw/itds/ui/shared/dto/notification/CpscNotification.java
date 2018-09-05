package com.ttw.itds.ui.shared.dto.notification;

import java.util.Date;


import com.ttw.itds.ui.shared.dto.ItdsUiBaseDto;

/**
 * @author hzhao
 * @since 1.0.40
 *
 */
public class CpscNotification extends ItdsUiBaseDto {

	private static final long serialVersionUID = -554458968653917245L;

	private Long id;
	private String note;
	private String effectiveDate;

	private String status;

	private String createUserId;
	private String createTimestamp;
	private String lastUpdateUserId;
	private String lastUpdateTimestamp;
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getId() {
		return id;
	}
	public String getNote() {
		return note;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public String getStatus() {
		return status;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getCreateTimestamp() {
		return createTimestamp;
	}
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}
	public String getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	
}
