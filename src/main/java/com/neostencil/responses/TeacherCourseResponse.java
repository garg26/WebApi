package com.neostencil.responses;

import java.util.Set;

public class TeacherCourseResponse {
  
  int totalNoOfCourses;
  
  Set<CustomCourse> courseBatches;

  /**
   * @return the totalNoOfCourses
   */
  public int getTotalNoOfCourses() {
    return totalNoOfCourses;
  }

  /**
   * @param totalNoOfCourses the totalNoOfCourses to set
   */
  public void setTotalNoOfCourses(int totalNoOfCourses) {
    this.totalNoOfCourses = totalNoOfCourses;
  }

  /**
   * @return the courseBatches
   */
  public Set<CustomCourse> getCourseBatches() {
    return courseBatches;
  }

  /**
   * @param courseBatches the courseBatches to set
   */
  public void setCourseBatches(Set<CustomCourse> courseBatches) {
    this.courseBatches = courseBatches;
  }

}
