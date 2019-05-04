package com.neostencil.responses;

import com.neostencil.common.enums.ProductType;
import com.neostencil.model.entities.CartOrderItem;
import com.neostencil.model.entities.OrderItem;

public class OrderResponseItem {

  String title;
  double price;
  double subTotal;
  int quantity;
  int productId;
  int commmodityId;
  ProductType productType;
  String productSlug;
  String imageUrl;

  public OrderResponseItem() {}

  public OrderResponseItem(CartOrderItem cartOrderItem) {
    this.title = cartOrderItem.getProduct().getProductTitle();
    this.price = cartOrderItem.getProduct().getPrice();
    this.quantity = cartOrderItem.getQuantity();
    this.productId = cartOrderItem.getProduct().getId();
    this.commmodityId = cartOrderItem.getProduct().getCommodityId();
    this.productType = cartOrderItem.getProduct().getType();
    this.productSlug = cartOrderItem.getProduct().getProductSlug();
    this.imageUrl = cartOrderItem.getProduct().getImageUrl();
    this.subTotal = this.price * this.quantity;
  }

  public OrderResponseItem(OrderItem orderItem) {
    this.title = orderItem.getProduct().getProductTitle();
    this.price = orderItem.getProduct().getPrice();
    this.quantity = orderItem.getQuantity();
    this.productId = orderItem.getProduct().getId();
    this.commmodityId = orderItem.getProduct().getCommodityId();
    this.productType = orderItem.getProduct().getType();
    this.productSlug = orderItem.getProduct().getProductSlug();
    this.imageUrl = orderItem.getProduct().getImageUrl();
    this.subTotal = this.price * this.quantity;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
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

  public int getCommmodityId() {
    return commmodityId;
  }

  public void setCommmodityId(int commmodityId) {
    this.commmodityId = commmodityId;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public double getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(double subTotal) {
    this.subTotal = subTotal;
  }

  public String getProductSlug() {
    return productSlug;
  }

  public void setProductSlug(String productSlug) {
    this.productSlug = productSlug;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
