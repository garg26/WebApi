package com.neostencil.requests;

import com.neostencil.model.entities.Quiz;

public class QuizWithHTMLRequest {

  Quiz quiz;
  String html;

  public Quiz getQuiz() {
    return quiz;
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }
}
