package com.gongkademy.service;

import com.gongkademy.domain.Member;
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
        if(member == null) throw new IllegalStateException("가입한 회원이 아닙니다.");
        //nickname 중복 처리
        if(memberRepository.findByNickname(request.getNickname()) == null) throw new IllegalStateException("다른 사람이 가지고 있는 닉네임입니다.");

        member.updateNickname(request.getNickname());

        return request.getNickname();
    }
}
