package com.gongkademy.config;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> delegate;
    public static final String REDIRECT_URI = "redirect_uri"; //redirect시 사용할 이름
    public static final String REFERER = "referer"; //redirect시 사용할 이름


    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //redirect는 쿠키에 그대로 고
        //referer만 추출
        String referer = CookieUtils.getCookie(request, REFERER).orElse(null).getValue();
        String redirectUri = CookieUtils.getCookie(request, REDIRECT_URI).orElse(null).getValue();

        if (referer != null) {
            referer = URLDecoder.decode(referer, StandardCharsets.UTF_8);
        }
        if (redirectUri != null) {
            redirectUri = URLDecoder.decode(redirectUri, StandardCharsets.UTF_8);
        }

        CookieUtils.deleteCookie(request, response, REFERER);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI);

        getRedirectStrategy().sendRedirect(request, response,referer+"redirect/login?redirect_uri="+redirectUri);
    }
}
