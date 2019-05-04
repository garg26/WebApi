package com.neostencil.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.PublishStatus;

@Entity
@Indexed
@Table(name = "ns_institutes")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Institute extends DomainObject {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "institute_gen")
  @TableGenerator(name = "institute_gen", table = "ns_institute_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "InstituteId_Gen", initialValue = 100,
      allocationSize = 1)
  @Column(name = "institute_id")
  @Id
  private int id;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_name", length = 2000)
  private String name;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_url", length = 1000)
  private String url;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_description", length = 20000)
  private String description;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_image", length = 2000)
  private String image;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_image_url", length = 2000)
  private String imageUrl;

  @Column(name = "institute_image_alt_text")
  private String imageAltText;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "institute")
  @OrderBy("position asc")
  private Set<TeacherDetails> teachers;

  @Column(name = "email_id", length = 1000)
  private String emailId;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "owner_name", length = 1000)
  private String ownerName;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_address", length = 10000)
  private String address;

  @Column(name = "contact_no")
  private String contactNo;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_facebook_url", length = 1000)
  private String facebookUrl;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_linkedin_url", length = 1000)
  private String linkedinUrl;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_twitter_url", length = 1000)
  private String twitterUrl;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_google_plus_url", length = 1000)
  private String googlePlusUrl;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "other_information", length = 20000)
  private String otherInformation;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "institute_slug", length = 2000)
  private String instituteSlug;


  @Field(bridge = @FieldBridge(impl = EnumBridge.class),store=Store.YES)
  @Enumerated(EnumType.STRING)
  @Column(name = "institute_status")
  private PublishStatus status;

  @Fetch(value = FetchMode.SUBSELECT)
  @ElementCollection(fetch = FetchType.LAZY)
  @JoinTable(name = "ns_institute_meta_info", joinColumns = @JoinColumn(name = "institute_id"))
  @OrderColumn(name = "sequence", updatable = true)
  private List<MetaInformation> metaList;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "featured_image_id")
  private FeaturedImage featuredImage;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "breadcrumb_id")
  private BreadCrumb breadCrumb;

  /*
   * @JsonIgnore
   *
   * @JsonManagedReference(value = "course-institute-reference")
   */

  @JsonIgnoreProperties(value = "institute")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "institute")
  @OrderBy("position asc")
  private Set<Course> courses;

  @Column(name = "title_tag")
  private String titleTag;

  @Column(name = "banner_image")
  private String bannerImage;

  @Column(name = "institute_highlight",columnDefinition = "TEXT")
  private String highlights;

  @Column(name = "institute_related_info",columnDefinition = "TEXT")
  private String relatedInfo;

  @Column(name = "institute_achievement",columnDefinition = "TEXT")
  private String achievements;

  public String getHighlights() {
    return highlights;
  }

  public void setHighlights(String highlights) {
    this.highlights = highlights;
  }

  public String getRelatedInfo() {
    return relatedInfo;
  }

  public void setRelatedInfo(String relatedInfo) {
    this.relatedInfo = relatedInfo;
  }

  public String getAchievements() {
    return achievements;
  }

  public void setAchievements(String achievements) {
    this.achievements = achievements;
  }

  public Set<Course> getCourses() {
    return courses;
  }

  public void setCourses(Set<Course> courses) {
    this.courses = courses;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  /**
   * @return the teachers
   */
  public Set<TeacherDetails> getTeachers() {
    return teachers;
  }

  /**
   * @param teachers the teachers to set
   */
  public void setTeachers(Set<TeacherDetails> teachers) {
    this.teachers = teachers;
  }

  /**
   * @return the courses
   */

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContactNo() {
    return contactNo;
  }

  public void setContactNo(String contactNo) {
    this.contactNo = contactNo;
  }

  public String getFacebookUrl() {
    return facebookUrl;
  }

  public void setFacebookUrl(String facebookUrl) {
    this.facebookUrl = facebookUrl;
  }

  public String getLinkedinUrl() {
    return linkedinUrl;
  }

  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public String getTwitterUrl() {
    return twitterUrl;
  }

  public void setTwitterUrl(String twitterUrl) {
    this.twitterUrl = twitterUrl;
  }

  public String getGooglePlusUrl() {
    return googlePlusUrl;
  }

  public void setGooglePlusUrl(String googlePlusUrl) {
    this.googlePlusUrl = googlePlusUrl;
  }

  public String getOtherInformation() {
    return otherInformation;
  }

  public void setOtherInformation(String otherInformation) {
    this.otherInformation = otherInformation;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
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
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Institute other = (Institute) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

  /**
   * @return the instituteSlug
   */
  public String getInstituteSlug() {
    return instituteSlug;
  }

  /**
   * @param instituteSlug the instituteSlug to set
   */
  public void setInstituteSlug(String instituteSlug) {
    this.instituteSlug = instituteSlug;
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

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /**
   * @return the metaList
   */
  public List<MetaInformation> getMetaList() {
    return metaList;
  }

  /**
   * @param metaList the metaList to set
   */
  public void setMetaList(List<MetaInformation> metaList) {
    this.metaList = metaList;
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
   *
   * @return the breadcrumb
   */
  public BreadCrumb getBreadCrumb() {
    return breadCrumb;
  }

  /**
   *
   * @param breadCrumb the breadCrumb to set
   */
  public void setBreadCrumb(BreadCrumb breadCrumb) {
    this.breadCrumb = breadCrumb;
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

  public String getImageAltText() {
    return imageAltText;
  }

  public void setImageAltText(String imageAltText) {
    this.imageAltText = imageAltText;
  }

  public String getBannerImage() {
    return bannerImage;
  }

  public void setBannerImage(String bannerImage) {
    this.bannerImage = bannerImage;
  }
}
