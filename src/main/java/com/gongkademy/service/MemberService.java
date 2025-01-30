package com.gongkademy.service;

import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.UpdateProfileRequest;

public interface MemberService {
    String updateProfile(Long memberId, UpdateProfileRequest request);
    BoardListResponse findBoardListByMemberId(Long memberId, BoardCategory boardCategory, int page, BoardCriteria boardCriteria);
}
