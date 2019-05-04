package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.neostencil.common.enums.ExamSegmentTypes;

/**
 * 
 * @author shilpa
 *
 */
@Entity
@Table(name = "ns_discussions")
public class Discussion extends DomainObject {

  /**
   * 
   */
  private static final long serialVersionUID = -316586931436620867L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discussion_seq")
  @SequenceGenerator(name = "discussion_seq", sequenceName = "discussion_seq", allocationSize = 1)
  private long id;

  @Column(name = "slug", length = 400)
  private String slug;

  @Column(name = "title", length = 500)
  private String title;

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;

  @Column(name = "no_of_likes")
  private long noOfLikes;

  @Column(name = "exam_segment")
  @Enumerated(EnumType.STRING)
  private ExamSegmentTypes examSegment;

  @Column(name = "category")
  private String category;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

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
   * @return the slug
   */
  public String getSlug() {
    return slug;
  }

  /**
   * @param slug the slug to set
   */
  public void setSlug(String slug) {
    this.slug = slug;
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
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the noOfLikes
   */
  public long getNoOfLikes() {
    return noOfLikes;
  }

  /**
   * @param noOfLikes the noOfLikes to set
   */
  public void setNoOfLikes(long noOfLikes) {
    this.noOfLikes = noOfLikes;
  }

  /**
   * @return the examSegment
   */
  public ExamSegmentTypes getExamSegment() {
    return examSegment;
  }

  /**
   * @param examSegment the examSegment to set
   */
  public void setExamSegment(ExamSegmentTypes examSegment) {
    this.examSegment = examSegment;
  }

  /**
   * @return the category
   */
  public String getCategory() {
    return category;
  }

  /**
   * @param category the category to set
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(User user) {
    this.user = user;
  }


}
