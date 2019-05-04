package com.neostencil.requests;

public class ZestmoneyOrderItemRequest {

  String Id;
  String Description;
  String Quantity;
  String TotalPrice;
  String Category;

  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
  }

  public String getDescription() {
    return Description;
  }

  public void setDescription(String description) {
    Description = description;
  }

  public String getQuantity() {
    return Quantity;
  }

  public void setQuantity(String quantity) {
    Quantity = quantity;
  }

  public String getTotalPrice() {
    return TotalPrice;
  }

  public void setTotalPrice(String totalPrice) {
    TotalPrice = totalPrice;
  }

  public String getCategory() {
    return Category;
  }

  public void setCategory(String category) {
    Category = category;
  }
}
