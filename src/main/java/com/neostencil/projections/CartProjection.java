package com.neostencil.projections;

import java.util.Set;
import org.springframework.data.rest.core.config.Projection;
import com.neostencil.model.entities.Cart;

@Projection(name = "cartProjection", types = Cart.class)
public interface CartProjection {

  int getCartId();

  Set<CartItemProjection> getItems();

  UserProjection getCustomer();

}
