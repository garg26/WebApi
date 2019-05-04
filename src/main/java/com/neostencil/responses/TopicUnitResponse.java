package com.neostencil.responses;

import java.util.Map;
import java.util.Set;
import com.neostencil.model.entities.Unit;

public class TopicUnitResponse {

  Map<String, Set<Unit>> unitMap;

  int batchId;

  /**
   * @return the unitMap
   */
  public Map<String, Set<Unit>> getUnitMap() {
    return unitMap;
  }

  /**
   * @param unitMap the unitMap to set
   */
  public void setUnitMap(Map<String, Set<Unit>> unitMap) {
    this.unitMap = unitMap;
  }

  /**
   * @return the batchId
   */
  public int getBatchId() {
    return batchId;
  }

  /**
   * @param batchId the batchId to set
   */
  public void setBatchId(int batchId) {
    this.batchId = batchId;
  }

}
