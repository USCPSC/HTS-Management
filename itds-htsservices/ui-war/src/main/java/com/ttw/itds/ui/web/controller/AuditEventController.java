package com.ttw.itds.ui.web.controller;

import com.ttw.itds.ui.domain.entity.AuditEvent;
import com.ttw.itds.ui.service.impl.AuditEventService;
import com.ttw.itds.ui.service.impl.ServiceResponse;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/audit")
@Deprecated
public class AuditEventController {
    private final Logger log = Logger.getLogger(AuditEventController.class);

    @Inject
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
