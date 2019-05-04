package com.neostencil.requests;

import com.neostencil.common.enums.RefundType;

public class RefundRequest {

  int orderId;
  RefundType refundType;
  double amount;
  String refundReason;

  /**
   * @return the orderId
   */
  public int getOrderId() {
    return orderId;
  }

  /**
   * @param orderId the orderId to set
   */
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  /**
   * @return the refundType
   */
  public RefundType getRefundType() {
    return refundType;
  }

  /**
   * @param refundType the refundType to set
   */
  public void setRefundType(RefundType refundType) {
    this.refundType = refundType;
  }

  /**
   * @return the amount
   */
  public double getAmount() {
    return amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * @return the refundReason
   */
  public String getRefundReason() {
    return refundReason;
  }

  /**
   * @param refundReason the refundReason to set
   */
  public void setRefundReason(String refundReason) {
    this.refundReason = refundReason;
  }



}
