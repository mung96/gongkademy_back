package com.gongkademy.config;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    // 프론트에서 전달한 redirect_uri를 저장할 쿠키 이름
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    // HTTP 헤더의 referer 값을 저장할 쿠키 이름
    public static final String REFERER_COOKIE_NAME = "referer";
    public static final int COOKIE_EXPIRE_SECONDS = 180;
    private final CookieUtils cookieUtils;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return cookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                          .map(cookie -> cookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                          .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                         HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            cookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            cookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            cookieUtils.deleteCookie(request, response, REFERER_COOKIE_NAME);
            return;
        }

        // OAuth2AuthorizationRequest를 쿠키에 저장
        cookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                              cookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);

        // 프론트에서 전달한 redirect_uri 파라미터가 있다면 쿠키에 저장
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            cookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }

        // HTTP 헤더의 referer 값을 별도의 쿠키에 저장 (값이 존재할 때)
        String referer = request.getHeader("referer");
        if (StringUtils.isNotBlank(referer)) {
            cookieUtils.addCookie(response, REFERER_COOKIE_NAME, referer, COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        return loadAuthorizationRequest(request);
    }

    // 인증 완료 후 쿠키를 삭제하기 위한 메서드
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        cookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        cookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        cookieUtils.deleteCookie(request, response, REFERER_COOKIE_NAME);
    }
}
