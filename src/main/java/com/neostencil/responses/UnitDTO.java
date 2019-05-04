package com.neostencil.responses;

import com.neostencil.model.entities.Unit;
import java.util.Set;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.UnitType;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.Query;
import com.neostencil.model.entities.UserUnitLinkage;

/**
 * 
 * @author shilpa
 *
 */
public class UnitDTO {

  private int unitId;

  private UnitType type;

  private int typeId;

  private String title;

  private String description;

  private CourseBatch batch;


  private Set<UserUnitLinkage> userUnitLinkages;

  private boolean product;

  private boolean publiclyBuyable;

  private double price;

  private String topic;

  private int position;

  private boolean free;

  private PublishStatus status;

  private int noOfViews;

  private Set<Query> queries;

  private boolean hasStarted;
  
  private float watchPercent;

  private boolean active;

  private Unit unit;


  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
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
   * @return the publiclyBuyable
   */
  public boolean isPubliclyBuyable() {
    return publiclyBuyable;
  }

  /**
   * @param publiclyBuyable the publiclyBuyable to set
   */
  public void setPubliclyBuyable(boolean publiclyBuyable) {
    this.publiclyBuyable = publiclyBuyable;
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

  /**
   * @return the noOfViews
   */
  public int getNoOfViews() {
    return noOfViews;
  }

  /**
   * @param noOfViews the noOfViews to set
   */
  public void setNoOfViews(int noOfViews) {
    this.noOfViews = noOfViews;
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

  /**
   * @return the watchPercent
   */
  public float getWatchPercent() {
    return watchPercent;
  }

  /**
   * @param watchPercent the watchPercent to set
   */
  public void setWatchPercent(float watchPercent) {
    this.watchPercent = watchPercent;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }
}
