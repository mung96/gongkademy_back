package com.gongkademy.controller;

import com.gongkademy.service.MemberService;
import com.gongkademy.service.dto.PrincipalDetails;
import com.gongkademy.service.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping
    public void updateProfile(@AuthenticationPrincipal PrincipalDetails principalDetails
            ,@Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        memberService.updateProfile(principalDetails.getMember().getId(), updateProfileRequest);
    }
}
