package com.polhul.payment.domain;

/**
 * Created by TPolhul on 3/20/2018.
 */
public enum SessionStatus {
    ACTIVE("active"),
    EXPIRED("expired");

    private final String message;

    public final String getMessage() {
        return this.message;
    }

    SessionStatus(String message) {
        this.message = message;
    }
}
