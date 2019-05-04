package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "ns_user_device_log")
public class UserDeviceLog {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_device_log_seq")
  @SequenceGenerator(name = "user_device_log_seq", sequenceName = "user_device_log_seq", allocationSize = 1)
  private long userDeeviceLogID;

  @Column(name = "old_device")
  long oldDeviceID;

  @Column(name = "new_device")
  long newDeviceID;


  @Column(name = "user_id")
  long userID;

  @Column(name = "update_cause")
  String updateCause;


  public long getUserDeeviceLogID() {
    return userDeeviceLogID;
  }

  public void setUserDeeviceLogID(long userDeeviceLogID) {
    this.userDeeviceLogID = userDeeviceLogID;
  }

  public long getOldDeviceID() {
    return oldDeviceID;
  }

  public void setOldDeviceID(long oldDeviceID) {
    this.oldDeviceID = oldDeviceID;
  }

  public long getNewDeviceID() {
    return newDeviceID;
  }

  public void setNewDeviceID(long newDeviceID) {
    this.newDeviceID = newDeviceID;
  }

  public long getUserID() {
    return userID;
  }

  public void setUserID(long userID) {
    this.userID = userID;
  }

  public String getUpdateCause() {
    return updateCause;
  }

  public void setUpdateCause(String updateCause) {
    this.updateCause = updateCause;
  }
}
