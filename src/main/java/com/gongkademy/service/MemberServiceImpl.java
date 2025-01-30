package com.gongkademy.service;

import static com.gongkademy.domain.board.BoardCategory.QUESTION;
import static com.gongkademy.exception.ErrorCode.DUPLICATED_NICKNAME;
import static com.gongkademy.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.gongkademy.domain.Comment;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.domain.board.Question;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.BoardRepository;
import com.gongkademy.repository.CommentRepository;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.service.dto.BoardItemDto;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.UpdateProfileRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Override
    public String updateProfile(Long memberId, UpdateProfileRequest request) {
        //가입한 회원인지 확인
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(()-> new CustomException(MEMBER_NOT_FOUND));

        //nickname 중복 처리
        memberRepository.findByNickname(request.getNickname())
                        .ifPresent(m -> {throw new CustomException(DUPLICATED_NICKNAME);});

        member.updateNickname(request.getNickname());

        return request.getNickname();
    }

    @Override
    public BoardListResponse findBoardListByMemberId(Long memberId, BoardCategory boardCategory, int page,
                                                     BoardCriteria boardCriteria) {
        List<BoardItemDto> boardList = boardRepository.findAllByCategoryAndMemberId(memberId,boardCategory, page, boardCriteria)
                                                      .stream()
                                                      .map(board -> BoardItemDto.builder()
                                                                                .title(board.getTitle())
                                                                                .body(board.getBody())
                                                                                .date(board.getUpdatedAt().toString())
                                                                                .courseTitle(boardCategory == QUESTION ? ((Question)board).getLecture().getCourse().getTitle() : null)
                                                                                .commentCount(commentRepository.findByBoardId(board.getId()).size())
                                                                                .build()).toList();

        Long totalPage = boardRepository.countAllByCategoryAndMemberId(memberId,boardCategory);

        return BoardListResponse.builder().boardList(boardList).totalPage(totalPage).build();
    }
}
