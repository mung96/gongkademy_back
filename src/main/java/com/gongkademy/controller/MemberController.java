package com.gongkademy.controller;

import com.gongkademy.controller.dto.MemberInfoResponse;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.domain.course.RegisterStatus;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.service.BoardService;
import com.gongkademy.service.MemberService;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.CourseListResponse;
import com.gongkademy.service.dto.PrincipalDetails;
import com.gongkademy.service.dto.UpdateProfileDto;
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
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = memberRepository.findById(principalDetails.getMember().getId()).orElseThrow(()->new CustomException(
                ErrorCode.MEMBER_NOT_FOUND));
        MemberInfoResponse memberInfoResponse = MemberInfoResponse.builder().nickname(member.getNickname()).build();
        return ResponseEntity.status(HttpStatus.OK).body(memberInfoResponse);
    }

    @PatchMapping
    public ResponseEntity<UpdateProfileDto> updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails
            ,@Valid @RequestBody UpdateProfileDto updateProfileRequest) {
        String updatedNickname = memberService.updateProfile(principalDetails.getMember().getId(), updateProfileRequest);
        //TODO: 관리자는 닉네임으로 안됨.

        UpdateProfileDto updateProfileResponse = UpdateProfileDto.builder().nickname(updatedNickname).build();
        return ResponseEntity.status(HttpStatus.OK).body(updateProfileResponse);
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

    @GetMapping("/courses")
    public ResponseEntity<CourseListResponse> getCourseList (@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                             @RequestParam(required = false, value = "status") RegisterStatus status) {
        CourseListResponse courseListResponse = memberService.findCourseListByMemberIdAndRegisterStatus(principalDetails.getMember().getId(), status);
        return ResponseEntity.status(HttpStatus.OK).body(courseListResponse);
    }

}
