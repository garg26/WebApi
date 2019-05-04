package com.neostencil.responses;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class UserUnitTopicWiseResponse implements Serializable {

  String courseSlug;

  LinkedHashMap<String, LinkedList<Object>> response;
  
  float watchedPercent;
  float inProgressPercent;
  float notStartedPercent;

  public LinkedHashMap<String, LinkedList<Object>> getResponse() {
    return response;
  }

  public void setResponse(LinkedHashMap<String, LinkedList<Object>> response) {
    this.response = response;
  }

  /**
   * @return the courseSlug
   */
  public String getCourseSlug() {
    return courseSlug;
  }

  /**
   * @param courseSlug the courseSlug to set
   */
  public void setCourseSlug(String courseSlug) {
    this.courseSlug = courseSlug;
  }

  /**
   * @return the watchedPercent
   */
  public float getWatchedPercent() {
    return watchedPercent;
  }

  /**
   * @param watchedPercent the watchedPercent to set
   */
  public void setWatchedPercent(float watchedPercent) {
    this.watchedPercent = watchedPercent;
  }

  /**
   * @return the inProgressPercent
   */
  public float getInProgressPercent() {
    return inProgressPercent;
  }

  /**
   * @param inProgressPercent the inProgressPercent to set
   */
  public void setInProgressPercent(float inProgressPercent) {
    this.inProgressPercent = inProgressPercent;
  }

  /**
   * @return the notStartedPercent
   */
  public float getNotStartedPercent() {
    return notStartedPercent;
  }

  /**
   * @param notStartedPercent the notStartedPercent to set
   */
  public void setNotStartedPercent(float notStartedPercent) {
    this.notStartedPercent = notStartedPercent;
  }

  

}
