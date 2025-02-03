package com.gongkademy.utils;

import java.net.MalformedURLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {
//    @Value("${file.dir.course-thumbnail}")
//    private String courseThumbnailDir;

    @Value("${file.dir.course-note}")
    private String courseNoteDir;

//    public UrlResource getCourseThumbnailResource(String filename){
//        return getUrlResource(courseThumbnailDir + filename);
//    }

    public UrlResource getCourseNoteResource(String filename){
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
