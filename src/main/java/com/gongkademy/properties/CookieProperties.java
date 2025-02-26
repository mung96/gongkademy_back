package com.gongkademy.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "cookie")
@Getter
@Setter
public class CookieProperties {
    private  String domain;
}
