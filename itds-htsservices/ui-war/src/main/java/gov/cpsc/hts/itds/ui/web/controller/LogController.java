package gov.cpsc.hts.itds.ui.web.controller;

import gov.cpsc.hts.itds.ui.domain.entity.LogEntity;
import gov.cpsc.hts.itds.ui.service.impl.LogService;
import gov.cpsc.hts.itds.ui.service.impl.ServiceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/logs")
@Deprecated
public class LogController {

    private final Logger log = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    /**
     * POST /debug -> add a new debug log
     *
     * @param logEntity
     * @return
     */
    @RequestMapping(value = "/log",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addDebugLog(@RequestBody LogEntity logEntity) {
        log.debug("REST call to addDebugLog");
        ServiceResponse<String> response = logService.addLogService(logEntity);

        return response.getHttpResponseEntity();
    }
}
