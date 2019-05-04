package com.neostencil.responses;

import java.util.LinkedHashMap;
import java.util.Set;

public class UserUnitResponse {

  LinkedHashMap<String, Set<Object>> unitMap;
  
  float watchedPercent;
  float inProgressPercent;
  float notStartedPercent;

  /**
   * @return the unitMap
   */
  public LinkedHashMap<String, Set<Object>> getUnitMap() {
    return unitMap;
  }

  /**
   * @param unitMap the unitMap to set
   */
  public void setUnitMap(LinkedHashMap<String, Set<Object>> unitMap) {
    this.unitMap = unitMap;
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
