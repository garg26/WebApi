package com.neostencil.requests;

import java.util.List;

public class QuizRequest {

  String name;
  String emailId;
  String password;
  String mobile;
  String city;

  List<QuizSubmissionRequest> list;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public List<QuizSubmissionRequest> getList() {
    return list;
  }

  public void setList(List<QuizSubmissionRequest> list) {
    this.list = list;
  }
}
