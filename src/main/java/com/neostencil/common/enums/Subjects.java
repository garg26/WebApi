package com.neostencil.common.enums;

import org.codehaus.jackson.annotate.JsonValue;

public enum Subjects {

  GS_HISTORY(ExamSegmentTypes.IAS,CourseCategory.PRELIMS, CourseSubCategory.GENERAL_STUDIES_PRELIMS,"GS History"),
  GS_POLITY(ExamSegmentTypes.IAS,CourseCategory.PRELIMS,CourseSubCategory.GENERAL_STUDIES_PRELIMS, "GS Polity"),
  GS_GEOGRAPHY(ExamSegmentTypes.IAS,CourseCategory.PRELIMS, CourseSubCategory.GENERAL_STUDIES_PRELIMS, "GS Geography"),
  CURRENT_AFFAIRS(ExamSegmentTypes.IAS,CourseCategory.PRELIMS,CourseSubCategory.GENERAL_STUDIES_PRELIMS,"Current Affairs"),
  GS_SCIENCE_AND_TECH(ExamSegmentTypes.IAS,CourseCategory.PRELIMS,CourseSubCategory.GENERAL_STUDIES_PRELIMS,"GS Science and Tech"),


  GS(ExamSegmentTypes.IAS,CourseCategory.PRELIMS,CourseSubCategory.TEST_SERIES_PRELIMS,"GS"),
  CSAT(ExamSegmentTypes.IAS,CourseCategory.PRELIMS,CourseSubCategory.TEST_SERIES_PRELIMS,"CSAT"),


  GS_1(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.GENERAL_STUDIES, "GS 1"),
  GS_2(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.GENERAL_STUDIES, "GS 2"),
  GS_3(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.GENERAL_STUDIES, "GS 3"),
  GS_4(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.GENERAL_STUDIES, "GS 4"),
  ESSAY(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.GENERAL_STUDIES, "Essay"),
  CURRENT_AFFAIRS_GENERAL_STUDIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.GENERAL_STUDIES, "Current Affairs"),

  PUBLIC_ADMINISTRATION(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Public Administration"),
  GEOGRAPHY(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Geography"),
  SOCIOLOGY(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Sociology"),
  HISTORY(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "History"),
  PHILOSOPHY(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Philosophy"),
  POLITICAL_SCIENCE(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Political Science"),
  ANTHROPOLOGY(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Anthropology"),
  HINDI_LITERATURE(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Hindi Literature"),
  TELUGU_LITERATURE(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Telugu Literature"),
  URDU_LITERATURE(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Urdu Literature"),
  LAW(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.OPTIONAL, "Law"),
  ECONOMICS(ExamSegmentTypes.IAS,CourseCategory.MAINS,CourseSubCategory.OPTIONAL,"Economics"),



  GS_MAINS_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "GS Mains Test Series"),
  ESSAY_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Essay"),
  PUBLIC_ADMINISTRATION_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Public Administration"),
  GEOGRAPHY_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Geography"),
  SOCIOLOGY_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Sociology"),
  HISTORY_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "History"),
  PHILOSOPHY_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Philosophy"),
  POLITICAL_SCIENCE_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Political Science"),
  ANTHROPOLOGY_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Anthropology"),
  HINDI_LITERATURE_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Hindi Literature"),
  TELUGU_LITERATURE_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Telugu Literature"),
  URDU_LITERATURE_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Urdu Literature"),
  LAW_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS, CourseSubCategory.TEST_SERIES, "Law"),
  ECONOMICS_TEST_SERIES(ExamSegmentTypes.IAS,CourseCategory.MAINS,CourseSubCategory.TEST_SERIES, "Ecomomics Test series"),

  GS_MAINS(ExamSegmentTypes.IAS,CourseCategory.MAINS,CourseSubCategory.ANSWER_WRITING_ENRICHMENT,"GS"),
  PUBLIC_ADMINISTRATION_MAINS(ExamSegmentTypes.IAS,CourseCategory.MAINS,CourseSubCategory.ANSWER_WRITING_ENRICHMENT,"Public Administration"),
  SOCIOLOGY_MAINS(ExamSegmentTypes.IAS,CourseCategory.MAINS,CourseSubCategory.ANSWER_WRITING_ENRICHMENT,"Sociology"),
  GEOGRAPHY_MAINS(ExamSegmentTypes.IAS,CourseCategory.MAINS,CourseSubCategory.ANSWER_WRITING_ENRICHMENT,"Geography"),

  PUBLIC_ADMINISTRATION_FREE_IAS_PREP(ExamSegmentTypes.IAS,CourseCategory.FREE_IAS_PREP,CourseSubCategory.OPTIONAL_FREE_IAS_PREP,"Public Administration"),
  PHILOSOPHY_FREE_IAS_PREP(ExamSegmentTypes.IAS,CourseCategory.FREE_IAS_PREP,CourseSubCategory.OPTIONAL_FREE_IAS_PREP,"Philosophy"),
  GEOGRAPHY_FREE_IAS_PREP(ExamSegmentTypes.IAS,CourseCategory.FREE_IAS_PREP,CourseSubCategory.OPTIONAL_FREE_IAS_PREP,"Geography");



  public ExamSegmentTypes examSegmentTypes;
  public  CourseCategory courseCategory;
  public  CourseSubCategory courseSubCategory;
  public String jsonValue;


  Subjects(ExamSegmentTypes examSegmentTypes,CourseCategory courseCategory, CourseSubCategory courseSubCategory,
      String jsonValue) {
    this.examSegmentTypes = examSegmentTypes;
    this.courseSubCategory = courseSubCategory;
    this.courseCategory = courseCategory;
    this.jsonValue = jsonValue;
  }


  /**
   * @return the courseCategory
   */
  public static CourseCategory getCourseCategory(ExamSegmentTypes examSegmentTypes, String jsonValue) {

    for (CourseCategory courseCategory : CourseCategory.values()) {
      if (courseCategory.jsonValue.equalsIgnoreCase(jsonValue) && courseCategory.examSegmentType.equals(examSegmentTypes)) {
        return courseCategory;
      }
    }
    return null;
  }

  /**
   * @return the subcategory
   */
  public static CourseSubCategory getCourseSubCategory(CourseCategory courseCategory,String jsonValue) {

    for (CourseSubCategory courseSubCategory : CourseSubCategory.values()) {
      if (courseSubCategory.jsonValue().equalsIgnoreCase(jsonValue) && courseSubCategory.courseCategory.equals(courseCategory)) {
        return courseSubCategory;
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


  /**
   * @return the courseSubCategory
   */
  public  CourseSubCategory getCourseSubCategory() {
    return courseSubCategory;
  }

  /**
   * @return the jsonValue
   */
  @JsonValue
  public String getJsonValue() {
    return jsonValue;
  }

  public CourseCategory getCourseCategory() {
    return courseCategory;
  }

  public void setCourseCategory(CourseCategory courseCategory) {
    this.courseCategory = courseCategory;
  }

  public void setCourseSubCategory(CourseSubCategory courseSubCategory) {
    this.courseSubCategory = courseSubCategory;
  }

  public void setJsonValue(String jsonValue) {
    this.jsonValue = jsonValue;
  }

  public ExamSegmentTypes getExamSegmentTypes() {
    return examSegmentTypes;
  }

  public void setExamSegmentTypes(ExamSegmentTypes examSegmentTypes) {
    this.examSegmentTypes = examSegmentTypes;
  }
}
