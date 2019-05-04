package com.neostencil.model.repositories;

import com.neostencil.model.entities.Cart;
import com.neostencil.model.entities.User;
import com.neostencil.projections.CartProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

  Cart findByCartId(int id);

  Cart findByCustomer(User u);

  CartProjection findAllProjectedByCustomer(User u);
}
