package com.neostencil.model.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.neostencil.common.enums.NeoCashAction;

/**
 * 
 * @author shilpa
 *
 */
@Entity
@Table(name = "ns_user_neo_cash_history")
public class UserNeoCashHistory extends DomainObject {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "neo_cash_history_seq")
  @SequenceGenerator(name = "neo_cash_history_seq", sequenceName = "neo_cash_history_seq",
      allocationSize = 1)
  private long id;

  @Column(name = "user_id")
  private long userId;

  @Column(name = "neo_cash_value")
  private double neoCashValue;

  @Column(name = "action")
  @Enumerated(EnumType.STRING)
  private NeoCashAction action;

  @Column(name = "expiry_date")
  private Timestamp expiryDate;

  @Column(name = "reason")
  private String reason;

  /**
   * Will have value 'auto' for all the automatic user action based credits e.g. signup Otherwise
   * for all the manual credits(via admin dashboard) it will have the email Id of the admin user who
   * performed the credit
   */
  @Column(name = "credited_by")
  private String creditedBy;

  @Column(name = "additional_info", columnDefinition = "TEXT")
  private String additionalInfo;

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

}
