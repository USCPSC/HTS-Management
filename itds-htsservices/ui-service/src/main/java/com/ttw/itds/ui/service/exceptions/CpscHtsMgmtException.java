package com.ttw.itds.ui.service.exceptions;

public class CpscHtsMgmtException extends RuntimeException {
    
    private static final long serialVersionUID = 4517274630830172073L;
    private final CpscHtsMgmtCodeEnum errorCode;
    private final int tag;
    private final String message;

    public CpscHtsMgmtException(CpscHtsMgmtCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
        this.tag = 0;
        this.message = "";
    }

    public CpscHtsMgmtException(CpscHtsMgmtCodeEnum errorCode, int tag) {
        super();
        this.errorCode = errorCode;
        this.tag = tag;
        this.message = "";
    }

    public CpscHtsMgmtException(CpscHtsMgmtCodeEnum errorCode, int tag, String message) {
        super(message);
        this.errorCode = errorCode;
        this.tag = tag;
        this.message = message;
    }

    public CpscHtsMgmtException(CpscHtsMgmtCodeEnum errorCode, int tag, String message, Throwable t) {
        super(message, t);
        this.errorCode = errorCode;
        this.tag = tag;
        this.message = message;
    }

    public CpscHtsMgmtException(CpscHtsMgmtCodeEnum errorCode, String message, Throwable t) {
        super(message, t);
        this.errorCode = errorCode;
        this.tag = 0;
        this.message = message;
    }

    public CpscHtsMgmtCodeEnum getErrorCode() {
        return errorCode;
    }

    public int getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

}
