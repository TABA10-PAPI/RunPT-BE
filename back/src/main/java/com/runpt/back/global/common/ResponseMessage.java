package com.runpt.back.global.common;

public interface ResponseMessage {
    String SUCCESS = "success";
    String DATABASE_ERROR = "database error";
    String VALIDATION_FAIL = "validation failed";
    String OAUTH_ERROR = "OAuth error";
    String BAD_REQUEST = "bad request";
    String INVALID_TOKEN = "invalid token";
    String TOKEN_EXPIRED = "token expired";
    String INCORRECT_REQUEST = "incorrect request";
}