package com.neostencil.model.repositories;

import com.neostencil.model.entities.Cart;
import com.neostencil.model.entities.CartOrderItem;
import com.neostencil.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartOrderItemRepository extends JpaRepository<CartOrderItem, Integer> {

  CartOrderItem findByItemId(int id);
  CartOrderItem findByCartAndProduct(Cart c, Product p);
}
