package com.gongkademy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseSoftDeleteAndTimeEntity extends BaseTimeEntity{

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void delete(){
        this.isDeleted = true;
    }
    public void recover(){
        this.isDeleted = false;
    }
}

