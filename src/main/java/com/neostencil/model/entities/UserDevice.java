package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.neostencil.common.enums.DeviceType;



@Entity
@Table(name = "ns_devices")
public class UserDevice extends DomainObject {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq")
  @SequenceGenerator(name = "device_seq", sequenceName = "device_seq", allocationSize = 1)
  private long deviceID;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  DeviceType type;

  @Column(name = "fingerprint",columnDefinition="TEXT")
  String fingerPrint;

  @Column(name = "short_info",columnDefinition="TEXT")
  String shortInfo;

  @Column(name = "info",columnDefinition="TEXT")
  String info;

  public long getDeviceID() {
    return deviceID;
  }

  public void setDeviceID(long deviceID) {
    this.deviceID = deviceID;
  }

  public DeviceType getType() {
    return type;
  }

  public void setType(DeviceType type) {
    this.type = type;
  }

  public String getFingerPrint() {
    return fingerPrint;
  }

  public void setFingerPrint(String fingerPrint) {
    this.fingerPrint = fingerPrint;
  }

  public String getShortInfo() {
    return shortInfo;
  }

  public void setShortInfo(String shortInfo) {
    this.shortInfo = shortInfo;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }
}
