package com.neostencil.responses;

/**
 * 
 * @author shilpa
 *
 */
public class ValidateNeoCashRedemptionResponse {

  private boolean valid;
  private double neoCashRedeemed;
  private double payableAmount;
  private String message;
  /**
   * @return the valid
   */
  public boolean isValid() {
    return valid;
  }
  /**
   * @param valid the valid to set
   */
  public void setValid(boolean valid) {
    this.valid = valid;
  }
  
  /**
   * @return the payableAmount
   */
  public double getPayableAmount() {
    return payableAmount;
  }
  /**
   * @param payableAmount the payableAmount to set
   */
  public void setPayableAmount(double payableAmount) {
    this.payableAmount = payableAmount;
  }
  /**
   * @return the neoCashRedeemed
   */
  public double getNeoCashRedeemed() {
    return neoCashRedeemed;
  }
  /**
   * @param neoCashRedeemed the neoCashRedeemed to set
   */
  public void setNeoCashRedeemed(double neoCashRedeemed) {
    this.neoCashRedeemed = neoCashRedeemed;
  }
  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }
  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }
  
  
}
