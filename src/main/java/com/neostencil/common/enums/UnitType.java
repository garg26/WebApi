package com.neostencil.common.enums;

public enum UnitType {

  LECTURE("Lectures"), ASSIGNMENT("Assignment"), NOTES("Notes"), QUIZ("Quiz");

  private String jsonValue;

  private UnitType(String jsonValue) {
    this.jsonValue = jsonValue;
  }

  /**
   * @return the jsonValue
   */
  public String getJsonValue() {
    return jsonValue;
  }

  /**
   * @param jsonValue the jsonValue to set
   */
  public void setJsonValue(String jsonValue) {
    this.jsonValue = jsonValue;
  }

}
