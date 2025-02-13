package com.gongkademy.controller;

import com.gongkademy.controller.dto.CheckSessionResponse;
import com.gongkademy.service.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
@Log4j2
public class AuthController {

    @GetMapping("/session/check")
    public ResponseEntity<CheckSessionResponse> testSession(@AuthenticationPrincipal PrincipalDetails principalDetails){
        log.info("세션 테스트 API가 요청되었습니다. principalDetails: "+principalDetails);

        if(principalDetails == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(CheckSessionResponse.builder().isLogin(true).memberId(principalDetails.getMember().getId()).build());
    }
}
