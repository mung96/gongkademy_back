package com.gongkademy.controller;

import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.service.BoardService;
import com.gongkademy.service.dto.BoardDetailResponse;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.EditBoardRequest;
import com.gongkademy.service.dto.PrincipalDetails;
import com.gongkademy.service.dto.WriteBoardRequest;
import com.gongkademy.service.dto.WriteCommentRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/boards")
@Log4j2
public class BoardController {

    private final BoardService boardService;

    //게시글 목록 조회
    @GetMapping
    public ResponseEntity<BoardListResponse> getBoards(@RequestParam(value = "category") BoardCategory boardCategory,
                                                       @RequestParam(required = false, defaultValue = "1", value = "page") int page,
                                                       @RequestParam(required = false, defaultValue = "CREATED_AT", value = "criteria") BoardCriteria boardCriteria,
                                                        @RequestParam(required = false, value = "course") Long courseId,
                                                       @RequestParam(required = false, value = "lecture") Long lectureId,
                                                       @RequestParam(required = false, value = "keyword") String keyword

    ) {
        //keyword는 ,형태로 온다.
        BoardListResponse boardList;

        if(courseId == null){
            if(keyword == null){
                boardList = boardService.findBoardList(boardCategory, page,boardCriteria);
            }else{
                boardList = boardService.findBoardListByKeyword(boardCategory,keyword,page);
            }
        }else{
            if(keyword == null){
                boardList = boardService.findQuestionList(page,boardCriteria,courseId,lectureId);
            }else{
                boardList = boardService.findQuestionListByKeyword(keyword,page ,courseId,lectureId);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(boardList);
    }

    //게시글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoardDetail(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable Long boardId){
        BoardDetailResponse boardDetail;
        if(principalDetails == null){
             boardDetail = boardService.findBoardDetail(null,boardId);
            return ResponseEntity.status(HttpStatus.OK).body(boardDetail);
        }else{
             boardDetail = boardService.findBoardDetail(principalDetails.getMember().getId(),boardId);
        }

        return ResponseEntity.status(HttpStatus.OK).body(boardDetail);
    }


    //게시글 작성
    @PostMapping
    public ResponseEntity<?> writeBoard(@AuthenticationPrincipal PrincipalDetails principalDetails
                                        , @RequestParam BoardCategory category
                                        , @Valid @RequestBody WriteBoardRequest writeBoardRequest
                                        ){
        Long boardId = boardService.write(principalDetails.getMember().getId(),writeBoardRequest,category);


        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/boards/" + boardId));

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }

    //게시글 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<?> editBoard(@AuthenticationPrincipal PrincipalDetails principalDetails
                                        , @PathVariable Long boardId
                                        , @RequestParam BoardCategory category
                                        , @Valid @RequestBody EditBoardRequest editBoardRequest){
        boardService.edit(principalDetails.getMember().getId(),boardId,editBoardRequest,category);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails
                                         ,@PathVariable Long boardId) {

        boardService.delete(principalDetails.getMember().getId(),boardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //댓글 작성
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<?> writeComment(@AuthenticationPrincipal PrincipalDetails principalDetails
                                          , @PathVariable Long boardId
                                          , @Valid @RequestBody WriteCommentRequest writeCommentRequest) {

        boardService.writeComment(principalDetails.getMember().getId(),boardId,writeCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal PrincipalDetails principalDetails
                                           ,@PathVariable Long commentId) {

        boardService.deleteComment(principalDetails.getMember().getId(), commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
