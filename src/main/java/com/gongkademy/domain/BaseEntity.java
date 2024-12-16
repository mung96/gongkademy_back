package com.gongkademy.domain;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
}
