package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.neostencil.common.enums.StreamType;
import com.neostencil.model.entities.Lecture;

@Projection(name = "lectureProjection", types = Lecture.class)
public interface LectureProjection {

  int getId();

  String getName();

  String getUrl();

  String getReleaseDate();

  String getDuration();

  String getHeading();

  CourseBatchProjection getCourseBatch();

  String getTeacherName();

  JWMacroProjection getJwMacro();
}
