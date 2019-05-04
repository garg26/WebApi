package com.neostencil.requests;

/**
 * 
 * @author shilpa
 *
 */
public class CreditNeoCashRequest {

  private double amount;
  private String reason;
  private long creditTo;

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
   * @return the creditTo
   */
  public long getCreditTo() {
    return creditTo;
  }

  /**
   * @param creditTo the creditTo to set
   */
  public void setCreditTo(long creditTo) {
    this.creditTo = creditTo;
  }

  

  
}
