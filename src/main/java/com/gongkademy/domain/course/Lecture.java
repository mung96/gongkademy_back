package com.gongkademy.domain.course;

import com.gongkademy.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lecture_id")
    private Long id;

    @Column(nullable = false,length = 100)
    private String title;

    @Column(nullable = false)
    //강의 시간 : 초 단위로 기록
    private int runtime;

    @Column(nullable = false,unique = true)
    //TODO: 강의 순서 unique 테스트코드 작성
    private int lectureOrder;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="course_id")
    private Course course;

    @Builder
    private Lecture(String title, int runtime, int lectureOrder, String url, Course course) {
        this.title = title;
        this.runtime = runtime;
        this.lectureOrder = lectureOrder;
        this.url = url;
        this.course = course;
    }
}
