package com.neostencil.common.enums;

public enum TeacherSubjects {

  Anthropology(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Anthropology"),
  Economics(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Economics"),
  Electrical_Engineering(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Electrical Engineering"),
  Geography(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Geography"),
  History(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"History"),
  Law(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Law"),
  Philosophy(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Philosophy"),
  Political_Science_And_International_Relations(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"PSIR"),
  Public_Administration(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Public Administration"),
  Sociology(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Sociology"),
  Telugu(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Telugu"),
  Urdu(ExamSegmentTypes.IAS,TeacherCategory.OPTIONAL,"Urdu"),

  GS_Paper_1(ExamSegmentTypes.IAS,TeacherCategory.GENERAL_STUDIES,"GS Paper 1"),
  GS_Paper_2(ExamSegmentTypes.IAS,TeacherCategory.GENERAL_STUDIES,"GS Paper 2"),
  GS_Paper_3(ExamSegmentTypes.IAS,TeacherCategory.GENERAL_STUDIES,"GS Paper 3"),
  GS_Paper_4_Ethics(ExamSegmentTypes.IAS,TeacherCategory.GENERAL_STUDIES,"GS Paper 4 / Ethics"),
  Essay(ExamSegmentTypes.IAS,TeacherCategory.GENERAL_STUDIES,"Essay"),

  Mechanical_IES(ExamSegmentTypes.IES,TeacherCategory.STREAMS,"Mechanical Engineering"),
  Electrical_IES(ExamSegmentTypes.IES,TeacherCategory.STREAMS,"Electrical Engineering"),
  Electronics_IES(ExamSegmentTypes.IES,TeacherCategory.STREAMS,"Electronics and Tele Communication"),
  Civil_IES(ExamSegmentTypes.IES,TeacherCategory.STREAMS,"Civil"),

  Mechanical_GATE(ExamSegmentTypes.GATE,TeacherCategory.STREAMS,"Mechanical Engineering"),
  Electrical_GATE(ExamSegmentTypes.GATE,TeacherCategory.STREAMS,"Electrical Engineering"),
  Electronics_GATE(ExamSegmentTypes.GATE,TeacherCategory.STREAMS,"Electronics and Tele Communication"),
  Civil_GATE(ExamSegmentTypes.GATE,TeacherCategory.STREAMS,"Civil"),
  Computer_Science_GATE(ExamSegmentTypes.GATE,TeacherCategory.STREAMS,"Computer Science"),
  Instrumentation_GATE(ExamSegmentTypes.GATE,TeacherCategory.STREAMS,"Instrumentation"),

  CGL(ExamSegmentTypes.SSC,TeacherCategory.EXAMS,"CGL"),
  JE(ExamSegmentTypes.SSC,TeacherCategory.EXAMS,"JE"),

  Rajasthan(ExamSegmentTypes.STATE_PSC,TeacherCategory.STATE_SERVICES,"Rajasthan"),
  Madhya_Pradesh(ExamSegmentTypes.STATE_PSC,TeacherCategory.STATE_SERVICES,"Madhya Pradesh"),
  Bihar(ExamSegmentTypes.STATE_PSC,TeacherCategory.STATE_SERVICES,"Bihar"),
  Telangana(ExamSegmentTypes.STATE_PSC,TeacherCategory.STATE_SERVICES,"Telangana"),
  Andra_Pradesh(ExamSegmentTypes.STATE_PSC,TeacherCategory.STATE_SERVICES,"Andra Pradesh"),
  Jammu_And_Kashmir(ExamSegmentTypes.STATE_PSC,TeacherCategory.STATE_SERVICES,"Jammu And Kashmir"),
  Maharashtra(ExamSegmentTypes.STATE_PSC,TeacherCategory.STATE_SERVICES,"Maharashtra");

  ExamSegmentTypes examSegmentTypes;
  TeacherCategory teacherCategory;
  String jsonValue;


  TeacherSubjects(ExamSegmentTypes examSegmentTypes, TeacherCategory teacherCategory,
      String jsonValue) {

    this.examSegmentTypes=examSegmentTypes;
    this.teacherCategory=teacherCategory;
    this.jsonValue=jsonValue;
  }

  public ExamSegmentTypes getExamSegmentTypes() {
    return examSegmentTypes;
  }

  public TeacherCategory getTeacherCategory() {
    return teacherCategory;
  }

  public String getJsonValue() {
    return jsonValue;
  }

}
