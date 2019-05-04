package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.neostencil.model.entities.CartOrderItem;

@Projection(name = "cartItemProjection", types = CartOrderItem.class)
public interface CartItemProjection {

  int getItemId();

  ProductProjection getProduct();

  int getQuantity();
}
