package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ns_quiz_template_meta_information")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QuizTemplateMetaInformation {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_template_meta_seq")
  @SequenceGenerator(name = "quiz_template_seq", sequenceName = "quiz_template_seq", allocationSize = 1)
  private long id;

  @JsonBackReference(value = "meta-reference")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "quiz_template_id")
  private
  QuizTemplate quizTemplate;

  @Embedded
  private MetaInformation metaInformation;

  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the post
   */
  public QuizTemplate getQuizTemplate() {
    return quizTemplate;
  }

  public void setQuizTemplate(QuizTemplate quizTemplate) {
    this.quizTemplate = quizTemplate;
  }

  /**
   * @return the metaInformation
   */
  public MetaInformation getMetaInformation() {
    return metaInformation;
  }

  /**
   * @param metaInformation the metaInformation to set
   */
  public void setMetaInformation(MetaInformation metaInformation) {
    this.metaInformation = metaInformation;
  }

}
