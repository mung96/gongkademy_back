package com.gongkademy.repository;

import com.gongkademy.domain.course.Play;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlayRepositoryImpl implements PlayRepository{

    private final EntityManager em;

    @Override
    public Long save(Play play) {
        if(play.getId() == null){
            em.persist(play);
        }else{
            em.merge(play);
        }
        return play.getId();
    }

    @Override
    public Optional<Play> findByMemberIdAndCourseIdByModifiedTime(Long memberId, Long courseId) {
        List<Play> playList = em.createQuery(
                                        "SELECT p FROM Play p WHERE p.member.id = :memberId AND p.lecture.course.id = :courseId ORDER BY p.updatedAt DESC",
                                        Play.class)
                                .setParameter("memberId", memberId)
                                .setParameter("courseId", courseId)
                                .getResultList();
        return playList.isEmpty() ? Optional.empty() : Optional.of(playList.get(0));
    }

    @Override
    public Optional<Play> findByMemberIdAndLectureId(Long memberId, Long lectureId) {
        List<Play> playList =  em.createQuery("SELECT p FROM Play p WHERE p.member.id = :memberId AND p.lecture.id = :lectureId ", Play.class)
                                 .setParameter("memberId", memberId)
                                 .setParameter("lectureId", lectureId)
                                 .getResultList();

        return playList.isEmpty() ? Optional.empty() : Optional.of(playList.get(0));
    }
}
