package com.neostencil.responses;

public class AnnouncementHeaderResponse {

  int headerId;

  public int getHeaderId() {
    return headerId;
  }

  public void setHeaderId(int headerId) {
    this.headerId = headerId;
  }

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

  String headerText;
  String headerUrl;

  public String getOnPageToDisplay() {
    return onPageToDisplay;
  }

  public void setOnPageToDisplay(String onPageToDisplay) {
    this.onPageToDisplay = onPageToDisplay;
  }

  String onPageToDisplay;

}
