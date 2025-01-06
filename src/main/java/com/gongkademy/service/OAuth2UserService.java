package com.gongkademy.service;

import com.gongkademy.domain.Member;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.service.dto.NaverResponse;
import com.gongkademy.service.dto.OAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        Member findMember = memberRepository.findByProviderAndProviderId(oAuth2Response.getProvider(),oAuth2Response.getProviderId()).orElse(null);
        if (findMember == null) {
            memberRepository.save(Member.builder()
                                        .email(oAuth2Response.getEmail())
                                        .nickname(oAuth2Response.getName())
                                        .name(oAuth2Response.getName())
                                        .provider(oAuth2Response.getProvider())
                                        .providerId(oAuth2Response.getProviderId())
                                        .build());
        }
        else {
            findMember.changeEmail(oAuth2Response.getEmail());
            findMember.changeName(oAuth2Response.getName());
        }

        return oAuth2User;
    }

}
