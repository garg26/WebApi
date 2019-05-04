package com.neostencil.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.PostCategory;
import com.neostencil.common.enums.PostSubCategory;
import com.neostencil.common.enums.PostType;
import com.neostencil.common.enums.PublishStatus;

@Entity
@Indexed
@Table(name = "ns_posts")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post extends DomainObject {

  @Id
  @Column(name = "post_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
  @SequenceGenerator(name = "post_seq", sequenceName = "post_seq", allocationSize = 1)
  private int postId;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "post_slug", length = 20000)
  private String postSlug;

  @Column(name = "temp_id")
  private int tempId;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "post_title", length = 10000)
  private String title;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "post_text", columnDefinition = "TEXT", length = 200000)
  private String text;

  @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
  @Column(name = "post_status")
  @Enumerated(EnumType.STRING)
  private PublishStatus status;

  @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
  @Column(name = "post_category", length = 600)
  @Enumerated(EnumType.STRING)
  private PostCategory category;

  @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
  @Column(name = "post_sub_category", length = 800)
  @Enumerated(EnumType.STRING)
  private PostSubCategory postSubCategory;

  @Column(name = "is_featured")
  private boolean featured;

  @Column(name = "is_popular")
  private boolean popular;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "read_time")
  private String readTime;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "brief", columnDefinition = "TEXT")
  private String brief;

  @Column(name = "video_url", length = 20000)
  private String videoUrl;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "no_of_views")
  private int noOfViews;

  @Column(name = "post_type", length = 5000)
  @Enumerated(EnumType.STRING)
  private PostType type;

  /**
   * For displaying date time on screen
   */
  @Column(name = "post_date")
  private int postDate;

  @Column(name = "post_month")
  private String postMonth;

  @Column(name = "post_year")
  private int postYear;

  @Enumerated(EnumType.STRING)
  @Column(name = "exam_segment")
  private ExamSegmentTypes examSegment;
  /**
   * Comment By Kartikeya Garg. No need to use this feature
   */
  // @OneToOne(mappedBy = "previousParentPost", cascade = CascadeType.ALL)
  // private CustomPost previousPost;
  //
  // @OneToOne(mappedBy = "nextParentPost", cascade = CascadeType.ALL)
  // private CustomPost nextPost;

  @JsonIgnoreProperties("posts")
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "ns_related_posts", joinColumns = {
      @JoinColumn(name = "post_id", referencedColumnName = "post_id")}, inverseJoinColumns = {
      @JoinColumn(name = "post_summary_id", referencedColumnName = "post_summary_id")})
  private Set<PostSummary> relatedPosts;

  @JsonManagedReference(value = "meta-reference")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn(name = "sequence", updatable = true)
  private List<PostMetaInformation> metaList;

  @IndexedEmbedded
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "featured_image_id")
  private FeaturedImage featuredImage;

  @JsonManagedReference(value = "widget-reference")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn(name = "sequence", updatable = true)
  private List<PostWidgetImages> widgets = new ArrayList<PostWidgetImages>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "breadcrumb_id")
  private BreadCrumb breadCrumb;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "post_tags")
  private String tags;

  @Column(name = "title_tag")
  private String titleTag;

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
   * @return the tempId
   */
  public int getTempId() {
    return tempId;
  }

  /**
   * @param tempId the tempId to set
   */
  public void setTempId(int tempId) {
    this.tempId = tempId;
  }

  /**
   * @return the featured
   */
  public boolean isFeatured() {
    return featured;
  }

  /**
   * @param featured the featured to set
   */
  public void setFeatured(boolean featured) {
    this.featured = featured;
  }

  /**
   * @return the popular
   */
  public boolean isPopular() {
    return popular;
  }

  /**
   * @param popular the popular to set
   */
  public void setPopular(boolean popular) {
    this.popular = popular;
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
   * @return the videoUrl
   */
  public String getVideoUrl() {
    return videoUrl;
  }

  /**
   * @param videoUrl the videoUrl to set
   */
  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  /**
   * @return the noOfViews
   */
  public int getNoOfViews() {
    return noOfViews;
  }

  /**
   * @param noOfViews the noOfViews to set
   */
  public void setNoOfViews(int noOfViews) {
    this.noOfViews = noOfViews;
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
   * @return the previousPost
   */
  // public CustomPost getPreviousPost() {
  // return previousPost;
  // }
  //
  // /**
  // * @param previousPost the previousPost to set
  // */
  // public void setPreviousPost(CustomPost previousPost) {
  // this.previousPost = previousPost;
  // }
  //
  // /**
  // * @return the nextPost
  // */
  // public CustomPost getNextPost() {
  // return nextPost;
  // }
  //
  // /**
  // * @param nextPost the nextPost to set
  // */
  // public void setNextPost(CustomPost nextPost) {
  // this.nextPost = nextPost;
  // }

  /**
   * @return the relatedPosts
   */
  public Set<PostSummary> getRelatedPosts() {
    return relatedPosts;
  }

  /**
   * @param relatedPosts the relatedPosts to set
   */
  public void setRelatedPosts(Set<PostSummary> relatedPosts) {
    this.relatedPosts = relatedPosts;
  }

  /**
   * @return the type
   */
  public PostType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(PostType type) {
    this.type = type;
  }

  /**
   * @return the featuredImage
   */
  public FeaturedImage getFeaturedImage() {
    return featuredImage;
  }

  /**
   * @param featuredImage the featuredImage to set
   */
  public void setFeaturedImage(FeaturedImage featuredImage) {
    this.featuredImage = featuredImage;
  }

  /**
   * @return the breadcrumb
   */
  public BreadCrumb getBreadCrumb() {
    return breadCrumb;
  }

  /**
   * @param breadCrumb the breadCrumb to set
   */
  public void setBreadCrumb(BreadCrumb breadCrumb) {
    this.breadCrumb = breadCrumb;
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

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  /**
   * @return the titleTag
   */
  public String getTitleTag() {
    return titleTag;
  }

  /**
   * @param titleTag the titleTag to set
   */
  public void setTitleTag(String titleTag) {
    this.titleTag = titleTag;
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
   * @return the postSlug
   */
  public String getPostSlug() {
    return postSlug;
  }

  /**
   * @param postSlug the postSlug to set
   */
  public void setPostSlug(String postSlug) {
    this.postSlug = postSlug;
  }

  /**
   * @return the postId
   */
  public int getPostId() {
    return postId;
  }

  /**
   * @param postId the postId to set
   */
  public void setPostId(int postId) {
    this.postId = postId;
  }

  /**
   * @return the metaList
   */
  public List<PostMetaInformation> getMetaList() {
    return metaList;
  }

  /**
   * @param metaList the metaList to set
   */
  public void setMetaList(List<PostMetaInformation> metaList) {
    this.metaList = metaList;
  }

  /**
   * @return the widgets
   */
  public List<PostWidgetImages> getWidgets() {
    return widgets;
  }

  /**
   * @param widgets the widgets to set
   */
  public void setWidgets(List<PostWidgetImages> widgets) {
    this.widgets = widgets;
  }

}
