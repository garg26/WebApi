package com.neostencil.requests;

import java.util.List;
import com.neostencil.model.entities.Message;

public class MessageBroadcastRequest {

  private Message message;
  private List<String> receiverList;

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

  /**
   * @return the receiverList
   */
  public List<String> getReceiverList() {
    return receiverList;
  }

  /**
   * @param receiverList the receiverList to set
   */
  public void setReceiverList(List<String> receiverList) {
    this.receiverList = receiverList;
  }

}
