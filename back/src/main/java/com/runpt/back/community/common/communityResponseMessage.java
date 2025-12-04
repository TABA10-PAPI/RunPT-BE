package com.runpt.back.community.common;

import com.runpt.back.global.common.ResponseMessage;

public interface communityResponseMessage extends ResponseMessage {
    String UID_NOT_EXIST = "UID does not exist.";
    String USER_NOT_EXIST = "User does not exist.";
    String POST_NOT_FOUND = "Post does not exist.";
    String COMMENT_NOT_FOUND = "Comment does not exist.";
    String INVALID_ID = "Invalid ID.";
    String COMMUNITY_NOT_FOUND = "Community not found.";
    String PARTICIPATE_NOT_FOUND = "Participation record not found.";      
    String FORBIDDEN = "Action is forbidden.";
    String JSON_PARSE_ERROR = "Error parsing JSON data.";
    String USER_GENDER_INVALID = "User gender is invalid.";
    String COMMENT_COUNT_ERROR = "Comment count error.";
    String ALREADY_PARTICIPATED = "User has already participated.";
    String NOT_PARTICIPATED = "User has not participated.";
}
