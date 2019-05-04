package com.neostencil.requests;

import java.sql.Timestamp;

/**
 * 
 * @author shilpa
 *
 */
public class SalesReportRequest {

  Timestamp startDate;
  Timestamp endDate;
  String[] examSegments;
  String[] orderTypes;

  /* *//**
        * @return the examSegments
        */
  /*
   * public String getExamSegments() { return examSegments; }
   * 
   *//**
     * @param examSegments the examSegments to set
     */
  /*
   * public void setExamSegments(String examSegments) { this.examSegments = examSegments; }
   * 
   *//**
     * @return the orderTypes
     */
  /*
   * public String getOrderTypes() { return orderTypes; }
   * 
   *//**
     * @param orderTypes the orderTypes to set
     *//*
       * public void setOrderTypes(String orderTypes) { this.orderTypes = orderTypes; }
       */
  /**
   * @return the startDate
   */
  /*
   * public String getStartDate() { return startDate; }
   * 
   *//**
     * @param startDate the startDate to set
     */
  /*
   * public void setStartDate(String startDate) { this.startDate = startDate; }
   * 
   *//**
     * @return the endDate
     */
  /*
   * public String getEndDate() { return endDate; }
   * 
   *//**
     * @param endDate the endDate to set
     *//*
       * public void setEndDate(String endDate) { this.endDate = endDate; }
       */

  /**
   * @return the examSegments
   */
  public String[] getExamSegments() {
    return examSegments;
  }

  /**
   * @param examSegments the examSegments to set
   */
  public void setExamSegments(String[] examSegments) {
    this.examSegments = examSegments;
  }

  /**
   * @return the orderTypes
   */
  public String[] getOrderTypes() {
    return orderTypes;
  }

  /**
   * @param orderTypes the orderTypes to set
   */
  public void setOrderTypes(String[] orderTypes) {
    this.orderTypes = orderTypes;
  }

  /**
   * @return the startDate
   */
  public Timestamp getStartDate() {
    return startDate;
  }

  /**
   * @param startDate the startDate to set
   */
  public void setStartDate(Timestamp startDate) {
    this.startDate = startDate;
  }

  /**
   * @return the endDate
   */
  public Timestamp getEndDate() {
    return endDate;
  }

  /**
   * @param endDate the endDate to set
   */
  public void setEndDate(Timestamp endDate) {
    this.endDate = endDate;
  }

}
