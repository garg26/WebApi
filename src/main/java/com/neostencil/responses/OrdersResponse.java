package com.neostencil.responses;

import java.util.List;
import com.neostencil.model.entities.Order;
import com.neostencil.projections.OrderProjection;

public class OrdersResponse {

  List<OrderProjection> orders;
  int totalOrders;

  /**
   * @return the totalOrders
   */
  public int getTotalOrders() {
    return totalOrders;
  }

  /**
   * @param totalOrders the totalOrders to set
   */
  public void setTotalOrders(int totalOrders) {
    this.totalOrders = totalOrders;
  }

  /**
   * @return the orders
   */
  public List<OrderProjection> getOrders() {
    return orders;
  }

  /**
   * @param orders the orders to set
   */
  public void setOrders(List<OrderProjection> orders) {
    this.orders = orders;
  }

}
