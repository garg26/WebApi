package com.neostencil.responses;

import com.neostencil.model.entities.Message;

public class SendMessageResponse {

  boolean success;
  Message message;

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

  /**
   * @return the message
   */
  public Message getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(Message message) {
    this.message = message;
  }



}
