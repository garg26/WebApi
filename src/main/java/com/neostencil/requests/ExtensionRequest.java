package com.neostencil.requests;

import com.neostencil.common.enums.PaymentModeType;

/**
 * 
 * @author shilpa
 *
 */
public class ExtensionRequest {

	private int batchId;
	private PaymentModeType paymentMode;
	private String email;
	private double amount;
	private int validity;

	/**
	 * @return the batchId
	 */
	public int getBatchId() {
		return batchId;
	}

	/**
	 * @param batchId
	 *            the batchId to set
	 */
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	/**
	 * @return the paymentMode
	 */
	public PaymentModeType getPaymentMode() {
		return paymentMode;
	}

	/**
	 * @param paymentMode
	 *            the paymentMode to set
	 */
	public void setPaymentMode(PaymentModeType paymentMode) {
		this.paymentMode = paymentMode;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the validity
	 */
	public int getValidity() {
		return validity;
	}

	/**
	 * @param validity
	 *            the validity to set
	 */
	public void setValidity(int validity) {
		this.validity = validity;
	}

}
