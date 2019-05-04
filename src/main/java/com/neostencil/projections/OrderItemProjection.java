package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "orderItemProjection", types = OrderItemProjection.class)
public interface OrderItemProjection {

  int getItemId();

  ProductProjection getProduct();

  double getPrice();

  double getSubtotal();

  int getQuantity();

  double getPayableAmount();
}
