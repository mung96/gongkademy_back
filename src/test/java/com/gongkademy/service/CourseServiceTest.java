package com.gongkademy.service;

import static com.gongkademy.exception.ErrorCode.REGISTERED_COURSE;
import static com.gongkademy.exception.ErrorCode.REGISTER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Provider;
import com.gongkademy.domain.course.Course;
import com.gongkademy.domain.course.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.course.Play;
import com.gongkademy.domain.course.PlayStatus;
import com.gongkademy.domain.course.Register;
import com.gongkademy.exception.CustomException;
import com.gongkademy.repository.RegisterRepository;
import com.gongkademy.service.dto.CourseDetailResponse;
import com.gongkademy.service.dto.LectureDetailResponse;
import com.gongkademy.service.dto.LectureListResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired CourseService courseService;
    @Autowired EntityManager em;
    @Autowired RegisterRepository registerRepository;

    Member member1;
    Member member2;
    Course course;

    @BeforeEach
    void setUp() {
        member1 = createMember("유저1", "aaa@naver.com","aaa","user1");
        member2 = createMember("유저2", "bbb@naver.com","bbb","user2");
        course = createCourse("재료역학", "aaa");
    }

    private Member createMember(String nickname, String email, String providerId,String name) {
        Member member = Member.builder().name(name).provider(Provider.NAVER).providerId(providerId).nickname(nickname).email(email).build();
        em.persist(member);
        return member;
    }

    private Course createCourse(String title, String thumbnail) {
        Course course = Course.builder().title(title).thumbnail(thumbnail).build();
        em.persist(course);
        return course;
    }

    private Lecture createLecture(String title, int order, String url, int runtime, Course course) {
        Lecture lecture = Lecture.builder()
                                 .title(title)
                                 .lectureOrder(order)
                                 .url(url)
                                 .runtime(runtime)
                                 .course(course)
                                 .build();
        em.persist(lecture);
        return lecture;
    }

    private Play createPlay(int lastPlayedTime, Member member, Lecture lecture) {
        Play play = Play.builder()
                        .lastPlayedTime(lastPlayedTime)
                        .member(member)
                        .lecture(lecture)
                        .playStatus(PlayStatus.NOT_PLAY)
                        .build();
        em.persist(play);
        return play;
    }

    @Test
    void 수강_신청() {
        Long registerCourseId = courseService.registerCourse(member1.getId(), course.getId());
        Register findRegister = em.find(Register.class, registerCourseId);

        CustomException e = assertThrows(CustomException.class, () ->
                courseService.registerCourse(member1.getId(), course.getId())
        );

        assertEquals(findRegister.getId(), registerCourseId);
        assertEquals(REGISTERED_COURSE, e.getErrorCode());
    }

    @Test
    void 수강_취소() {
        Long registerId = courseService.registerCourse(member1.getId(), course.getId());

        Long deletedId = courseService.dropCourse(member1.getId(), course.getId());
        CustomException e = assertThrows(CustomException.class, () ->
                courseService.dropCourse(member2.getId(), course.getId())
        );

        assertEquals(registerId, deletedId);
        assertEquals(REGISTER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 강좌_상세_조회() {
        Lecture lecture1 = createLecture("재료역학이란", 1, "url1", 15 * 60, course);
        Lecture lecture2 = createLecture("응력이란", 2, "url2", 16 * 60, course);

        courseService.registerCourse(member1.getId(), course.getId());

        CourseDetailResponse registered = courseService.findCourseDetail(member1.getId(), course.getId());
        CourseDetailResponse unregistered = courseService.findCourseDetail(member2.getId(), course.getId());
        CourseDetailResponse anonymous = courseService.findCourseDetail(null, course.getId());

        assertEquals(course.getTitle(), registered.getTitle());
        assertTrue(registered.getIsRegister());
        assertEquals(lecture1.getRuntime() + lecture2.getRuntime(), registered.getCourseTime());

        assertFalse(unregistered.getIsRegister());
        assertFalse(anonymous.getIsRegister());
    }

    @Test
    void 강좌의_강의_목록_조회() {
        Lecture lecture1 = createLecture("재료역학이란", 1, "url1", 15 * 60, course);
        Lecture lecture2 = createLecture("응력이란", 2, "url2", 16 * 60, course);
        createPlay(lecture1.getRuntime() - 30, member1, lecture1);
        createPlay(lecture2.getRuntime() - 9, member1, lecture2);

        courseService.registerCourse(member1.getId(), course.getId());

        LectureListResponse regList = courseService.findLectureList(member1.getId(), course.getId());
        LectureListResponse unregList = courseService.findLectureList(member2.getId(), course.getId());
        LectureListResponse anonList = courseService.findLectureList(null, course.getId());

        assertEquals(2, regList.getLectureList().size());
        assertTrue(regList.getIsRegister());
        assertEquals(2, unregList.getLectureList().size());
        assertEquals(2, anonList.getLectureList().size());
    }

    @Test
    void 강좌의_최근_수강강의_조회() {
        Lecture lecture1 = createLecture("재료역학이란", 1, "url1", 15 * 60, course);
        Lecture lecture2 = createLecture("응력이란", 2, "url2", 16 * 60, course);
        createPlay(lecture1.getRuntime() - 30, member1, lecture2);

        courseService.registerCourse(member1.getId(), course.getId());
        courseService.registerCourse(member2.getId(), course.getId());

        LectureDetailResponse lastByM1 = courseService.findLastLecture(member1.getId(), course.getId());
        LectureDetailResponse lastByM2 = courseService.findLastLecture(member2.getId(), course.getId());

        assertEquals(lecture2.getTitle(), lastByM1.getTitle());
//        assertEquals(lecture1.getTitle(), lastByM2.getTitle());
    }

    @Test
    void 강의_상세_조회() {
        Lecture lecture1 = createLecture("재료역학이란", 1, "url1", 15 * 60, course);
        Lecture lecture2 = createLecture("응력이란", 2, "url2", 16 * 60, course);
        Play play1 = createPlay(lecture1.getRuntime() - 30, member1, lecture1);

        courseService.registerCourse(member1.getId(), course.getId());

        LectureDetailResponse detail1 = courseService.findLectureDetail(member1.getId(), lecture1.getId());
        LectureDetailResponse detail2 = courseService.findLectureDetail(member1.getId(), lecture2.getId());

        assertEquals(play1.getLastPlayedTime(), detail1.getLastPlayedTime());
        assertEquals(0, detail2.getLastPlayedTime());
    }

    @Test
    void 수강_기록_남기기() {
        Lecture lecture1 = createLecture("재료역학이란", 1, "url1", 15 * 60, course);
        Lecture lecture2 = createLecture("응력이란", 2, "url2", 16 * 60, course);
        createPlay(10, member1, lecture1);

        courseService.registerCourse(member1.getId(), course.getId());

        courseService.saveLastPlayedTime(member1.getId(), lecture1.getId(), 100);
        courseService.saveLastPlayedTime(member1.getId(), lecture2.getId(), 200);

        LectureDetailResponse updated = courseService.findLectureDetail(member1.getId(), lecture1.getId());
        LectureDetailResponse newly = courseService.findLectureDetail(member1.getId(), lecture2.getId());

        assertEquals(100, updated.getLastPlayedTime());
        assertEquals(200, newly.getLastPlayedTime());
    }
}
