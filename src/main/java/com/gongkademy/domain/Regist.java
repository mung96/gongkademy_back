package com.gongkademy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Regist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="regist_id")
    private Long id;

    private LocalDateTime createdTime;
}
