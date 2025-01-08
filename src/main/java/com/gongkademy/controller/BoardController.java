package com.gongkademy.controller;

import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.service.BoardService;
import com.gongkademy.service.dto.BoardDetailResponse;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.LectureDetailResponse;
import com.gongkademy.service.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    //게시글 목록 조회
    @GetMapping
    public ResponseEntity<BoardListResponse> getBoards(@RequestParam BoardCategory category){
        BoardListResponse boardList = boardService.findBoardList(category, 0);
        return ResponseEntity.ok(boardList);
    }

    //게시글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoards(@PathVariable Long boardId){
        BoardDetailResponse boardDetail = boardService.findBoardDetail(boardId);
        return ResponseEntity.ok(boardDetail);
    }

    //게시글 작성

    //게시글 수정

    //게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails
            , @PathVariable Long boardId) {
        boardService.delete(principalDetails.getMember().getId(),boardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //댓글 작성

    //댓글 삭제
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal PrincipalDetails principalDetails
            , @PathVariable Long commentId) {
        boardService.deleteComment(principalDetails.getMember().getId(), commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
