package com.neostencil.responses;

import java.util.List;
import com.neostencil.model.entities.Message;

public class MessageThread {

  List<Message> messages;

  int noOfUnreadMessages;

  /**
   * @return the noOfUnreadMessages
   */
  public int getNoOfUnreadMessages() {
    return noOfUnreadMessages;
  }

  /**
   * @param noOfUnreadMessages the noOfUnreadMessages to set
   */
  public void setNoOfUnreadMessages(int noOfUnreadMessages) {
    this.noOfUnreadMessages = noOfUnreadMessages;
  }

  /**
   * @return the messages
   */
  public List<Message> getMessages() {
    return messages;
  }

  /**
   * @param messages the messages to set
   */
  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

}
