package gov.cpsc.hts.itds.ui.web.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gov.cpsc.hts.itds.ui.domain.entity.AuditEvent;
import gov.cpsc.hts.itds.ui.domain.entity.LogEntity;
import gov.cpsc.hts.itds.ui.domain.entity.User;
import gov.cpsc.hts.itds.ui.service.LdapService;
import gov.cpsc.hts.itds.ui.service.impl.AuditEventService;
import gov.cpsc.hts.itds.ui.service.impl.LogService;
import gov.cpsc.hts.itds.ui.service.impl.ServiceResponse;
import gov.cpsc.hts.itds.ui.service.impl.UserService;
import gov.cpsc.hts.itds.ui.shared.codec.JsonCodec;

@SpringBootApplication
@RestController
public class SessionController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuditEventService auditEventService;

    @Autowired
    private LogService logService;
    
    @Autowired
    private LdapService ldapService;

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
		String email = request.getHeader("Email"); // header name is case insensitive, but changing here to match itds-ui
		String sn = request.getHeader("Ln"); 
		String fn = request.getHeader("Fn"); 
		String cn = request.getHeader("Cn"); 
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
