package com.neostencil.requests;

import java.util.Date;

public class AnnouncementHeaderRequest {

  public String getHeaderText() {
    return headerText;
  }

  public void setHeaderText(String headerText) {
    this.headerText = headerText;
  }

  public String getHeaderUrl() {
    return headerUrl;
  }

  public void setHeaderUrl(String headerUrl) {
    this.headerUrl = headerUrl;
  }

  public int getHeaderId() {
    return headerId;
  }

  public void setHeaderId(int headerId) {
    this.headerId = headerId;
  }

  String headerText;
  String headerUrl;
  int headerId;

  public String getOnPageToDisplay() {
    return onPageToDisplay;
  }

  public void setOnPageToDisplay(String onPageToDisplay) {
    this.onPageToDisplay = onPageToDisplay;
  }

  String onPageToDisplay;

  public String getBtnText() {
    return btnText;
  }

  public void setBtnText(String btnText) {
    this.btnText = btnText;
  }

  String btnText;

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

  Date createdAt;
  Date updatedAt;



}
