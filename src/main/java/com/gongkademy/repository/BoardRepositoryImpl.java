package com.gongkademy.repository;

import static com.gongkademy.domain.board.BoardCategory.QUESTION;
import static com.gongkademy.domain.board.BoardCategory.WORRY;

import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.domain.board.Question;
import com.gongkademy.domain.course.Lecture;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class BoardRepositoryImpl implements BoardRepository{

    private final EntityManager em;

//    @Override
//    public List<Board> findAllByCategory(BoardCategory category, int page, BoardCriteria boardCriteria) {
//        if (category == QUESTION) {
//            if(boardCriteria == BoardCriteria.CREATED_AT){
//                return em.createQuery("SELECT q FROM Question q JOIN FETCH q.course JOIN FETCH q.lecture WHERE q.isDeleted = false ORDER BY q.createdAt DESC", Board.class)
//                         .setFirstResult((page - 1) * 20)
//                         .setMaxResults(20)
//                         .getResultList();
//            }else if(boardCriteria == BoardCriteria.COMMENT_CNT){
//
//                return em.createQuery("SELECT b FROM Comment c JOIN c.board b GROUP BY b.id ORDER BY COUNT(c) DESC", Board.class)
//                         .setFirstResult((page - 1) * 20)
//                         .setMaxResults(20)
//                         .getResultList();
//            }
//
//        } else if (category == WORRY) {
//            return em.createQuery("SELECT w FROM Worry w JOIN FETCH w.member WHERE w.isDeleted = false ORDER BY w.createdAt DESC", Board.class)
//                     .setFirstResult((page - 1) * 20)
//                     .setMaxResults(20)
//                     .getResultList();
//        }
//        return List.of();
//    }
    @Override
    public List<Board> findBoardByKeyword(BoardCategory boardCategory, String keyword, int page) {
        // category 내에서 board의 title과 body에 keyword가 포함된 board 중 해당 page를 반환한다.
        if (boardCategory == BoardCategory.QUESTION) {
            return em.createQuery("SELECT q FROM Question q JOIN FETCH q.course JOIN FETCH q.lecture WHERE q.boardCategory = :boardCategory AND (q.title LIKE :keyword OR q.body LIKE :keyword) AND q.isDeleted = false ORDER BY q.createdAt DESC", Board.class)
                     .setParameter("boardCategory", boardCategory)
                     .setParameter("keyword", "%" + keyword + "%")
                     .setFirstResult((page - 1) * 20)
                     .setMaxResults(20)
                     .getResultList();
        } else if (boardCategory == BoardCategory.WORRY) {
            return em.createQuery("SELECT w FROM Worry w JOIN FETCH w.member WHERE w.boardCategory = :boardCategory AND (w.title LIKE :keyword OR w.body LIKE :keyword) AND w.isDeleted = false ORDER BY w.createdAt DESC", Board.class)
                     .setParameter("boardCategory", boardCategory)
                     .setParameter("keyword", "%" + keyword + "%")
                     .setFirstResult((page - 1) * 20)
                     .setMaxResults(20)
                     .getResultList();
        }
        return List.of();
    }

    @Override
    public List<Board> findAllByCategory(BoardCategory category, int page, BoardCriteria boardCriteria) {
        if(category == QUESTION){
            if(boardCriteria == BoardCriteria.CREATED_AT){
            return em.createQuery("SELECT q FROM Question q WHERE q.isDeleted = false ORDER BY q.createdAt DESC ",Board.class)
                    .setFirstResult((page-1)*20)
                    .setMaxResults(20)
                     .getResultList();
            } else if(boardCriteria == BoardCriteria.COMMENT_CNT){
//                log.info("댓글 순 쿼리 실행 + 일반쿼리");
//                return em.createQuery("SELECT q FROM Question q JOIN Comment c ON c.board.id = q.id WHERE q.boardCategory = 'QUESTION' GROUP BY q.id ORDER BY COUNT(c) DESC", Board.class)
//                         .setFirstResult((page - 1) * 20)
//                         .setMaxResults(20)
//                         .getResultList();

//                log.info("댓글 순 쿼리 실행 + fetch쿼리");
//                return em.createQuery("SELECT q FROM Question q JOIN Comment c ON c.board.id = q.id  WHERE q.boardCategory = 'QUESTION' GROUP BY q.id ORDER BY COUNT(c) DESC", Board.class)
//                         .setFirstResult((page - 1) * 20)
//                         .setMaxResults(20)
//                         .getResultList();

                log.info("댓글 순 쿼리 실행 + fetch쿼리");
                return em.createQuery("SELECT q FROM Question q JOIN FETCH q.course JOIN FETCH q.lecture JOIN Comment c ON c.board.id = q.id WHERE q.boardCategory = 'QUESTION' GROUP BY q.id ORDER BY COUNT(c) DESC", Board.class)
                         .setFirstResult((page - 1) * 20)
                         .setMaxResults(20)
                         .getResultList();

//                log.info("댓글 순 쿼리 실행 + 서브쿼리");
//                return em.createQuery(
//                           "SELECT q FROM Question q " +
//                                   "JOIN FETCH q.course c " +
//                                   "JOIN FETCH q.lecture l " +
//                                   "WHERE q.boardCategory = :boardCategory " +
//                                   "ORDER BY (SELECT COUNT(cmt.id) FROM Comment cmt WHERE cmt.board = q) DESC",
//                           Board.class
//                           )
//                         .setParameter("boardCategory", BoardCategory.QUESTION)
//                           .setMaxResults(20)
//                           .getResultList();
            }

        }else if(category == WORRY){
            if(boardCriteria == BoardCriteria.CREATED_AT) {
                return em.createQuery("SELECT w FROM Worry w WHERE w.isDeleted = false ORDER BY w.createdAt DESC",
                                      Board.class)
                         .setFirstResult((page - 1) * 20)
                         .setMaxResults(20)
                         .getResultList();
            }
             else if(boardCriteria == BoardCriteria.COMMENT_CNT){
                return em.createQuery("SELECT b FROM Comment c JOIN c.board b GROUP BY b.id ORDER BY COUNT(c) DESC", Board.class)
                         .setFirstResult((page - 1) * 20)
                         .setMaxResults(20)
                         .getResultList();
            }
        }

        return List.of();
    }
//    @Override
//    public List<Board> findBoardByKeyword(BoardCategory boardCategory, String keyword,int page) {
//        // category내에서 board의 title과 body에 keyword가 포함된 board중 해당 page를 반환한다.
//        return em.createQuery("SELECT b FROM Board b WHERE b.boardCategory = :boardCategory AND (b.title LIKE :keyword OR b.body LIKE :keyword) AND b.isDeleted = false ORDER BY b.createdAt DESC",Board.class)
//          .setParameter("boardCategory",boardCategory)
//          .setParameter("keyword","%"+keyword+"%")
//          .setFirstResult((page-1)*20)
//          .setMaxResults(20)
//          .getResultList();
//
//    }
    @Override
    public Long countTotalPageBoardByKeyword(BoardCategory category, String keyword) {
        // category내에서 board의 title과 body에 keyword가 포함된 board수를 반환한다.
        Long totalBoard =  em.createQuery("SELECT count(b) FROM Board b WHERE b.boardCategory = :boardCategory AND (b.title LIKE :keyword OR b.body LIKE :keyword) AND b.isDeleted = false",Long.class)
          .setParameter("boardCategory",category)
          .setParameter("keyword","%"+keyword+"%")
          .getSingleResult();

        return ((totalBoard-1)/20)+1;
    }

    @Override
    public List<Question> findQuestionByKeyword(String keyword,Long courseId, Long lectureId, int page) {
        //lecutreId가 null이면 courseId에 해당하는 Question중 keyword가 포함된 Question을 반환한다. lecutreId가 있다면 lectureId에 해당하는 Question중 keyword가 포함된 Question을 반환한다. 단 해당 page만 반환한다
        if(lectureId == null){
            return em.createQuery("SELECT q FROM Question q WHERE q.course.id = :courseId AND (q.title LIKE :keyword OR q.body LIKE :keyword) AND q.isDeleted = false ORDER BY q.createdAt DESC ",Question.class)
                     .setParameter("courseId",courseId)
                     .setParameter("keyword","%"+keyword+"%")
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }else{
            return em.createQuery("SELECT q FROM Question q WHERE q.course.id = :courseId AND q.lecture.id = :lectureId AND (q.title LIKE :keyword OR q.body LIKE :keyword) AND q.isDeleted = false ORDER BY q.createdAt DESC ",Question.class)
                     .setParameter("courseId",courseId)
                     .setParameter("lectureId",lectureId)
                     .setParameter("keyword","%"+keyword+"%")
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }
    }


    @Override
    public Long countTotalPageQuestionByKeyword(String keyword, Long courseId, Long lectureId) {
        //lecutreId가 null이면 courseId에 해당하는 Question중 keyword가 포함된 Question의 수를 반환한다..
        Long totalBoard = 0L;
        if(lectureId == null){
             totalBoard = em.createQuery("SELECT count(q) FROM Question q WHERE q.course.id = :courseId AND (q.title LIKE :keyword OR q.body LIKE :keyword) AND q.isDeleted = false ",Long.class)
                     .setParameter("courseId",courseId)
                     .setParameter("keyword","%"+keyword+"%")
                     .getSingleResult();
        }else{
//            lecutreId가 있다면 lectureId에 해당하는 Question중 keyword가 포함된 Question의 수를 반환한다.
             totalBoard =  em.createQuery("SELECT count(q) FROM Question q WHERE q.course.id = :courseId AND q.lecture.id = :lectureId AND (q.title LIKE :keyword OR q.body LIKE :keyword) AND q.isDeleted = false ",Long.class)
                     .setParameter("courseId",courseId)
                     .setParameter("lectureId",lectureId)
                     .setParameter("keyword","%"+keyword+"%")
                     .getSingleResult();
        }

        return ((totalBoard-1)/20)+1;
    }

    @Override
    public List<Question> findAllQuestionByCourseIdAndLectureId(Long courseId, Long lectureId, int page) {
        if(lectureId == null){
            return em.createQuery("SELECT q FROM Question q WHERE q.course.id = :courseId AND q.isDeleted = false ORDER BY q.createdAt DESC ",Question.class)
                     .setParameter("courseId",courseId)
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }else{
            return em.createQuery("SELECT q FROM Question q WHERE q.course.id = :courseId AND q.lecture.id = :lectureId AND q.isDeleted = false ORDER BY q.createdAt DESC ",Question.class)
                     .setParameter("courseId",courseId)
                     .setParameter("lectureId",lectureId)
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }
    }
    @Override
    public Long countAllQuestionByCourseIdAndLectureId(Long courseId, Long lectureId) {
        Long totalBoard = 0L;
        if(lectureId == null){
            totalBoard = em.createQuery("SELECT count(q) FROM Question q WHERE q.course.id = :courseId AND q.isDeleted = false  ",Long.class)
                     .setParameter("courseId",courseId)
                     .getSingleResult();
        }else{
            totalBoard = em.createQuery("SELECT count(q) FROM Question q WHERE q.course.id = :courseId AND q.lecture.id = :lectureId AND q.isDeleted = false  ",Long.class)
                           .setParameter("courseId",courseId)
                           .setParameter("lectureId",lectureId)
                           .getSingleResult();
        }
        return ((totalBoard-1)/20)+1;
    }

    @Override
    public List<Board> findAllByCategoryAndMemberId(Long memberId, BoardCategory category, int page,
                                                    BoardCriteria boardCriteria) {
        if(category == QUESTION){
            return em.createQuery("SELECT q FROM Question q WHERE q.member.id = :memberId And q.isDeleted = false  ORDER BY q.createdAt ASC",Board.class)
                    .setParameter("memberId",memberId)
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }else if(category == WORRY){
            return em.createQuery("SELECT w FROM Worry w WHERE w.member.id = :memberId And w.isDeleted = false  ORDER BY w.createdAt ASC",Board.class)
                     .setParameter("memberId",memberId)
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }
        return List.of();
    }

    @Override
    public Long countAllByCategoryAndMemberId(Long memberId, BoardCategory category) {
        Long totalBoard = 0L;
        if(category == QUESTION){
            totalBoard = em.createQuery("SELECT count(q) FROM Question q WHERE q.member.id = :memberId AND q.isDeleted = false",Long.class)
                           .setParameter("memberId",memberId)
                           .getSingleResult();
        }else if(category == WORRY){
            totalBoard = em.createQuery("SELECT count(w) FROM Worry w WHERE w.member.id = :memberId AND w.isDeleted = false",Long.class)
                           .setParameter("memberId",memberId)
                           .getSingleResult();
        }
        return ((totalBoard-1)/20)+1;
    }

    @Override
    public Long countAllByCategory(BoardCategory category) {
        Long totalBoard = 0L;
        if(category == QUESTION){
            totalBoard = em.createQuery("SELECT count(q) FROM Question q WHERE q.isDeleted = false",Long.class)
                      .getSingleResult();
        }else if(category == WORRY){
            totalBoard = em.createQuery("SELECT count(w) FROM Worry w WHERE w.isDeleted = false",Long.class)
                      .getSingleResult();
        }
        return ((totalBoard-1)/20)+1;
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        List<Board> result = em.createQuery("SELECT b FROM Board b WHERE b.id = :boardId AND b.isDeleted = false", Board.class)
                               .setParameter("boardId", boardId)
                               .getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    @Override
    public List<Board> findAllByMemberId(Long memberId) {
        return em.createQuery("SELECT b FROM Board b WHERE b.member.id = :memberId AND b.isDeleted = false",Board.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    @Override
    public Long save(Board board) {
        em.persist(board);
        return board.getId();
    }

    @Override
    public Long update(Long boardId, Board board) {
        Board findBoard = em.find(Board.class,boardId);

        if(findBoard == null) throw new IllegalStateException("존재하지 않는 게시글입니다.");

        findBoard.changeTitle(board.getTitle());
        findBoard.changeBody(board.getBody());

        if(findBoard instanceof Question){
            ((Question) findBoard).changeLecture(((Question) board).getLecture());
        }
        return findBoard.getId();
    }

    @Override
    public Long delete(Board board) {
        board.delete();
        em.persist(board);
        return board.getId();
    }
}
