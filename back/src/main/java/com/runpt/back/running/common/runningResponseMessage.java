package com.runpt.back.running.common;

import com.runpt.back.global.common.ResponseMessage;

public interface RunningResponseMessage extends ResponseMessage {
    String UID_NOT_EXISTS = "user id does not exist";
    String DATE_NOT_EXISTS = "date does not exist";
    String BATTERY_NOT_FOUND = "battery information not found";
    String RECOMMENDATION_PARSE_ERROR = "error parsing recommendation data";
}
