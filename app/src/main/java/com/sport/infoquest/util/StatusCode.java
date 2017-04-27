package com.sport.infoquest.util;

/**
 * Created by Ionut on 17/10/2016.
 */
public enum StatusCode {
    SERVER_ERROR(500, "Internal Server Error", "Something went wrong with the server call"),
    UNAUTHORIZED(401, "Unauthorized", "Username or Password is invalid !"),
    FORBIDDEN(403, "Forbidden", "You are forbidden to connect!"),
    NOT_FOUND(404, "NOT_FOUND", "Resource not found!"),
    CONFLICT(201, "CONFLICT", "Is already on the current game"),
    ALREADY_EXISTS(409, "Conflict", "Question already exists"),
    GONE(410, "GONE", "Finish Game"),
    //if other server error code comes up, add enum value here, use this generic one for the message for now
    OTHER(-1, "Server problem", "Something went wrong with the server call"),
    OK(200, "OK", "OK"),
    NO_INTERNET_CONNECTION(0, "No internet connection", "Check your internet connection!");

    private int code;
    private String description;
    private String messageToShow;

    StatusCode(int code, String description, String messageToShow) {
        this.code = code;
        this.description = description;
        this.messageToShow = messageToShow;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getMessageToShow() {
        return messageToShow;
    }

    public static StatusCode getStatusFromCode(int code) {
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.getCode() == code) {
                return statusCode;
            }
        }
        return OTHER;
    }

    public static StatusCode getStatusFromDescriprion(String description) {
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.getDescription().equals(description)) {
                return statusCode;
            }
        }
        return OTHER;
    }
}
