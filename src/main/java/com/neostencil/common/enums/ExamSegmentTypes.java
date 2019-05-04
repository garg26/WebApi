package com.neostencil.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExamSegmentTypes {

  @JsonProperty("IAS")
  IAS("IAS"),
  @JsonProperty("JEE")
  JEE("JEE"),
  @JsonProperty("NEET")
  NEET("NEET"),
  @JsonProperty("IES")
  IES("IES"),
  @JsonProperty("SSC")
  SSC("SSC"),
  @JsonProperty("STATE_PSC")
  STATE_PSC("STATE PSC"),
  @JsonProperty("GATE")
  GATE("GATE"),
  @JsonProperty("IES_GATE")
  IES_GATE("IES GATE"),
  @JsonProperty("LAW")
  LAW("LAW"),
  @JsonProperty("RRB")
  RRB("RRB");

  public String jsonValue;

  private ExamSegmentTypes(final String json) {
    this.jsonValue = json;
  }

  public static ExamSegmentTypes fromString(String jsonValue) {

    for (ExamSegmentTypes examSegmentTypes : ExamSegmentTypes.values()) {
      if (examSegmentTypes.jsonValue.equalsIgnoreCase(jsonValue)) {
        return examSegmentTypes;
      }
    }
    return null;
  }

  public String jsonValue() {
    return this.jsonValue;
  }
}
