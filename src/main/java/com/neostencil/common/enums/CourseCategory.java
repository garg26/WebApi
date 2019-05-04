package com.neostencil.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CourseCategory {

  @JsonProperty("PRELIMS")
  PRELIMS(ExamSegmentTypes.IAS, "Prelims",true),
  @JsonProperty("GS_FOUNDATION")
  GS_FOUNDATION(ExamSegmentTypes.IAS,"GS Foundation",false),
  @JsonProperty("PRELIMS_AND_MAINS")
  PRELIMS_AND_MAINS(ExamSegmentTypes.IAS, "Prelims And Mains",false),

  @JsonProperty("MAINS")
  MAINS(ExamSegmentTypes.IAS, "Mains",true),

  @JsonProperty("INTERVIEW")
  INTERVIEW(ExamSegmentTypes.IAS, "Interview",false),
  @JsonProperty("CURRENT_AFFAIRS")
  CURRENT_AFFAIRS(ExamSegmentTypes.IAS, "Current Affairs",false),

  @JsonProperty("MECHANICAL")
  MECHANICAL(ExamSegmentTypes.IES, "Mechanical",false),
  @JsonProperty("CIVIL")
  CIVIL(ExamSegmentTypes.IES, "Civil",false),
  @JsonProperty("ELECTRICAL")
  ELECTRICAL(ExamSegmentTypes.IES, "Electrical",false),
  @JsonProperty("ELECTRONICS_AND_TELE_COMMUNICATION")
  ELECTRONICS_AND_TELE_COMMUNICATION(ExamSegmentTypes.IES, "Electronics and Tele Communication",false),
  @JsonProperty("GENERAL_STUDIES")
  GENERAL_STUDIES(ExamSegmentTypes.IES, "General Studies",false),

  @JsonProperty("FREE_IAS_PREP")
  FREE_IAS_PREP(ExamSegmentTypes.IAS,"Free IAS prep",true),

  @JsonProperty("MECHANICAL_GATE")
  MECHANICAL_GATE(ExamSegmentTypes.GATE, "Mechanical",false),
  CIVIL_GATE(ExamSegmentTypes.GATE,
      "Civil",false),
  ELECTRICAL_GATE(ExamSegmentTypes.GATE, "Electrical",false),
  @JsonProperty("ELECTRONICS_AND_TELE_COMMUNICATION_GATE")
  ELECTRONICS_AND_TELE_COMMUNICATION_GATE(ExamSegmentTypes.GATE,
      "Electronics and Tele Communication",false),
  @JsonProperty("COMPUTER_SCIENCE")
  COMPUTER_SCIENCE(ExamSegmentTypes.GATE, "Computer Science",false),INSTRUMENTATION_ENGINEERING(ExamSegmentTypes.GATE, "Instrumental Engineering",false),
  @JsonProperty("SSC_CGL")
  SSC_CGL(ExamSegmentTypes.SSC,
      "SSC CGL",false), SSC_JE(ExamSegmentTypes.SSC, "SSC JE",false),

  @JsonProperty("BPSC")
  BPSC(ExamSegmentTypes.STATE_PSC, "BPSC",false),
  @JsonProperty("APPSC_TSPSC")
  APPSC_TSPSC(ExamSegmentTypes.STATE_PSC, "APPSC TPSC",false),
  @JsonProperty("MPSC")
  MPSC(ExamSegmentTypes.STATE_PSC, "MPSC",false),
  @JsonProperty("MPPSC")
  MPPSC(ExamSegmentTypes.STATE_PSC, "MPPSC",false),
  @JsonProperty("RAS")
  RAS(ExamSegmentTypes.STATE_PSC, "RAS",false),
  @JsonProperty("KAS")
  KAS(ExamSegmentTypes.STATE_PSC, "KAS",false),
  @JsonProperty("JPSC")
  JPSC(ExamSegmentTypes.STATE_PSC, "JPSC",false),
  @JsonProperty("UPPSC")
  UPPSC(ExamSegmentTypes.STATE_PSC, "UPPSC",false),

  @JsonProperty("JUDICIAL_SERVICE")
  JUDICIAL_SERVICE(ExamSegmentTypes.LAW,"Judicial Service",false),
  @JsonProperty("CLAT")
  CLAT(ExamSegmentTypes.LAW,"CLAT",false),

  @JsonProperty("RRB_JE")
  RRB_JE(ExamSegmentTypes.RRB,"RRB JE",false),
  @JsonProperty("RRB_ALP")
  RRB_ALP(ExamSegmentTypes.RRB,"RRB ALP",false),
  @JsonProperty("RRB_GROUP_d")
  RRB_GROUP_d(ExamSegmentTypes.RRB,"RRB GROUP D",false),
  @JsonProperty("RRB_NTPC")
  RRB_NTPC(ExamSegmentTypes.RRB,"RRB NTPC",false);

  public String jsonValue;
  ExamSegmentTypes examSegmentType;
  boolean havingSubject;

  public boolean isHavingSubject() {
    return havingSubject;
  }

  private CourseCategory(ExamSegmentTypes examSegmentType, String json,boolean havingSubject) {
    this.examSegmentType = examSegmentType;
    this.jsonValue = json;
    this.havingSubject = havingSubject;
  }

  public static CourseCategory fromString(String jsonValue) {

    for (CourseCategory courseCategory : CourseCategory.values()) {
      if (courseCategory.jsonValue.equalsIgnoreCase(jsonValue)) {
        return courseCategory;
      }
    }
    return null;
  }

  public static CourseCategory getCourseCategory(String examSegment, String courseCategory ){
    CourseCategory response = null;
    ExamSegmentTypes examSegmentTypes = ExamSegmentTypes.fromString(examSegment);
    for(CourseCategory values : CourseCategory.values()){

      if(values.examSegmentType.equals(examSegmentTypes)){

        if(values.jsonValue.equalsIgnoreCase(courseCategory)){

          response = values;
          break;

        }


      }

    }

    return response;
  }

  public static CourseCategory getCourseCategory(ExamSegmentTypes examSegment, String courseCategory ){
    CourseCategory response = null;
    for(CourseCategory values : CourseCategory.values()){

      if(values.getExamSegmentType().equals(examSegment)){

        if(values.toString().equalsIgnoreCase(courseCategory)){

          response = values;
          break;

        }


      }

    }

    return response;
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

  public String jsonValue() {
    return this.jsonValue;
  }

}
