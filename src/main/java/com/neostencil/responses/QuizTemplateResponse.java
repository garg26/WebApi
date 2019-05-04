package com.neostencil.responses;

import com.neostencil.model.entities.Question;
import com.neostencil.model.entities.QuizTemplate;
import java.util.LinkedHashMap;
import java.util.List;

public class QuizTemplateResponse {

  QuizTemplate quizTemplate;
  List<Question> questions;
  LinkedHashMap<String,List<Question>> questionMap;

  public QuizTemplate getQuizTemplate() {
    return quizTemplate;
  }

  public void setQuizTemplate(QuizTemplate quizTemplate) {
    this.quizTemplate = quizTemplate;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public LinkedHashMap<String, List<Question>> getQuestionMap() {
    return questionMap;
  }

  public void setQuestionMap(
      LinkedHashMap<String, List<Question>> questionMap) {
    this.questionMap = questionMap;
  }
}
