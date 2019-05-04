package com.neostencil.responses;

import com.neostencil.responses.quizresponse.QuizWithAnswerResponse;
import com.neostencil.responses.quizresponse.SectionLevel;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class UserQuizSubmissionResponse {

  SectionLevel sectionLevel;
  LinkedHashMap<String,SectionLevel> topicLevel;
  List<QuizWithAnswerResponse> solution;
  LinkedList<QuizLeaderBoardResponse> leaderBoard;
  String token;

  public SectionLevel getSectionLevel() {
    return sectionLevel;
  }

  public void setSectionLevel(SectionLevel sectionLevel) {
    this.sectionLevel = sectionLevel;
  }

  public LinkedHashMap<String, SectionLevel> getTopicLevel() {
    return topicLevel;
  }

  public void setTopicLevel(
      LinkedHashMap<String, SectionLevel> topicLevel) {
    this.topicLevel = topicLevel;
  }

  public List<QuizWithAnswerResponse> getSolution() {
    return solution;
  }

  public void setSolution(List<QuizWithAnswerResponse> solution) {
    this.solution = solution;
  }

  public LinkedList<QuizLeaderBoardResponse> getLeaderBoard() {
    return leaderBoard;
  }

  public void setLeaderBoard(
      LinkedList<QuizLeaderBoardResponse> leaderBoard) {
    this.leaderBoard = leaderBoard;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}

