package com.neostencil.responses;

import com.neostencil.model.entities.ExtraInfo;

public class ExtraInfoResponse {

  ExtraInfo extraInfo;
  String message;
  boolean isValid;

  public ExtraInfo getExtraInfo() {
    return extraInfo;
  }

  public void setExtraInfo(ExtraInfo extraInfo) {
    this.extraInfo = extraInfo;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean valid) {
    isValid = valid;
  }
}
