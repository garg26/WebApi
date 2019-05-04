package com.neostencil.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;

public enum CourseSubCategory {

  @JsonProperty("GENERAL_STUDIES_PRELIMS")
  GENERAL_STUDIES_PRELIMS(ExamSegmentTypes.IAS, CourseCategory.PRELIMS, "General Studies"),
  @JsonProperty("TEST_SERIES_PRELIMS")
  TEST_SERIES_PRELIMS(ExamSegmentTypes.IAS, CourseCategory.PRELIMS, "Test Series"),
  @JsonProperty("CSAT")
  CSAT(ExamSegmentTypes.IAS, CourseCategory.PRELIMS, "CSAT"),

  @JsonProperty("GENERAL_STUDIES")
  GENERAL_STUDIES(ExamSegmentTypes.IAS, CourseCategory.MAINS, "General Studies"),
  @JsonProperty("OPTIONAL")
  OPTIONAL(ExamSegmentTypes.IAS, CourseCategory.MAINS, "Optionals"),
  @JsonProperty("TEST_SERIES")
  TEST_SERIES(ExamSegmentTypes.IAS, CourseCategory.MAINS, "Test Series"),
  @JsonProperty("ANSWER_WRITING_ENRICHMENT")
  ANSWER_WRITING_ENRICHMENT(ExamSegmentTypes.IAS, CourseCategory.MAINS, "Answer Writing Enrichment"),
  @JsonProperty("ESSAY")
  ESSAY(ExamSegmentTypes.IAS, CourseCategory.MAINS, "Essay"),

  @JsonProperty("GS_1")
  GS_1(ExamSegmentTypes.IAS,CourseCategory.PRELIMS_AND_MAINS,"GS 1"),
  @JsonProperty("GS_2")
  GS_2(ExamSegmentTypes.IAS,CourseCategory.PRELIMS_AND_MAINS,"GS 2"),
  @JsonProperty("GS_3")
  GS_3(ExamSegmentTypes.IAS,CourseCategory.PRELIMS_AND_MAINS,"GS 3"),
  @JsonProperty("CURRENT_AFFAIRS")
  CURRENT_AFFAIRS(ExamSegmentTypes.IAS,CourseCategory.PRELIMS_AND_MAINS,"Current Affairs"),
  @JsonProperty("TEST_SERIES_PRELIMS_AND_MAINS")
  TEST_SERIES_PRELIMS_AND_MAINS(ExamSegmentTypes.IAS, CourseCategory.PRELIMS_AND_MAINS, "Test Series"),

  @JsonProperty("GENERAL_STUDIES_FREE_IAS_PREP")
  GENERAL_STUDIES_FREE_IAS_PREP(ExamSegmentTypes.IAS, CourseCategory.FREE_IAS_PREP, "General Studies"),
  @JsonProperty("OPTIONAL_FREE_IAS_PREP")
  OPTIONAL_FREE_IAS_PREP(ExamSegmentTypes.IAS, CourseCategory.FREE_IAS_PREP, "Optional"),
  @JsonProperty("ESSAY_FREE_IAS_PREP")
  ESSAY_FREE_IAS_PREP(ExamSegmentTypes.IAS, CourseCategory.FREE_IAS_PREP, "Essay"),

  @JsonProperty("COURSES_ELECTRICAL")
  COURSES_ELECTRICAL(ExamSegmentTypes.IES,CourseCategory.ELECTRICAL,"Courses"),
  @JsonProperty("TEST_SERIES_ELECTRICAL")
  TEST_SERIES_ELECTRICAL(ExamSegmentTypes.IES,CourseCategory.ELECTRICAL,"Test Series"),

  @JsonProperty("COURSES_MECHANICAL")
  COURSES_MECHANICAL(ExamSegmentTypes.IES,CourseCategory.MECHANICAL,"Courses"),
  @JsonProperty("TEST_SERIES_MECHANICAL")
  TEST_SERIES_MECHANICAL(ExamSegmentTypes.IES,CourseCategory.MECHANICAL,"Test Series"),

  @JsonProperty("COURSES_CIVIL")
  COURSES_CIVIL(ExamSegmentTypes.IES,CourseCategory.CIVIL,"Courses"),
  @JsonProperty("TEST_SERIES_CIVIL")
  TEST_SERIES_CIVIL(ExamSegmentTypes.IES,CourseCategory.CIVIL,"Test Series"),

  @JsonProperty("COURSES_ELECTRONICS_AND_TELE_COMMUNICATION")
  COURSES_ELECTRONICS_AND_TELE_COMMUNICATION(ExamSegmentTypes.IES,CourseCategory.ELECTRONICS_AND_TELE_COMMUNICATION,"Courses"),
  @JsonProperty("TEST_SERIES_ELECTRONICS_AND_TELE_COMMUNICATION")
  TEST_SERIES_ELECTRONICS_AND_TELE_COMMUNICATION(ExamSegmentTypes.IES,CourseCategory.ELECTRONICS_AND_TELE_COMMUNICATION,"Test Series"),

  @JsonProperty("COURSES_ELECTRICAL_GATE")
  COURSES_ELECTRICAL_GATE(ExamSegmentTypes.GATE,CourseCategory.ELECTRICAL_GATE,"Courses"),
  @JsonProperty("TEST_SERIES_ELECTRICAL_GATE")
  TEST_SERIES_ELECTRICAL_GATE(ExamSegmentTypes.GATE,CourseCategory.ELECTRICAL_GATE,"Test Series"),

  @JsonProperty("COURSES_MECHANICAL_GATE")
  COURSES_MECHANICAL_GATE(ExamSegmentTypes.GATE,CourseCategory.MECHANICAL_GATE,"Courses"),
  @JsonProperty("TEST_SERIES_MECHANICAL_GATE")
  TEST_SERIES_MECHANICAL_GATE(ExamSegmentTypes.GATE,CourseCategory.MECHANICAL_GATE,"Test Series"),

  @JsonProperty("COURSES_CIVIL_GATE")
  COURSES_CIVIL_GATE(ExamSegmentTypes.GATE,CourseCategory.CIVIL_GATE,"Courses"),
  @JsonProperty("TEST_SERIES_CIVIL_GATE")
  TEST_SERIES_CIVIL_GATE(ExamSegmentTypes.GATE,CourseCategory.CIVIL_GATE,"Test Series"),

  @JsonProperty("COURSES_ELECTRONICS_AND_TELE_COMMUNICATION_GATE")
  COURSES_ELECTRONICS_AND_TELE_COMMUNICATION_GATE(ExamSegmentTypes.GATE,CourseCategory.ELECTRONICS_AND_TELE_COMMUNICATION_GATE,"Courses"),
  @JsonProperty("TEST_SERIES_ELECTRONICS_AND_TELE_COMMUNICATION_GATE")
  TEST_SERIES_ELECTRONICS_AND_TELE_COMMUNICATION_GATE(ExamSegmentTypes.GATE,CourseCategory.ELECTRONICS_AND_TELE_COMMUNICATION_GATE,"Test Series"),

  @JsonProperty("COURSES_COMPUTER_SCIENCE_GATE")
  COURSES_COMPUTER_SCIENCE_GATE(ExamSegmentTypes.GATE,CourseCategory.COMPUTER_SCIENCE,"Courses"),
  @JsonProperty("TEST_SERIES_COMPUTER_SCIENCE_GATE")
  TEST_SERIES_COMPUTER_SCIENCE_GATE(ExamSegmentTypes.GATE,CourseCategory.COMPUTER_SCIENCE,"Test Series"),

  @JsonProperty("COURSES_INSTRUMENTATION_ENGINEERING_GATE")
  COURSES_INSTRUMENTATION_ENGINEERING_GATE(ExamSegmentTypes.GATE,CourseCategory.INSTRUMENTATION_ENGINEERING,"Courses"),
  @JsonProperty("TEST_SERIES_INSTRUMENTATION_ENGINEERING_GATE")
  TEST_SERIES_INSTRUMENTATION_ENGINEERING_GATE(ExamSegmentTypes.GATE,CourseCategory.INSTRUMENTATION_ENGINEERING,"Test Series");



  public ExamSegmentTypes examSegmentType;
  public CourseCategory courseCategory;
  public String jsonValue;

  private CourseSubCategory(ExamSegmentTypes examSegmentType, CourseCategory courseCategory,
      String json) {
    this.examSegmentType = examSegmentType;
    this.courseCategory = courseCategory;
    this.jsonValue = json;
  }

  public static CourseCategory getCourseCategory(ExamSegmentTypes examSegment,String jsonValue) {

    for (CourseCategory courseCategory : CourseCategory.values()) {
      if (courseCategory.jsonValue.equalsIgnoreCase(jsonValue) && courseCategory.examSegmentType.equals(examSegment)) {
        return courseCategory;
      }
    }
    return null;
  }

  public static ExamSegmentTypes getExamSegment(String jsonValue) {

    for (ExamSegmentTypes examSegmentTypes : ExamSegmentTypes.values()) {
      if (examSegmentTypes.jsonValue.equalsIgnoreCase(jsonValue)) {
        return examSegmentTypes;
      }
    }
    return null;
  }

  public static CourseSubCategory getCourseSubCategory(String courseCategory, String courseSubCategory ){
    CourseSubCategory response = null;
    CourseCategory category = CourseCategory.fromString(courseCategory);
    for(CourseSubCategory values : CourseSubCategory.values()){

      if(values.courseCategory.equals(category)){

        if(values.jsonValue.equals(courseSubCategory)){

          response = values;
          break;

        }


      }

    }

    return response;
  }

  public String jsonValue() {
    return this.jsonValue;
  }

  /**
   * @return the examSegmentType
   */
  public ExamSegmentTypes getExamSegmentType() {
    return examSegmentType;
  }

  /**
   * @param examSegmentType the examSegmentType to set
   */
  public void setExamSegmentType(ExamSegmentTypes examSegmentType) {
    this.examSegmentType = examSegmentType;
  }

  /**
   * @return the courseCategory
   */
  public CourseCategory getCourseCategory() {
    return courseCategory;
  }

  /**
   * @param courseCategory the courseCategory to set
   */
  public void setCourseCategory(CourseCategory courseCategory) {
    this.courseCategory = courseCategory;
  }

  public static CourseSubCategory fromString(String jsonValue) {
    for (CourseSubCategory subCategory : CourseSubCategory.values()) {
      if (subCategory.jsonValue.equalsIgnoreCase(jsonValue)) {
        return subCategory;
      }
    }
    return null;
  }

  public static List<CourseSubCategory> getListOfCategory(String jsonValue) {

    List<CourseSubCategory> subCategoryList = new ArrayList<>();
    for (CourseSubCategory subCategory : CourseSubCategory.values()) {
      if (subCategory.jsonValue.equalsIgnoreCase(jsonValue)) {
        subCategoryList.add(subCategory);
      }
    }
    return subCategoryList;
  }

}
