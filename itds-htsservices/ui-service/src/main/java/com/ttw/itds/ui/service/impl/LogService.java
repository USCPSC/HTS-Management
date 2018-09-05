package com.ttw.itds.ui.service.impl;

import com.ttw.itds.ui.domain.entity.LogEntity;
import com.ttw.itds.ui.domain.repository.LogRepository;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final Logger log = Logger.getLogger(LogService.class);

    @Inject
    private LogRepository logRepository;
    /**
     * Uses log4j to add a custom log to the database
     * @param logEntity - the log object being inserted into the database
     * @return response - OK or INTERNAL_SERVER_ERROR
     */
    public ServiceResponse<String> addLogService(LogEntity logEntity) {
        ServiceResponse<String> response = new ServiceResponse<>();
        try {
            logRepository.saveAndFlush(logEntity);
        } catch (Exception e) {
            String errorMsg = "error adding custom log: " + e.getMessage();
            log.error("error adding custom log", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
            response.setValue(errorMsg);
        }
        return response;
    }
}
