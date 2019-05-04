package com.neostencil.requests;

public class CreateBreadCrumbRequest {

  String childText;
  String childUrl;
  String parentUrl;

  public String getChildText() {
    return childText;
  }

  public void setChildText(String childText) {
    this.childText = childText;
  }

  public String getChildUrl() {
    return childUrl;
  }

  public void setChildUrl(String childUrl) {
    this.childUrl = childUrl;
  }

  public String getParentUrl() {
    return parentUrl;
  }

  public void setParentUrl(String parentUrl) {
    this.parentUrl = parentUrl;
  }
}
