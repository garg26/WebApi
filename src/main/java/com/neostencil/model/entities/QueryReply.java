package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ns_query_replies")
public class QueryReply extends DomainObject {

  @Id
  @Column(name = "reply_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_seq")
  @SequenceGenerator(name = "reply_seq", sequenceName = "reply_seq", allocationSize = 1)
  long id;

  @Column(name = "reply_text", columnDefinition = "TEXT")
  String replyText;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "replied_by")
  User repliedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "query")
  Query query;

  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the replyText
   */
  public String getReplyText() {
    return replyText;
  }

  /**
   * @param replyText the replyText to set
   */
  public void setReplyText(String replyText) {
    this.replyText = replyText;
  }

  /**
   * @return the repliedBy
   */
  public User getRepliedBy() {
    return repliedBy;
  }

  /**
   * @param repliedBy the repliedBy to set
   */
  public void setRepliedBy(User repliedBy) {
    this.repliedBy = repliedBy;
  }

  /**
   * @return the query
   */
  public Query getQuery() {
    return query;
  }

  /**
   * @param query the query to set
   */
  public void setQuery(Query query) {
    this.query = query;
  }


}
