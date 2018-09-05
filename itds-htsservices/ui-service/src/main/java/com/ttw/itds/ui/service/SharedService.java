package com.ttw.itds.ui.service;

import com.ttw.itds.sharedservice.dto.SysEvent;
import com.ttw.itds.sharedservice.dto.audit.AuditRecord;
import com.ttw.itds.ui.domain.entity.SysEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SharedService {
	
	public void addSysEvent(SysEvent dto);
	
	public void addAuditRecord(AuditRecord dto);

	public Page<SysEventEntity> getSysEvents(Pageable pageable);

	public void runSysEvent(String event);
	
}
