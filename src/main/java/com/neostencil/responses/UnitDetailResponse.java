package com.neostencil.responses;

import com.neostencil.model.entities.Lecture;
import com.neostencil.model.entities.Unit;

public class UnitDetailResponse {

  String unitType;

  String lectureScript;

  Unit assignment;

  Lecture lecture;

  Unit note;

  QuizWithQuestionResponse quiz;
  
  boolean enableQueryButton;

  UnitOnMobile unitOnMobile;
  
  float watchPercent;


  /**
   * @return the lectureScript
   */
  public String getLectureScript() {
    return lectureScript;
  }

  /**
   * @param lectureScript the lectureScript to set
   */
  public void setLectureScript(String lectureScript) {
    this.lectureScript = lectureScript;
  }

  /**
   * @return the assignment
   */
  public Unit getAssignment() {
    return assignment;
  }

  /**
   * @param assignment the assignment to set
   */
  public void setAssignment(Unit assignment) {
    this.assignment = assignment;
  }

  /**
   * @return the lecture
   */
  public Lecture getLecture() {
    return lecture;
  }

  /**
   * @param lecture the lecture to set
   */
  public void setLecture(Lecture lecture) {
    this.lecture = lecture;
  }

  /**
   * @return the note
   */
  public Unit getNote() {
    return note;
  }

  /**
   * @param note the note to set
   */
  public void setNote(Unit note) {
    this.note = note;
  }

  public QuizWithQuestionResponse getQuiz() {
    return quiz;
  }

  public void setQuiz(QuizWithQuestionResponse quiz) {
    this.quiz = quiz;
  }

  /**
   * @return the enableQueryButton
   */
  public boolean isEnableQueryButton() {
    return enableQueryButton;
  }

  /**
   * @param enableQueryButton the enableQueryButton to set
   */
  public void setEnableQueryButton(boolean enableQueryButton) {
    this.enableQueryButton = enableQueryButton;
  }

  public UnitOnMobile getUnitOnMobile() {
    return unitOnMobile;
  }

  public void setUnitOnMobile(UnitOnMobile unitOnMobile) {
    this.unitOnMobile = unitOnMobile;
  }

  public String getUnitType() {
    return unitType;
  }

  public void setUnitType(String unitType) {
    this.unitType = unitType;
  }

  /**
   * @return the watchPercent
   */
  public float getWatchPercent() {
    return watchPercent;
  }

  /**
   * @param watchPercent the watchPercent to set
   */
  public void setWatchPercent(float watchPercent) {
    this.watchPercent = watchPercent;
  }
}
