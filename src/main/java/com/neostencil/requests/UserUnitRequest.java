package com.neostencil.requests;

public class UserUnitRequest {

  private String type;
  private long time;
  private int count;
  private String currentState;
  private long duration;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getCurrentState() {
    return currentState;
  }

  public void setCurrentState(String currentState) {
    this.currentState = currentState;
  }

  /**
   * @return the duration
   */
  public long getDuration() {
    return duration;
  }

  /**
   * @param duration the duration to set
   */
  public void setDuration(long duration) {
    this.duration = duration;
  }

  /**
   * @return the time
   */
  public long getTime() {
    return time;
  }

  /**
   * @param time the time to set
   */
  public void setTime(long time) {
    this.time = time;
  }
}
