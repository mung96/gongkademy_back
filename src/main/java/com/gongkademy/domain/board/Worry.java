package com.gongkademy.domain.board;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@DiscriminatorValue("Worry")
@Getter
public class Worry extends Board {

}
