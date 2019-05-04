package com.neostencil.requests;

public class UnitPositionAndMacroRequest {
  int unitId;
  int newPosition;
  String  jwMacro;

  public int getUnitId() {
    return unitId;
  }

  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }

  public int getNewPosition() {
    return newPosition;
  }

  public void setNewPosition(int newPosition) {
    this.newPosition = newPosition;
  }

  public String getJwMacro() {
    return jwMacro;
  }

  public void setJwMacro(String jwMacro) {
    this.jwMacro = jwMacro;
  }
}
