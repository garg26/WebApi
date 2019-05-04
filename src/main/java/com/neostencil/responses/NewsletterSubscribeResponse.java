package com.neostencil.responses;

import java.io.Serializable;

public class NewsletterSubscribeResponse implements Serializable {

  public boolean subscribe;
  public String errorMessage;

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String emailId;

  public boolean isSubscribe() {
    return subscribe;
  }

  public void setSubscribe(boolean subscribe) {
    this.subscribe = subscribe;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
