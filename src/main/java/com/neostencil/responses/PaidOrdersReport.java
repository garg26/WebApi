package com.neostencil.responses;

/**
 * 
 * @author shilpa
 *
 */
public class PaidOrdersReport {

  private int totalNoOfOrders;
  private int totalNoOfCustomers;
  private int totalNoOfOrderItems;
  private double grossSales;
  private double netSales;
  private double avgGrossDailySales;
  private double avgNetDailySales;
  private long noOfNewCustomers;
  private long noOfOrdersByNewCustomers;
  private double totalNeoCashRedeemed;

  /**
   * @return the totalNoOfOrders
   */
  public int getTotalNoOfOrders() {
    return totalNoOfOrders;
  }

  /**
   * @param totalNoOfOrders the totalNoOfOrders to set
   */
  public void setTotalNoOfOrders(int totalNoOfOrders) {
    this.totalNoOfOrders = totalNoOfOrders;
  }

  /**
   * @return the totalNoOfCustomers
   */
  public int getTotalNoOfCustomers() {
    return totalNoOfCustomers;
  }

  /**
   * @param totalNoOfCustomers the totalNoOfCustomers to set
   */
  public void setTotalNoOfCustomers(int totalNoOfCustomers) {
    this.totalNoOfCustomers = totalNoOfCustomers;
  }

  /**
   * @return the totalNoOfOrderItems
   */
  public int getTotalNoOfOrderItems() {
    return totalNoOfOrderItems;
  }

  /**
   * @param totalNoOfOrderItems the totalNoOfOrderItems to set
   */
  public void setTotalNoOfOrderItems(int totalNoOfOrderItems) {
    this.totalNoOfOrderItems = totalNoOfOrderItems;
  }

  /**
   * @return the grossSales
   */
  public double getGrossSales() {
    return grossSales;
  }

  /**
   * @param grossSales the grossSales to set
   */
  public void setGrossSales(double grossSales) {
    this.grossSales = grossSales;
  }

  /**
   * @return the netSales
   */
  public double getNetSales() {
    return netSales;
  }

  /**
   * @param netSales the netSales to set
   */
  public void setNetSales(double netSales) {
    this.netSales = netSales;
  }

  /**
   * @return the avgGrossDailySales
   */
  public double getAvgGrossDailySales() {
    return avgGrossDailySales;
  }

  /**
   * @param avgGrossDailySales the avgGrossDailySales to set
   */
  public void setAvgGrossDailySales(double avgGrossDailySales) {
    this.avgGrossDailySales = avgGrossDailySales;
  }

  /**
   * @return the avgNetDailySales
   */
  public double getAvgNetDailySales() {
    return avgNetDailySales;
  }

  /**
   * @param avgNetDailySales the avgNetDailySales to set
   */
  public void setAvgNetDailySales(double avgNetDailySales) {
    this.avgNetDailySales = avgNetDailySales;
  }

  /**
   * @return the noOfNewCustomers
   */
  public long getNoOfNewCustomers() {
    return noOfNewCustomers;
  }

  /**
   * @param noOfNewCustomers the noOfNewCustomers to set
   */
  public void setNoOfNewCustomers(long noOfNewCustomers) {
    this.noOfNewCustomers = noOfNewCustomers;
  }

  /**
   * @return the noOfOrdersByNewCustomers
   */
  public long getNoOfOrdersByNewCustomers() {
    return noOfOrdersByNewCustomers;
  }

  /**
   * @param noOfOrdersByNewCustomers the noOfOrdersByNewCustomers to set
   */
  public void setNoOfOrdersByNewCustomers(long noOfOrdersByNewCustomers) {
    this.noOfOrdersByNewCustomers = noOfOrdersByNewCustomers;
  }

  /**
   * @return the totalNeoCashRedeemed
   */
  public double getTotalNeoCashRedeemed() {
    return totalNeoCashRedeemed;
  }

  /**
   * @param totalNeoCashRedeemed the totalNeoCashRedeemed to set
   */
  public void setTotalNeoCashRedeemed(double totalNeoCashRedeemed) {
    this.totalNeoCashRedeemed = totalNeoCashRedeemed;
  }

}
