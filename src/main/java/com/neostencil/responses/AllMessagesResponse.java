package com.neostencil.responses;

import java.util.List;
import com.neostencil.model.entities.Message;

public class AllMessagesResponse {

  List<Message> messages;
  int messageCount;

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

  /**
   * @return the messageCount
   */
  public int getMessageCount() {
    return messageCount;
  }

  /**
   * @param messageCount the messageCount to set
   */
  public void setMessageCount(int messageCount) {
    this.messageCount = messageCount;
  }

}
