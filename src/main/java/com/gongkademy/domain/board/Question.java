package com.gongkademy.domain.board;

import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Question")
@Getter
public class Question extends Board{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lecture_id")
    private Lecture lecture;
}
