package com.neostencil.common.enums;

public enum CommentType {

  REVIEW("Review"), COMMENT("Comment");

  public String jsonValue;

  private CommentType(String json) {

    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }

  public void setJsonValue(String jsonValue) {
    this.jsonValue = jsonValue;
  }
}
