package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.QuizType;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ns_quizzes")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

public class Quiz extends DomainObject{

  @Id
  @Column(name = "quiz_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_seq")
  @SequenceGenerator(name = "quiz_seq", sequenceName = "quiz_seq",
      allocationSize = 1000)
  private int quizId;

  /**
   * Title of the quiz
   */
  @Column(name = "quiz_title",nullable = false)
  private String title;

  /**
   * Description of the quiz
   */
  @Column(name = "quiz_description",length = 20000)
  private String description;

  /**
   * quiz time limit if any
   */
  @Column(name = "quiz_time_limit")
  private double timeLimit = 0.0;

  /**
   * default false
   * if true-> store question topic wise on ui
   */
  @Column(name = "topic_wise_question")
  private boolean topicWiseQuestion;


  @Column(name = "topic_wise_analysis")
  private boolean topicWiseAnalysis;

  /**
   * default false
   * if true-> show correct answer after submitting the quiz
   */
  @Column(name = "show_correct_answers_at_last")
  private boolean showCorrectAnswersAtLast;

  /**
   * default false
   * if true-> shuffle the quiz question. If topicWiseQuestion is true than shuffle the question within the topic.
   */
  @Column(name = "shuffle_question")
  private boolean shuffleQuestion;

  /**
   * type of the quiz i.e., Practice test, Mock test, Scholarship test etc.
   */
  @Column(name = "quiz_type")
  @Enumerated(EnumType.STRING)
  private QuizType quizType;

  /**
   *  default false
   *  if true-> then hide the quiz result
   */
  @Column(name = "hide_results")
  private boolean hideResults;

  /**
   * if publiclyAvailable true than generate a quiz string to create a unique url. If quiz has expiration time than generate
   * a random string with expiration time.
   */
  @Column(name = "access_code",nullable = true,unique = true)
  private String accessCode;

  /**
   * Status of the quiz i.e., Publish, Dratf etc.
   */
  @Column(name = "quiz_status")
  @Enumerated(EnumType.STRING)
  private PublishStatus publishStatus;

  /**
   * Show quiz publicly
   */
  @Column(name = "is_public")
  private boolean publiclyAvailable;

  /**
   * default-> false
   * if true-> quiz has expiration time if quiz is publicly
   */
  @Column(name = "is_quiz_expire")
  private boolean quizExpire;

  /**
   *
   * if quizExpire is true than quiz expiration time
   */
  @Column(name = "expiration_time")
  private Date expirationTime;

  @Column(name = "total_question")
  private int totalNoOfQuestion = 0;

  @Column(name = "total_marks")
  private int totalMarks = 0;

  @Column(name = "positive_mark_per_question")
  private double positiveMarkPerQuestion = 0.0;

  @Column(name = "negative_mark_per_question")
  private double negativeMarkPerQuestion = 0.0;


  public int getQuizId() {
    return quizId;
  }

  public void setQuizId(int quizId) {
    this.quizId = quizId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getTimeLimit() {
    return timeLimit;
  }

  public void setTimeLimit(double timeLimit) {
    this.timeLimit = timeLimit;
  }

  public boolean isTopicWiseQuestion() {
    return topicWiseQuestion;
  }

  public void setTopicWiseQuestion(boolean topicWiseQuestion) {
    this.topicWiseQuestion = topicWiseQuestion;
  }

  public boolean isShowCorrectAnswersAtLast() {
    return showCorrectAnswersAtLast;
  }

  public void setShowCorrectAnswersAtLast(boolean showCorrectAnswersAtLast) {
    this.showCorrectAnswersAtLast = showCorrectAnswersAtLast;
  }

  public boolean isShuffleQuestion() {
    return shuffleQuestion;
  }

  public void setShuffleQuestion(boolean shuffleQuestion) {
    this.shuffleQuestion = shuffleQuestion;
  }

  public QuizType getQuizType() {
    return quizType;
  }

  public void setQuizType(QuizType quizType) {
    this.quizType = quizType;
  }

  public boolean isHideResults() {
    return hideResults;
  }

  public void setHideResults(boolean hideResults) {
    this.hideResults = hideResults;
  }

  public String getAccessCode() {
    return accessCode;
  }

  public void setAccessCode(String accessCode) {
    this.accessCode = accessCode;
  }

  public PublishStatus getPublishStatus() {
    return publishStatus;
  }

  public void setPublishStatus(PublishStatus publishStatus) {
    this.publishStatus = publishStatus;
  }

  public boolean isPubliclyAvailable() {
    return publiclyAvailable;
  }

  public void setPubliclyAvailable(boolean publiclyAvailable) {
    this.publiclyAvailable = publiclyAvailable;
  }

  public boolean isQuizExpire() {
    return quizExpire;
  }

  public void setQuizExpire(boolean quizExpire) {
    this.quizExpire = quizExpire;
  }

  public Date getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(Date expirationTime) {
    this.expirationTime = expirationTime;
  }

  public int getTotalNoOfQuestion() {
    return totalNoOfQuestion;
  }

  public void setTotalNoOfQuestion(int totalNoOfQuestion) {
    this.totalNoOfQuestion = totalNoOfQuestion;
  }

  public int getTotalMarks() {
    return totalMarks;
  }

  public void setTotalMarks(int totalMarks) {
    this.totalMarks = totalMarks;
  }

  public double getPositiveMarkPerQuestion() {
    return positiveMarkPerQuestion;
  }

  public void setPositiveMarkPerQuestion(double positiveMarkPerQuestion) {
    this.positiveMarkPerQuestion = positiveMarkPerQuestion;
  }

  public double getNegativeMarkPerQuestion() {
    return negativeMarkPerQuestion;
  }

  public void setNegativeMarkPerQuestion(double negativeMarkPerQuestion) {
    this.negativeMarkPerQuestion = negativeMarkPerQuestion;
  }

  public boolean isTopicWiseAnalysis() {
    return topicWiseAnalysis;
  }

  public void setTopicWiseAnalysis(boolean topicWiseAnalysis) {
    this.topicWiseAnalysis = topicWiseAnalysis;
  }
}

