package com.neostencil.responses;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class TeacherFilterResponse {

  LinkedHashMap<String, LinkedList<LinkedHashMap<String, LinkedList<String>>>> filters =
      new LinkedHashMap<>();

  public LinkedHashMap<String, LinkedList<LinkedHashMap<String, LinkedList<String>>>> getFilters() {
    return filters;
  }

  public void setFilters(
      LinkedHashMap<String, LinkedList<LinkedHashMap<String, LinkedList<String>>>> filters) {
    this.filters = filters;
  }
}
