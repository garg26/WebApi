package com.neostencil.requests;

public class QuizSubmissionRequest {

  boolean attempt;
  String answer;
  int questionId;
  String topic;

  public boolean isAttempt() {
    return attempt;
  }

  public void setAttempt(boolean attempt) {
    this.attempt = attempt;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public int getQuestionId() {
    return questionId;
  }

  public void setQuestionId(int questionId) {
    this.questionId = questionId;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
}
