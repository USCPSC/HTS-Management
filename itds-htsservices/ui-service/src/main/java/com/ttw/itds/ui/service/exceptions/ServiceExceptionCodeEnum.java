package com.ttw.itds.ui.service.exceptions;

import org.springframework.http.HttpStatus;

public enum ServiceExceptionCodeEnum {

    OK, BAD_REQUEST, UNAUTHORIZED, INTERNAL_SERVER_ERROR, NOT_FOUND;
    
    public static HttpStatus getHttpStatus(ServiceExceptionCodeEnum responseCode) {
        HttpStatus httpStatus = HttpStatus.OK;
        switch(responseCode) {
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
            case NOT_FOUND:
                httpStatus = HttpStatus.NOT_FOUND;
                break;
        }
        return httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return ServiceExceptionCodeEnum.getHttpStatus(this);
    }

}
