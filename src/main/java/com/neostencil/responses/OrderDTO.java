package com.neostencil.responses;

import java.time.LocalDate;
import com.opencsv.bean.CsvBindByPosition;

public class OrderDTO {

	int orderId;

	LocalDate paymentDate;

	String orderStatus;

	String studentName;

	String studentEmail;

	String paymentMode;

	String address;

	String phoneNumber;

	String course;

	double grossSales;

	double netSales;

	int quantity;

	String Institute;

	String orderNotes;

	String productType;
	
	double refundAmount;
	
	double neoCashRedeemed;

	public OrderDTO() {

	}

	public OrderDTO(int orderId, LocalDate paymentDate, String orderStatus, String studentName, String studentEmail,
			String paymentMode, String address, String phoneNumber, String course, double grossSales, double netSales,
			int quantity, String institute,double refundAmount,double neoCashRedeemed) {
		this.orderId = orderId;
		this.paymentDate = paymentDate;
		this.orderStatus = orderStatus;
		this.studentName = studentName;
		this.studentEmail = studentEmail;
		this.paymentMode = paymentMode;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.course = course;
		this.grossSales = grossSales;
		this.netSales = netSales;
		this.quantity = quantity;
		this.refundAmount=refundAmount;
		Institute = institute;
		this.neoCashRedeemed=neoCashRedeemed;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public double getGrossSales() {
		return grossSales;
	}

	public void setGrossSales(double grossSales) {
		this.grossSales = grossSales;
	}

	public double getNetSales() {
		return netSales;
	}

	public void setNetSales(double netSales) {
		this.netSales = netSales;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getInstitute() {
		return Institute;
	}

	public void setInstitute(String institute) {
		Institute = institute;
	}

	/**
	 * @return the orderNotes
	 */
	public String getOrderNotes() {
		return orderNotes;
	}

	/**
	 * @param orderNotes
	 *            the orderNotes to set
	 */
	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}

	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}

	/**
	 * @param productType
	 *            the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

  /**
   * @return the refundAmount
   */
  public double getRefundAmount() {
    return refundAmount;
  }

  /**
   * @param refundAmount the refundAmount to set
   */
  public void setRefundAmount(double refundAmount) {
    this.refundAmount = refundAmount;
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
