package com.neostencil.requests;

/**
 * 
 * @author shilpa
 *
 */
public class DebitNeoCashRequest {

  private double amount;
  private String reason;
  private int orderId;

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
   * @return the reason
   */
  public String getReason() {
    return reason;
  }

  /**
   * @param reason the reason to set
   */
  public void setReason(String reason) {
    this.reason = reason;
  }

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


}
