package com.gongkademy.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorCode {


    //회원관련
    MEMBER_NOT_FOUND("가입하지 않은 회원입니다.",HttpStatus.NOT_FOUND),
    DUPLICATED_NICKNAME("이미 사용 중인 닉네임입니다.",HttpStatus.CONFLICT),

    //강좌관련
    COURSE_NOT_FOUND("존재하지 않는 강좌입니다.",HttpStatus.NOT_FOUND),
    REGISTERED_COURSE("이미 수강 중인 강좌입니다.",HttpStatus.CONFLICT),;

    private final String message;
    private final HttpStatus httpStatus;
}
