package com.gongkademy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GongkademyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GongkademyApplication.class, args);
    }

}
