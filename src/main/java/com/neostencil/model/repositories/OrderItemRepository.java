package com.neostencil.model.repositories;

import com.neostencil.model.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

  OrderItem findByItemId(int id);
}
