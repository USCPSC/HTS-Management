package com.ttw.itds.ui.web.controller;

import com.ttw.itds.ui.domain.entity.LogEntity;
import com.ttw.itds.ui.service.impl.LogService;
import com.ttw.itds.ui.service.impl.ServiceResponse;
import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

@RestController
@RequestMapping("/logs")
@Deprecated
public class LogController {

    private final Logger log = Logger.getLogger(LogController.class);

    @Inject
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
