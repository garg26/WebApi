package com.neostencil.model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
@Table(name="ns_featured_images")
public class FeaturedImage implements Serializable {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "image_gen")
  @TableGenerator(name = "image_gen", table = "ns_image_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "image_id_Gen", initialValue = 3500000,
      allocationSize = 100)
  @Id
  @Column(name = "image_id")
  int id;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "url")
  String url;

  @Column(name = "name")
  String name;

  @Column(name = "title")
  String title;

  @Column(name = "caption")
  String caption;

  @Column(name = "altText")
  String altText;

  @Column(name = "description",columnDefinition = "TEXT")
  String description;

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
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the caption
   */
  public String getCaption() {
    return caption;
  }

  /**
   * @param caption the caption to set
   */
  public void setCaption(String caption) {
    this.caption = caption;
  }

  /**
   * @return the altText
   */
  public String getAltText() {
    return altText;
  }

  /**
   * @param altText the altText to set
   */
  public void setAltText(String altText) {
    this.altText = altText;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   *
   * @return the feature image name
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @param name the feature image name
   */
  public void setName(String name) {
    this.name = name;
  }
}
