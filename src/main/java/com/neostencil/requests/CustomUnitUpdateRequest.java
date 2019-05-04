package com.neostencil.requests;

import java.util.HashMap;
import java.util.List;

public class CustomUnitUpdateRequest {

  List<UnitPositionAndMacroRequest> request;

  public List<UnitPositionAndMacroRequest> getRequest() {
    return request;
  }

  public void setRequest(List<UnitPositionAndMacroRequest> request) {
    this.request = request;
  }
}
