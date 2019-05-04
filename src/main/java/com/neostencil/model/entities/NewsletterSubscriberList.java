package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ns_newsletter_subscriber_list")
public class NewsletterSubscriberList implements Serializable {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "subscriber_list_gen")
  @TableGenerator(name = "subscriber_list_gen", table = "ns_subscriber_list_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "SubscriberListId_Gen", initialValue = 30000,
      allocationSize = 100)
  @Id
  @Column(name = "subscriber_list_id")
  private int id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @JsonIgnoreProperties(value = "subscriberList")
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subscriberList", cascade = CascadeType.ALL)
  private List<NewsletterSubscriber> subscribers;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<NewsletterSubscriber> getSubscribers() {
    return subscribers;
  }

  public void setSubscribers(List<NewsletterSubscriber> subscribers) {
    this.subscribers = subscribers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NewsletterSubscriberList)) {
      return false;
    }
    NewsletterSubscriberList that = (NewsletterSubscriberList) o;
    return getId() == that.getId();
  }

  @Override
  public int hashCode() {

    return Objects.hash(getId());
  }
}
