package com.runpt.back.user.common;

import com.runpt.back.global.common.ResponseCode;

public interface UserResponseCode extends ResponseCode {
    String USER_NOT_EXISTS = "UNE";
    String ENTITY_NOT_FOUND = "ENF";
    String CERTIFICATION_FAIL = "CF";
    String DUPLICATE_ID = "DI";
    String DUPLICATE_NICKNAME = "DN";
    String UNKNOWN_USER = "UU";
    
    String INVALID_ACCESS_TOKEN = "IAT";
    String OAUTH_API_ERROR = "OAE";            
    String INVALID_CODE = "IC";                
    String CODE_TO_TOKEN_FAIL = "CTF";        
    String INVALID_DATE_FORMAT = "IDF";       
    String INVALID_RUNNING_DATA = "IRD";      
    String TIER_CALCULATION_ERROR = "TCE";  
    String AI_SERVER_ERROR = "ASE";            
}
