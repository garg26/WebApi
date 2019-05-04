package com.neostencil.responses;

import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.Unit;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class CourseBatchResponse implements Serializable {

  int id;
  private Date startDate;
  private Date endDate;
  private int duration;
  private String startTime;
  private String endTime;
  private String classTiming;
  private int discount;
  private double regularPrice;
  private double salePrice;
  private int displaySize;
  private PublishStatus status;
  private int validity;
  private String batchName;
  private int noOfAvailableSeats;
  private Set<Unit> units;
  private String displayStartDate;
  private String noOfSession;
  private String validityDisplay;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getClassTiming() {
    return classTiming;
  }

  public void setClassTiming(String classTiming) {
    this.classTiming = classTiming;
  }

  public int getDiscount() {
    return discount;
  }

  public void setDiscount(int discount) {
    this.discount = discount;
  }

  public double getRegularPrice() {
    return regularPrice;
  }

  public void setRegularPrice(double regularPrice) {
    this.regularPrice = regularPrice;
  }

  public double getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(double salePrice) {
    this.salePrice = salePrice;
  }

  public int getDisplaySize() {
    return displaySize;
  }

  public void setDisplaySize(int displaySize) {
    this.displaySize = displaySize;
  }

  public PublishStatus getStatus() {
    return status;
  }

  public void setStatus(PublishStatus status) {
    this.status = status;
  }

  public int getValidity() {
    return validity;
  }

  public void setValidity(int validity) {
    this.validity = validity;
  }

  public String getBatchName() {
    return batchName;
  }

  public void setBatchName(String batchName) {
    this.batchName = batchName;
  }

  public Set<Unit> getUnits() {
    return units;
  }

  public void setUnits(Set<Unit> units) {
    this.units = units;
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

  public String getDisplayStartDate() {
    return displayStartDate;
  }

  public void setDisplayStartDate(String displayStartDate) {
    this.displayStartDate = displayStartDate;
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
