package com.neostencil.requests;

import com.neostencil.common.enums.ProductType;

public class CartItemUpdateRequest {

  ProductType productType;
  int commodityId;
  int quantity;
  int productId;

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public int getCommodityId() {
    return commodityId;
  }

  public void setCommodityId(int commodityId) {
    this.commodityId = commodityId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }
}
