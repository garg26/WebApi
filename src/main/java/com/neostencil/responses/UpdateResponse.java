package com.neostencil.responses;

import java.io.Serializable;

public class UpdateResponse implements Serializable {

  private boolean success;

  /**
   * @return the success
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * @param success the success to set
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }

}
