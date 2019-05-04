package com.neostencil.responses;

import java.util.Map;


public class MessageThreadsResponse {

  Map<String, MessageThread> messagesMap;
  Map<String, MessageThread> notificationsMap;
  
  /**
   * @return the messagesMap
   */
  public Map<String, MessageThread> getMessagesMap() {
    return messagesMap;
  }

  /**
   * @param messagesMap the messagesMap to set
   */
  public void setMessagesMap(Map<String, MessageThread> messagesMap) {
    this.messagesMap = messagesMap;
  }

  /**
   * @return the notificationsMap
   */
  public Map<String, MessageThread> getNotificationsMap() {
    return notificationsMap;
  }

  /**
   * @param notificationsMap the notificationsMap to set
   */
  public void setNotificationsMap(Map<String, MessageThread> notificationsMap) {
    this.notificationsMap = notificationsMap;
  }

}
