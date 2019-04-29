package gov.cpsc.hts.itds.ui.web.controller;

import gov.cpsc.hts.itds.ui.domain.entity.AuditEvent;
import gov.cpsc.hts.itds.ui.service.impl.AuditEventService;
import gov.cpsc.hts.itds.ui.service.impl.ServiceResponse;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/audit")
@Deprecated
public class AuditEventController {
    private final Logger log = LoggerFactory.getLogger(AuditEventController.class);

    @Autowired
    private AuditEventService auditEventService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AuditEvent>> getAuditEventsByDate(
            @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime fromDate,
            @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime toDate) {
        log.debug("REST call to getAuditEventsByDate");
        ServiceResponse<List<AuditEvent>> response = auditEventService.getAllAuditEventsByDateRange(fromDate,toDate);

        return response.getHttpResponseEntity();
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE,  MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AuditEvent> saveAuditEvent(@RequestBody AuditEvent auditEvent) {
        log.debug("REST call to saveAuditEvent");
        ServiceResponse<AuditEvent> response = auditEventService.saveAuditEvent(auditEvent);

        return response.getHttpResponseEntity();
    }
}
