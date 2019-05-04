package com.neostencil.responses;

import java.util.List;
import java.util.Map;

public class EarningsResponse {

  Map<String, List<CommissionDTO>> earningsMap;

  /**
   * @return the earningsMap
   */
  public Map<String, List<CommissionDTO>> getEarningsMap() {
    return earningsMap;
  }

  /**
   * @param earningsMap the earningsMap to set
   */
  public void setEarningsMap(Map<String, List<CommissionDTO>> earningsMap) {
    this.earningsMap = earningsMap;
  }



}
