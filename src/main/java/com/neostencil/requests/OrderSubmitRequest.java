package com.neostencil.requests;

import com.neostencil.common.enums.PaymentModeType;

public class OrderSubmitRequest {

	String couponCode;
	int addressId;
	PaymentModeType paymentModeType;
	boolean emi;
	int installmentNumber;
	double actualAmount;
	String reason;
	double neoCashRedeemed;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public boolean isEmi() {
		return emi;
	}

	public void setEmi(boolean emi) {
		this.emi = emi;
	}

	public int getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(int installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	// For submitting the order from the admin dashboard
	long userId;

	public OrderSubmitRequest() {
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressID) {
		this.addressId = addressID;
	}

	public PaymentModeType getPaymentModeType() {
		return paymentModeType;
	}

	public void setPaymentModeType(PaymentModeType paymentModeType) {
		this.paymentModeType = paymentModeType;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
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
}
