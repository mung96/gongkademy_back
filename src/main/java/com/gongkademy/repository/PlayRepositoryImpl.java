package com.gongkademy.repository;

import com.gongkademy.domain.Play;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlayRepositoryImpl implements PlayRepository{

    private final EntityManager em;

    @Override
    public Long save(Play play) {
        em.persist(play);
        return play.getId();
    }

    @Override
    public Play findByCourseIdByModifiedTime(Long courseId) {
        return null;
    }

    @Override
    public Play findByLectureId(Long playId) {
        return null;
    }
}
