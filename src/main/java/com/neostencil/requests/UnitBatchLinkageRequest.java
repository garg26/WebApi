package com.neostencil.requests;

import java.util.List;

public class UnitBatchLinkageRequest {

  List<Integer> units;

  List<Integer> batches;

  /**
   * @return the units
   */
  public List<Integer> getUnits() {
    return units;
  }

  /**
   * @param units the units to set
   */
  public void setUnits(List<Integer> units) {
    this.units = units;
  }

  /**
   * @return the batches
   */
  public List<Integer> getBatches() {
    return batches;
  }

  /**
   * @param batches the batches to set
   */
  public void setBatches(List<Integer> batches) {
    this.batches = batches;
  }



}
