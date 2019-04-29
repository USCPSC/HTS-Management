package gov.cpsc.hts.itds.ui.service.impl;

import gov.cpsc.hts.itds.ui.domain.entity.LogEntity;
import gov.cpsc.hts.itds.ui.domain.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    @Autowired
    private LogRepository logRepository;
    /**
     * Uses slf4j to add a custom log to the database
     * @param logEntity - the log object being inserted into the database
     * @return response - OK or INTERNAL_SERVER_ERROR
     */
    public ServiceResponse<String> addLogService(LogEntity logEntity) {
        ServiceResponse<String> response = new ServiceResponse<>();
        try {
            logRepository.saveAndFlush(logEntity);
        } catch (Exception e) {
            String errorMsg = "error adding custom log: " + e.getMessage();
            logger.error("error adding custom log", e);
            response.setResponseCode(ServiceResponseCodeEnum.INTERNAL_SERVER_ERROR);
            response.setValue(errorMsg);
        }
        return response;
    }
}
