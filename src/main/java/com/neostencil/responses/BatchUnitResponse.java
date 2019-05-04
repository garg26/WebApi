package com.neostencil.responses;

import com.neostencil.model.entities.Unit;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class BatchUnitResponse {

  LinkedHashMap<String, LinkedList<Unit>> response;

  boolean alreadyBought;

  public LinkedHashMap<String, LinkedList<Unit>> getResponse() {
    return response;
  }

  public void setResponse(LinkedHashMap<String, LinkedList<Unit>> response) {
    this.response = response;
  }

  /**
   * @return the alreadyBought
   */
  public boolean isAlreadyBought() {
    return alreadyBought;
  }

  /**
   * @param alreadyBought the alreadyBought to set
   */
  public void setAlreadyBought(boolean alreadyBought) {
    this.alreadyBought = alreadyBought;
  }

}

