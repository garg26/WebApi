package com.neostencil.dtos;

import java.util.Date;
import java.util.List;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.MetaInformation;

public class CourseBatchDetailedDTO {

  int id;
  private Date startDate;
  private int duration;
  private String classTiming;
  private int discount;
  private Double regularPrice;
  private Double salePrice;
  private int displaySize;
  private PublishStatus status;
  private int validity;

  private String batchName;

  private int noOfAvailableSeats;

  private String noOfSession;

  private Date createdAt;

  private Date updatedAt;

  private List<MetaInformation> metaList;

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

  /**
   * @return the noOfSession
   */
  public String getNoOfSession() {
    return noOfSession;
  }

  /**
   * @param noOfSession the noOfSession to set
   */
  public void setNoOfSession(String noOfSession) {
    this.noOfSession = noOfSession;
  }

  /**
   * @return the createdAt
   */
  public Date getCreatedAt() {
    return createdAt;
  }

  /**
   * @param createdAt the createdAt to set
   */
  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * @return the updatedAt
   */
  public Date getUpdatedAt() {
    return updatedAt;
  }

  /**
   * @param updatedAt the updatedAt to set
   */
  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
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
   * @return the validityDisplay
   */
  public String getValidityDisplay() {
    return validityDisplay;
  }

  /**
   * @param validityDisplay the validityDisplay to set
   */
  public void setValidityDisplay(String validityDisplay) {
    this.validityDisplay = validityDisplay;
  }



}
