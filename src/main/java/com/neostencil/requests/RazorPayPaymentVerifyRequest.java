package com.neostencil.requests;

public class RazorPayPaymentVerifyRequest {

  int orderId;
  int amountInPaisa;
  String paymentId;

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public int getAmountInPaisa() {
    return amountInPaisa;
  }

  public void setAmountInPaisa(int amountInPaisa) {
    this.amountInPaisa = amountInPaisa;
  }
}
