package com.neostencil.responses;

import com.neostencil.model.entities.CustomStudent;
import java.util.Map;
import java.util.Set;

public class TeacherStudentResponse {

  Map<CustomBatch, Set<CustomStudent>> studentsMap;

  int totalStudents;

  /**
   * @return the totalStudents
   */
  public int getTotalStudents() {
    return totalStudents;
  }

  /**
   * @param totalStudents the totalStudents to set
   */
  public void setTotalStudents(int totalStudents) {
    this.totalStudents = totalStudents;
  }

  /**
   * @return the studentsMap
   */
  public Map<CustomBatch, Set<CustomStudent>> getStudentsMap() {
    return studentsMap;
  }

  /**
   * @param studentsMap the studentsMap to set
   */
  public void setStudentsMap(Map<CustomBatch, Set<CustomStudent>> studentsMap) {
    this.studentsMap = studentsMap;
  }

}
