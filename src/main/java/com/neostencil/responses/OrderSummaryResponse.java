package com.neostencil.responses;

import com.neostencil.model.entities.Address;
import com.neostencil.model.entities.Order;

public class OrderSummaryResponse extends OrderResponse {

  Address address;
  String paymentId;

  public OrderSummaryResponse() {}

  public OrderSummaryResponse(Order order) {
    super(order);
    this.address = order.getAddress();
    this.paymentId = order.getTransactionId();
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }
}
