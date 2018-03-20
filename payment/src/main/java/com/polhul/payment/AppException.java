package com.polhul.payment;

import lombok.Getter;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Getter
public class AppException extends RuntimeException {
    private final StatusCode code;

    public AppException(StatusCode code) {
        super();
        this.code = code;
    }

    public AppException(String message, Throwable cause, StatusCode code) {
        super(message, cause);
        this.code = code;
    }

    public AppException(String message, StatusCode code) {
        super(message);
        this.code = code;
    }

    public AppException(Throwable cause, StatusCode code) {
        super(cause);
        this.code = code;
    }

    public StatusCode getCode() {
        return this.code;
    }
}
