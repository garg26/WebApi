package com.neostencil.responses;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class UserCoursesResponse {

  Map<String, Set<CustomCourse>> courseMap = new LinkedHashMap<String, Set<CustomCourse>>();

  /**
   * @return the courseMap
   */
  public Map<String, Set<CustomCourse>> getCourseMap() {
    return courseMap;
  }

  /**
   * @param courseMap the courseMap to set
   */
  public void setCourseMap(Map<String, Set<CustomCourse>> courseMap) {
    this.courseMap = courseMap;
  }

}
