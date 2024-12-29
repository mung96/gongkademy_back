package com.gongkademy.service;


import static com.gongkademy.exception.ErrorCode.COURSE_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.gongkademy.domain.Course;
import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.Register;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.CourseRepository;
import com.gongkademy.repository.LectureRepository;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.repository.RegisterRepository;
import com.gongkademy.service.dto.CourseDetailResponse;
import com.gongkademy.service.dto.LectureDetailResponse;
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
                                              .orElseThrow(() -> new CustomException(ErrorCode.REGISTER_NOT_FOUND));
        registerRepository.delete(register);
        return register.getId();
    }

    //강좌 상세 조회
    @Override
    public CourseDetailResponse findCourseDetail(Long memberId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                                        .orElseThrow(()-> new CustomException(COURSE_NOT_FOUND));
        List<Lecture> lectureList = lectureRepository.findLecturesByCourseId(courseId);

        //수강 중인지 확인
        boolean isRegister = registerRepository.findByMemberIdAndCourseId(memberId, courseId)
                                               .isPresent();

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
    public List<LectureDetailResponse> findLectureList(Long courseId) {
        return List.of();
    }

}
