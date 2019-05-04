package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.CommentType;
import com.neostencil.common.enums.PublishStatus;

import java.sql.Date;
import java.util.Set;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/*
 * Command to update reply_count if there is any reply of that comment.
 *
 * update ns_comments c1 set reply_count=(select count(c2.comment_id) from ns_comments c2 where
 * c2.comment_parent=c1.comment_id);
 */

@Entity
@Table(name = "ns_comments")
public class Comment extends DomainObject {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "comment_gen")
  @TableGenerator(name = "comment_gen", table = "ns_comment_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "CommentId_Gen", initialValue = 10000,
      allocationSize = 1)
  @Column(name = "comment_id")
  private int commentId;

  @Column(name = "slug")
  private String slug;

  @Column(name = "slug_type")
  private String slugType;

  @Column(name = "comment_author_name")
  private String name;

  @Column(name = "comment_author_email")
  private String email;

  @Column(name = "comment_author_url")
  private String url;

  @Column(name = "comment_author_IP")
  private String ip;

  @Column(name = "comment_date")
  private String date;

  @Column(name = "comment_text", length = 1000000)
  private String text;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "ns_comment_image_items", joinColumns = @JoinColumn(name = "comment_id"))
  @Column(name = "comment_images")
  private Set<CommentAttachment> attachments;

  @Column(name = "parent_id")
  private int parentId;

  @Column(name = "review_title",columnDefinition = "TEXT")
  private String reviewTitle;

  @Column(name = "review_rating")
  private float reviewRating = 0;

  @JsonIgnore
  @Column(name = "bp_activity_comment_id")
  private String bp_activity_comment_id;

  @Column(name = "quiz_id")
  private String quizId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User commentedBy;

  @Column(name = "comment_type")
  @Enumerated(EnumType.STRING)
  private CommentType type;

  @Column(name = "comment_count")
  private int count;

  @Column(name = "comment_status")
  @Enumerated(EnumType.STRING)
  private PublishStatus status;

  @Column(name = "reply_count")
  private int reply_count;

  public int getReply_count() {
    return reply_count;
  }

  public void setReply_count(int reply_count) {
    this.reply_count = reply_count;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  public int getCommentId() {
    return commentId;
  }

  public void setCommentId(int commentId) {
    this.commentId = commentId;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getSlugType() {
    return slugType;
  }

  public void setSlugType(String slugType) {
    this.slugType = slugType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Set<CommentAttachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(Set<CommentAttachment> attachments) {
    this.attachments = attachments;
  }

  public String getBp_activity_comment_id() {
    return bp_activity_comment_id;
  }

  public void setBp_activity_comment_id(String bp_activity_comment_id) {
    this.bp_activity_comment_id = bp_activity_comment_id;
  }

  public User getCommentedBy() {
    return commentedBy;
  }

  public void setCommentedBy(User commentedBy) {
    this.commentedBy = commentedBy;
  }

  public CommentType getType() {
    return type;
  }

  public void setType(CommentType type) {
    this.type = type;
  }

  public String getReviewTitle() {
    return reviewTitle;
  }

  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  public float getReviewRating() {
    return reviewRating;
  }

  public void setReviewRating(int reviewRating) {
    this.reviewRating = reviewRating;
  }

  public String getQuizId() {
    return quizId;
  }

  public void setQuizId(String quizId) {
    this.quizId = quizId;
  }

  public PublishStatus getStatus() {
    return status;
  }

  public void setStatus(PublishStatus status) {
    this.status = status;
  }
}
