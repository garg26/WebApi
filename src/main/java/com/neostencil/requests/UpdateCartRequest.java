package com.neostencil.requests;

import com.neostencil.common.enums.ProductType;

public class UpdateCartRequest {

  long userId;
  ProductType productType;
  int commodityId;
  int quantity;
  int productId;

  /**
   * @return the userId
   */
  public long getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(long userId) {
    this.userId = userId;
  }

  /**
   * @return the productType
   */
  public ProductType getProductType() {
    return productType;
  }

  /**
   * @param productType the productType to set
   */
  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  /**
   * @return the commodityId
   */
  public int getCommodityId() {
    return commodityId;
  }

  /**
   * @param commodityId the commodityId to set
   */
  public void setCommodityId(int commodityId) {
    this.commodityId = commodityId;
  }

  /**
   * @return the quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * @param quantity the quantity to set
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * @return the productId
   */
  public int getProductId() {
    return productId;
  }

  /**
   * @param productId the productId to set
   */
  public void setProductId(int productId) {
    this.productId = productId;
  }

}
