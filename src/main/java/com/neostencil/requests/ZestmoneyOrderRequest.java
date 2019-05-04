package com.neostencil.requests;

import java.util.List;

public class ZestmoneyOrderRequest {

  String BasketAmount;
  String DeliveryPostCode;
  String OrderId;
  String ReturnUrl;
  String ApprovedUrl;
  String EmailAddress;
  String FullName;
  String AddressLine1;
  String MobileNumber;
  String MerchantCustomerId;
  List<ZestmoneyOrderItemRequest> Basket;

  public String getBasketAmount() {
    return BasketAmount;
  }

  public void setBasketAmount(String basketAmount) {
    BasketAmount = basketAmount;
  }

  public String getOrderId() {
    return OrderId;
  }

  public void setOrderId(String orderId) {
    OrderId = orderId;
  }

  public String getReturnUrl() {
    return ReturnUrl;
  }

  public void setReturnUrl(String returnUrl) {
    ReturnUrl = returnUrl;
  }

  public String getApprovedUrl() {
    return ApprovedUrl;
  }

  public void setApprovedUrl(String approvedUrl) {
    ApprovedUrl = approvedUrl;
  }

  public String getEmailAddress() {
    return EmailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    EmailAddress = emailAddress;
  }

  public String getFullName() {
    return FullName;
  }

  public void setFullName(String fullName) {
    FullName = fullName;
  }

  public String getAddressLine1() {
    return AddressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    AddressLine1 = addressLine1;
  }

  public String getMobileNumber() {
    return MobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    MobileNumber = mobileNumber;
  }

  public String getMerchantCustomerId() {
    return MerchantCustomerId;
  }

  public void setMerchantCustomerId(String merchantCustomerId) {
    MerchantCustomerId = merchantCustomerId;
  }

  public List<ZestmoneyOrderItemRequest> getBasket() {
    return Basket;
  }

  public void setBasket(List<ZestmoneyOrderItemRequest> basket) {
    Basket = basket;
  }

  public String getDeliveryPostCode() {
    return DeliveryPostCode;
  }

  public void setDeliveryPostCode(String deliveryPostCode) {
    DeliveryPostCode = deliveryPostCode;
  }
}
