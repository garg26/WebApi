package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.UnitType;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "ns_units")
public class Unit extends DomainObject {

	@GeneratedValue(strategy = GenerationType.TABLE, generator = "unit_gen")
	@TableGenerator(name = "unit_gen", table = "unit_id_gen", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "unit_Gen", initialValue = 300, allocationSize = 1)
	@Id
	int unitId;

	@Column(name = "unit_type")
	@Enumerated(EnumType.STRING)
	UnitType type;

	@Column(name = "typeId")
	int typeId;

	@Column(name = "title", length = 1000)
	String title;

	@Column(name = "unit_description", columnDefinition = "TEXT")
	String description;

	@JsonIgnoreProperties({ "units", "course" })
	@ManyToOne(fetch = FetchType.LAZY)
	CourseBatch batch;

	@JsonIgnore
	@OneToMany(mappedBy = "unit", fetch = FetchType.LAZY)
	private Set<UserUnitLinkage> userUnitLinkages;

	@Column(name = "is_product")
	private boolean product;

	@Column(name="publicly_buyable")
  private  boolean publiclyBuyable;

	@Column(name = "unit_price")
	private double price;

	@Column(name = "unit_topic")
	private String topic;

	@Column(name = "unit_position")
	private int position;

	@Column(name = "free")
	private boolean free;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 2000)
	private PublishStatus status;

	@Column(name = "no_of_views")
	private int noOfViews;

	@JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "unit")
  private Set<Query> queries;

	@Column(name="has_started")
	private boolean hasStarted;
	
  public Unit() {

  }

  public Unit(Unit unit) {
    this.title = unit.title;
    this.type = unit.type;
    this.typeId = unit.typeId;
    this.position = unit.position;
    this.product = unit.product;
    this.topic = unit.topic;
    this.description = unit.description;
    this.batch = unit.batch;
    this.price = unit.price;
    this.status = unit.status;
    this.publiclyBuyable=unit.publiclyBuyable;
    this.hasStarted=unit.hasStarted;
  }

  public int getNoOfViews() {
    return noOfViews;
  }

  public void setNoOfViews(int noOfViews) {
    this.noOfViews = noOfViews;
  }

  /**
   * @return the type
   */
  public UnitType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(UnitType type) {
    this.type = type;
  }

  /**
   * @return the unitId
   */
  public int getUnitId() {
    return unitId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }

  /**
   * @return the typeId
   */
  public int getTypeId() {
    return typeId;
  }

  /**
   * @param typeId the typeId to set
   */
  public void setTypeId(int typeId) {
    this.typeId = typeId;
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
   * @return the userUnitLinkages
   */
  public Set<UserUnitLinkage> getUserUnitLinkages() {
    return userUnitLinkages;
  }

  /**
   * @param userUnitLinkages the userUnitLinkages to set
   */
  public void setUserUnitLinkages(Set<UserUnitLinkage> userUnitLinkages) {
    this.userUnitLinkages = userUnitLinkages;
  }

  /**
   * @return the product
   */
  public boolean isProduct() {
    return product;
  }

  /**
   * @param product the product to set
   */
  public void setProduct(boolean product) {
    this.product = product;
  }

  /**
   * @return the price
   */
  public double getPrice() {
    return price;
  }

  /**
   * @param price the price to set
   */
  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * @return the topic
   */
  public String getTopic() {
    return topic;
  }

  /**
   * @param topic the topic to set
   */
  public void setTopic(String topic) {
    this.topic = topic;
  }

  /**
   * @return the batch
   */
  public CourseBatch getBatch() {
    return batch;
  }

  /**
   * @param batch the batch to set
   */
  public void setBatch(CourseBatch batch) {
    this.batch = batch;
  }

  /**
   * @return the position
   */
  public int getPosition() {
    return position;
  }

  /**
   * @param position the position to set
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * @return the free
   */
  public boolean isFree() {
    return free;
  }

  /**
   * @param free the free to set
   */
  public void setFree(boolean free) {
    this.free = free;
  }

  /**
   * @return the status
   */
  public PublishStatus getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(PublishStatus status) {
    this.status = status;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the queries
   */
  public Set<Query> getQueries() {
    return queries;
  }

  /**
   * @param queries the queries to set
   */
  public void setQueries(Set<Query> queries) {
    this.queries = queries;
  }

  public boolean isPubliclyBuyable() {
    return publiclyBuyable;
  }

  public void setPubliclyBuyable(boolean publiclyBuyable) {
    this.publiclyBuyable = publiclyBuyable;
  }

  /**
   * @return the hasStarted
   */
  public boolean isHasStarted() {
    return hasStarted;
  }

  /**
   * @param hasStarted the hasStarted to set
   */
  public void setHasStarted(boolean hasStarted) {
    this.hasStarted = hasStarted;
  }

}
