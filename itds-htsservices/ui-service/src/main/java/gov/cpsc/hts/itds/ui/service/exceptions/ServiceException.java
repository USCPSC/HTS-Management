package gov.cpsc.hts.itds.ui.service.exceptions;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = 4513774630830772873L;

    private final ServiceExceptionCodeEnum responseCode;

    public ServiceException(ServiceExceptionCodeEnum responseCode, String msg) {
        super(msg);
        this.responseCode = responseCode;
    }

    public ServiceException(ServiceExceptionCodeEnum responseCode, String msg, Throwable t) {
        super(msg, t);
        this.responseCode = responseCode;
    }

    public ServiceExceptionCodeEnum getResponseCode() {
        return responseCode;
    }
    
    public HttpStatus getHttpStatus(){
        return responseCode.getHttpStatus();
    }
    
}
