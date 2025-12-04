package com.runpt.back.community.common;

import com.runpt.back.global.common.ResponseCode;

public interface communityResponseCode extends ResponseCode {
    String USER_NOT_EXIST = "UNE";
    String UID_NOT_EXIST = "UNE";
    String INVALID_ID = "IID";
    String COMMUNITY_NOT_FOUND = "CNF";
    String POST_NOT_FOUND = "PNF";
    String COMMENT_NOT_FOUND = "CNF";
    String PARTICIPATE_NOT_FOUND = "PNF";
    String FORBIDDEN = "FORBIDDEN";
    String JSON_PARSE_ERROR = "JPE";
    String USER_GENDER_INVALID = "UGI";
    String COMMENT_COUNT_ERROR = "CCE";
    String ALREADY_PARTICIPATED = "APD";
    String NOT_PARTICIPATED = "NPD";
} 