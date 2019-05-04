package com.neostencil.model.entities;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.TeacherCategory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Indexed
@Table(name = "ns_teacher_details")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class TeacherDetails extends DomainObject {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "teacher_details_gen")
  @TableGenerator(name = "teacher_details_gen", table = "ns_teacher_details_id_gen",
      pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "teacher_details_Gen",
      initialValue = 100, allocationSize = 1)
  @Id
  int id;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "teacher_name")
  private String teacherName;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "teacher_description", columnDefinition = "TEXT")
  private String teacherDescription;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "ns_teacher_photo_gallery",
      joinColumns = @JoinColumn(name = "teacher_id"))
  @Column(name = "photo_gallery")
  @OrderColumn(name = "sequence", updatable = true)
  private List<String> teacherGallery = new ArrayList<String>();

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "instructors")
  @OrderBy("position asc")
  private Set<Course> courses = new LinkedHashSet<Course>();

  /**
   * Represents the subjects , a teacher is going to teach(Comma Separated).
   */
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "subjects", length = 20000)
  private String subjects;

  @JsonBackReference
  @IndexedEmbedded
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "institute_id")
  private Institute institute;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User userAccount;
  /**
   * Short description about the teacher and his/her achievements.
   */
  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "teacher_bio", columnDefinition = "TEXT")
  private String teacherBio;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "teacher_slug")
  private String slug;

  @Column(name = "total_experience")
  private int totalExperience;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "teacher_location", length = 1000)
  private String teacherLocation;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "display_picture", length = 1000)
  private String displayPicture;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "display_picture_url")
  private String displayPictureUrl;

  @Column(name = "teacher_image_alt_text")
  private String imageAltText;

  @Column(name = "address", length = 20000)
  private String address;

  @Column(name = "contact_no")
  private String contactNo;

  @Column(name = "contact_no_to_display")
  private String contactNoToDisplay;

  @Column(name = "teacher_email_id")
  private String emailId;

  @Column(name = "teacher_email_to_display")
  private String emailToDisplay;

  @Column(name = "teacher_website")
  private String website;

  @Column(name = "facebook_url", length = 1000)
  private String facebookUrl;

  @Column(name = "twitter_url", length = 1000)
  private String twitterUrl;

  @Column(name = "linkedin_url", length = 1000)
  private String linkedinUrl;

  @Column(name = "google_plus_url", length = 1000)
  private String googlePlusUrl;

  @Column(name = "current_status", columnDefinition = "TEXT")
  private String currentStatus;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "ns_teacher_certificate", joinColumns = @JoinColumn(name = "teacher_id"))
  @Column(name = "certificate_name")
  @OrderColumn(name = "sequence", updatable = true)
  private List<String> certificates = new ArrayList<String>();

  @Column(name = "teacher_rating")
  private float teacherRating;

  // To be taken from the admin dashboard
  @Column(name = "no_of_reviewers")
  private int noOfReviewers;

  // To be updated everytime a teacher is updated
  @Column(name = "no_of_students")
  private int noOfStudents;

  @Column(name = "commission_percentage")
  private float commissionPercentage;

  @Field(bridge = @FieldBridge(impl = EnumBridge.class), store = Store.YES)
  @Enumerated(EnumType.STRING)
  @Column(name = "publish_status")
  private PublishStatus status;

  @Fetch(value = FetchMode.SUBSELECT)
  @ElementCollection(fetch = FetchType.LAZY)
  @JoinTable(name = "ns_teacher_meta_info", joinColumns = @JoinColumn(name = "teacher_id"))
  @OrderColumn(name = "sequence", updatable = true)
  private List<MetaInformation> metaList;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "featured_image_id")
  private FeaturedImage featuredImage;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "exam_segment")
  private String teacherExamSegment;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "category")
  private String teacherCategory;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "breadcrumb_id")
  private BreadCrumb breadCrumb;

  @Column(name = "teacher_position")
  private int position;

  @Column(name = "title_tag")
  private String titleTag;
  
  @Column(name="is_receive_queries")
  private boolean receiveQueries;

  @Column(name = "teacher_achievement",columnDefinition = "TEXT")
  private String achievements;

  @Column(name = "teacher_education",columnDefinition = "TEXT")
  private String educations;

  @Column(name = "teacher_experience",columnDefinition = "TEXT")
  private String experiences;

  /**
   * @return the teacherName
   */
  public String getTeacherName() {
    return teacherName;
  }

  /**
   * @param teacherName the teacherName to set
   */
  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }

  /**
   * @return the teacherDescription
   */
  public String getTeacherDescription() {
    return teacherDescription;
  }

  /**
   * @param teacherDescription the teacherDescription to set
   */
  public void setTeacherDescription(String teacherDescription) {
    this.teacherDescription = teacherDescription;
  }

  /**
   * @return the teacherGallery
   */
  public List<String> getTeacherGallery() {
    return teacherGallery;
  }

  /**
   * @param teacherGallery the teacherGallery to set
   */
  public void setTeacherGallery(List<String> teacherGallery) {
    this.teacherGallery = teacherGallery;
  }

  /**
   * @return the courses
   */
  public Set<Course> getCourses() {
    return courses;
  }

  /**
   * @param courses the courses to set
   */
  public void setCourses(Set<Course> courses) {
    this.courses = courses;
  }

  /**
   * @return the institute
   */
  public Institute getInstitute() {
    return institute;
  }

  /**
   * @param institute the institute to set
   */
  public void setInstitute(Institute institute) {
    this.institute = institute;
  }

  /**
   * @return the teacherBio
   */
  public String getTeacherBio() {
    return teacherBio;
  }

  /**
   * @param teacherBio the teacherBio to set
   */
  public void setTeacherBio(String teacherBio) {
    this.teacherBio = teacherBio;
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
   * @return the totalExperience
   */
  public int getTotalExperience() {
    return totalExperience;
  }

  /**
   * @param totalExperience the totalExperience to set
   */
  public void setTotalExperience(int totalExperience) {
    this.totalExperience = totalExperience;
  }

  /**
   * @return the teacherLocation
   */
  public String getTeacherLocation() {
    return teacherLocation;
  }

  /**
   * @param teacherLocation the teacherLocation to set
   */
  public void setTeacherLocation(String teacherLocation) {
    this.teacherLocation = teacherLocation;
  }

  /**
   * @return the displayPicture
   */
  public String getDisplayPicture() {
    return displayPicture;
  }

  /**
   * @param displayPicture the displayPicture to set
   */
  public void setDisplayPicture(String displayPicture) {
    this.displayPicture = displayPicture;
  }


  /**
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param address the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * @return the contactNo
   */
  public String getContactNo() {
    return contactNo;
  }

  /**
   * @param contactNo the contactNo to set
   */
  public void setContactNo(String contactNo) {
    this.contactNo = contactNo;
  }

  public String getContactNoToDisplay() {
    return contactNoToDisplay;
  }

  public void setContactNoToDisplay(String contactNoToDisplay) {
    this.contactNoToDisplay = contactNoToDisplay;
  }

  /**
   * @return the emailId
   */
  public String getEmailId() {
    return emailId;
  }

  /**
   * @param emailId the emailId to set
   */
  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getEmailToDisplay() {
    return emailToDisplay;
  }

  public void setEmailToDisplay(String emailToDisplay) {
    this.emailToDisplay = emailToDisplay;
  }

  /**
   * @return the website
   */
  public String getWebsite() {
    return website;
  }

  /**
   * @param website the website to set
   */
  public void setWebsite(String website) {
    this.website = website;
  }

  /**
   * @return the facebookUrl
   */
  public String getFacebookUrl() {
    return facebookUrl;
  }

  /**
   * @param facebookUrl the facebookUrl to set
   */
  public void setFacebookUrl(String facebookUrl) {
    this.facebookUrl = facebookUrl;
  }

  /**
   * @return the twitterUrl
   */
  public String getTwitterUrl() {
    return twitterUrl;
  }

  /**
   * @param twitterUrl the twitterUrl to set
   */
  public void setTwitterUrl(String twitterUrl) {
    this.twitterUrl = twitterUrl;
  }

  /**
   * @return the linkedinUrl
   */
  public String getLinkedinUrl() {
    return linkedinUrl;
  }

  /**
   * @param linkedinUrl the linkedinUrl to set
   */
  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  /**
   * @return the googlePlusUrl
   */
  public String getGooglePlusUrl() {
    return googlePlusUrl;
  }

  /**
   * @param googlePlusUrl the googlePlusUrl to set
   */
  public void setGooglePlusUrl(String googlePlusUrl) {
    this.googlePlusUrl = googlePlusUrl;
  }

  /**
   * @return the currentStatus
   */
  public String getCurrentStatus() {
    return currentStatus;
  }

  /**
   * @param currentStatus the currentStatus to set
   */
  public void setCurrentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
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

  /**
   * @return the subjects
   */
  public String getSubjects() {
    return subjects;
  }

  /**
   * @param subjects the subjects to set
   */
  public void setSubjects(String subjects) {
    this.subjects = subjects;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TeacherDetails other = (TeacherDetails) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

  /**
   * @return the teacherRating
   */
  public float getTeacherRating() {
    return teacherRating;
  }

  /**
   * @param teacherRating the teacherRating to set
   */
  public void setTeacherRating(float teacherRating) {
    this.teacherRating = teacherRating;
  }

  /**
   * @return the noOfReviewers
   */
  public int getNoOfReviewers() {
    return noOfReviewers;
  }

  /**
   * @param noOfReviewers the noOfReviewers to set
   */
  public void setNoOfReviewers(int noOfReviewers) {
    this.noOfReviewers = noOfReviewers;
  }

  /**
   * @return the noOfStudents
   */
  public int getNoOfStudents() {
    return noOfStudents;
  }

  /**
   * @param noOfStudents the noOfStudents to set
   */
  public void setNoOfStudents(int noOfStudents) {
    this.noOfStudents = noOfStudents;
  }


  /**
   * @return the certificates
   */
  public List<String> getCertificates() {
    return certificates;
  }

  /**
   * @param certificates the certificates to set
   */
  public void setCertificates(List<String> certificates) {
    this.certificates = certificates;
  }

  /**
   * @return the commissionPercentage
   */
  public float getCommissionPercentage() {
    return commissionPercentage;
  }

  /**
   * @param commissionPercentage the commissionPercentage to set
   */
  public void setCommissionPercentage(float commissionPercentage) {
    this.commissionPercentage = commissionPercentage;
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
   *
   * @return displayPictureUrl
   */
  public String getDisplayPictureUrl() {
    return displayPictureUrl;
  }

  /**
   *
   * @param displayPictureUrl
   */
  public void setDisplayPictureUrl(String displayPictureUrl) {
    this.displayPictureUrl = displayPictureUrl;
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
   *
   * @return the teacherExamSegment
   */
  public String getTeacherExamSegment() {
    return teacherExamSegment;
  }

  /**
   *
   * @param teacherExamSegment
   */
  public void setTeacherExamSegment(String teacherExamSegment) {
    this.teacherExamSegment = teacherExamSegment;
  }

  /**
   *
   * @return teacherCategory
   */
  public String getTeacherCategory() {
    return teacherCategory;
  }

  /**
   *
   * @param teacherCategory
   */
  public void setTeacherCategory(String teacherCategory) {
    this.teacherCategory = teacherCategory;
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
   * @return the breadCrumb
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
   *
   * @return the position
   */
  public int getPosition() {
    return position;
  }

  /**
   *
   * @param position the position to set
   */
  public void setPosition(int position) {
    this.position = position;
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

  public User getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(User userAccount) {
    this.userAccount = userAccount;
  }

  /**
   * @return the receiveQueries
   */
  public boolean isReceiveQueries() {
    return receiveQueries;
  }

  /**
   * @param receiveQueries the receiveQueries to set
   */
  public void setReceiveQueries(boolean receiveQueries) {
    this.receiveQueries = receiveQueries;
  }

  public String getAchievements() {
    return achievements;
  }

  public void setAchievements(String achievements) {
    this.achievements = achievements;
  }

  public String getEducations() {
    return educations;
  }

  public void setEducations(String educations) {
    this.educations = educations;
  }

  public String getExperiences() {
    return experiences;
  }

  public void setExperiences(String experiences) {
    this.experiences = experiences;
  }
}
