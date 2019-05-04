package com.neostencil.model.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.neostencil.common.enums.MessageStatus;

@Entity
@Table(name = "ns_messages")
public class Message extends DomainObject {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "message_gen")
  @TableGenerator(name = "message_gen", table = "ns_message_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "messageId_Gen", initialValue = 10000,
      allocationSize = 1)
  @Column(name = "message_id")
  private int id;

  @Column(name = "message_from")
  private String messageFrom;

  @Column(name = "message_to")
  private String messageTo;

  @Column(name = "topic")
  private String topic;

  @Column(name = "group_id")
  private String groupId;

  @Column(name = "body",length=20000)
  private String body;

  @Column(name = "message_url")
  private String messageUrl;
  
  @Column(name="is_notification")
  private boolean notification; 

  public String getMessageUrl() {
    return messageUrl;
  }

  public void setMessageUrl(String messageUrl) {
    this.messageUrl = messageUrl;
  }

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "ns_message_attachments", joinColumns = @JoinColumn(name = "message_id"))
  @Column(name = "message_attachments")
  private List<CommentAttachment> attachments;

  @Column(name = "send_time")
  private Timestamp sendTime;

  @Column(name = "message_status")
  private MessageStatus status;

  @Column(name = "is_read")
  private boolean read;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param topic
     *            the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     *            the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the sendTime
     */
    public Timestamp getSendTime() {
        return sendTime;
    }

    /**
     * @param sendTime
     *            the sendTime to set
     */
    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * @return the status
     */
    public MessageStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Message other = (Message) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    /**
     * @return the messageFrom
     */
    public String getMessageFrom() {
        return messageFrom;
    }

    /**
     * @param messageFrom
     *            the messageFrom to set
     */
    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    /**
     * @return the messageTo
     */
    public String getMessageTo() {
        return messageTo;
    }

    /**
     * @param messageTo
     *            the messageTo to set
     */
    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    /**
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read
     *            the read to set
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return the attachments
     */
    public List<CommentAttachment> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments
     *            the attachments to set
     */
    public void setAttachments(List<CommentAttachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the notification
     */
    public boolean isNotification() {
      return notification;
    }

    /**
     * @param notification the notification to set
     */
    public void setNotification(boolean notification) {
      this.notification = notification;
    }

}
