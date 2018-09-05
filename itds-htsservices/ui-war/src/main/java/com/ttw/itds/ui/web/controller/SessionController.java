package com.ttw.itds.ui.web.controller;

import java.io.IOException;
import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ttw.itds.ui.domain.entity.AuditEvent;
import com.ttw.itds.ui.domain.entity.LogEntity;
import com.ttw.itds.ui.domain.entity.User;
import com.ttw.itds.ui.service.LdapService;
import com.ttw.itds.ui.service.impl.AuditEventService;
import com.ttw.itds.ui.service.impl.LogService;
import com.ttw.itds.ui.service.impl.ServiceResponse;
import com.ttw.itds.ui.service.impl.UserService;
import com.ttw.itds.ui.shared.codec.JsonCodec;

@SpringBootApplication
@RestController
public class SessionController {
    private final Logger log = Logger.getLogger(UserController.class);

    @Inject
    private UserService userService;

    @Inject
    private AuditEventService auditEventService;

    @Inject
    private LogService logService;
    
    @Inject
    private LdapService ldapService;

    /**
     *
     * @param user
     * @param session
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/login")
    public Principal login(Principal user, HttpSession session, HttpServletRequest request, HttpServletResponse response,
               @RequestParam(value = "logout", required = false) String logout
               ) {
        log.debug("REST call to login");
        if (logout != null){
            return null;
        }
        User sessionUser = userService.getUserByUsername(user.getName()).getValue();
        session.setAttribute("user",sessionUser);

        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setPrincipal(sessionUser.getUsername());
        auditEvent.setDataType(AuditEventController.class.getSimpleName());
        LogEntity logEntity = new LogEntity();
        logEntity.setUsername(sessionUser.getUsername());
        logEntity.setLevel("Info");
        auditEvent.setDataMessage("User '" + sessionUser.getUsername() + "' successfully logged in");
        auditEvent.setEventType("AUTHORIZATION_SUCCESS");
        logEntity.setMessage("User '" + sessionUser.getUsername() + "' successfully logged in");
        userService.updateLoginTime(sessionUser.getUsername());
        auditEventService.saveAuditEvent(auditEvent);
        logService.addLogService(logEntity);
        return user;
    }

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public String getSession(Principal user, HttpSession session) {
        log.debug("REST call to session");
        String username = (null == user) ? null : user.getName();
        ServiceResponse<HttpSession> response = new ServiceResponse<>();
        response.setValue(session);
        // TODO return object instead of String
        String nameSegment = username == null ? "null" : "\"" + username + "\"";
        return "{\"session\": \"" + session.getId() + "\", \"username\": " + nameSegment + "}";
    }

    /**
    * Called from the client to determine if SSO login should be bypassed. Normally the login process relies on SSO,
    * but in development/testing stages there may be a legitimate need to bypass SSO. To cause the client to bypass
    * SSO, ensure that there is a System property with name "Client.SSOEnabled" and value "false". One way to set such
    * a System property is by using Wildfly's "Configuration: System Properties" feature. If Client.SSOEnabled does
    * not exist, or if it has any value other than "false", then the result is the same as if it is set to "true".
    *
    * @return false iff System property Client.SSOEnabled exists and is (case-insensitive) "false", else true.
    */    
    @RequestMapping(value = "/login/ssoenabled", method = RequestMethod.GET)
    public boolean ssoEnabled() {
    	String s = System.getProperty("Client.SSOEnabled", "The value of Client.SSOEnabled is null");
    	boolean result = true;
    	if (s != null && s.equalsIgnoreCase("false")) {
    		result = false;
    	}
    	log.info("REST call to get property Client.SSOEnabled from System class. Client.SSOEnabled=" + s + "; val=" + result);
        return result;
    }
    
    @RequestMapping(value = "/login/sso", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
    public String ssoLogin(@RequestParam(value = "logout", required = false) String logout, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        if (logout != null) {
            return null;
        }
        log.info("SessionController::ssoLogin():"); 
    	String username = null;
		String email = request.getHeader("email"); // header name is case insensitive
		String sn = request.getHeader("ln"); 
		String fn = request.getHeader("fn"); 
		String cn = request.getHeader("cn"); 
    	log.error("SessionController::ssoLogin(): HTTP Headers: email="+email+"<<< sn="+sn+"<<< fn="+fn+"<<< cn="+cn+"<<<"); 
		if (email == null && sn == null && fn == null && cn==null) {
			String msg = "No SSO Headers present.";
			log.info(msg);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return JsonCodec.marshal(msg);
		}
		// validate headers against LDAP and lookup RAM username from cpsc_users table
    	try {
			username = ldapService.getRamUsernameByLdapInfo(email, fn, sn, cn);
	    	log.info("SessionController::ssoLogin(): after getRamUsernameByLdapInfo(), username="+username+"<<<"); 
		} catch (IOException e) {
	    	log.info("SessionController::ssoLogin(): exception from getRamUsernameByLdapInfo(). msg="+e.getMessage()+"<<<"); 
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return JsonCodec.marshal("process SSO exception: " + e.getMessage());
		}
		if(username == null || username.trim().length() ==0) {
			String msg = "no RAM username found for cn=" + cn;
			log.info(msg);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return JsonCodec.marshal(msg);
    	}
        log.info("SessionController::ssoLogin(): success. username="+username+"<<<"); 
    	return JsonCodec.marshal(username);
    }

}
