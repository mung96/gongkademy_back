package com.gongkademy.controller;

import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.service.BoardService;
import com.gongkademy.service.MemberService;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.PrincipalDetails;
import com.gongkademy.service.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;

    @PatchMapping
    public void updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails
            ,@Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        memberService.updateProfile(principalDetails.getMember().getId(), updateProfileRequest);
    }

    //내가 쓴 게시글 조회
    @GetMapping("/boards/{boardCategory}")
    public ResponseEntity<BoardListResponse> getBoards(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable BoardCategory boardCategory,
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") BoardCriteria boardCriteria) {

        BoardListResponse boardList = memberService.findBoardListByMemberId(principalDetails.getMember().getId(),boardCategory, page,boardCriteria);
        return ResponseEntity.status(HttpStatus.OK).body(boardList);
    }
}
