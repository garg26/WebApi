package com.neostencil.projections;

import java.util.Date;
import java.util.Set;
import org.springframework.data.rest.core.config.Projection;
import com.neostencil.common.enums.DiscountType;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.Coupon;
import com.neostencil.model.entities.Product;

/**
 * Interface Projection for Coupon class. This is needed for performance improvement as well as
 * security reasons
 * 
 * @author shilpa
 *
 */
@Projection(name = "couponDTO", types = Coupon.class)
public interface CouponProjection {

  int getId();

  Set<ProductProjection> getIncludedProducts();

  Set<ProductProjection> getExcludedProducts();

  Set<UserProjection> getAllowedUsers();

  String getCode();

  String getDescription();

  float getAmount();

  DiscountType getDiscountType();

  Date getExpiryDate();

  double getMinimumSpend();

  double getDiscountPercentage();

  PublishStatus getStatus();

  int getNoOfUsages();

  int getUsageLimitPerCoupon();
}
