package gov.cpsc.hts.itds.ui.service.impl;

/**
 *
 * @author rzauel
 */
public enum ServiceResponseCodeEnum {

    /**
     *
     */
    OK,

    /**
     *
     */
    BAD_REQUEST,

    /**
     *
     */
    UNAUTHORIZED,

    /**
     *
     */
    INTERNAL_SERVER_ERROR,

    /**
     *
     */
    NOT_FOUND,
    
    UNDEFINED_SOURCE,
    
    SOURCE_INVALID_FOR_STATE,
    
    UNDEFINED_FILTER,
    
    UNSUPPORTED_SEARCH,
    
    FILE_IS_EMPTY,
    
    FILE_UPLOAD_ERROR,
    
    FILE_IMPORT_ERROR,
    
    FINALIZATION_INVALID_WITHOUT_WIP,
    
    FINALIZATION_FAILED,
    
    RESET_OCCURRED

}
