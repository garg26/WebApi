package com.neostencil.responses.quizresponse;

public class SectionLevel {
  double marksObtained;
  int totalNoOfQuestion;
  int noOfCorrectQuestion;
  int noOfIncorrectQuestion;
  int noOfSkippedQuestion;
  int noOfAttemptQuestion;

  public double getMarksObtained() {
    return marksObtained;
  }

  public void setMarksObtained(double marksObtained) {
    this.marksObtained = marksObtained;
  }

  public int getTotalNoOfQuestion() {
    return totalNoOfQuestion;
  }

  public void setTotalNoOfQuestion(int totalNoOfQuestion) {
    this.totalNoOfQuestion = totalNoOfQuestion;
  }

  public int getNoOfCorrectQuestion() {
    return noOfCorrectQuestion;
  }

  public void setNoOfCorrectQuestion(int noOfCorrectQuestion) {
    this.noOfCorrectQuestion = noOfCorrectQuestion;
  }

  public int getNoOfIncorrectQuestion() {
    return noOfIncorrectQuestion;
  }

  public void setNoOfIncorrectQuestion(int noOfIncorrectQuestion) {
    this.noOfIncorrectQuestion = noOfIncorrectQuestion;
  }

  public int getNoOfSkippedQuestion() {
    return noOfSkippedQuestion;
  }

  public void setNoOfSkippedQuestion(int noOfSkippedQuestion) {
    this.noOfSkippedQuestion = noOfSkippedQuestion;
  }

  public int getNoOfAttemptQuestion() {
    return noOfAttemptQuestion;
  }

  public void setNoOfAttemptQuestion(int noOfAttemptQuestion) {
    this.noOfAttemptQuestion = noOfAttemptQuestion;
  }
}
