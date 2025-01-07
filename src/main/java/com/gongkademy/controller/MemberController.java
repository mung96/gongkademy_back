package com.gongkademy.controller;

import com.gongkademy.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public void updateProfile(@AuthenticationPrincipal OAuth2User oAuth2User) {
//        @RequestBody UpdateProfileRequest updateProfileRequest
        System.out.println("test");
        System.out.println(oAuth2User.getAttributes());
    }
}
