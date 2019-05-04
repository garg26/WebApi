package com.neostencil.projections;

import java.util.Set;
import org.springframework.data.rest.core.config.Projection;
import com.neostencil.model.entities.TeacherDetails;

@Projection(name = "teacherCoursesProjection", types = TeacherDetails.class)
public interface TeacherCoursesProjection {

  Set<CourseProjection> getCourses();
}
