package com.neostencil.dtos;

import java.util.Set;
import com.neostencil.common.enums.CommentType;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.CommentAttachment;

/**
 * For transferring comment data over the network
 * 
 * @author shilpa
 *
 */
public class CommentDTO {

  private int commentId;

  private String slug;

  private String slugType;

  private String name;

  private String email;

  private String url;

  private String ip;

  private String date;

  private String text;

  private Set<CommentAttachment> attachments;

  private int parentId;

  private String reviewTitle;

  private float reviewRating = 0;

  private String bp_activity_comment_id;

  private String quizId;

  private UserDTO user;

  private CommentType type;

  private int count;

  private PublishStatus status;

  private int reply_count;

  /**
   * @return the commentId
   */
  public int getCommentId() {
    return commentId;
  }

  /**
   * @param commentId the commentId to set
   */
  public void setCommentId(int commentId) {
    this.commentId = commentId;
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
   * @return the slugType
   */
  public String getSlugType() {
    return slugType;
  }

  /**
   * @param slugType the slugType to set
   */
  public void setSlugType(String slugType) {
    this.slugType = slugType;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
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
   * @return the ip
   */
  public String getIp() {
    return ip;
  }

  /**
   * @param ip the ip to set
   */
  public void setIp(String ip) {
    this.ip = ip;
  }

  /**
   * @return the date
   */
  public String getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @return the attachments
   */
  public Set<CommentAttachment> getAttachments() {
    return attachments;
  }

  /**
   * @param attachments the attachments to set
   */
  public void setAttachments(Set<CommentAttachment> attachments) {
    this.attachments = attachments;
  }

  /**
   * @return the parentId
   */
  public int getParentId() {
    return parentId;
  }

  /**
   * @param parentId the parentId to set
   */
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  /**
   * @return the reviewTitle
   */
  public String getReviewTitle() {
    return reviewTitle;
  }

  /**
   * @param reviewTitle the reviewTitle to set
   */
  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  /**
   * @return the reviewRating
   */
  public float getReviewRating() {
    return reviewRating;
  }

  /**
   * @param reviewRating the reviewRating to set
   */
  public void setReviewRating(float reviewRating) {
    this.reviewRating = reviewRating;
  }

  /**
   * @return the bp_activity_comment_id
   */
  public String getBp_activity_comment_id() {
    return bp_activity_comment_id;
  }

  /**
   * @param bp_activity_comment_id the bp_activity_comment_id to set
   */
  public void setBp_activity_comment_id(String bp_activity_comment_id) {
    this.bp_activity_comment_id = bp_activity_comment_id;
  }

  /**
   * @return the quizId
   */
  public String getQuizId() {
    return quizId;
  }

  /**
   * @param quizId the quizId to set
   */
  public void setQuizId(String quizId) {
    this.quizId = quizId;
  }

  /**
   * @return the user
   */
  public UserDTO getUser() {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(UserDTO user) {
    this.user = user;
  }

  /**
   * @return the type
   */
  public CommentType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(CommentType type) {
    this.type = type;
  }

  /**
   * @return the count
   */
  public int getCount() {
    return count;
  }

  /**
   * @param count the count to set
   */
  public void setCount(int count) {
    this.count = count;
  }

  /**
   * @return the status
   */
  public PublishStatus getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(PublishStatus status) {
    this.status = status;
  }

  /**
   * @return the reply_count
   */
  public int getReply_count() {
    return reply_count;
  }

  /**
   * @param reply_count the reply_count to set
   */
  public void setReply_count(int reply_count) {
    this.reply_count = reply_count;
  }



}
