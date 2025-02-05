package com.gongkademy.service;

import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.domain.course.RegisterStatus;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.CourseListResponse;
import com.gongkademy.service.dto.UpdateProfileDto;

public interface MemberService {
    String updateProfile(Long memberId, UpdateProfileDto request);
    BoardListResponse findBoardListByMemberId(Long memberId, BoardCategory boardCategory, int page, BoardCriteria boardCriteria);
    CourseListResponse findCourseListByMemberIdAndRegisterStatus(Long memberId, RegisterStatus registerStatus);
}
