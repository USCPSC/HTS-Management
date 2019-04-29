package gov.cpsc.hts.itds.ui.web.controller;

import gov.cpsc.hts.itds.ui.domain.entity.User;
import gov.cpsc.hts.itds.ui.service.impl.ServiceResponse;
import gov.cpsc.hts.itds.ui.service.impl.ServiceResponseCodeEnum;
import gov.cpsc.hts.itds.ui.service.impl.UserService;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
*
* Restful Resource class which holds the endpoints for rest, unpacks the call, calls the Service class, and responds to the http request.
*
* @author rzauel
*/
@RestController
public class UserController {

   private final Logger log = LoggerFactory.getLogger(UserController.class);

   @Autowired
   private UserService userService;

   /**
    * @return
    */
   @RequestMapping(value = "/users",
           method = RequestMethod.GET,
           produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   public ResponseEntity<Page<User>> getUsersList(
           @RequestParam(value = "userName", required = false, defaultValue = "") String userName,
           @RequestParam(value = "firstName", required = false, defaultValue = "") String firstName,
           @RequestParam(value = "lastName", required = false, defaultValue = "") String lastName,
           @RequestParam(value = "email", required = false, defaultValue = "") String email,
           @RequestParam(value = "orgCode", required = false, defaultValue = "") String orgCode,
           @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
           @RequestParam(value = "pageSize", required = false, defaultValue = "" + Integer.MAX_VALUE) Integer pageSize,
           @RequestParam(value = "currentUser", required = false, defaultValue = "") String currentUser,
           @RequestParam(value = "password", required = false, defaultValue = "") String password) {

       log.debug("REST call to getUsersList");
       ServiceResponse<Page<User>> response = new ServiceResponse<>();
       Map<String, String> queries = new HashMap<>();
       queries.put("username", userName);
       queries.put("firstname", firstName);
       queries.put("lastname", lastName);
       queries.put("email", email);
       queries.put("orgcode", orgCode);
       Pageable pageable = new PageRequest(page, pageSize);

       response = userService.getUsersListService(queries, pageable);

       return response.getHttpResponseEntity();
   }

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

   /**
    * @param username
    * @return
    */
   @RequestMapping(value = "/users/{username}",
       method = RequestMethod.GET,
       produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   public ResponseEntity<User> getOneUser(@PathVariable String username) {
       log.debug("REST call to getOneUser");
       ServiceResponse<User> response = userService.getUserWithPortsByUsername(username);
       return response.getHttpResponseEntity();
   }

   /**
    * @param user
    * @return
    */
   @RequestMapping(value = "/users",
           method = RequestMethod.POST,
           consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
           produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   public ResponseEntity<User> saveUser(@RequestBody User user, HttpSession session) {
       log.debug("REST call to save");
       User actingUser = (User) session.getAttribute("user");
       String actingUsername = actingUser != null ? actingUser.getUsername() : "SYSTEM";
       user.setLastupdateuserid(actingUsername);
       user.setLastupdateTimestamp(new DateTime());
       ServiceResponse<User> response = new ServiceResponse<>();
       if (!actingUsername.equals(user.getUsername()))
           response = userService.saveUser(user);
       else
           response.setResponseCode(ServiceResponseCodeEnum.BAD_REQUEST);

       return response.getHttpResponseEntity();
   }

   /**
    * @param user
    * @return
    */
   @RequestMapping(value = "/account",
           method = RequestMethod.POST,
           consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
           produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   public ResponseEntity<User> saveSelf(@RequestBody User user, HttpSession session) {
       log.debug("REST call to save");
       User actingUser = (User) session.getAttribute("user");
       String actingUsername = actingUser != null ? actingUser.getUsername() : "SYSTEM";
       user.setLastupdateuserid(actingUsername);
       user.setLastupdateTimestamp(new DateTime());
       ServiceResponse<User> response = new ServiceResponse<>();
       if (actingUsername.equals(user.getUsername()))
           response = userService.saveSelf(user);
       else
           response.setResponseCode(ServiceResponseCodeEnum.BAD_REQUEST);
       return response.getHttpResponseEntity();
   }

   /**
    * @param username
    * @return
    */
   @RequestMapping(value = "/users/{username}/delete",
           method = RequestMethod.DELETE,
           produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  //gz public ResponseEntity<User> deleteUser(@PathVariable String username) {
   public ResponseEntity<User> deleteUser(@PathVariable String username, HttpSession session) {
       log.debug("REST call to delete");
       User actingUser = (User) session.getAttribute("user");  //gz++
       ServiceResponse response = userService.deleteUser(username, actingUser);  //gz

       return response.getHttpResponseEntity();
   }
}
