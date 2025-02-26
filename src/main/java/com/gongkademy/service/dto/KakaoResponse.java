package com.gongkademy.service.dto;

import com.gongkademy.domain.Provider;
import java.util.Map;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public Provider getProvider() {
        return Provider.KAKAO;
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) attribute.get("email");
    }

    @Override
    public String getName() {
        return ((Map<String,Map<String,String>>) attribute.get("kakao_account")).get("profile").get("nickname");
    }
}
