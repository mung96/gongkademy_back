package com.gongkademy.service.dto;

import com.gongkademy.domain.Member;
import com.gongkademy.domain.RoleType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@RequiredArgsConstructor
@Builder
public class PrincipalDetails implements OAuth2User {

    private final List<RoleType> roleTypes;
    private final Member member;
    private final Map<String, Object> attributes;
    private final String sessionId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        for(RoleType roleType : roleTypes){
            collect.add(new SimpleGrantedAuthority(roleType.toString()));
        }
        return null;
    }

    @Override
    public String getName() {
        return member.getName();
    }
}
