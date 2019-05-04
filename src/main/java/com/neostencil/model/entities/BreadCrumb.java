package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="ns_breadcrumbs")
public class BreadCrumb extends DomainObject {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "breadcrumb_gen")
  @TableGenerator(name = "breadcrumb_gen", table = "ns_breadcrumb_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "BreadcrumbId_Gen", initialValue = 100,
      allocationSize = 1)
  @Column(name = "breadcrumb_id")
  private int breadcrumbId;

  @Column(name = "breadcrumb_text")
  private String text;

  @Column(name = "breadcrumb_link")
  private String link;

  @Column(name = "breadcrumb_parent_id")
  private int parentId;

  /**
   *
   * @return the breadcrumbId
   */
  public int getBreadcrumbId() {
    return breadcrumbId;
  }

  /**
   *
   * @param breadcrumbId
   */
  public void setBreadcrumbId(int breadcrumbId) {
    this.breadcrumbId = breadcrumbId;
  }

  /**
   *
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   *
   * @param text
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   *
   * @return the link
   */
  public String getLink() {
    return link;
  }

  /**
   *
   * @param link
   */
  public void setLink(String link) {
    this.link = link;
  }

  /**
   *
   * @return the parentId
   */
  public int getParentId() {
    return parentId;
  }

  /**
   *
   * @param parentId
   */
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }
}
