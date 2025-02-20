package com.gongkademy.config;

import com.gongkademy.service.OAuth2UserService;
import com.gongkademy.properties.FrontProperties;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Getter
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2LogoutSuccessHandler oAuth2LogoutSuccessHandler;
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final FrontProperties frontProperties;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));


        httpSecurity.authorizeHttpRequests((auth) -> auth
//                                           api/courses/{courseId}/lectures
//                                           api/courses/{courseId}
                .requestMatchers( "/api/auth/session/check",
                                  "/api/boards","/api/boards/*",
                                  "api/courses","/api/courses/*", "/api/courses/*/lectures"
                                    ,"/health").permitAll()
                .anyRequest().authenticated());

        httpSecurity.oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorizationEndpoint ->
                                               authorizationEndpoint
                                                       .authorizationRequestRepository(authorizationRequestRepository)
                )
                .userInfoEndpoint(userInfoEndpointConfig ->
                                          userInfoEndpointConfig.userService(oAuth2UserService)
                )
                .successHandler(authenticationSuccessHandler)

        );

        httpSecurity.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(oAuth2LogoutSuccessHandler));


        httpSecurity.sessionManagement((session) -> session
                        .sessionFixation(SessionFixationConfigurer::newSession));

        // 커스텀 인증 진입점 설정: 인증되지 않은 요청에 대해 401 에러 반환
        httpSecurity.exceptionHandling(exception ->
                                               exception.authenticationEntryPoint((request, response, authException) -> {
                                                   response.setContentType("application/json;charset=UTF-8");
                                                   response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                                   //TODO: 콘솔에 안띄우려면 200대를 터트려야하는구나.
                                                   String json = "{\"message\": \"" +"로그인되지 않은 사용자입니다."+ "\"}";
                                                   response.getWriter().write(json);
                                               })
        );
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(frontProperties.getLocalUrl(), frontProperties.getDevUrl(), frontProperties.getProdUrl()));
        config.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT","PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Location", "Content-Disposition"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
