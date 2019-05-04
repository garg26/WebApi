package com.neostencil.model.repositories;

import com.neostencil.model.entities.TeacherDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.neostencil.model.entities.TeacherStatistics;

public interface TeacherStatisticsRepository extends JpaRepository<TeacherStatistics, Integer> {

  TeacherStatistics findById(int id);

  List<TeacherStatistics> findByTeacherAndVisibleToTeacherIsTrue(TeacherDetails teacher);

  TeacherStatistics findByTeacherAndCourseBatch(TeacherDetails teacher, String courseBatch);

  TeacherStatistics findByTeacherAndCourseBatchAndMonthYear(TeacherDetails teacher,
      String courseBatch, String monthYear);
}
