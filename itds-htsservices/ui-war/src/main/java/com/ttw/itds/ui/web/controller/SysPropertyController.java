package com.ttw.itds.ui.web.controller;

import com.ttw.itds.ui.domain.entity.SysPropertyEntity;
import com.ttw.itds.ui.service.impl.ServiceResponse;
import com.ttw.itds.ui.service.impl.SysPropertyService;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.apache.log4j.Logger;
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

    private final Logger log = Logger.getLogger(SysPropertyController.class);

    @Inject
    private SysPropertyService sysPropertyService;

    @RequestMapping(value = "/sysprops",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SysPropertyEntity>> getSysPropertyList() {
        log.debug("REST call to getSysPropertyList");
        ServiceResponse<List<SysPropertyEntity>> response = sysPropertyService.getSysPropertyList();
        return response.getHttpResponseEntity();
    }

    @RequestMapping(value = "/syspropszzz",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SysPropertyEntity>> getSysPropertyListZZZ() {
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
