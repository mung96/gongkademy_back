package com.gongkademy.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "front")
@Getter
@Setter
public class FrontProperties {
    private  String localUrl;
    private  String devUrl;
    private  String prodUrl;
}
