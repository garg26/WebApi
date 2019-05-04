package com.neostencil.requests;

public class ShuffleUnitsRequest {

  int unitId;
  int newPosition;

  /**
   * @return the unitId
   */
  public int getUnitId() {
    return unitId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }

  /**
   * @return the newPosition
   */
  public int getNewPosition() {
    return newPosition;
  }

  /**
   * @param newPosition the newPosition to set
   */
  public void setNewPosition(int newPosition) {
    this.newPosition = newPosition;
  }
}
