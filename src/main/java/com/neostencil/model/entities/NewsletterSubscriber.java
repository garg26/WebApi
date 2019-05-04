package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ns_newsletter_subscribers")
public class NewsletterSubscriber extends DomainObject {

  @JsonIgnoreProperties(value = "subscribers")
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "ns_newsletter_subscriber_list_linkage",
      joinColumns = {@JoinColumn(name = "subscriber_id")},
      inverseJoinColumns = {@JoinColumn(name = "subscriber_list_id")})
  public List<NewsletterSubscriberList> subscriberList;
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "subscriber_gen")
  @TableGenerator(name = "subscriber_gen", table = "ns_subscriber_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "SubscriberId_Gen", initialValue = 30000,
      allocationSize = 100)
  @Id
  @Column(name = "subscriber_id")
  private int id;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "email_id")
  private String emailId;
  @Column(name = "gender")
  private String gender;
  @Column(name = "city")
  private String city;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public List<NewsletterSubscriberList> getSubscriberList() {
    return subscriberList;
  }

  public void setSubscriberList(
      List<NewsletterSubscriberList> subscriberList) {
    this.subscriberList = subscriberList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NewsletterSubscriber)) {
      return false;
    }
    NewsletterSubscriber that = (NewsletterSubscriber) o;
    return getId() == that.getId();
  }

  @Override
  public int hashCode() {

    return Objects.hash(getId());
  }
}
