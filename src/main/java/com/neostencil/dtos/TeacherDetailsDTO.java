package com.neostencil.dtos;

import java.util.List;
import java.util.Set;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.*;

/**
 * For sending teacher details over the network
 * 
 * @author shilpa
 *
 */
public class TeacherDetailsDTO {

  int id;
  private String teacherName;
  private String teacherDescription;
  private List<String> teacherGallery;
  private Set<CourseSummaryDTO> courses;
  private String subjects;
  private String teacherBio;
  private String slug;
  private int totalExperience;
  private String teacherLocation;
  private String displayPictureUrl;
  private String imageAltText;
  private List<Achievements> teacherAchievements;
  private List<Highlights> education;
  private List<Highlights> experience;
  private String address;
  private String contactNo;
  private String contactNoToDisplay;
  private String emailId;
  private String emailToDisplay;
  private String website;
  private String facebookUrl;
  private String twitterUrl;
  private String linkedinUrl;
  private String googlePlusUrl;
  private List<String> certificates;
  private float teacherRating;
  private int noOfReviewers;
  private int noOfStudents;
  private float commissionPercentage;
  private PublishStatus status;
  private List<MetaInformation> metaList;
  private FeaturedImage featuredImage;
  private String teacherExamSegment;
  private String teacherCategory;
  private BreadCrumb breadCrumb;
  private int position;
  private String titleTag;
  private InstituteDTO institute;
  private String currentStatus;

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
  public Set<CourseSummaryDTO> getCourses() {
    return courses;
  }

  /**
   * @param courses the courses to set
   */
  public void setCourses(Set<CourseSummaryDTO> courses) {
    this.courses = courses;
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
   * @return the displayPictureUrl
   */
  public String getDisplayPictureUrl() {
    return displayPictureUrl;
  }

  /**
   * @param displayPictureUrl the displayPictureUrl to set
   */
  public void setDisplayPictureUrl(String displayPictureUrl) {
    this.displayPictureUrl = displayPictureUrl;
  }

  /**
   * @return the imageAltText
   */
  public String getImageAltText() {
    return imageAltText;
  }

  /**
   * @param imageAltText the imageAltText to set
   */
  public void setImageAltText(String imageAltText) {
    this.imageAltText = imageAltText;
  }


  /**
   * @return the teacherAchievements
   */

  public List<Achievements> getTeacherAchievements() {
    return teacherAchievements;
  }

  /**
   * @param teacherAchievements the teacherAchievements to set
   */
  public void setTeacherAchievements(List<Achievements> teacherAchievements) {
    this.teacherAchievements = teacherAchievements;
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
   * @return the teacherExamSegment
   */
  public String getTeacherExamSegment() {
    return teacherExamSegment;
  }

  /**
   * @param teacherExamSegment the teacherExamSegment to set
   */
  public void setTeacherExamSegment(String teacherExamSegment) {
    this.teacherExamSegment = teacherExamSegment;
  }

  /**
   * @return the teacherCategory
   */
  public String getTeacherCategory() {
    return teacherCategory;
  }

  /**
   * @param teacherCategory the teacherCategory to set
   */
  public void setTeacherCategory(String teacherCategory) {
    this.teacherCategory = teacherCategory;
  }

  /**
   * @return the breadCrumb
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
   * @return the position
   */
  public int getPosition() {
    return position;
  }

  /**
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

  /**
   * @return the institute
   */
  public InstituteDTO getInstitute() {
    return institute;
  }

  /**
   * @param institute the institute to set
   */
  public void setInstitute(InstituteDTO institute) {
    this.institute = institute;
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
   * @return the education
   */
  public List<Highlights> getEducation() {
    return education;
  }

  /**
   * @param education the education to set
   */
  public void setEducation(List<Highlights> education) {
    this.education = education;
  }

  /**
   * @return the experience
   */
  public List<Highlights> getExperience() {
    return experience;
  }

  /**
   * @param experience the experience to set
   */
  public void setExperience(List<Highlights> experience) {
    this.experience = experience;
  }


}
