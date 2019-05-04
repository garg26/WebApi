package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

public class Answer extends DomainObject {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "answer_id_gen")
  @TableGenerator(name = "answer_id_gen", table = "ns_answer_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "answerId_Gen", initialValue = 10,
      allocationSize = 1)
  @Column(name = "answer_id")
  int id;

  String answerText;

  @OneToOne
  @JoinColumn(name = "question_id")
  Question question_id;

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
   * @return the answerText
   */
  public String getAnswerText() {
    return answerText;
  }

  /**
   * @param answerText the answerText to set
   */
  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }

  /**
   * @return the question_id
   */
  public Question getQuestion_id() {
    return question_id;
  }

  /**
   * @param question_id the question_id to set
   */
  public void setQuestion_id(Question question_id) {
    this.question_id = question_id;
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
    Answer other = (Answer) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }
}
