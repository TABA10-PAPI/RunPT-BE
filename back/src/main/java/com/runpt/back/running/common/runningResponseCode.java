package com.runpt.back.running.common;
import com.runpt.back.global.common.ResponseCode;

public interface runningResponseCode extends ResponseCode {
    String UID_NOT_EXISTS = "UNE";
    String DATE_NOT_EXISTS = "DNE";
    String BATTERY_NOT_FOUND = "BNE";
    String RECOMMENDATION_PARSE_ERROR = "RPE";
}