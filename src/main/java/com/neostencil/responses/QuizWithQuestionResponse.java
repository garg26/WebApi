package com.neostencil.responses;

import com.neostencil.model.entities.Question;
import com.neostencil.model.entities.Quiz;
import com.neostencil.model.entities.Unit;
import java.util.LinkedHashMap;
import java.util.List;

public class QuizWithQuestionResponse {

  Unit unit;
  Quiz quiz;
  LinkedHashMap<String,List<Question>> questionMap;
  List<Question> questionList;

  public Quiz getQuiz() {
    return quiz;
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  public LinkedHashMap<String, List<Question>> getQuestionMap() {
    return questionMap;
  }

  public void setQuestionMap(
      LinkedHashMap<String, List<Question>> questionMap) {
    this.questionMap = questionMap;
  }

  public List<Question> getQuestionList() {
    return questionList;
  }

  public void setQuestionList(List<Question> questionList) {
    this.questionList = questionList;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }
}
