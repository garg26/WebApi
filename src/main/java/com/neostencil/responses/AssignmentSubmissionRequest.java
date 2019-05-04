package com.neostencil.responses;

public class AssignmentSubmissionRequest {

  int unitId;
  String url;
  String name;
  String type;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getUnitId() {
    return unitId;
  }

  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
