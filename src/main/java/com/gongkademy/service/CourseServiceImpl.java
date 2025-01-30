package com.gongkademy.service;


import static com.gongkademy.exception.ErrorCode.COURSE_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.REGISTER_NOT_FOUND;

import com.gongkademy.domain.course.Course;
import com.gongkademy.domain.course.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.course.Play;
import com.gongkademy.domain.course.Register;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.CourseRepository;
import com.gongkademy.repository.LectureRepository;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.repository.PlayRepository;
import com.gongkademy.repository.RegisterRepository;
import com.gongkademy.service.dto.CourseDetailResponse;
import com.gongkademy.service.dto.CourseItemDto;
import com.gongkademy.service.dto.CourseListResponse;
import com.gongkademy.service.dto.LectureDetailResponse;
import com.gongkademy.service.dto.LectureItemDto;
import com.gongkademy.service.dto.LectureListResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final RegisterRepository registerRepository;
    private final LectureRepository lectureRepository;
    private final PlayRepository playRepository;

    @Override
    public CourseListResponse findCourse() {
        List<CourseItemDto> courseList = courseRepository.findCourse()
                                                         .stream()
                                                         .map(course -> CourseItemDto.builder()
                                                                                    .courseId(course.getId())
                                                                                    .title(course.getTitle())
                                                                                    .thumbnail(course.getThumbnail())
                                                                                    .build())
                                                         .toList();
        return CourseListResponse.builder().courseList(courseList).build();
    }

    //수강 신청
    @Override
    public Long registerCourse(Long memberId, Long courseId) {
        //가입한 회원인지 확인
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(()-> new CustomException(MEMBER_NOT_FOUND));
        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(()-> new CustomException(COURSE_NOT_FOUND));

        //이미 수강 신청한 강좌면 예외
        registerRepository.findByMemberIdAndCourseId(memberId,courseId)
                          .ifPresent(register -> {
                              throw new CustomException(ErrorCode.REGISTERED_COURSE);
                          });

        Register register = Register.builder().member(member).course(course).build();
        registerRepository.save(register);

        return register.getId();
    }

    //수강 취소
    @Override
    public Long dropCourse(Long memberId, Long courseId) {
        Register register = registerRepository.findByMemberIdAndCourseId(memberId, courseId)
                                              .orElseThrow(() -> new CustomException(REGISTER_NOT_FOUND));
        registerRepository.delete(register);
        return register.getId();
    }

    //강좌 상세 조회
    @Override
    @Transactional(readOnly = true)
    public CourseDetailResponse findCourseDetail(Long memberId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(()-> new CustomException(COURSE_NOT_FOUND));
        List<Lecture> lectureList = lectureRepository.findLecturesByCourseId(courseId);

        //수강 중인지 확인\
        boolean isRegister = false;
        if(memberId != null){
            isRegister = registerRepository.findByMemberIdAndCourseId(memberId, courseId).isPresent();
        }

        //총 강좌 시간
        int courseTime = 0;
        for(Lecture lecture:lectureList){
            courseTime += lecture.getRuntime();
        }

        return CourseDetailResponse.builder()
                                    .title(course.getTitle())
                                    .thumbnail(course.getThumbnail())
                                    .courseNote(course.getCourseNote())
                                    .courseTime(courseTime)
                                    .isRegister(isRegister)
                                    .build();
    }

    @Override
    @Transactional(readOnly = true)
    public LectureListResponse findLectureList(Long memberId, Long courseId) {
        List<Lecture> lectureList = lectureRepository.findLecturesByCourseId(courseId);

        //수강 중인지 확인
        boolean isRegister = false;
        if(memberId != null){
            isRegister = registerRepository.findByMemberIdAndCourseId(memberId, courseId).isPresent();
        }

        //LectureListResponse 생성
        List<LectureItemDto> lectureItemDtoList = new ArrayList<>();
        for(Lecture lecture:lectureList){
            boolean isComplete = false;
            if(memberId != null){
                isComplete = playRepository.findByMemberIdAndLectureId(memberId, lecture.getId())
                                           .map(play -> play.getLastPlayedTime() > play.getLecture().getRuntime() - 10)
                                           .orElse(false);
            }

           lectureItemDtoList.add(LectureItemDto.builder()
                                                .lectureId(lecture.getId())
                                                .title(lecture.getTitle())
                                                .runtime(lecture.getRuntime())
                                                .isComplete(isComplete)
                                                .build());
        }
        return LectureListResponse.builder()
                                  .lectureList(lectureItemDtoList)
                                  .isRegister(isRegister)
                                  .build();
    }

    @Override
    @Transactional(readOnly = true)
    public LectureDetailResponse findLastLecture(Long memberId, Long courseId) {
        //수강 중인지 확인
        registerRepository.findByMemberIdAndCourseId(memberId, courseId).orElseThrow(()->new CustomException(REGISTER_NOT_FOUND));

        Play lastPlayLecture = playRepository.findByMemberIdAndCourseIdByModifiedTime(memberId,courseId).orElse(null);

        if(lastPlayLecture == null){
            Lecture firstLecture = lectureRepository.findLecturesByCourseId(courseId).get(0);
            return LectureDetailResponse.builder()
                                        .title(firstLecture.getTitle())
                                        .url(firstLecture.getUrl())
                                        .lastPlayedTime(0).build();
        }

        return LectureDetailResponse.builder()
                                    .title(lastPlayLecture.getLecture().getTitle())
                                    .url(lastPlayLecture.getLecture().getUrl())
                                    .lastPlayedTime(lastPlayLecture.getLastPlayedTime())
                                    .build();
    }

    @Override
    public Long saveLastPlayedTime(Long memberId, Long lectureId, int lastPlayedTime) {
        Play play = playRepository.findByMemberIdAndLectureId(memberId, lectureId).orElse(null);
        if(play == null){
            Member member = memberRepository.findById(memberId)
                                            .orElseThrow(()-> new CustomException(MEMBER_NOT_FOUND));
            Lecture lecture = lectureRepository.findById(lectureId)
                                              .orElseThrow(()-> new CustomException(ErrorCode.LECTURE_NOT_FOUND));
            play = Play.builder().lastPlayedTime(lastPlayedTime).member(member).lecture(lecture).build();
            playRepository.save(play);
        }else{
            play.changeLastPlayedTime(lastPlayedTime);
        }
        return play.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public LectureDetailResponse findLectureDetail(Long memberId, Long lectureId) {
        //수강 중인지 확인
        Long courseId = lectureRepository.findById(lectureId).get().getCourse().getId();
        registerRepository.findByMemberIdAndCourseId(memberId, courseId).orElseThrow(()->new CustomException(REGISTER_NOT_FOUND));

        Lecture lecture = lectureRepository.findById(lectureId)
                                          .orElseThrow(()-> new CustomException(ErrorCode.LECTURE_NOT_FOUND));
        Play play = playRepository.findByMemberIdAndLectureId(memberId, lectureId).orElse(null);

        return LectureDetailResponse.builder()
                                    .title(lecture.getTitle())
                                    .url(lecture.getUrl())
                                    .lastPlayedTime(play == null ? 0 : play.getLastPlayedTime())
                                    .build();
    }
}
