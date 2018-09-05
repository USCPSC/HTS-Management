package com.ttw.itds.ui.web.controller;

import com.ttw.itds.ui.domain.entity.User;
import com.ttw.itds.ui.service.impl.ServiceResponse;
import com.ttw.itds.ui.service.impl.ServiceResponseCodeEnum;
import com.ttw.itds.ui.service.impl.UserService;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * Restful Resource class which holds the endpoints for rest, unpacks the call, calls the Service class, and responds to the http request.
 */
@RestController
public class UserController {

    private final Logger log = Logger.getLogger(UserController.class);

    @Inject
    private UserService userService;

    /**
     * @param username
     * @return
     */
    @RequestMapping(value = "/users/{username}/isunique",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Boolean> isUsernameUnique(@PathVariable String username) {
        log.debug("REST call to isUsernameUnique");
        ServiceResponse<Boolean> response = userService.isUsernameUnique(username);

        return response.getHttpResponseEntity();
    }

}
