package com.project.base.model.exception;

/**
 * @author ex-yinshaobo001 at 2020/1/19 18:11
 */
public class CheckException extends Exception {

    private String httpStatusCode;

    public CheckException(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public CheckException(String httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "httpStatusCode='" + httpStatusCode + '\'' +
                "} " + super.toString();
    }
}
