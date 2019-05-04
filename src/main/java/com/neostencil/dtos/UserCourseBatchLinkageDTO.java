package com.neostencil.dtos;

import java.sql.Timestamp;
import com.neostencil.common.enums.UserCourseLinkageStatus;

public class UserCourseBatchLinkageDTO {

  private int linkageId;

  private UserDTO user;

  private CourseBatchDTO courseBatch;

  private Timestamp expiryDate;
  private Timestamp enrolledOn;
  private UserCourseLinkageStatus status;
  private boolean restrictedAccess;

  /**
   * @return the linkageId
   */
  public int getLinkageId() {
    return linkageId;
  }

  /**
   * @param linkageId the linkageId to set
   */
  public void setLinkageId(int linkageId) {
    this.linkageId = linkageId;
  }

  /**
   * @return the user
   */
  public UserDTO getUser() {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(UserDTO user) {
    this.user = user;
  }

  /**
   * @return the courseBatch
   */
  public CourseBatchDTO getCourseBatch() {
    return courseBatch;
  }

  /**
   * @param courseBatch the courseBatch to set
   */
  public void setCourseBatch(CourseBatchDTO courseBatch) {
    this.courseBatch = courseBatch;
  }

  /**
   * @return the expiryDate
   */
  public Timestamp getExpiryDate() {
    return expiryDate;
  }

  /**
   * @param expiryDate the expiryDate to set
   */
  public void setExpiryDate(Timestamp expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * @return the enrolledOn
   */
  public Timestamp getEnrolledOn() {
    return enrolledOn;
  }

  /**
   * @param enrolledOn the enrolledOn to set
   */
  public void setEnrolledOn(Timestamp enrolledOn) {
    this.enrolledOn = enrolledOn;
  }

  /**
   * @return the status
   */
  public UserCourseLinkageStatus getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(UserCourseLinkageStatus status) {
    this.status = status;
  }

  /**
   * @return the restrictedAccess
   */
  public boolean isRestrictedAccess() {
    return restrictedAccess;
  }

  /**
   * @param restrictedAccess the restrictedAccess to set
   */
  public void setRestrictedAccess(boolean restrictedAccess) {
    this.restrictedAccess = restrictedAccess;
  }



}
