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
    public Play findByMemberIdAndCourseIdByModifiedTime(Long memberId, Long courseId) {
        return em.createQuery("SELECT p FROM Play p WHERE p.member.id = :memberId AND p.lecture.course.id = :courseId ORDER BY p.modifiedTime DESC", Play.class)
                 .setParameter("memberId", memberId)
                 .setParameter("courseId", courseId)
                .getResultList()
                 .getFirst();
    }

    @Override
    public Play findByMemberIdAndLectureId(Long memberId,Long lectureId) {
        return em.createQuery("SELECT p FROM Play p WHERE p.member.id = :memberId AND p.lecture.id = :lectureId ", Play.class)
                 .setParameter("memberId", memberId)
                 .setParameter("lectureId", lectureId)
                .getSingleResult();
    }
}
