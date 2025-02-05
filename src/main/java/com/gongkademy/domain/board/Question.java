package com.gongkademy.domain.board;

import com.gongkademy.domain.course.Lecture;
import com.gongkademy.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Board{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="lecture_id")
    private Lecture lecture;

    @Builder
    private Question(String title, String body,Member member,Lecture lecture) {
        super(title,body,member,BoardCategory.QUESTION);
        this.lecture = lecture;
    }

    //수정 메소드
    public void changeLecture(Lecture lecture){
        this.lecture = lecture;
    }
}
