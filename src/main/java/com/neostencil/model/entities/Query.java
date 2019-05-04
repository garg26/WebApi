package com.neostencil.model.entities;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ns_queries")
public class Query extends DomainObject {

  @Id
  @Column(name = "query_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "query_seq")
  @SequenceGenerator(name = "query_seq", sequenceName = "query_seq", allocationSize = 1)
  int id;

  @Column(name = "text", columnDefinition = "TEXT")
  String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "queried_by")
  User queriedBy;

  /**
   * This will be the instructors user account
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "queried_to")
  User queriedTo;

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "query")
  Set<QueryReply> replies;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "unit_id")
  Unit unit;

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @return the queriedBy
   */
  public User getQueriedBy() {
    return queriedBy;
  }

  /**
   * @param queriedBy the queriedBy to set
   */
  public void setQueriedBy(User queriedBy) {
    this.queriedBy = queriedBy;
  }

  /**
   * @return the queriedTo
   */
  public User getQueriedTo() {
    return queriedTo;
  }

  /**
   * @param queriedTo the queriedTo to set
   */
  public void setQueriedTo(User queriedTo) {
    this.queriedTo = queriedTo;
  }

  /**
   * @return the replies
   */
  public Set<QueryReply> getReplies() {
    return replies;
  }

  /**
   * @param replies the replies to set
   */
  public void setReplies(Set<QueryReply> replies) {
    this.replies = replies;
  }

  /**
   * @return the unit
   */
  public Unit getUnit() {
    return unit;
  }

  /**
   * @param unit the unit to set
   */
  public void setUnit(Unit unit) {
    this.unit = unit;
  }



}
