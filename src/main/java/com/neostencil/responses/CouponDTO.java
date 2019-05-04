package com.neostencil.responses;

import java.util.Date;

public class CouponDTO {

  String couponCode;
  String description;
  float amount;
  String discountType;
  Date expiryDate;
  double minimumSpend;
  double discountPercentage;
  String status;
  String includedProducts;
  String excludedProducts;
  String allowedUsers;

  /**
   * @return the couponCode
   */
  public String getCouponCode() {
    return couponCode;
  }

  /**
   * @param couponCode the couponCode to set
   */
  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the amount
   */
  public float getAmount() {
    return amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(float amount) {
    this.amount = amount;
  }

  /**
   * @return the discountType
   */
  public String getDiscountType() {
    return discountType;
  }

  /**
   * @param discountType the discountType to set
   */
  public void setDiscountType(String discountType) {
    this.discountType = discountType;
  }

  /**
   * @return the expiryDate
   */
  public Date getExpiryDate() {
    return expiryDate;
  }

  /**
   * @param expiryDate the expiryDate to set
   */
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  /**
   * @return the minimumSpend
   */
  public double getMinimumSpend() {
    return minimumSpend;
  }

  /**
   * @param minimumSpend the minimumSpend to set
   */
  public void setMinimumSpend(double minimumSpend) {
    this.minimumSpend = minimumSpend;
  }

  /**
   * @return the discountPercentage
   */
  public double getDiscountPercentage() {
    return discountPercentage;
  }

  /**
   * @param discountPercentage the discountPercentage to set
   */
  public void setDiscountPercentage(double discountPercentage) {
    this.discountPercentage = discountPercentage;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return the includedProducts
   */
  public String getIncludedProducts() {
    return includedProducts;
  }

  /**
   * @param includedProducts the includedProducts to set
   */
  public void setIncludedProducts(String includedProducts) {
    this.includedProducts = includedProducts;
  }

  /**
   * @return the excludedProducts
   */
  public String getExcludedProducts() {
    return excludedProducts;
  }

  /**
   * @param excludedProducts the excludedProducts to set
   */
  public void setExcludedProducts(String excludedProducts) {
    this.excludedProducts = excludedProducts;
  }

  /**
   * @return the allowedUsers
   */
  public String getAllowedUsers() {
    return allowedUsers;
  }

  /**
   * @param allowedUsers the allowedUsers to set
   */
  public void setAllowedUsers(String allowedUsers) {
    this.allowedUsers = allowedUsers;
  }



}
