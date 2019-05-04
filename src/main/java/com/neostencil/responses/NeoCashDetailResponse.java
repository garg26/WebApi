package com.neostencil.responses;

/**
 * 
 * @author shilpa
 *
 */
public class NeoCashDetailResponse {
  
  private double useableNeoCash;
  private double totalNeoCash;
  private String message;
  /**
   * @return the useableNeoCash
   */
  public double getUseableNeoCash() {
    return useableNeoCash;
  }
  /**
   * @param useableNeoCash the useableNeoCash to set
   */
  public void setUseableNeoCash(double useableNeoCash) {
    this.useableNeoCash = useableNeoCash;
  }
  /**
   * @return the totalNeoCash
   */
  public double getTotalNeoCash() {
    return totalNeoCash;
  }
  /**
   * @param totalNeoCash the totalNeoCash to set
   */
  public void setTotalNeoCash(double totalNeoCash) {
    this.totalNeoCash = totalNeoCash;
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
