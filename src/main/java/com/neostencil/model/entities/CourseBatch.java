package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.neostencil.common.enums.PublishStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "ns_course_batches")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class CourseBatch implements Serializable {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "course_batch_gen")
  @TableGenerator(name = "course_batch_gen", table = "ns_course_batch_id_gen",
      pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "CourseBatchId_Gen",
      initialValue = 100, allocationSize = 1)
  @Id
  @Column(name = "batch_id")
  int id;

  @JsonBackReference(value = "batch-reference")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  Course course;

  @JsonIgnore
  @JsonIgnoreProperties(value = "courseBatch")
  @OneToMany(mappedBy = "courseBatch", fetch = FetchType.LAZY)
  Set<UserCourseBatchLinkage> userCourseBatchLinkages;
  @Column(name = "start_date")
  private Date startDate;
  @Column(name = "end_date")
  private Date endDate;
  @Column(name = "duration")
  private int duration;
  @Column(name = "start_time")
  private String startTime;
  @Column(name = "end_time")
  private String endTime;
  @Column(name = "class_timing")
  private String classTiming;
  @Column(name = "discount")
  private int discount;
  @NumberFormat(style = NumberFormat.Style.CURRENCY)
  @Column(name = "regular_price")
  private Double regularPrice;
  @Column(name = "sale_price")
  private Double salePrice;
  @Column(name = "display_size")
  private int displaySize;
  @Column(name = "batch_status")
  @Enumerated(EnumType.STRING)
  private PublishStatus status;
  @Column(name = "validity")
  private int validity;

  @Column(name = "batch_name", length = 10000)
  private String batchName;

  @Column(name = "no_of_available_seats")
  private int noOfAvailableSeats;

  @Column(name = "no_of_session")
  private String noOfSession;

  @OrderBy("position asc")
  @JsonIgnore
  @JsonIgnoreProperties("batch")
  @OneToMany(mappedBy = "batch", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<Unit> units;

  @Column(nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  private Date createdAt;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  private Date updatedAt;

  @Fetch(value = FetchMode.SUBSELECT)
  @ElementCollection(fetch = FetchType.LAZY)
  @JoinTable(name = "ns_batch_meta_info", joinColumns = @JoinColumn(name = "batch_id"))
  @OrderColumn(name = "sequence", updatable = true)
  private List<MetaInformation> metaList;

  @Column(name = "course_validity_for_display")
  private String validityDisplay;


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
   * @return the startDate
   */
  public Date getStartDate() {
    return startDate;
  }

  /**
   * @param startDate the startDate to set
   */
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  /**
   * @return the endDate
   */
  public Date getEndDate() {
    return endDate;
  }

  /**
   * @param endDate the endDate to set
   */
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * @return the duration
   */
  public int getDuration() {
    return duration;
  }

  /**
   * @param duration the duration to set
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * @return the startTime
   */
  public String getStartTime() {
    return startTime;
  }

  /**
   * @param startTime the startTime to set
   */
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  /**
   * @return the endTime
   */
  public String getEndTime() {
    return endTime;
  }

  /**
   * @param endTime the endTime to set
   */
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  /**
   * @return the classTiming
   */
  public String getClassTiming() {
    return classTiming;
  }

  /**
   * @param classTiming the classTiming to set
   */
  public void setClassTiming(String classTiming) {
    this.classTiming = classTiming;
  }

  /**
   * @return the discount
   */
  public int getDiscount() {
    return discount;
  }

  /**
   * @param discount the discount to set
   */
  public void setDiscount(int discount) {
    this.discount = discount;
  }

  /**
   * @return the regularPrice
   */
  public Double getRegularPrice() {
    return regularPrice;
  }

  /**
   * @param regularPrice the regularPrice to set
   */
  public void setRegularPrice(Double regularPrice) {
    this.regularPrice = regularPrice;
  }

  /**
   * @return the salePrice
   */
  public Double getSalePrice() {
    return salePrice;
  }

  /**
   * @param salePrice the salePrice to set
   */
  public void setSalePrice(Double salePrice) {
    this.salePrice = salePrice;
  }

  /**
   * @return the displaySize
   */
  public int getDisplaySize() {
    return displaySize;
  }

  /**
   * @param displaySize the displaySize to set
   */
  public void setDisplaySize(int displaySize) {
    this.displaySize = displaySize;
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
   * @return the validity
   */
  public int getValidity() {
    return validity;
  }

  /**
   * @param validity the validity to set
   */
  public void setValidity(int validity) {
    this.validity = validity;
  }

  /**
   * @return the units
   */
  public Set<Unit> getUnits() {
    return units;
  }

  /**
   * @param units the units to set
   */
  public void setUnits(Set<Unit> units) {
    this.units = units;
  }

  /**
   * @return the course
   */
  public Course getCourse() {
    return course;
  }

  /**
   * @param course the course to set
   */
  public void setCourse(Course course) {
    this.course = course;
  }

  /**
   * @return the users
   */
  /*
   * public Set<User> getUsers() { return users; }
   *
   *//**
     * @param users the users to set
     *//*
       * public void setUsers(Set<User> users) { this.users = users; }
       */

  /**
   * @return the userCourseBatchLinkages
   */
  public Set<UserCourseBatchLinkage> getUserCourseBatchLinkages() {
    return userCourseBatchLinkages;
  }

  /**
   * @param userCourseBatchLinkages the userCourseBatchLinkages to set
   */
  public void setUserCourseBatchLinkages(Set<UserCourseBatchLinkage> userCourseBatchLinkages) {
    this.userCourseBatchLinkages = userCourseBatchLinkages;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
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
   * @return the noOfAvailableSeats
   */
  public int getNoOfAvailableSeats() {
    return noOfAvailableSeats;
  }

  /**
   * @param noOfAvailableSeats the noOfAvailableSeats to set
   */
  public void setNoOfAvailableSeats(int noOfAvailableSeats) {
    this.noOfAvailableSeats = noOfAvailableSeats;
  }

  public String getNoOfSession() {
    return noOfSession;
  }

  public void setNoOfSession(String noOfSession) {
    this.noOfSession = noOfSession;
  }

  public String getValidityDisplay() {
    return validityDisplay;
  }

  public void setValidityDisplay(String validityDisplay) {
    this.validityDisplay = validityDisplay;
  }
}
