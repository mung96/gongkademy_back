package com.gongkademy.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {


    //회원관련
    MEMBER_NOT_FOUND("가입하지 않은 회원입니다.",HttpStatus.NOT_FOUND),
    DUPLICATED_NICKNAME("이미 사용 중인 닉네임입니다.",HttpStatus.CONFLICT),

    //강좌관련
    COURSE_NOT_FOUND("존재하지 않는 강좌입니다.",HttpStatus.NOT_FOUND),
    REGISTERED_COURSE("이미 수강 중인 강좌입니다.",HttpStatus.CONFLICT),
    REGISTER_NOT_FOUND("수강 중인 강좌가 아닙니다.",HttpStatus.NOT_FOUND),
    LECTURE_NOT_FOUND("존재하지 않는 강의입니다.",HttpStatus.NOT_FOUND),

    //게시글 관련
    BOARD_NOT_FOUND("존재하지 않는 게시글입니다.",HttpStatus.NOT_FOUND),
    BOARD_CATEGORY_NOT_FOUND("존재하지 않는 게시글 카테고리입니다.",HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("존재하지 않는 댓글입니다.",HttpStatus.NOT_FOUND),
    NOT_BOARD_WRITER("게시글의 작성자가 아닙니다.",HttpStatus.FORBIDDEN),
    NOT_COMMENT_WRITER("댓글의 작성자가 아닙니다.",HttpStatus.FORBIDDEN),
    //적절한 요청이 아닙니다.
    NOT_VALID_QUESTION_REQUEST("강좌가 선택되지 않았습니다.",HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
