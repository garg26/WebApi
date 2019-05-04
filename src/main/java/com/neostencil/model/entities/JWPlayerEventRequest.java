package com.neostencil.model.entities;

import java.sql.Timestamp;

public class JWPlayerEventRequest {

  Timestamp date;
  int count;
  long seek;
  String state;

  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  /**
   * @return the seek
   */
  public long getSeek() {
    return seek;
  }

  /**
   * @param seek the seek to set
   */
  public void setSeek(long seek) {
    this.seek = seek;
  }
}
