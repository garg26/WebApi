package com.neostencil.requests;

public class LoginRequest {

  String emailId;
  String password;
  // TODO: remove
  String deviceId;

  public LoginRequest() {

  }

  public LoginRequest(String emailId, String password, String deviceId) {
    super();
    this.emailId = emailId;
    this.password = password;
    this.deviceId = deviceId;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "LoginRequest [emailId=" + emailId + ", password=" + password + ", deviceId=" + deviceId
        + ", getEmailId()=" + getEmailId() + ", getDeviceId()=" + getDeviceId() + ", getPassword()="
        + getPassword() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
        + ", toString()=" + super.toString() + "]";
  }
}
