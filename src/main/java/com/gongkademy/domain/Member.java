package com.gongkademy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//TODO: role 추가해야함.
public class Member extends BaseTimeEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(length = 320, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false)
    private String providerId;

    @Builder
    private Member(String nickname, String email,String name,Provider provider, String providerId) {
        this.nickname = nickname;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
    }

    //변경 메소드
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
    public void changeEmail(String email){
        this.email = email;
    }
    public void changeName(String name){
        this.name = name;
    }
}
