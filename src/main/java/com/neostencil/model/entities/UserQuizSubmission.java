package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ns_user_quiz_submission")
public class UserQuizSubmission extends DomainObject{

  @Id
  @Column(name = "quiz_submission_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_quiz_submission_seq")
  @SequenceGenerator(name = "user_quiz_submission_seq", sequenceName = "user_quiz_submission_seq",
      allocationSize = 1)
  int quizSubmissionId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "unit_id")
  private Unit unit;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quiz_id")
  private Quiz quiz;


  @Column(name = "user_submission",columnDefinition = "Text")
  private String userSubmission;

  @Column(name = "mark_obtained")
  private double marksObtained = 0.0;

  @Column(name = "time_taken")
  private String timeTaken;

  public int getQuizSubmissionId() {
    return quizSubmissionId;
  }

  public void setQuizSubmissionId(int quizSubmissionId) {
    this.quizSubmissionId = quizSubmissionId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getUserSubmission() {
    return userSubmission;
  }

  public void setUserSubmission(String userSubmission) {
    this.userSubmission = userSubmission;
  }

  public double getMarksObtained() {
    return marksObtained;
  }

  public void setMarksObtained(double marksObtained) {
    this.marksObtained = marksObtained;
  }

  public String getTimeTaken() {
    return timeTaken;
  }

  public void setTimeTaken(String timeTaken) {
    this.timeTaken = timeTaken;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }
}
