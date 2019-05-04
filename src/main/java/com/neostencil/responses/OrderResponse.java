package com.neostencil.responses;

import com.neostencil.common.enums.OrderStatusType;
import com.neostencil.common.enums.PaymentModeType;
import com.neostencil.model.entities.Cart;
import com.neostencil.model.entities.CartOrderItem;
import com.neostencil.model.entities.Order;
import com.neostencil.model.entities.OrderItem;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class OrderResponse {

  Set<OrderResponseItem> items;
  int orderId;
  OrderStatusType statusType;
  private double totalAmount;
  private double payAmount;
  private double discount;
  private PaymentModeType paymentMethod;
  private boolean freeOnlyOrder;

  public OrderResponse() {}

  public OrderResponse(Cart c) {
    this(c, 0);
  }

  public OrderResponse(Order order) {

    items = new HashSet<OrderResponseItem>();
    totalAmount = 0;
    // hardcoding discount to 0 in this constructore
    for (OrderItem item : order.getOrderItems()) {
      items.add(new OrderResponseItem((item)));
      totalAmount = totalAmount + item.getProduct().getPrice() * item.getQuantity();
    }
    if(totalAmount == 0 && order.getPayableAmount() ==0)
    {
      freeOnlyOrder = true;
    }
    payAmount = order.getPayableAmount();
    orderId = order.getOrderId();
    statusType = order.getOrderStatus();
    paymentMethod = order.getPaymentMode();
  }

  public OrderResponse(Cart c, double discount) {
    if (c == null) {
      return;
    }

    items = new LinkedHashSet<>();
    totalAmount = 0;
    payAmount = 0;
    if (c.getItems() != null) {
      for (CartOrderItem item : c.getItems()) {
        items.add(new OrderResponseItem((item)));
        totalAmount = totalAmount + item.getProduct().getPrice() * item.getQuantity();
      }
    }
    payAmount = totalAmount - discount;
  }


  public Set<OrderResponseItem> getItems() {
    return items;
  }

  public void setItems(Set<OrderResponseItem> items) {
    this.items = items;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
  }

  public double getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(double payAmount) {
    this.payAmount = payAmount;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public OrderStatusType getStatusType() {
    return statusType;
  }

  public void setStatusType(OrderStatusType statusType) {
    this.statusType = statusType;
  }

  public PaymentModeType getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentModeType paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  /**
   * @return the freeOnlyOrder
   */
  public boolean isFreeOnlyOrder() {
    return freeOnlyOrder;
  }

  /**
   * @param freeOnlyOrder the freeOnlyOrder to set
   */
  public void setFreeOnlyOrder(boolean freeOnlyOrder) {
    this.freeOnlyOrder = freeOnlyOrder;
  }
}
