package com.gongkademy.domain.board;

import com.gongkademy.domain.BaseSoftDeleteAndTimeEntity;
import com.gongkademy.domain.BaseTimeEntity;
import com.gongkademy.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="DTYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Board extends BaseSoftDeleteAndTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

//    @Lob
    @Column(nullable = false, length = 10_000,columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="member_id")
    private Member member;

    protected Board(String title, String body, Member member,BoardCategory boardCategory) {
        this.title = title;
        this.body = body;
        this.member = member;
        this.boardCategory = boardCategory;
    }

    //수정 메소드
    public void changeTitle(String title){
        this.title = title;
    }
    public void changeBody(String body){
        this.body = body;
    }

}
