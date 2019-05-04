package com.neostencil.requests;

import java.sql.Timestamp;
import java.util.Set;
import com.neostencil.common.enums.VideoRequestStatusType;
import com.neostencil.common.enums.VideoRequestType;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.VideoChunks;

/**
 * 
 * @author shilpa
 *
 */
public class VideoRequestObject {

  private String sourceVideoPath;

  private Set<VideoChunks> chunks;

  private VideoRequestType requestType;

  private Timestamp requestCreationDate;

  private Timestamp requestApprovalDate;

  private User requestCreatedBy;

  private User requestApprovedBy;

  private VideoRequestStatusType requestStatus;

  private String destinationVideoPath;

  private boolean free;

  private boolean deleteOriginal;
  
  private boolean updateUnitAuto;

  private String requestReason;

  private String approverComments;
  
  private int unitId;

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
   * @return the requestCreatedBy
   */
  public User getRequestCreatedBy() {
    return requestCreatedBy;
  }

  /**
   * @param requestCreatedBy the requestCreatedBy to set
   */
  public void setRequestCreatedBy(User requestCreatedBy) {
    this.requestCreatedBy = requestCreatedBy;
  }

  /**
   * @return the requestApprovedBy
   */
  public User getRequestApprovedBy() {
    return requestApprovedBy;
  }

  /**
   * @param requestApprovedBy the requestApprovedBy to set
   */
  public void setRequestApprovedBy(User requestApprovedBy) {
    this.requestApprovedBy = requestApprovedBy;
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
   * @return the updateUnitAuto
   */
  public boolean isUpdateUnitAuto() {
    return updateUnitAuto;
  }

  /**
   * @param updateUnitAuto the updateUnitAuto to set
   */
  public void setUpdateUnitAuto(boolean updateUnitAuto) {
    this.updateUnitAuto = updateUnitAuto;
  }

}
