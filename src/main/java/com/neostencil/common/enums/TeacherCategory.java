package com.neostencil.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TeacherCategory {

  OPTIONAL(ExamSegmentTypes.IAS,"Optional"),
  GENERAL_STUDIES(ExamSegmentTypes.IAS,"General Studies"),
  STREAMS(ExamSegmentTypes.IES,"Streams"),
  EXAMS(ExamSegmentTypes.SSC,"Exams"),
  STATE_SERVICES(ExamSegmentTypes.STATE_PSC,"State Services");

  ExamSegmentTypes examSegmentTypes;
  String jsonValue;

  TeacherCategory(ExamSegmentTypes examSegmentTypes, String s) {
    this.examSegmentTypes=examSegmentTypes;
    this.jsonValue=s;
  }

  public static TeacherCategory fromString(String jsonValue) {

    for (TeacherCategory teacherCategory : TeacherCategory.values()) {
      if (teacherCategory.jsonValue.equalsIgnoreCase(jsonValue)) {
        return teacherCategory;
      }
    }
    return null;
  }


  /**
   * @return the examSegmentType
   */
  public ExamSegmentTypes getExamSegmentTypes() {
    return examSegmentTypes;
  }

  /**
   * @param examSegmentType the examSegmentType to set
   */
  public void setExamSegmentTypes(ExamSegmentTypes examSegmentType) {
    this.examSegmentTypes = examSegmentType;
  }

  @JsonValue
  public String jsonValue() {
    return this.jsonValue;
  }
}
