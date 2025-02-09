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

    @Override
    public Long findLastLectureIdByCourseId(Long courseId) {
        return em.createQuery("SELECT MAX(l.id) FROM Lecture l JOIN l.course c WHERE c.id = :courseId",Long.class)
                .setParameter("courseId",courseId)
                .getSingleResult();
    }

    @Override
    public Long findPrevLectureIdByLectureId(Long lectureId) {
        return em.createQuery("SELECT MAX(l.id) FROM Lecture l WHERE l.lectureOrder < (SELECT l2.lectureOrder FROM Lecture l2 WHERE l2.id = :lectureId) AND l.course.id = (SELECT l3.course.id FROM Lecture l3 WHERE l3.id = :lectureId)", Long.class)
                 .setParameter("lectureId", lectureId)
                 .getSingleResult();
    }

    @Override
    public Long findNextLectureIdByLectureId(Long lectureId) {
        return em.createQuery("SELECT MIN(l.id) FROM Lecture l WHERE l.lectureOrder > (SELECT l2.lectureOrder FROM Lecture l2 WHERE l2.id = :lectureId) AND l.course.id = (SELECT l3.course.id FROM Lecture l3 WHERE l3.id = :lectureId)", Long.class)
                 .setParameter("lectureId", lectureId)
                 .getSingleResult();
    }

}
