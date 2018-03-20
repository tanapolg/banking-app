package com.polhul.payment;

/**
 * Created by TPolhul on 3/16/2018.
 */
public enum StatusCode {

    //6xx - General hub error code
    GENERAL_ERROR(601, "Not categorized payment error"),
    MISSING_PARAMETER(602, "Missing parameter"),
    INVALID_VALUE(603, "Invalid value"),
    //61x - Payment related error code
    WITHDRAW_NOT_ACCEPTED(611, "Withdraw not accepted"),
    DEPOSIT_NOT_ACCEPTED(612, "Deposit not accepted"),
    //62x - CLient related error code
    CLIENT_ALREADY_EXIST(621, "CLient already exist"),
    NO_CLIENT_EXIST(622, "No such client exist");

    private final int code;
    private final String message;

    public final int getCode() {
        return this.code;
    }

    public final String getMessage() {
        return this.message;
    }

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
