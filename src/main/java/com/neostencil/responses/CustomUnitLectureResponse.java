package com.neostencil.responses;

import com.neostencil.model.entities.Lecture;
import com.neostencil.model.entities.Unit;


public class CustomUnitLectureResponse {

  Unit unit;
  Lecture lecture;


  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

  public Lecture getLecture() {
    return lecture;
  }

  public void setLecture(Lecture lecture) {
    this.lecture = lecture;
  }
}
