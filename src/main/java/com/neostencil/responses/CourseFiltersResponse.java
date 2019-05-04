package com.neostencil.responses;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CourseFiltersResponse {

//  LinkedHashMap<String, LinkedList<LinkedList<LinkedList<String>>>> filter;

  LinkedHashMap<String,LinkedList<LinkedHashMap<String,LinkedHashMap<String,LinkedList<String>>>>> filter;

  /**
   * @return the filter
   */
  public LinkedHashMap<String,LinkedList<LinkedHashMap<String,LinkedHashMap<String,LinkedList<String>>>>> getFilter() {
    return filter;
  }

  /**
   * @param filter the filter to set
   */
  public void setFilter(LinkedHashMap<String,LinkedList<LinkedHashMap<String,LinkedHashMap<String,LinkedList<String>>>>> filter) {
    this.filter = filter;
  }
}
