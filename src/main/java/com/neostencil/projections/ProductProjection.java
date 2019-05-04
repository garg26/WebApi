package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.neostencil.common.enums.ProductType;
import com.neostencil.model.entities.Product;

@Projection(name = "productProjection", types = Product.class)
public interface ProductProjection {

  int getId();

  String getProductTitle();

  String getProductSlug();

  int getCommodityId();

  ProductType getType();

  double getPrice();

  String getImageUrl();
  
  double getRegularPrice();

}
