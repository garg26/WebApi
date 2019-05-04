package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.WatchStatus;

@Entity
@Table(name = "ns_user_unit_linkages")
public class UserUnitLinkage extends DomainObject {

  @Id
  @Column(name = "linkage_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_unit_linkage_seq")
  @SequenceGenerator(name = "user_unit_linkage_seq", sequenceName = "user_unit_linkage_seq",
      allocationSize = 1)
  int linkageId;


  @Column(name = "is_active")
  boolean active;

  @Column(name = "watch_status")
  @Enumerated(EnumType.STRING)
  WatchStatus watchStatus;

  /**
   * To track if the video is being misused or shared
   */
  @Column(name = "no_of_clicks")
  int noOfClicks;

  /**
   * This represents the time in the video where it was left the last time.
   */
  @Column(name = "resume_from")
  float resumeFrom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "unit_id")
  private Unit unit;

  @JsonIgnoreProperties
  @Embedded
  private AssignmentSubmission assignmentSubmission;

  /**
   * @return the active
   */
  public boolean isActive() {
    return active;
  }

  /**
   * @param active the active to set
   */
  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * @return the watchStatus
   */
  public WatchStatus getWatchStatus() {
    return watchStatus;
  }

  /**
   * @param watchStatus the watchStatus to set
   */
  public void setWatchStatus(WatchStatus watchStatus) {
    this.watchStatus = watchStatus;
  }

  /**
   * @return the noOfClicks
   */
  public int getNoOfClicks() {
    return noOfClicks;
  }

  /**
   * @param noOfClicks the noOfClicks to set
   */
  public void setNoOfClicks(int noOfClicks) {
    this.noOfClicks = noOfClicks;
  }

  /**
   * @return the resumeFrom
   */
  public float getResumeFrom() {
    return resumeFrom;
  }

  /**
   * @param resumeFrom the resumeFrom to set
   */
  public void setResumeFrom(float resumeFrom) {
    this.resumeFrom = resumeFrom;
  }

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
   * @return the unit
   */
  public Unit getUnit() {
    return unit;
  }

  /**
   * @param unit the unit to set
   */
  public void setUnit(Unit unit) {
    this.unit = unit;
  }


  /**
   * @return the assignmentSubmission
   */
  public AssignmentSubmission getAssignmentSubmission() {
    return assignmentSubmission;
  }

  /**
   * @param assignmentSubmission the assignmentSubmission to set
   */
  public void setAssignmentSubmission(AssignmentSubmission assignmentSubmission) {
    this.assignmentSubmission = assignmentSubmission;
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
}
