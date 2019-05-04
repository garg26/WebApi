package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "ns_questions")
@JsonIgnoreProperties({"quiz"})

public class Question extends DomainObject {

  @Id
  @Column(name = "question_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_seq")
  @SequenceGenerator(name = "question_seq", sequenceName = "question_seq",
      allocationSize = 100)
  private int questionId;


  @Column(name = "question_text",columnDefinition = "TEXT")
  private String questionText;

  @Column(name = "question_option_json",columnDefinition = "TEXT")
  private String optionJson;

  @Column(name = "question_figure_json",columnDefinition = "TEXT")
  private String figureJson;

  @Column(name = "question_topic")
  private String topic;

  @Column(name = "question_position")
  private int position = 0;

  @Column(name = "positive_points")
  private double positivePoints = 0.0;

  @Column(name = "negative_points")
  private double negativePoints = 0.0;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quiz_id")
  private Quiz quiz;

  @Column(name = "answer")
  private String answer;

  @Column(name = "answer_explanation",columnDefinition = "Text")
  private String answerExplanation;

  @Column(name = "explanation_image",columnDefinition = "Text")
  private String explanationImage;

  @Column(name = "question_html",columnDefinition = "Text")
  private String questionHtml;

  @Column(name = "explanation_html",columnDefinition = "Text")
  private String explanationHtml;

  /**
   * @return the questionText
   */
  public String getQuestionText() {
    return questionText;
  }

  /**
   * @param questionText the questionText to set
   */
  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }


  public int getQuestionId() {
    return questionId;
  }

  public void setQuestionId(int questionId) {
    this.questionId = questionId;
  }

  public double getPositivePoints() {
    return positivePoints;
  }

  public void setPositivePoints(double positivePoints) {
    this.positivePoints = positivePoints;
  }

  public double getNegativePoints() {
    return negativePoints;
  }

  public void setNegativePoints(double negativePoints) {
    this.negativePoints = negativePoints;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  public String getOptionJson() {
    return optionJson;
  }

  public void setOptionJson(String optionJson) {
    this.optionJson = optionJson;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @JsonIgnore
  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getFigureJson() {
    return figureJson;
  }

  public void setFigureJson(String figureJson) {
    this.figureJson = figureJson;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  @JsonIgnore
  public String getAnswerExplanation() {
    return answerExplanation;
  }

  public String getExplanationImage() {
    return explanationImage;
  }

  public void setExplanationImage(String explanationImage) {
    this.explanationImage = explanationImage;
  }

  public String getQuestionHtml() {
    return questionHtml;
  }

  public void setQuestionHtml(String questionHtml) {
    this.questionHtml = questionHtml;
  }

  public String getExplanationHtml() {
    return explanationHtml;
  }

  public void setExplanationHtml(String explanationHtml) {
    this.explanationHtml = explanationHtml;
  }

  public void setAnswerExplanation(String answerExplanation) {
    this.answerExplanation = answerExplanation;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + questionId;
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Question other = (Question) obj;
    if (questionId != other.questionId) {
      return false;
    }
    return true;
  }
}
