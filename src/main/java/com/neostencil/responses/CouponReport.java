package com.neostencil.responses;

/**
 * 
 * @author shilpa
 *
 */
public class CouponReport {

  private int totalNoOfCouponsUsed;
  private double totalDiscountValue;
  private String mostPopularCoupons;

  /**
   * @return the totalNoOfCouponsUsed
   */
  public int getTotalNoOfCouponsUsed() {
    return totalNoOfCouponsUsed;
  }

  /**
   * @param totalNoOfCouponsUsed the totalNoOfCouponsUsed to set
   */
  public void setTotalNoOfCouponsUsed(int totalNoOfCouponsUsed) {
    this.totalNoOfCouponsUsed = totalNoOfCouponsUsed;
  }

  /**
   * @return the totalDiscountValue
   */
  public double getTotalDiscountValue() {
    return totalDiscountValue;
  }

  /**
   * @param totalDiscountValue the totalDiscountValue to set
   */
  public void setTotalDiscountValue(double totalDiscountValue) {
    this.totalDiscountValue = totalDiscountValue;
  }

  /**
   * @return the mostPopularCoupons
   */
  public String getMostPopularCoupons() {
    return mostPopularCoupons;
  }

  /**
   * @param mostPopularCoupons the mostPopularCoupons to set
   */
  public void setMostPopularCoupons(String mostPopularCoupons) {
    this.mostPopularCoupons = mostPopularCoupons;
  }


}
