package com.gongkademy.repository;

import static com.gongkademy.domain.board.BoardCategory.QUESTION;

import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.service.dto.BoardItemDto;
import com.gongkademy.service.dto.BoardListResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NativeBoardRepository {

    private final EntityManager em;

    public BoardListResponse findAllByCategory(int page) {
            String countSql = "SELECT COUNT(*) FROM board WHERE board_category = 'QUESTION'";
            Query countQuery = em.createNativeQuery(countSql);
            long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        Long totalPage = (long) Math.ceil((double) totalCount / 20);

            String sql = "SELECT " +
                    "    q1_0.board_id, " +
                    "    q1_1.title, " +
                    "    q1_1.body, " +
                    "    DATE_FORMAT(q1_1.created_at, '%Y-%m-%d %H:%i:%s'), " + // 문자열로 변환
                    "    CAST(comment_count AS SIGNED), " + // int로 변환
                    "    q1_1.board_category, " +
                    "    c1_0.title, " +
                    "    l1_0.title " +
                    "FROM ( " +
                    "    SELECT " +
                    "        q1_0.board_id, " +
                    "        COUNT(c2_0.comment_id) AS comment_count " +
                    "    FROM question q1_0 " +
                    "    JOIN comment c2_0 ON c2_0.board_id = q1_0.board_id " +
                    "    GROUP BY q1_0.board_id " +
                    "    ORDER BY comment_count DESC " +
                    "    LIMIT " + (page - 1) * 20 + ", 20 " +
                    ") sub " +
                    "JOIN question q1_0 ON sub.board_id = q1_0.board_id " +
                    "JOIN board q1_1 ON q1_0.board_id = q1_1.board_id " +
                    "JOIN course c1_0 ON c1_0.course_id = q1_0.course_id " +
                    "JOIN lecture l1_0 ON l1_0.lecture_id = q1_0.lecture_id " +
                    "WHERE q1_1.board_category = 'QUESTION'";

            Query query = em.createNativeQuery(sql);
            List<Object[]> list = query.getResultList();
            List<BoardItemDto> boardList = new ArrayList<>();
            for (Object[] objects : list) {
                boardList.add(BoardItemDto.builder()
                                          .boardId(Long.parseLong(objects[0].toString()))
                                          .title(objects[1].toString())
                                          .body(objects[2].toString())
                                          .date(objects[3].toString())
                                          .commentCount(Integer.parseInt(objects[4].toString()))
                                          .boardCategory(objects[5].toString())
                                          .courseTitle(objects[6].toString())
                                          .lectureTitle(objects[7].toString())
                                          .build());
            }

        return BoardListResponse.builder().boardList(boardList).totalPage(totalPage).build();
    }
}
