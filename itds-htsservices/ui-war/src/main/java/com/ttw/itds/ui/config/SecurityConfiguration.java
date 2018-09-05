package com.ttw.itds.ui.config;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.HttpMethod;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.ttw.itds.ui.domain.entity.AuditEvent;
import com.ttw.itds.ui.domain.entity.LogEntity;
import com.ttw.itds.ui.security.Http401UnauthorizedEntryPoint;
import com.ttw.itds.ui.service.impl.AuditEventService;
import com.ttw.itds.ui.service.impl.LogService;
import com.ttw.itds.ui.service.impl.UserService;
import com.ttw.itds.ui.web.controller.AuditEventController;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private DataSource dataSource;

    @Inject
    private UserService userService;

    @Inject
    private AuditEventService auditEventService;

    @Inject
    private LogService logService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
    	Boolean ssoLoginEnabled = true;
    	String usersByUsernameQuery = "select username,password,active from itds_exam.cpsc_user where username=?";
    	
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery(usersByUsernameQuery)
                .authoritiesByUsernameQuery(
                        "select u.username,CONCAT('ROLE_', a.action_code) as authority from itds_exam.cpsc_user u left outer join " +
                                "itds_exam.user_role r on u.username = r.user_id left outer join " +
                                "itds_exam.role_application_action a on r.role_name = a.ROLE_NAME where u.username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .and()
                .authorizeRequests()
                    .antMatchers("/index.html","/login/home.html", "/", "/lib/").permitAll()
                    .antMatchers("/properties","/login","/logout","/properties").permitAll()
                    .antMatchers("/login/sso").permitAll()
                    .antMatchers(HttpMethod.GET, "/users").hasRole("USER_READ")
                    .antMatchers(HttpMethod.POST, "/users").hasRole("USER_SAVE")
                    .antMatchers(HttpMethod.DELETE, "/users").hasRole("USER_DEL")
                    .antMatchers(HttpMethod.GET, "/metrics/dataprocessing","/metrics/workflow","/metrics/examsample").hasRole("METRICS_READ")
                    .antMatchers(HttpMethod.POST, "/file/uploadXRFSample", "/file/preview").hasRole("ETL")
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
                .logout().addLogoutHandler(customLogoutHandler()).logoutSuccessUrl("https://EXAMPLE")
                    .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/home");
    }
    
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean
    public ShaPasswordEncoder passwordEncoder(){
        ShaPasswordEncoder encoder = new ShaPasswordEncoder();
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
            if (authentication != null){
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

    @Bean Http401UnauthorizedEntryPoint authenticationEntryPoint() {
        return new Http401UnauthorizedEntryPoint();
    }
}
