package com.neostencil.responses;

import java.util.Map;
import java.util.Set;

/**
 * 
 * @author shilpa
 *
 */
public class ParentCourseResponse {

  Map<String, Set<CustomCourse>> courseMap;

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
