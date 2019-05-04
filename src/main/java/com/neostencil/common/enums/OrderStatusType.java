package com.neostencil.common.enums;

public enum OrderStatusType {

  PENDING_PAYMENT("Pending Payment"), PAYMENT_COMPLETE("Payment Complete"), PAYMENT_FAILED(
      "Payment Failed"), PENDING_PAYMENT_VERIFICATON(
          "Pending Payment Verification"), PENDING_INTERNAL_PROCESSING(
              "Pending Internal Processing"), CANCELLED("Cancelled"), REFUNDED(
                  "Refunded"), PROCESSED("Processed"), APPROVED("Approved"), ON_HOLD("On Hold");

  public String jsonValue;

  private OrderStatusType(String json) {
    this.jsonValue = json;
  }

  public String getJsonValue() {
    return jsonValue;
  }
}
