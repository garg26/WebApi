package com.neostencil.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnswerSheetCategory {

  PRELIMS("Prelims"), MAINS("Mains"), INTERVIEW("Interview"), PRE_CUM_MAINS("Pre Cum Mains");
  public String jsonValue;

  AnswerSheetCategory(String jsonValue) {
    this.jsonValue = jsonValue;
  }

  @JsonValue
  public String jsonValue() {
    return this.jsonValue;
  }

}
