package com.gongkademy.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // OAuth2 인증 실패 원인을 로그로 출력
        log.error("OAuth2 로그인 실패: {}", exception.getMessage(), exception);

        // 실패 메시지를 리다이렉트 URL에 추가하여 클라이언트에 전달
        response.sendRedirect("/login?error=" + URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8));
    }


}


