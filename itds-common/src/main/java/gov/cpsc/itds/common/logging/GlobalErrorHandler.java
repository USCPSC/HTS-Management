package gov.cpsc.itds.common.logging;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

  public GlobalErrorHandler() {
    LoggingProxy.setLoggingProxy(logger);
    System.out.println("test writing to stdout");
    System.err.println("test writing to stderr");
  }
    @ExceptionHandler(Exception.class)
    public void handleAnyExceptions(HttpServletRequest request,
                                       HttpServletResponse response, Exception e) {
        // These exceptions should be swallowed and not appear in the logs as a exceptions but
        // just indicate that it occured
        if (e instanceof IllegalStateException) {
            logger.debug("illegalStateException caused by user session expiration - normal operation");
        } else {
            if (e instanceof InvalidDefinitionException) {
                logger.debug("InvalidDefinitionException caused by empty bean serialization - normal operation");
            } else {
            logger.error("Unexpected error -->", e);
            }
        }
    }
}
