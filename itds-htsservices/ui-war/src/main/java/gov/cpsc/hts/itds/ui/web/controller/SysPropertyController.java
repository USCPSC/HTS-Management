package gov.cpsc.hts.itds.ui.web.controller;

import gov.cpsc.hts.itds.ui.domain.entity.SysPropertyEntity;
import gov.cpsc.hts.itds.ui.service.impl.ServiceResponse;
import gov.cpsc.hts.itds.ui.service.impl.SysPropertyService;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Restful Resource class which holds the endpoints for rest, unpacks the call, calls the Service class, and responds to the http request.
 */
@SpringBootApplication
@RestController
@RolesAllowed("ROLE_SYS_PROP_SAVE")
public class SysPropertyController {

    private final Logger log = LoggerFactory.getLogger(SysPropertyController.class);

    @Autowired
    private SysPropertyService sysPropertyService;

    @RequestMapping(value = "/sysprops",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SysPropertyEntity>> getSysPropertyList() {
        log.debug("REST call to getSysPropertyList");
        ServiceResponse<List<SysPropertyEntity>> response = sysPropertyService.getSysPropertyList();
        return response.getHttpResponseEntity();
    }

    /**
     * @param propname
     * @return
     */
    @RequestMapping(value = "/sysprops/{propname}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SysPropertyEntity> getSysProperty(@PathVariable String propname) {
        log.debug("REST call to getOneSysProperty");
        ServiceResponse<SysPropertyEntity> response = sysPropertyService.getSysPropertyByName(propname);

        return response.getHttpResponseEntity();
    }

    /**
     * @param sysProperty
     * @return
     */
    @RequestMapping(value = "/sysprops",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SysPropertyEntity> saveSysProperty(@RequestBody SysPropertyEntity sysProperty) {
        log.debug("REST call to save");
        ServiceResponse<SysPropertyEntity> response = sysPropertyService.saveSysProperty(sysProperty);

        return response.getHttpResponseEntity();
    }

    /**
     * @param sysPropertyname
     * @return
     */
    @RequestMapping(value = "/sysprops/{propname}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SysPropertyEntity> deleteSysProperty(@PathVariable String sysPropertyname) {
        log.debug("REST call to delete");
        ServiceResponse response = sysPropertyService.deleteSysProperty(sysPropertyname);

        return response.getHttpResponseEntity();
    }
    
}
