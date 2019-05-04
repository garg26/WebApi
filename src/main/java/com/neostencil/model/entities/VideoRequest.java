package com.neostencil.model.entities;

import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.neostencil.common.enums.VideoRequestStatusType;
import com.neostencil.common.enums.VideoRequestType;

/**
 * 
 * @author shilpa
 *
 */
@Entity
@Table(name = "ns_video_requests")
public class VideoRequest {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_request_seq")
  @SequenceGenerator(name = "video_request_seq", sequenceName = "video_request_seq",
      allocationSize = 1)
  private long id;

  @Column(name = "source_video_path")
  private String sourceVideoPath;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "videoRequest")
  private Set<VideoChunks> chunks;

  @Column(name = "request_type")
  @Enumerated(EnumType.STRING)
  private VideoRequestType requestType;

  @Column(name = "request_creation_date")
  private Timestamp requestCreationDate;

  @Column(name = "request_approval_date")
  private Timestamp requestApprovalDate;
  
  @Column(name="request_rejection_date")
  private Timestamp requestRejectionDate;

  @Column(name = "request_created_by")
  private String requestCreatedBy;

  @Column(name = "approver")
  private String approver;

  @Column(name = "request_status")
  @Enumerated(EnumType.STRING)
  private VideoRequestStatusType requestStatus;

  @Column(name = "destination_video_path")
  private String destinationVideoPath;

  @Column(name = "is_free")
  private boolean free;

  @Column(name = "delete_original")
  private boolean deleteOriginal;

  @Column(name = "request_reason", columnDefinition = "TEXT")
  private String requestReason;

  @Column(name = "approver_comments", columnDefinition = "TEXT")
  private String approverComments;

  @Column(name="unit_id")
  private int unitId;
  
  @Column(name="old_wowza_link")
  private String oldWowzaLink;
  
  @Column(name="new_wowza_link")
  private String newWowzaLink;
  
  /*@Column(name="update_unit_auto")
  private boolean updateUnitAuto;*/
  
  @Column(name="video_title", length=2000)
  String title;
  
  @Column(name="course_name",length=3000)
  String courseName;
  
  @Column(name="batch_name",length=2000)
  String batchName;
  
  @Column(name="institute_name",length=2000)
  String instituteName;
  
  @Column(name="teacher_name",length=2000)
  String teacherName;

  
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
   * @return the sourceVideoPath
   */
  public String getSourceVideoPath() {
    return sourceVideoPath;
  }

  /**
   * @param sourceVideoPath the sourceVideoPath to set
   */
  public void setSourceVideoPath(String sourceVideoPath) {
    this.sourceVideoPath = sourceVideoPath;
  }

  /**
   * @return the chunks
   */
  public Set<VideoChunks> getChunks() {
    return chunks;
  }

  /**
   * @param chunks the chunks to set
   */
  public void setChunks(Set<VideoChunks> chunks) {
    this.chunks = chunks;
  }

  /**
   * @return the requestType
   */
  public VideoRequestType getRequestType() {
    return requestType;
  }

  /**
   * @param requestType the requestType to set
   */
  public void setRequestType(VideoRequestType requestType) {
    this.requestType = requestType;
  }

  /**
   * @return the requestCreationDate
   */
  public Timestamp getRequestCreationDate() {
    return requestCreationDate;
  }

  /**
   * @param requestCreationDate the requestCreationDate to set
   */
  public void setRequestCreationDate(Timestamp requestCreationDate) {
    this.requestCreationDate = requestCreationDate;
  }

  /**
   * @return the requestApprovalDate
   */
  public Timestamp getRequestApprovalDate() {
    return requestApprovalDate;
  }

  /**
   * @param requestApprovalDate the requestApprovalDate to set
   */
  public void setRequestApprovalDate(Timestamp requestApprovalDate) {
    this.requestApprovalDate = requestApprovalDate;
  }

  /**
   * @return the requestStatus
   */
  public VideoRequestStatusType getRequestStatus() {
    return requestStatus;
  }

  /**
   * @param requestStatus the requestStatus to set
   */
  public void setRequestStatus(VideoRequestStatusType requestStatus) {
    this.requestStatus = requestStatus;
  }

  /**
   * @return the destinationVideoPath
   */
  public String getDestinationVideoPath() {
    return destinationVideoPath;
  }

  /**
   * @param destinationVideoPath the destinationVideoPath to set
   */
  public void setDestinationVideoPath(String destinationVideoPath) {
    this.destinationVideoPath = destinationVideoPath;
  }

  /**
   * @return the free
   */
  public boolean isFree() {
    return free;
  }

  /**
   * @param free the free to set
   */
  public void setFree(boolean free) {
    this.free = free;
  }

  /**
   * @return the deleteOriginal
   */
  public boolean isDeleteOriginal() {
    return deleteOriginal;
  }

  /**
   * @param deleteOriginal the deleteOriginal to set
   */
  public void setDeleteOriginal(boolean deleteOriginal) {
    this.deleteOriginal = deleteOriginal;
  }

  /**
   * @return the requestReason
   */
  public String getRequestReason() {
    return requestReason;
  }

  /**
   * @param requestReason the requestReason to set
   */
  public void setRequestReason(String requestReason) {
    this.requestReason = requestReason;
  }

  /**
   * @return the approverComments
   */
  public String getApproverComments() {
    return approverComments;
  }

  /**
   * @param approverComments the approverComments to set
   */
  public void setApproverComments(String approverComments) {
    this.approverComments = approverComments;
  }

  /**
   * @return the requestCreatedBy
   */
  public String getRequestCreatedBy() {
    return requestCreatedBy;
  }

  /**
   * @param requestCreatedBy the requestCreatedBy to set
   */
  public void setRequestCreatedBy(String requestCreatedBy) {
    this.requestCreatedBy = requestCreatedBy;
  }

  /**
   * @return the requestRejectionDate
   */
  public Timestamp getRequestRejectionDate() {
    return requestRejectionDate;
  }

  /**
   * @param requestRejectionDate the requestRejectionDate to set
   */
  public void setRequestRejectionDate(Timestamp requestRejectionDate) {
    this.requestRejectionDate = requestRejectionDate;
  }

  /**
   * @return the approver
   */
  public String getApprover() {
    return approver;
  }

  /**
   * @param approver the approver to set
   */
  public void setApprover(String approver) {
    this.approver = approver;
  }

  /**
   * @return the unitId
   */
  public int getUnitId() {
    return unitId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }

  /**
   * @return the oldWowzaLink
   */
  public String getOldWowzaLink() {
    return oldWowzaLink;
  }

  /**
   * @param oldWowzaLink the oldWowzaLink to set
   */
  public void setOldWowzaLink(String oldWowzaLink) {
    this.oldWowzaLink = oldWowzaLink;
  }

  /**
   * @return the newWowzaLink
   */
  public String getNewWowzaLink() {
    return newWowzaLink;
  }

  /**
   * @param newWowzaLink the newWowzaLink to set
   */
  public void setNewWowzaLink(String newWowzaLink) {
    this.newWowzaLink = newWowzaLink;
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
   * @return the courseName
   */
  public String getCourseName() {
    return courseName;
  }

  /**
   * @param courseName the courseName to set
   */
  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  /**
   * @return the batchName
   */
  public String getBatchName() {
    return batchName;
  }

  /**
   * @param batchName the batchName to set
   */
  public void setBatchName(String batchName) {
    this.batchName = batchName;
  }

  /**
   * @return the instituteName
   */
  public String getInstituteName() {
    return instituteName;
  }

  /**
   * @param instituteName the instituteName to set
   */
  public void setInstituteName(String instituteName) {
    this.instituteName = instituteName;
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
   * @return the updateUnitAuto
   */
  /*public boolean isUpdateUnitAuto() {
    return updateUnitAuto;
  }

  *//**
   * @param updateUnitAuto the updateUnitAuto to set
   *//*
  public void setUpdateUnitAuto(boolean updateUnitAuto) {
    this.updateUnitAuto = updateUnitAuto;
  }*/



}
