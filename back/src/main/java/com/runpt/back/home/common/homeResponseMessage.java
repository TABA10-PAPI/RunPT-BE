package com.runpt.back.home.common;

import com.runpt.back.global.common.ResponseMessage;

public interface homeResponseMessage extends ResponseMessage {
    String UID_NOT_EXIST = "UID does not exist.";
    String DATE_NOT_EXIST = "Data does not exist.";
    String USER_NOT_EXIST = "User does not exist.";
    String BATTERY_NOT_FOUND = "Battery information not found.";
    String RECOMMENDATRION_PARAM_ERROR = "Recommendation parameter error.";
}
