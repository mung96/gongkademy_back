package com.gongkademy.repository;

import com.gongkademy.domain.Lecture;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository{

    private final EntityManager em;

    @Override
    //courseId로 강의 목록 조회
    public List<Lecture> findLecturesByCourseId(Long courseId) {
        return em.createQuery("SELECT l FROM Lecture l JOIN Course c Where c.id = :courseId",Lecture.class)
                .setParameter("courseId",courseId)
                .getResultList();
    }

    @Override
    public Lecture findById(Long lectureId) {
        return em.find(Lecture.class,lectureId);
    }
}
