package com.gongkademy.service;


import static com.gongkademy.exception.ErrorCode.COURSE_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.REGISTER_NOT_FOUND;

import com.gongkademy.domain.course.Course;
import com.gongkademy.domain.course.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.course.Play;
import com.gongkademy.domain.course.PlayStatus;
import com.gongkademy.domain.course.Register;
import com.gongkademy.domain.course.RegisterStatus;
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
import com.gongkademy.utils.FileUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
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
    private final FileUtil fileUtil;

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

        Register register = Register.builder().member(member).course(course).registerStatus(RegisterStatus.IN_PROGRESS).build();
        registerRepository.save(register);

        //강좌의 수강 전체 다넣기
        List<Lecture> lectureList = lectureRepository.findLecturesByCourseId(courseId);
        for(Lecture lecture:lectureList){
            Play play = Play.builder().lastPlayedTime(0).member(member).lecture(lecture).playStatus(PlayStatus.NOT_PLAY).build();
            playRepository.save(play);
        }

        return register.getId();
    }

    //수강 취소
    @Override
    //TODO: soft delete로 변경
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
            Play play = null;
            if(memberId != null){
                play = playRepository.findByMemberIdAndLectureId(memberId, lecture.getId()).orElse(null);
            }

           lectureItemDtoList.add(LectureItemDto.builder()
                                                .lectureId(lecture.getId())
                                                .title(lecture.getTitle())
                                                .runtime(lecture.getRuntime())
                                                .playStatus(play ==  null ? null : play.getPlayStatus())
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
        //TODO: 수정필요
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(()-> new CustomException(MEMBER_NOT_FOUND));
        Lecture lecture = lectureRepository.findById(lectureId)
                                           .orElseThrow(()-> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

        Play play = playRepository.findByMemberIdAndLectureId(memberId, lectureId) .orElseThrow(()-> new CustomException(ErrorCode.REGISTER_NOT_FOUND));

        //lastplaytime이 90% 이상이면 수강완료로 변경, 아니면 수강중으로 변경,
        boolean isPlayComplete = lastPlayedTime > lecture.getRuntime() * 0.9;
        play.changeLastPlayedTime(lastPlayedTime);

        if(isPlayComplete){
            play.changePlayStatus(PlayStatus.COMPLETED);
            //모든 play가 완료되었는지 확인
            boolean isCourseComplete = playRepository.findByMemberIdAndCourseIdByModifiedTime(memberId, lecture.getCourse().getId())
                                                     .stream()
                                                     .allMatch(play1 -> play1.getPlayStatus() == PlayStatus.COMPLETED);
            if(isCourseComplete){
                Register register = registerRepository.findByMemberIdAndCourseId(memberId, lecture.getCourse().getId())
                                                      .orElseThrow(()-> new CustomException(REGISTER_NOT_FOUND));
                register.changeRegisterStatus(RegisterStatus.COMPLETED);
                registerRepository.save(register);
            }
        }else{
            play.changePlayStatus(PlayStatus.IN_PROGRESS);
        }

        playRepository.save(play);

        return play.getId();
    }

    //강좌자료 다운로드
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

    public UrlResource findCourseNote(Long courseId) {
        Course course = courseRepository.findById(courseId)
                            .orElseThrow(()-> new CustomException(COURSE_NOT_FOUND));

        return fileUtil.getCourseNoteResource(course.getCourseNote());
    }
}
