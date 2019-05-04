package com.neostencil.model.entities;

import com.neostencil.common.enums.PublishStatus;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ns_quiz_template")
public class QuizTemplate extends DomainObject{

  @Id
  @Column(name = "quiz_template_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_template_seq")
  @SequenceGenerator(name = "quiz_template_seq", sequenceName = "quiz_template_seq",
      allocationSize = 100)
  private int id;

  @Column(name = "quiz_template_slug",unique = true)
  private String slug;

  @Column(name = "metadata",columnDefinition = "TEXT")
  private String metadata;

  @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JoinColumn(name = "quiz_id",unique = true)
  private Quiz quiz;

  @Column(name = "footer_metadata",columnDefinition = "TEXT")
  private String footerMetadata;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "breadcrumb_id")
  private BreadCrumb breadCrumb;

  @Column(name = "exam_segment")
  private String examSegment;

  @Column(name = "title_tag")
  private String titleTag;

  @Column(name = "quiz_template_status")
  @Enumerated(EnumType.STRING)
  private PublishStatus status;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getMetadata() {
    return metadata;
  }

  public void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  public String getFooterMetadata() {
    return footerMetadata;
  }

  public void setFooterMetadata(String footerMetadata) {
    this.footerMetadata = footerMetadata;
  }

  public BreadCrumb getBreadCrumb() {
    return breadCrumb;
  }

  public void setBreadCrumb(BreadCrumb breadCrumb) {
    this.breadCrumb = breadCrumb;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getExamSegment() {
    return examSegment;
  }

  public void setExamSegment(String examSegment) {
    this.examSegment = examSegment;
  }

  public String getTitleTag() {
    return titleTag;
  }

  public void setTitleTag(String titleTag) {
    this.titleTag = titleTag;
  }

  public PublishStatus getStatus() {
    return status;
  }

  public void setStatus(PublishStatus status) {
    this.status = status;
  }
}
