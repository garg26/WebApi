package com.neostencil.responses;

import java.sql.Timestamp;
import java.util.Date;
import com.neostencil.common.enums.NeoCashAction;

/**
 * 
 * @author shilpa
 *
 */
public class UserNeoCashHistoryDTO {

  private long id;

  private long userId;

  private double neoCashValue;

  private NeoCashAction action;

  private Timestamp expiryDate;

  private String reason;

  private String creditedBy;

  private String additionalInfo;
  
  private Date createdAt;

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
   * @return the userId
   */
  public long getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(long userId) {
    this.userId = userId;
  }

  /**
   * @return the neoCashValue
   */
  public double getNeoCashValue() {
    return neoCashValue;
  }

  /**
   * @param neoCashValue the neoCashValue to set
   */
  public void setNeoCashValue(double neoCashValue) {
    this.neoCashValue = neoCashValue;
  }

  /**
   * @return the action
   */
  public NeoCashAction getAction() {
    return action;
  }

  /**
   * @param action the action to set
   */
  public void setAction(NeoCashAction action) {
    this.action = action;
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
   * @return the reason
   */
  public String getReason() {
    return reason;
  }

  /**
   * @param reason the reason to set
   */
  public void setReason(String reason) {
    this.reason = reason;
  }

  /**
   * @return the creditedBy
   */
  public String getCreditedBy() {
    return creditedBy;
  }

  /**
   * @param creditedBy the creditedBy to set
   */
  public void setCreditedBy(String creditedBy) {
    this.creditedBy = creditedBy;
  }

  /**
   * @return the additionalInfo
   */
  public String getAdditionalInfo() {
    return additionalInfo;
  }

  /**
   * @param additionalInfo the additionalInfo to set
   */
  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
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

 }
