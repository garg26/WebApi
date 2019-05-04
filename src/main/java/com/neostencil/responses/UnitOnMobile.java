package com.neostencil.responses;

import com.neostencil.model.entities.Unit;

public class UnitOnMobile {

  MobileNote note;
  PlayLectureOnMobile lectureOnMobile;
  Unit unit;

  public MobileNote getNote() {
    return note;
  }

  public void setNote(MobileNote note) {
    this.note = note;
  }

  public PlayLectureOnMobile getLectureOnMobile() {
    return lectureOnMobile;
  }

  public void setLectureOnMobile(PlayLectureOnMobile lectureOnMobile) {
    this.lectureOnMobile = lectureOnMobile;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }
}
