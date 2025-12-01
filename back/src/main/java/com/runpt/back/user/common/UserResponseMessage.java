package com.runpt.back.user.common;

import com.runpt.back.global.common.ResponseMessage;

public interface UserResponseMessage extends ResponseMessage{
    String USER_NOT_EXISTS = "user is not exists";
    String DUPLICATE_ID = "duplicate id";
    String ENTITY_NOT_FOUND = "entity not found";
    String CERTIFICATION_FAIL = "certification failed";
    String DUPLICATE_NICKNAME = "duplicated nickname";
    String UNKNOWN_USER = "unknown user";

    String INVALID_ACCESS_TOKEN = "invalid access token";
    String OAUTH_API_ERROR = "OAuth API error";
    String INVALID_CODE = "invalid code";
    String CODE_TO_TOKEN_FAIL = "failed to convert code to access token";
    String INVALID_DATE_FORMAT = "invalid date format";
    String INVALID_RUNNING_DATA = "invalid running data";
    String TIER_CALCULATION_ERROR = "tier calculation error";
    String AI_SERVER_ERROR = "AI server communication error";
}
