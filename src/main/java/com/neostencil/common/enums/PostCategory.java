package com.neostencil.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PostCategory {

  IAS("IAS"), Default("Default"), Seminar("Seminar"), Answer_Writing_Challenge("Answer Writing Challenge"),Toppers_Answer_Booklet("Topper's Answer Booklet"),Toppers_Blog("Topper's Blog"),Toppers_Seminar_Videos("Topper's Seminar Videos"),Gs_Study_Materials("GS Study Materials"),
  IES("IES"),SSC_CGL("SSC-CGL"),GATE("GATE"),Current_Affairs("Current Affairs");

  private String jsonValue;

  private PostCategory(final String json) {
    this.jsonValue = json;
  }

  @JsonValue
  public String jsonValue() {
    return this.jsonValue;
  }

}
