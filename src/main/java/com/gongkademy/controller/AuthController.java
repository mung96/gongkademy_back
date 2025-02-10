package com.gongkademy.controller;

import com.gongkademy.service.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {

    @GetMapping("/session/check")
    public ResponseEntity<?> testSession(@AuthenticationPrincipal PrincipalDetails principalDetails){
        HttpStatus httpStatus = HttpStatus.OK;
        if(principalDetails == null){
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        return ResponseEntity.status(httpStatus).build();
    }
}
