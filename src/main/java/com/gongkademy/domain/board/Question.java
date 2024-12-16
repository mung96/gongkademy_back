package com.gongkademy.domain.board;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Question")
@Getter
public class Question extends Board{

}
