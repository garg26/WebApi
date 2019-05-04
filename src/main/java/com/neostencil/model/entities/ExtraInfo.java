package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
@Table(name = "ns_extra_info")
public class ExtraInfo extends DomainObject {

  @Id
  @Column(name = "info_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "extra_info_seq")
  @SequenceGenerator(name = "extra_info_seq", sequenceName = "extra_info_seq",
      allocationSize = 1000)
  private int infoId;

  @Column(name = "mobile_number")
  private String mobileNumber;

  public int getInfoId() {
    return infoId;
  }

  public void setInfoId(int infoId) {
    this.infoId = infoId;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }
}
