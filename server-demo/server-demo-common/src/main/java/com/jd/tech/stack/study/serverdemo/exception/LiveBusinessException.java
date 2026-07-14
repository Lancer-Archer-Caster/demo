package com.jd.tech.stack.study.serverdemo.exception;

/**
 * Description: 直播业务异常
 *
 * @author DongBoot
 */
public class LiveBusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 错误信息
     */
    private final String errorMessage;

    public LiveBusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public LiveBusinessException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public LiveBusinessException(String message) {
        super(message);
        this.errorCode = null;
        this.errorMessage = message;
    }

    public LiveBusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.errorMessage = message;
    }

    public LiveBusinessException(Throwable cause) {
        super(cause);
        this.errorCode = null;
        this.errorMessage = null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}