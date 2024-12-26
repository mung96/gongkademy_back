package com.gongkademy.domain.board;

import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Worry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Worry extends Board {

    @Builder
    private Worry(String title, String body, Member member) {
        super(title,body,member);
    }
}
