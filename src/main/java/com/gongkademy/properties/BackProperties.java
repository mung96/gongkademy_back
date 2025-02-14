package com.gongkademy.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "back")
@Getter
@Setter
public class BackProperties {
    private  String activeProfile;
}
