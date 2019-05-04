package com.neostencil.responses;

import com.neostencil.common.enums.ExamSegmentTypes;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author shilpa
 *
 */
public class DiscussionDTO {

  private long id;

  private String slug;

  private String title;

  private String content;

  private long noOfLikes;

  private ExamSegmentTypes examSegment;

  private String category;
  
  private String createdBy;

  private Date updatedAt;

  private String avatar;
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
   * @return the createdBy
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }
}
