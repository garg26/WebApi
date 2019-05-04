package com.neostencil.requests;

import java.util.List;

public class BatchNameRequest {

  List<Integer> courseIds;

  /**
   * @return the courseIds
   */
  public List<Integer> getCourseIds() {
    return courseIds;
  }

  /**
   * @param courseIds the courseIds to set
   */
  public void setCourseIds(List<Integer> courseIds) {
    this.courseIds = courseIds;
  }

}
