package com.neostencil.responses;

import com.neostencil.model.entities.User;
import java.sql.Date;

public class ReviewResponse {

  private int id;
  private String slug;
  private String slugType;
  private String date;
  private String reviewTitle;
  private float reviewRating;
  private String text;
  private String name;
  private String email;
  private String avatar;

  /**
   * To filter out duplicate name
   */
  private String dummyName;

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

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getDummyName() {
    return dummyName;
  }

  public void setDummyName(String dummyName) {
    this.dummyName = dummyName;
  }
}
