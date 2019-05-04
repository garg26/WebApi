package com.neostencil.model.repositories;

import com.neostencil.model.entities.Course;
import com.neostencil.requests.FilterRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ICourseCustomRepository {

  List<Course> filterCourses(FilterRequest request);
}
