package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ns_carts")
public class Cart {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ns_cart_gen")
  @TableGenerator(name = "ns_cart_gen", table = "ns_cart_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "cartId_Gen", initialValue = 500,
      allocationSize = 1)
  @Id
  @Column(name = "cart_id")
  int cartId;

  @JsonManagedReference(value = "cartReference")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart")
  @OrderBy("itemId")
  Set<CartOrderItem> items;

  @JsonIgnore
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  User customer;

  /**
   * @return the cartId
   */
  public int getCartId() {
    return cartId;
  }

  /**
   * @param cartId the cartId to set
   */
  public void setCartId(int cartId) {
    this.cartId = cartId;
  }

  /**
   * @return the items
   */
  public Set<CartOrderItem> getItems() {
    return items;
  }

  /**
   * @param items the items to set
   */
  public void setItems(Set<CartOrderItem> items) {
    this.items = items;
  }

  public User getCustomer() {
    return customer;
  }

  public void setCustomer(User customer) {
    this.customer = customer;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + cartId;
    result = prime * result + ((customer == null) ? 0 : customer.hashCode());
    result = prime * result + ((items == null) ? 0 : items.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Cart other = (Cart) obj;
    if (cartId != other.cartId) {
      return false;
    }
    if (customer == null) {
      if (other.customer != null) {
        return false;
      }
    } else if (!customer.equals(other.customer)) {
      return false;
    }
    if (items == null) {
      if (other.items != null) {
        return false;
      }
    } else if (!items.equals(other.items)) {
      return false;
    }
    return true;
  }
}
