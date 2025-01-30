package com.gongkademy.repository;

import com.gongkademy.domain.course.Lecture;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository{

    private final EntityManager em;

    @Override
    //courseId로 강의 목록 조회
    public List<Lecture> findLecturesByCourseId(Long courseId) {
        return em.createQuery("SELECT l FROM Lecture l JOIN l.course c Where c.id = :courseId",Lecture.class)
                .setParameter("courseId",courseId)
                .getResultList();
    }

    @Override
    public Optional<Lecture> findById(Long lectureId) {
        return Optional.ofNullable(em.find(Lecture.class, lectureId));
    }
}
