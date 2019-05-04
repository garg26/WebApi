package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ns_announcement_header")
public class AnnouncementHeader extends DomainObject {

  public int getHeaderId() {
    return headerId;
  }

  public void setHeaderId(int headerId) {
    this.headerId = headerId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "announcement_header_gen")
  @TableGenerator(name = "announcement_header_gen", table = "ns_announcement_Header_id_gen",
      pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "AnnouncementHeader_Gen",
      initialValue = 1000, allocationSize = 1)
  @Column(name = "header_id")
  public int headerId;

  public String getHeaderText() {
    return headerText;
  }

  public void setHeaderText(String headerText) {
    this.headerText = headerText;
  }

  @Column(name = "header_text", length = 10000)
  public String headerText;

  public String getHeaderUrl() {
    return headerUrl;
  }

  public void setHeaderUrl(String headerUrl) {
    this.headerUrl = headerUrl;
  }

  @Column(name = "header_url")
  public String headerUrl;

  public String getOnPageToDisplay() {
    return onPageToDisplay;
  }

  public void setOnPageToDisplay(String onPageToDisplay) {
    this.onPageToDisplay = onPageToDisplay;
  }

  @Column (name="on_page_to_display")
  public String onPageToDisplay;

  public String getBtnText() {
    return btnText;
  }

  public void setBtnText(String btnText) {
    this.btnText = btnText;
  }

  @Column (name = "btnText")
  public String btnText;

  public AnnouncementHeader() {
    super();
  }

  public AnnouncementHeader(int headerId, String headerText,String headerUrl, String onPageToDisplay, String btnText) {
    super();
    this.headerId = headerId;
    this.headerText = headerText;
    this.headerUrl = headerUrl;
    this.onPageToDisplay = onPageToDisplay;
    this.btnText = btnText;
  }


  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + headerId;
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
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AnnouncementHeader other = (AnnouncementHeader) obj;
    if (headerId != other.headerId) {
      return false;
    }
    return true;
  }


}
