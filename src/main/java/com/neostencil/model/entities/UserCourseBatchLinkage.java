package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.UserCourseLinkageStatus;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ns_user_batch_linkages")
public class UserCourseBatchLinkage extends DomainObject {

  @Id
  @Column(name = "linkage_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_user_linkage_seq")
  @SequenceGenerator(name = "course_user_linkage_seq", sequenceName = "course_user_linkage_seq",
      allocationSize = 1)
  int linkageId;

  //@JsonBackReference(value = "user-linkage")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @JsonIgnoreProperties(value = "userCourseBatchLinkages")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "batch_id")
  private CourseBatch courseBatch;

  @Column(name = "expiry_date")
  private Timestamp expiryDate;

  @Column(name = "enrolled_on")
  private Timestamp enrolledOn;

  @Column(name = "paused_on")
  private Timestamp pausedOn;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UserCourseLinkageStatus status;

  @Column(name = "restricted_access", columnDefinition = "boolean default false")
  private boolean restrictedAccess;

  /**
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * @return the courseBatch
   */
  public CourseBatch getCourseBatch() {
    return courseBatch;
  }

  /**
   * @param courseBatch the courseBatch to set
   */
  public void setCourseBatch(CourseBatch courseBatch) {
    this.courseBatch = courseBatch;
  }


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
   * @return the pausedOn
   */
  public Timestamp getPausedOn() {
    return pausedOn;
  }

  /**
   * @param pausedOn the pausedOn to set
   */
  public void setPausedOn(Timestamp pausedOn) {
    this.pausedOn = pausedOn;
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
