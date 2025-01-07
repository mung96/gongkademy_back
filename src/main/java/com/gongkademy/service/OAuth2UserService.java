package com.gongkademy.service;

import static com.gongkademy.domain.RoleType.USER;

import com.gongkademy.domain.Member;
import com.gongkademy.domain.HasRole;
import com.gongkademy.domain.Role;
import com.gongkademy.domain.RoleType;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.repository.HasRoleRepository;
import com.gongkademy.repository.RoleRepository;
import com.gongkademy.service.dto.NaverResponse;
import com.gongkademy.service.dto.OAuth2Response;
import com.gongkademy.service.dto.PrincipalDetails;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final HasRoleRepository hasRoleRepository;

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
        PrincipalDetails principalDetails = null;

        if (findMember == null) {
            Member newMember =Member.builder()
                                    .email(oAuth2Response.getEmail())
                                    .nickname(oAuth2Response.getName())
                                    .name(oAuth2Response.getName())
                                    .provider(oAuth2Response.getProvider())
                                    .providerId(oAuth2Response.getProviderId())
                                    .build();
            memberRepository.save(newMember);

            Role role = roleRepository.findByRoleType(USER).orElse(null);
            HasRole hasRole = HasRole.builder().member(newMember).role(role).build();
            log.info("hasRole={}", hasRole.toString());
            log.info("getRole={}", hasRole.getRole());
            hasRoleRepository.save(hasRole);

            principalDetails = PrincipalDetails.builder()
                                               .attributes(oAuth2User.getAttributes())
                                               .roleTypes(List.of(hasRole.getRole().getRoleType()))
                                               .member(newMember)
                                               .build();
        }
        else {
            findMember.changeEmail(oAuth2Response.getEmail());
            findMember.changeName(oAuth2Response.getName());
//            log.info("SessionId={}", SecurityContextHolder.getContext().getAuthentication().getDetails().toString());

            List<HasRole> hasRoles = hasRoleRepository.findByMemberId(findMember.getId());
            List<RoleType> roleTypes = new ArrayList<>();
            for(HasRole hasRole : hasRoles){
                roleTypes.add(hasRole.getRole().getRoleType());
            }

            principalDetails = PrincipalDetails.builder().attributes(oAuth2User.getAttributes()).roleTypes(roleTypes).member(findMember).build();
        }

        return principalDetails;
    }

}
