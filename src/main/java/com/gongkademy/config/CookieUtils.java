package com.gongkademy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongkademy.properties.CookieProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final CookieProperties cookieProperties;

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            return Arrays.stream(cookies)
                         .filter(cookie -> cookie.getName().equals(name))
                         .findFirst();
        }
        return Optional.empty();
    }

    public  void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        try {
            Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
            cookie.setPath("/");
            cookie.setDomain(cookieProperties.getDomain());
            cookie.setHttpOnly(true);
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Cookie encoding error", e);
        }
    }

    public  void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        getCookie(request, name).ifPresent(cookie -> {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setDomain(cookieProperties.getDomain());
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
    }

    // OAuth2AuthorizationRequest 직렬화/역직렬화 (Spring의 SerializationUtils 사용)
    public static String serialize(Object object) {
        byte[] bytes = SerializationUtils.serialize(object);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            byte[] bytes = Base64.getUrlDecoder().decode(URLDecoder.decode(cookie.getValue(), "UTF-8"));
            return cls.cast(SerializationUtils.deserialize(bytes));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Cookie decoding error", e);
        }
    }
}
