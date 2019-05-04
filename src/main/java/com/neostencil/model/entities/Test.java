package com.neostencil.model.entities;

import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.QuestionType;
import com.neostencil.common.enums.QuizPermission;
import com.neostencil.common.enums.ScoringPolicy;
import com.neostencil.common.enums.TestType;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ns_tests")
public class Test extends DomainObject {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "test_id_gen")
  @TableGenerator(name = "test_id_gen", table = "ns_test_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "testId_Gen", initialValue = 10,
      allocationSize = 1)
  @Column(name = "test_id")
  int id;

  @Column(name = "test_type")
  @Enumerated(EnumType.STRING)
  TestType type;

  @Column(name = "test_title")
  String title;

  @Column(name = "description")
  String description;

  @Column(name = "test_url")
  String url;

  @Column(name = "time_limit")
  int timeLimit;

  @Column(name = "shuffle_answers")
  boolean shuffleAnswers;

  @Column(name = "show_correct_answers")
  boolean showCorrectAnswers;

  @Column(name = "scoring_policy")
  @Enumerated(EnumType.STRING)
  ScoringPolicy scoringpolicy;

  @Column(name = "allowed_attempts")
  int allowedAttempts;

  @Column(name = "question_count")
  int questionCount;

  @Column(name = "possible_points")
  int possiblePoints;

  @Column(name = "access_code")
  String accessCode;

  @Column(name = "dueAt")
  Timestamp dueAt;

  @Column(name = "publish_status")
  @Enumerated(EnumType.STRING)
  PublishStatus publishStatus;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ns_quiz_permissions", joinColumns = @JoinColumn(name = "test_id"))
  @Column(name = "quiz_permissions")
  Set<QuizPermission> permissions;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ns_quiz_question_types", joinColumns = @JoinColumn(name = "test_id"))
  @Column(name = "quiz_question_types")
  Set<QuestionType> questionTypes;

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the type
   */
  public TestType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(TestType type) {
    this.type = type;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return the timeLimit
   */
  public int getTimeLimit() {
    return timeLimit;
  }

  /**
   * @param timeLimit the timeLimit to set
   */
  public void setTimeLimit(int timeLimit) {
    this.timeLimit = timeLimit;
  }

  /**
   * @return the shuffleAnswers
   */
  public boolean isShuffleAnswers() {
    return shuffleAnswers;
  }

  /**
   * @param shuffleAnswers the shuffleAnswers to set
   */
  public void setShuffleAnswers(boolean shuffleAnswers) {
    this.shuffleAnswers = shuffleAnswers;
  }

  /**
   * @return the showCorrectAnswers
   */
  public boolean isShowCorrectAnswers() {
    return showCorrectAnswers;
  }

  /**
   * @param showCorrectAnswers the showCorrectAnswers to set
   */
  public void setShowCorrectAnswers(boolean showCorrectAnswers) {
    this.showCorrectAnswers = showCorrectAnswers;
  }

  /**
   * @return the scoringpolicy
   */
  public ScoringPolicy getScoringpolicy() {
    return scoringpolicy;
  }

  /**
   * @param scoringpolicy the scoringpolicy to set
   */
  public void setScoringpolicy(ScoringPolicy scoringpolicy) {
    this.scoringpolicy = scoringpolicy;
  }

  /**
   * @return the allowedAttempts
   */
  public int getAllowedAttempts() {
    return allowedAttempts;
  }

  /**
   * @param allowedAttempts the allowedAttempts to set
   */
  public void setAllowedAttempts(int allowedAttempts) {
    this.allowedAttempts = allowedAttempts;
  }

  /**
   * @return the questionCount
   */
  public int getQuestionCount() {
    return questionCount;
  }

  /**
   * @param questionCount the questionCount to set
   */
  public void setQuestionCount(int questionCount) {
    this.questionCount = questionCount;
  }

  /**
   * @return the possiblePoints
   */
  public int getPossiblePoints() {
    return possiblePoints;
  }

  /**
   * @param possiblePoints the possiblePoints to set
   */
  public void setPossiblePoints(int possiblePoints) {
    this.possiblePoints = possiblePoints;
  }

  /**
   * @return the accessCode
   */
  public String getAccessCode() {
    return accessCode;
  }

  /**
   * @param accessCode the accessCode to set
   */
  public void setAccessCode(String accessCode) {
    this.accessCode = accessCode;
  }

  /**
   * @return the dueAt
   */
  public Timestamp getDueAt() {
    return dueAt;
  }

  /**
   * @param dueAt the dueAt to set
   */
  public void setDueAt(Timestamp dueAt) {
    this.dueAt = dueAt;
  }

  /**
   * @return the publishStatus
   */
  public PublishStatus getPublishStatus() {
    return publishStatus;
  }

  /**
   * @param publishStatus the publishStatus to set
   */
  public void setPublishStatus(PublishStatus publishStatus) {
    this.publishStatus = publishStatus;
  }

  /**
   * @return the permissions
   */
  public Set<QuizPermission> getPermissions() {
    return permissions;
  }

  /**
   * @param permissions the permissions to set
   */
  public void setPermissions(Set<QuizPermission> permissions) {
    this.permissions = permissions;
  }

  /**
   * @return the questionTypes
   */
  public Set<QuestionType> getQuestionTypes() {
    return questionTypes;
  }

  /**
   * @param questionTypes the questionTypes to set
   */
  public void setQuestionTypes(Set<QuestionType> questionTypes) {
    this.questionTypes = questionTypes;
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
    result = prime * result + id;
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
    Test other = (Test) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }
}
