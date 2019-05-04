package com.neostencil.model.entities;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.PostCategory;
import com.neostencil.common.enums.PostSubCategory;
import com.neostencil.common.enums.PublishStatus;

@Entity
@Table(name = "ns_post_summary")
public class PostSummary extends DomainObject {

  @Id
  @Column(name = "post_summary_id", nullable = false)
  int postSummaryId;

  @Column(name = "post_summary_slug")
  String postSummarySlug;

  @Column(name = "post_title", length = 10000)
  private String title;

  @Column(name = "post_status")
  @Enumerated(EnumType.STRING)
  private PublishStatus status;

  @Column(name = "post_category")
  @Enumerated(EnumType.STRING)
  private PostCategory category;

  @Column(name = "post_sub_category")
  @Enumerated(EnumType.STRING)
  private PostSubCategory postSubCategory;

  @Column(name = "read_time")
  private String readTime;

  @Column(name = "brief")
  private String brief;

  /**
   * For displaying date time on screen
   */
  @Column(name = "post_date")
  private int postDate;

  @Column(name = "post_month")
  private String postMonth;

  @Column(name = "post_year")
  private int postYear;

  @JsonIgnoreProperties("relatedPosts")
  @ManyToMany(mappedBy = "relatedPosts", fetch = FetchType.LAZY)
  private Set<Post> posts;

  @Column(name = "post_image_url")
  private String imageUrl;

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
   * @return the category
   */
  public PostCategory getCategory() {
    return category;
  }

  /**
   * @param category the category to set
   */
  public void setCategory(PostCategory category) {
    this.category = category;
  }

  /**
   * @return the postSubCategory
   */
  public PostSubCategory getPostSubCategory() {
    return postSubCategory;
  }

  /**
   * @param postSubCategory the postSubCategory to set
   */
  public void setPostSubCategory(PostSubCategory postSubCategory) {
    this.postSubCategory = postSubCategory;
  }

  /**
   * @return the readTime
   */
  public String getReadTime() {
    return readTime;
  }

  /**
   * @param readTime the readTime to set
   */
  public void setReadTime(String readTime) {
    this.readTime = readTime;
  }

  /**
   * @return the brief
   */
  public String getBrief() {
    return brief;
  }

  /**
   * @param brief the brief to set
   */
  public void setBrief(String brief) {
    this.brief = brief;
  }

  /**
   * @return the postDate
   */
  public int getPostDate() {
    return postDate;
  }

  /**
   * @param postDate the postDate to set
   */
  public void setPostDate(int postDate) {
    this.postDate = postDate;
  }

  /**
   * @return the postMonth
   */
  public String getPostMonth() {
    return postMonth;
  }

  /**
   * @param postMonth the postMonth to set
   */
  public void setPostMonth(String postMonth) {
    this.postMonth = postMonth;
  }

  /**
   * @return the postYear
   */
  public int getPostYear() {
    return postYear;
  }

  /**
   * @param postYear the postYear to set
   */
  public void setPostYear(int postYear) {
    this.postYear = postYear;
  }

  /**
   * @return the posts
   */
  public Set<Post> getPosts() {
    return posts;
  }

  /**
   * @param posts the posts to set
   */
  public void setPosts(Set<Post> posts) {
    this.posts = posts;
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

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /**
   * @return the postSummarySlug
   */
  public String getPostSummarySlug() {
    return postSummarySlug;
  }

  /**
   * @param postSummarySlug the postSummarySlug to set
   */
  public void setPostSummarySlug(String postSummarySlug) {
    this.postSummarySlug = postSummarySlug;
  }

  /**
   * @param postSummaryId the postSummaryId to set
   */
  public void setPostSummaryId(int postSummaryId) {
    this.postSummaryId = postSummaryId;
  }

  /**
   * @return the postSummaryId
   */
  public int getPostSummaryId() {
    return postSummaryId;
  }
}
