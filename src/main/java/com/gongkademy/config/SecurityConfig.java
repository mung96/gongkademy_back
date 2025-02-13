package com.gongkademy.config;

import com.gongkademy.service.OAuth2UserService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${front.url}")
    private String[] frontUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));


        httpSecurity.authorizeHttpRequests((auth) -> auth
//                .requestMatchers("/api").permitAll()
                .requestMatchers("/api/boards", "/api/auth/session/check").permitAll()
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

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(frontUrl));
        config.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT","PATCH"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Location", "Content-Disposition"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
