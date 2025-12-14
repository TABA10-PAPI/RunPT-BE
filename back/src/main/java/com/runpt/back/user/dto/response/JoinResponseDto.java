package com.runpt.back.user.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.runpt.back.global.dto.ResponseDto;
import com.runpt.back.user.common.UserResponseCode;
import com.runpt.back.user.common.UserResponseMessage;
import com.runpt.back.user.entity.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JoinResponseDto extends ResponseDto{
    
    private UserInfo user;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserInfo {
        private Long id;
        private String oauthProvider;
        private String oauthUid;
        private String nickname;
        private Integer age;
        private Integer height;
        private Integer weight;
        private String gender;

        public UserInfo(UserEntity user) {
            if (user != null) {
                this.id = user.getId();
                this.oauthProvider = user.getOauthProvider();
                this.oauthUid = user.getOauthUid();
                this.nickname = user.getNickname();
                this.age = user.getAge();
                this.height = user.getHeight();
                this.weight = user.getWeight();
                this.gender = user.getGender();
            }
        }
    }

    public JoinResponseDto(UserEntity user) {
        super();
        this.user = new UserInfo(user);
    }

    public static ResponseEntity<JoinResponseDto> joinSuccess(UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(new JoinResponseDto(user));
    }

    public static ResponseEntity<ResponseDto> userNotExitsts() {
        ResponseDto responseBody = new ResponseDto(UserResponseCode.USER_NOT_EXISTS, UserResponseMessage.USER_NOT_EXISTS);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
