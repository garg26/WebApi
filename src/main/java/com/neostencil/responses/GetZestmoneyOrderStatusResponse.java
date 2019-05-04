package com.neostencil.responses;

public class GetZestmoneyOrderStatusResponse {

  String ApplicationId;
  String OrderStatus;
  String DeliveryPinCode;
  String BasketAmount;

  public String getApplicationId() {
    return ApplicationId;
  }

  public void setApplicationId(String applicationId) {
    ApplicationId = applicationId;
  }

  public String getOrderStatus() {
    return OrderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    OrderStatus = orderStatus;
  }

  public String getDeliveryPinCode() {
    return DeliveryPinCode;
  }

  public void setDeliveryPinCode(String deliveryPinCode) {
    DeliveryPinCode = deliveryPinCode;
  }

  public String getBasketAmount() {
    return BasketAmount;
  }

  public void setBasketAmount(String basketAmount) {
    BasketAmount = basketAmount;
  }
}
