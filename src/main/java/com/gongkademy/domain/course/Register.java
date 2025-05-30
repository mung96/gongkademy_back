package com.gongkademy.domain.course;

import com.gongkademy.domain.BaseTimeEntity;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.Provider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Register extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="register_id")
    private Long id;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegisterStatus registerStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="course_id")
    private Course course;


    @Builder
    private Register(Member member, Course course, RegisterStatus registerStatus) {
        this.member = member;
        this.course = course;
        this.registerStatus = registerStatus;
    }

    public void changeRegisterStatus(RegisterStatus registerStatus){
        this.registerStatus = registerStatus;
    }
}
