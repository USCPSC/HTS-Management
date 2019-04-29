package gov.cpsc.hts.itds.ui.service;

import gov.cpsc.hts.itds.sharedservice.dto.SysEvent;
import gov.cpsc.hts.itds.sharedservice.dto.audit.AuditRecord;
import gov.cpsc.hts.itds.ui.domain.entity.SysEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SharedService {
	
	public void addSysEvent(SysEvent dto);
	
	public void addAuditRecord(AuditRecord dto);

	public Page<SysEventEntity> getSysEvents(Pageable pageable);

	public void runSysEvent(String event);
	
}
