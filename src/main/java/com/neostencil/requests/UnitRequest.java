package com.neostencil.requests;

import com.neostencil.common.enums.DeviceType;

public class UnitRequest {

  int unitID;

  String oldFingerprint;

  String newFingerprint;

  String fingerprintShortInfo;

  String fingerprintInfo;

  DeviceType deviceType;

  public DeviceType getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(DeviceType deviceType) {
    this.deviceType = deviceType;
  }

  public int getUnitID() {
    return unitID;
  }

  public void setUnitID(int unitID) {
    this.unitID = unitID;
  }

  public String getOldFingerprint() {
    return oldFingerprint;
  }

  public void setOldFingerprint(String oldFingerprint) {
    this.oldFingerprint = oldFingerprint;
  }

  public String getNewFingerprint() {
    return newFingerprint;
  }

  public void setNewFingerprint(String newFingerprint) {
    this.newFingerprint = newFingerprint;
  }

  public String getFingerprintShortInfo() {
    return fingerprintShortInfo;
  }

  public void setFingerprintShortInfo(String fingerprintShortInfo) {
    this.fingerprintShortInfo = fingerprintShortInfo;
  }

  public String getFingerprintInfo() {
    return fingerprintInfo;
  }

  public void setFingerprintInfo(String fingerprintInfo) {
    this.fingerprintInfo = fingerprintInfo;
  }
}
