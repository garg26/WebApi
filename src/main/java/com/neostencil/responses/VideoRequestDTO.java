package com.neostencil.responses;

import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Column;
import com.neostencil.common.enums.VideoRequestStatusType;
import com.neostencil.common.enums.VideoRequestType;

/**
 * 
 * @author shilpa
 *
 */
public class VideoRequestDTO {

  private long id;

  private String sourceVideoPath;

  private Set<VideoChunksDTO> chunks;

  private VideoRequestType requestType;

  private Timestamp requestCreationDate;

  private Timestamp requestApprovalDate;

  private String requestCreatedBy;

  private String approver;

  private VideoRequestStatusType requestStatus;

  private String destinationVideoPath;

  private boolean free;

  private boolean deleteOriginal;

  private String requestReason;

  private String approverComments;

  private int unitId;

  private String oldWowzaLink;

  private String newWowzaLink;
  
  private String title;
  
  private String courseName;
  
  private String batchName;
  
  private String instituteName;
  
  private String teacherName;
 

  public VideoRequestDTO(long id, String sourceVideoPath, VideoRequestType requestType,
      Timestamp requestCreationDate, Timestamp requestApprovalDate, String requestCreatedBy,
      String approver, VideoRequestStatusType requestStatus, String destinationVideoPath,
      boolean free, boolean deleteOriginal, String requestReason, String approverComments,
      int unitId, String oldWowzaLink, String newWowzaLink,String courseName,String batchName,String instituteName,String teacherName,String title ) {
    super();
    this.id = id;
    this.sourceVideoPath = sourceVideoPath;
    this.requestType = requestType;
    this.requestCreationDate = requestCreationDate;
    this.requestApprovalDate = requestApprovalDate;
    this.requestCreatedBy = requestCreatedBy;
    this.approver = approver;
    this.requestStatus = requestStatus;
    this.destinationVideoPath = destinationVideoPath;
    this.free = free;
    this.deleteOriginal = deleteOriginal;
    this.requestReason = requestReason;
    this.approverComments = approverComments;
    this.oldWowzaLink=oldWowzaLink;
    this.newWowzaLink=newWowzaLink;
    this.unitId = unitId;
    this.courseName = courseName;
    this.batchName=batchName;
    this.instituteName=instituteName;
    this.teacherName = teacherName;
    this.title=title;
  }

  public VideoRequestDTO() {
    super();
  }

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
   * @return the chunks
   */
  public Set<VideoChunksDTO> getChunks() {
    return chunks;
  }

  /**
   * @param chunks the chunks to set
   */
  public void setChunks(Set<VideoChunksDTO> chunks) {
    this.chunks = chunks;
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

}
