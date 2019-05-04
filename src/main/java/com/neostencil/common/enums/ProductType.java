package com.neostencil.common.enums;

public enum ProductType {

  COURSE, UNIT, QUIZ, TEST, TEST_SERIES;

  public static CourseSubCategory fromString(String jsonValue) {

    for (CourseSubCategory subCategory : CourseSubCategory.values()) {
      if (subCategory.toString().equalsIgnoreCase(jsonValue)) {
        return subCategory;
      }
    }
    return null;
  }
}
