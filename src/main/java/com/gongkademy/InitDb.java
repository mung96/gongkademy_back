package com.gongkademy;

import com.gongkademy.domain.Member;
import com.gongkademy.domain.Provider;
import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.Comment;
import com.gongkademy.domain.board.Worry;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb implements CommandLineRunner {

    private final InitService initService;

    @Override
    public void run(String... args) {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @RequiredArgsConstructor
    public static class InitService {

        private final EntityManager em;

        @Transactional
        public void dbInit1() {
            Member member = Member.builder()
                                  .nickname("익명")
                                  .email("aaa@gongkademy.com")
                                  .name("익명")
                                  .provider(Provider.NAVER)
                                  .providerId("NAVER")
                                  .build();
            em.persist(member);

            for(int i=0;i<1000;i++){
                Board board = Worry.builder()
                                   .title("제목제목제목"+i)
                                   .body("본문본문본문본문"+i)
                                   .member(member)
                                   .build();
                em.persist(board);
                for(int j=0;j<100;j++){
//                    String content, Member member, Board board
                    Comment comment = Comment.builder()
                                             .content(i+"번 게시글 댓글댓글댓글"+j)
                                             .member(member)
                                             .board(board).build();
                    em.persist(comment);
                }
            }


        }

        @Transactional
        public void dbInit2() {
            // 다른 초기화 로직
        }
    }
}
