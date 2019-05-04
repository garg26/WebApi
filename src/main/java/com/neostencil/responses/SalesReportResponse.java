package com.neostencil.responses;

/**
 * 
 * @author shilpa
 *
 */
public class SalesReportResponse {

  private PaidOrdersReport paidOrdersReport;
  private FreeOrdersReport freeOrdersReport;
  private RefundReport refundReport;
  private CouponReport couponReport;

  /**
   * @return the paidOrdersReport
   */
  public PaidOrdersReport getPaidOrdersReport() {
    return paidOrdersReport;
  }

  /**
   * @param paidOrdersReport the paidOrdersReport to set
   */
  public void setPaidOrdersReport(PaidOrdersReport paidOrdersReport) {
    this.paidOrdersReport = paidOrdersReport;
  }

  /**
   * @return the freeOrdersReport
   */
  public FreeOrdersReport getFreeOrdersReport() {
    return freeOrdersReport;
  }

  /**
   * @param freeOrdersReport the freeOrdersReport to set
   */
  public void setFreeOrdersReport(FreeOrdersReport freeOrdersReport) {
    this.freeOrdersReport = freeOrdersReport;
  }

  /**
   * @return the refundReport
   */
  public RefundReport getRefundReport() {
    return refundReport;
  }

  /**
   * @param refundReport the refundReport to set
   */
  public void setRefundReport(RefundReport refundReport) {
    this.refundReport = refundReport;
  }

  /**
   * @return the couponReport
   */
  public CouponReport getCouponReport() {
    return couponReport;
  }

  /**
   * @param couponReport the couponReport to set
   */
  public void setCouponReport(CouponReport couponReport) {
    this.couponReport = couponReport;
  }



}
