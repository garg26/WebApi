package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@Entity
@Table(name = "ns_cart_items")
public class CartOrderItem extends DomainObject {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ns_cart_item_gen")
  @TableGenerator(name = "ns_cart_item_gen", table = "ns_cart_item_id_gen", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "OrderItemId_Gen", initialValue = 30000, allocationSize = 100)
  @Id
  @Column(name = "item_id")
  int itemId;

  /*
   * @Column(name = "product_id") int productId;
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  Product product;

  @JsonBackReference(value = "cartReference")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  Cart cart;

  @Column(name = "quantity")
  int quantity;

  @Column(name = "total_amount")
  double totalAmount;

  @Column(name = "payable_amount")
  double payableAmount;
  
  @Column(name="regular_amount")
  double regularAmount;

  public CartOrderItem() {
    super();
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

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  /**
   * @return the totalAmount
   */
  public double getTotalAmount() {
    return totalAmount;
  }

  /**
   * @param totalAmount the totalAmount to set
   */
  public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
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

}
