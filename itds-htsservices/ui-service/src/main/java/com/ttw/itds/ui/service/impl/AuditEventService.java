package com.ttw.itds.ui.service.impl;

import com.ttw.itds.ui.domain.entity.AuditEvent;
import com.ttw.itds.ui.domain.repository.AuditEventRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import java.util.List;

@Service
public class AuditEventService {
    private final Logger log = Logger.getLogger(AuditEventService.class);

    @Inject
    private AuditEventRepository auditEventRepository;

    public ServiceResponse<List<AuditEvent>> getAllAuditEventsByDateRange(DateTime fromDate, DateTime toDate) {
        ServiceResponse<List<AuditEvent>> response = new ServiceResponse<>();
        try {
            List<AuditEvent> auditEvents =
                    auditEventRepository.findAllByEventDateBetween(fromDate, toDate);
            response.setValue(auditEvents);
        } catch (Exception e) {
            log.error("error getting audit event list", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    @Transactional
    public ServiceResponse<AuditEvent> saveAuditEvent(AuditEvent auditEvent) {
        ServiceResponse<AuditEvent> response = new ServiceResponse<>();
        try {
            auditEvent = auditEventRepository.saveAndFlush(auditEvent);
            response.setValue(auditEvent);
        } catch (Exception e) {
            log.error("error saving audit event", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
