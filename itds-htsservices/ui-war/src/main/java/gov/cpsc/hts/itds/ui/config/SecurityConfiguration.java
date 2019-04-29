package gov.cpsc.hts.itds.ui.config;

import gov.cpsc.hts.itds.ui.domain.entity.AuditEvent;
import gov.cpsc.hts.itds.ui.domain.entity.LogEntity;
import gov.cpsc.hts.itds.ui.security.Http401UnauthorizedEntryPoint;
import gov.cpsc.hts.itds.ui.service.impl.AuditEventService;
import gov.cpsc.hts.itds.ui.service.impl.LogService;
import gov.cpsc.hts.itds.ui.service.impl.UserService;
import gov.cpsc.hts.itds.ui.web.controller.AuditEventController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.HttpMethod;
import java.io.IOException;

/**
 * Created by dlauber on 5/9/16.
 */
@Configuration
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private UserService userService;

  @Autowired
  private AuditEventService auditEventService;

  @Autowired
  private LogService logService;

  @Value("${hts.security.usersQueryIfSsoEnabled}")
  private String usersQueryIfSsoEnabled;

  @Value("${hts.security.usersQueryIfSsoDisabled}")
  private String usersQueryIfSsoDisabled;

  @Value("${hts.security.authoritiesQuery}")
  private String authoritiesQuery;
  
  @Value("${hts.security.logoutSuccessUrl}")
  private String logoutSuccessUrl;

  @Value("${hts.security.messageDigestPasswordEncoder}")
  private String messageDigestPasswordEncoder;

  @Value("${hts.security.ssoDisabled}")
  private String ssoDisabled;

  @Autowired // @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    String usersByUsernameQuery = usersQueryIfSsoEnabled;
	if ("true".equalsIgnoreCase(ssoDisabled)) {
		usersByUsernameQuery = usersQueryIfSsoDisabled;
	}
    auth.jdbcAuthentication().dataSource(dataSource)
      .passwordEncoder(passwordEncoder())
      .usersByUsernameQuery(usersByUsernameQuery)
      .authoritiesByUsernameQuery(
    	authoritiesQuery);
  }

  @Override
  // @Autowired causes deployment-time error: HttpSecurity is not an autowire candidate
  protected void configure(HttpSecurity http) throws Exception {
    http
      .httpBasic()
      .authenticationEntryPoint(authenticationEntryPoint())
      .and()
      .authorizeRequests()
      .antMatchers("/index.html", "/login/home.html", "/", "/lib/").permitAll()
      .antMatchers("/properties", "/login", "/logout", "/properties").permitAll()
      .antMatchers("/login/sso").permitAll()
      .antMatchers(HttpMethod.GET, "/users").hasRole("USER_READ")
      .antMatchers(HttpMethod.POST, "/users").hasRole("USER_SAVE")
      .antMatchers(HttpMethod.DELETE, "/users").hasRole("USER_DEL")
      .antMatchers(HttpMethod.GET, "/").hasRole("MODEL_SAVE")
      .antMatchers(HttpMethod.GET, "/").hasRole("MODEL_READ")
      .antMatchers(HttpMethod.GET, "/").hasRole("MODEL_DEL")
      .antMatchers(HttpMethod.POST, "/").hasRole("MODEL_SAVE")
      .antMatchers(HttpMethod.POST, "/").hasRole("MODEL_DEL")
      .and()
      .formLogin()
      .loginPage("/login")
      .failureHandler(authFailureHandler())
      .and()
      .logout().addLogoutHandler(customLogoutHandler()).logoutSuccessUrl(logoutSuccessUrl)
      .and()
      .csrf().disable();
  }

  @Override
  // @Autowired causes deployment-time error: WebSecurity is not an autowire candidate
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/home");
  }

  private CsrfTokenRepository csrfTokenRepository() {
    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
    repository.setHeaderName("X-XSRF-TOKEN");
    return repository;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder(messageDigestPasswordEncoder);
    encoder.setEncodeHashAsBase64(true);
    return encoder;
  }

  @Bean
  public AuthenticationFailureHandler authFailureHandler() {
    return new AuthenticationFailureHandler() {
      @Override
      public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        System.out.println("Failed login!!!");
      }
    };
  }

  @Bean
  public CustomLogoutHandler customLogoutHandler() {
    return new CustomLogoutHandler();
  }

  public class CustomLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
      if (authentication != null) {
        String currentUser = authentication.getName();
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setPrincipal(currentUser);
        auditEvent.setDataType(AuditEventController.class.getSimpleName());
        auditEvent.setDataMessage("User '" + currentUser + "' successfully logged out");
        auditEvent.setEventType("AUTHORIZATION_SUCCESS");
        auditEventService.saveAuditEvent(auditEvent);

        LogEntity logEntity = new LogEntity();
        logEntity.setUsername(currentUser);
        logEntity.setLevel("Info");
        logEntity.setMessage("User '" + currentUser + "' successfully logged out");
        logService.addLogService(logEntity);

        userService.updateLogoutTime(currentUser);

        new SecurityContextLogoutHandler().logout(request, response, authentication);
      }
    }
  }

  @Bean
  Http401UnauthorizedEntryPoint authenticationEntryPoint() {
    return new Http401UnauthorizedEntryPoint();
  }
}