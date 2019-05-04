package com.neostencil.requests;

public class ValidateOTPRequest {

	private int otpnum;

	private UnitRequest unitRequest;

	/**
	 * @return the otpnum
	 */
	public int getOtpnum() {
		return otpnum;
	}

	/**
	 * @param otpnum
	 *            the otpnum to set
	 */
	public void setOtpnum(int otpnum) {
		this.otpnum = otpnum;
	}

	/**
	 * @return the unitRequest
	 */
	public UnitRequest getUnitRequest() {
		return unitRequest;
	}

	/**
	 * @param unitRequest
	 *            the unitRequest to set
	 */
	public void setUnitRequest(UnitRequest unitRequest) {
		this.unitRequest = unitRequest;
	}

}
