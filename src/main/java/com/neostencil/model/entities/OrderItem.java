package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ns_order_items")
public class OrderItem extends DomainObject {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ns_order_item_gen")
  @TableGenerator(name = "ns_order_item_gen", table = "ns_order_item_id_gen", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "OrderItemId_Gen", initialValue = 30000, allocationSize = 100)
  @Id
  @Column(name = "item_id")
  int itemId;

  /*
   * @Column(name = "product_id") int productId;
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  Product product;

  @JsonBackReference(value = "orderReference")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  Order order;

  @Column(name = "quantity")
  int quantity;

  @Column(name = "price")
  double price;

  @Column(name = "sub_total")
  double subtotal;

  @Column(name = "payable_amount")
  double payableAmount;
  
  @Column(name="regular_amount")
  double regularAmount;
  
  @Column(name="neo_cash")
  double neoCash;

  public OrderItem() {
    super();
  }

  public OrderItem(Product product, Order order, int quantity, double price, double subtotal,
      double payableAmount,double regularAmount) {
    this.product = product;
    this.order = order;
    this.quantity = quantity;
    this.price = price;
    this.subtotal = subtotal;
    this.payableAmount = payableAmount;
    this.regularAmount=regularAmount;
  }

  public OrderItem(CartOrderItem item, Order order) {
    this(item.getProduct(), order, item.getQuantity(), item.getProduct().getPrice(),
        item.getTotalAmount(),
        item.getPayableAmount(),item.getRegularAmount());
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  /**
   * @return the itemId
   */
  public int getItemId() {
    return itemId;
  }

  /**
   * @param itemId the itemId to set
   */
  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  /**
   * @return the quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * @param quantity the quantity to set
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * @return the subtotal
   */
  public double getSubtotal() {
    return subtotal;
  }

  /**
   * @param subtotal the subtotal to set
   */
  public void setSubtotal(double subtotal) {
    this.subtotal = subtotal;
  }

  /**
   * @return the product
   */
  public Product getProduct() {
    return product;
  }

  /**
   * @param product the product to set
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * @return the price
   */
  public double getPrice() {
    return price;
  }

  /**
   * @param price the price to set
   */
  public void setPrice(double price) {
    this.price = price;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "OrderItem [itemId=" + itemId + "| product=" + product + "| quantity=" + quantity
        + "| price=" + price
        + "| subtotal=" + subtotal + "]";
  }

  /**
   * @return the payableAmount
   */
  public double getPayableAmount() {
    return payableAmount;
  }

  /**
   * @param payableAmount the payableAmount to set
   */
  public void setPayableAmount(double payableAmount) {
    this.payableAmount = payableAmount;
  }

  /**
   * @return the regularAmount
   */
  public double getRegularAmount() {
    return regularAmount;
  }

  /**
   * @param regularAmount the regularAmount to set
   */
  public void setRegularAmount(double regularAmount) {
    this.regularAmount = regularAmount;
  }

  /**
   * @return the neoCash
   */
  public double getNeoCash() {
    return neoCash;
  }

  /**
   * @param neoCash the neoCash to set
   */
  public void setNeoCash(double neoCash) {
    this.neoCash = neoCash;
  }

}
