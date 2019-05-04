package com.neostencil.dtos;

import java.util.List;
import java.util.Set;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.*;

/**
 * For passing the details of an institute over the network
 * 
 * @author shilpa
 *
 */
public class InstituteDetailedDTO {

  private int id;

  private String name;

  private String url;

  private String description;

  private String image;

  private String imageUrl;

  private String imageAltText;

  private Set<TeacherSummaryDTO> teachers;

  private String emailId;

  private String ownerName;

  private String address;

  private String contactNo;

  private List<Highlights> instituteRelateds;

  private List<Highlights> instituteHighlights;

  private List<Achievements> instituteAchievements;

  private String facebookUrl;

  private String linkedinUrl;

  private String twitterUrl;

  private String googlePlusUrl;
  private String otherInformation;

  private String instituteSlug;


  private PublishStatus status;

  private List<MetaInformation> metaList;

  private FeaturedImage featuredImage;

  private BreadCrumb breadCrumb;

  private Set<CourseSummaryDTO> courses;

  private String titleTag;

  private String bannerImage;

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
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the image
   */
  public String getImage() {
    return image;
  }

  /**
   * @param image the image to set
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * @return the imageUrl
   */
  public String getImageUrl() {
    return imageUrl;
  }

  /**
   * @param imageUrl the imageUrl to set
   */
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
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
   * @return the teachers
   */
  public Set<TeacherSummaryDTO> getTeachers() {
    return teachers;
  }

  /**
   * @param teachers the teachers to set
   */
  public void setTeachers(Set<TeacherSummaryDTO> teachers) {
    this.teachers = teachers;
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

  /**
   * @return the ownerName
   */
  public String getOwnerName() {
    return ownerName;
  }

  /**
   * @param ownerName the ownerName to set
   */
  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
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

  /**
   * @return the instituteAchievements
   */
  public List<Achievements> getInstituteAchievements() {
    return instituteAchievements;
  }

  /**
   * @param instituteAchievements the instituteAchievements to set
   */

  public void setInstituteAchievements(List<Achievements> instituteAchievements) {
    this.instituteAchievements = instituteAchievements;
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
   * @return the otherInformation
   */
  public String getOtherInformation() {
    return otherInformation;
  }

  /**
   * @param otherInformation the otherInformation to set
   */
  public void setOtherInformation(String otherInformation) {
    this.otherInformation = otherInformation;
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
   * @return the instituteRelateds
   */
  public List<Highlights> getInstituteRelateds() {
    return instituteRelateds;
  }

  /**
   * @param instituteRelateds the instituteRelateds to set
   */
  public void setInstituteRelateds(List<Highlights> instituteRelateds) {
    this.instituteRelateds = instituteRelateds;
  }

  /**
   * @return the instituteHighlights
   */
  public List<Highlights> getInstituteHighlights() {
    return instituteHighlights;
  }

  /**
   * @param instituteHighlights the instituteHighlights to set
   */
  public void setInstituteHighlights(List<Highlights> instituteHighlights) {
    this.instituteHighlights = instituteHighlights;
  }

  public String getBannerImage() {
    return bannerImage;
  }

  public void setBannerImage(String bannerImage) {
    this.bannerImage = bannerImage;
  }
}
