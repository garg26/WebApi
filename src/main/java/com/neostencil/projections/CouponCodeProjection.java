package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.neostencil.model.entities.Coupon;

@Projection(name = "couponCodeProjection", types = Coupon.class)
public interface CouponCodeProjection {

  String getCode();
}
