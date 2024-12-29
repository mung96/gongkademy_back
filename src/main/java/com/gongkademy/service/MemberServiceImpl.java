package com.gongkademy.service;

import static com.gongkademy.exception.ErrorCode.DUPLICATED_NICKNAME;
import static com.gongkademy.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.gongkademy.domain.Member;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.service.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public String updateProfile(Long memberId, UpdateProfileRequest request) {
        Member member = memberRepository.findById(memberId);

        //가입한 회원인지 확인
        if(member == null) throw new CustomException(MEMBER_NOT_FOUND);
        //nickname 중복 처리
        if(memberRepository.findByNickname(request.getNickname()) == null) throw new CustomException(DUPLICATED_NICKNAME);

        member.updateNickname(request.getNickname());

        return request.getNickname();
    }
}
