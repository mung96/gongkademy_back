package com.gongkademy.domain.course;

import com.gongkademy.domain.BaseTimeEntity;
import com.gongkademy.domain.Member;
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
public class Play extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="play_id")
    private Long id;

    @Column(nullable = false)
    private int lastPlayedTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlayStatus playStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="lecture_id")
    private Lecture lecture;

    @Builder
    private Play(int lastPlayedTime, Member member, Lecture lecture, PlayStatus playStatus) {
        this.lastPlayedTime = lastPlayedTime;
        this.member = member;
        this.lecture = lecture;
        this.playStatus = playStatus;
    }

    public void changeLastPlayedTime(int lastPlayedTime){
        this.lastPlayedTime = lastPlayedTime;
    }
    public void changePlayStatus(PlayStatus playStatus){
        this.playStatus = playStatus;
    }
}
