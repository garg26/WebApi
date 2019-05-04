package com.neostencil.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PostSubCategory {

  Prelims("Prelims"), Default("Default"), History("History"), Geography("Geography"), Arts_Culture(
      "Arts and Culture"), Environment("Environment"), Economy("Economy"), Science_Technology(
          "Science And Technology"), Polity("Polity"), IAS_Topper("IAS Topper"), IAS_Strategy(
              "IAS Strategy"), IAS_Seminar("IAS Seminar"), IES_Topper("IES Topper"), IES_Strategy(
                  "IES Strategy"), IES_Seminar("IES Seminar"), SSC_CGL_Topper(
                      "SSC-CGL Topper"), SSC_CGL_Strategy("SSC-CGL Strategy"), SSC_CGL_Seminar(
                          "SSC-CGL Seminar"), GATE_Topper("GATE Topper"), GATE_Strategy(
                              "GATE Strategy"), GATE_Seminar("GATE Seminar"), Public_Administration(
                                  "Public Administration"), Sociology("Sociology"), Anthropology(
                                      "Anthropology"), Political_Science(
                                          "Political Science"), Philosophy("Philosophy"), Analysis(
                                              "Analysis"), Quiz("Quiz"), Monthly_Current_affairs(
                                                  "Monthly Current Affairs");

  private String jsonValue;

  private PostSubCategory(final String json) {
    this.jsonValue = json;
  }

  @JsonValue
  public String jsonValue() {
    return this.jsonValue;
  }

}
