package com.neostencil.requests;

import java.util.List;

public class FilterRequest {

  List<Filter> filters;

  /**
   * @return the filters
   */
  public List<Filter> getFilters() {
    return filters;
  }

  /**
   * @param filters the filters to set
   */
  public void setFilters(List<Filter> filters) {
    this.filters = filters;
  }
}
