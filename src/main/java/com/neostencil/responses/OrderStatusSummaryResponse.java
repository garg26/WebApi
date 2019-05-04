package com.neostencil.responses;

public class OrderStatusSummaryResponse {

  int noOfRefunds;
  int noOfProcessed;
  int noOfPendingPayment;
  int noOfPaymentComplete;
  int noOfPaymentFailed;
  int noOfCancelled;
  int noOfApproved;
  int noOfOnHold;

  /**
   * @return the noOfRefunds
   */
  public int getNoOfRefunds() {
    return noOfRefunds;
  }

  /**
   * @param noOfRefunds the noOfRefunds to set
   */
  public void setNoOfRefunds(int noOfRefunds) {
    this.noOfRefunds = noOfRefunds;
  }

  /**
   * @return the noOfProcessed
   */
  public int getNoOfProcessed() {
    return noOfProcessed;
  }

  /**
   * @param noOfProcessed the noOfProcessed to set
   */
  public void setNoOfProcessed(int noOfProcessed) {
    this.noOfProcessed = noOfProcessed;
  }

  /**
   * @return the noOfPendingPayment
   */
  public int getNoOfPendingPayment() {
    return noOfPendingPayment;
  }

  /**
   * @param noOfPendingPayment the noOfPendingPayment to set
   */
  public void setNoOfPendingPayment(int noOfPendingPayment) {
    this.noOfPendingPayment = noOfPendingPayment;
  }

  /**
   * @return the noOfPaymentComplete
   */
  public int getNoOfPaymentComplete() {
    return noOfPaymentComplete;
  }

  /**
   * @param noOfPaymentComplete the noOfPaymentComplete to set
   */
  public void setNoOfPaymentComplete(int noOfPaymentComplete) {
    this.noOfPaymentComplete = noOfPaymentComplete;
  }

  /**
   * @return the noOfPaymentFailed
   */
  public int getNoOfPaymentFailed() {
    return noOfPaymentFailed;
  }

  /**
   * @param noOfPaymentFailed the noOfPaymentFailed to set
   */
  public void setNoOfPaymentFailed(int noOfPaymentFailed) {
    this.noOfPaymentFailed = noOfPaymentFailed;
  }

  /**
   * @return the noOfCancelled
   */
  public int getNoOfCancelled() {
    return noOfCancelled;
  }

  /**
   * @param noOfCancelled the noOfCancelled to set
   */
  public void setNoOfCancelled(int noOfCancelled) {
    this.noOfCancelled = noOfCancelled;
  }

  /**
   * @return the noOfApproved
   */
  public int getNoOfApproved() {
    return noOfApproved;
  }

  /**
   * @param noOfApproved the noOfApproved to set
   */
  public void setNoOfApproved(int noOfApproved) {
    this.noOfApproved = noOfApproved;
  }

  /**
   * @return the noOfOnHold
   */
  public int getNoOfOnHold() {
    return noOfOnHold;
  }

  /**
   * @param noOfOnHold the noOfOnHold to set
   */
  public void setNoOfOnHold(int noOfOnHold) {
    this.noOfOnHold = noOfOnHold;
  }



}
