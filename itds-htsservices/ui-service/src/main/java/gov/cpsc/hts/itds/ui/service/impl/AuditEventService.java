package gov.cpsc.hts.itds.ui.service.impl;

import gov.cpsc.hts.itds.ui.domain.entity.AuditEvent;
import gov.cpsc.hts.itds.ui.domain.repository.AuditItdsEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import java.util.List;

@Service
public class AuditEventService {
    private final Logger logger = LoggerFactory.getLogger(AuditEventService.class);

    @Autowired
    private AuditItdsEventRepository auditEventRepository;

    public ServiceResponse<List<AuditEvent>> getAllAuditEventsByDateRange(DateTime fromDate, DateTime toDate) {
        ServiceResponse<List<AuditEvent>> response = new ServiceResponse<>();
        try {
            List<AuditEvent> auditEvents =
                    auditEventRepository.findAllByEventDateBetween(fromDate, toDate);
            response.setValue(auditEvents);
        } catch (Exception e) {
            logger.error("error getting audit event list", e);
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
            logger.error("error saving audit event", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
