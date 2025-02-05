package com.gongkademy.utils;

import java.net.MalformedURLException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class FileUtil {
//    @Value("${file.dir.course-thumbnail}")
//    private String courseThumbnailDir;

    @Value("${file.dir.course-note}")
    private String courseNoteDir;

//    public UrlResource getCourseThumbnailResource(String filename){
//        return getUrlResource(courseThumbnailDir + filename);
//    }

    public String getCourseNoteUrl(String filename){
        return courseNoteDir+filename;
    }

    public UrlResource getCourseNoteResource(String filename){
        log.info("강의자료 경로 : {}", courseNoteDir);
        return getUrlResource(courseNoteDir + filename);
    }

    private UrlResource getUrlResource(String path){
        try {
            return new UrlResource("file:"+ path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
