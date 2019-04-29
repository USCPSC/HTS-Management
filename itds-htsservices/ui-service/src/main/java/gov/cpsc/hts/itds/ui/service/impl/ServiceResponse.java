package gov.cpsc.hts.itds.ui.service.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * Response Object used to hold the response from internal services so they can
 * easily be translated into http Response Entity for response in restful call
 */
public class ServiceResponse<T> {
    
    // defaults response to OK
    private ServiceResponseCodeEnum responseCode = ServiceResponseCodeEnum.OK;
    private T value = null;

    public ServiceResponse() {
    }

    public ServiceResponse(T value) {
        this.value = value;
        this.responseCode = ServiceResponseCodeEnum.OK;
    }

    public ServiceResponse(ServiceResponseCodeEnum responseCode, T value) {
        this.responseCode = responseCode;
        this.value = value;
    }

    public ServiceResponse(ServiceResponseCodeEnum responseCode) {
        this.responseCode = responseCode;
        this.value = null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ServiceResponseCodeEnum getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ServiceResponseCodeEnum responseCode) {
        this.responseCode = responseCode;
    }
    
    public boolean isOK() {
        return (responseCode == ServiceResponseCodeEnum.OK);
    }

    public boolean isNotOK() {
        return (responseCode != ServiceResponseCodeEnum.OK);
    }

    /**
     * Translates local Service Response into Http Response Entity
     * @return Http Response Entity
     */
    public ResponseEntity<T> getHttpResponseEntity(){
        HttpStatus httpStatus = HttpStatus.OK;
        switch(this.responseCode) {
            case OK:
                httpStatus = HttpStatus.OK;
                break;
            case BAD_REQUEST:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            case INTERNAL_SERVER_ERROR:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            case UNAUTHORIZED:
                httpStatus = HttpStatus.UNAUTHORIZED;
                break;
        }
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.set("Cache-Control", "no-cache");
//        responseHeaders.set("Pragma", "no-cache");
        ResponseEntity<T> httpResponseEntity = new ResponseEntity<>(value, httpStatus);
        return httpResponseEntity;
        
    }
    
}
