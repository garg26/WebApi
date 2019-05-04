package com.neostencil.responses;

/**
 * 
 * @author shilpa
 *
 */
public class RefundReport {

  private int noOfFullRefunds;
  private double fullRefundValue;
  private int noOfPartialRefunds;
  private double partialRefundValue;

  /**
   * @return the noOfFullRefunds
   */
  public int getNoOfFullRefunds() {
    return noOfFullRefunds;
  }

  /**
   * @param noOfFullRefunds the noOfFullRefunds to set
   */
  public void setNoOfFullRefunds(int noOfFullRefunds) {
    this.noOfFullRefunds = noOfFullRefunds;
  }

  /**
   * @return the fullRefundValue
   */
  public double getFullRefundValue() {
    return fullRefundValue;
  }

  /**
   * @param fullRefundValue the fullRefundValue to set
   */
  public void setFullRefundValue(double fullRefundValue) {
    this.fullRefundValue = fullRefundValue;
  }

  /**
   * @return the noOfPartialRefunds
   */
  public int getNoOfPartialRefunds() {
    return noOfPartialRefunds;
  }

  /**
   * @param noOfPartialRefunds the noOfPartialRefunds to set
   */
  public void setNoOfPartialRefunds(int noOfPartialRefunds) {
    this.noOfPartialRefunds = noOfPartialRefunds;
  }

  /**
   * @return the partialRefundValue
   */
  public double getPartialRefundValue() {
    return partialRefundValue;
  }

  /**
   * @param partialRefundValue the partialRefundValue to set
   */
  public void setPartialRefundValue(double partialRefundValue) {
    this.partialRefundValue = partialRefundValue;
  }


}
