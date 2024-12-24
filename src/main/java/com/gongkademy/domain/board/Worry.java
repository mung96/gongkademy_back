package com.gongkademy.domain.board;

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
public class Worry extends Board {

    @Builder
    protected Worry() {
    }
}
