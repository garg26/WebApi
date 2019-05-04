package com.neostencil.model.specifications;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.neostencil.model.entities.Coupon;
import com.neostencil.requests.DataFieldRequest;

public class CouponSpecificationBuilder {

  private List<DataFieldRequest> params;

  public CouponSpecificationBuilder() {
    this.params = new ArrayList<DataFieldRequest>();
  }

  public CouponSpecificationBuilder with(String key, String value) {
    params.add(new DataFieldRequest(key, key, value));
    return this;
  }

  public Specification<Coupon> build() {
    if (params.size() == 0) {
      return null;
    }

    List<Specification<Coupon>> specs = new ArrayList<Specification<Coupon>>();
    for (DataFieldRequest param : params) {
      specs.add(new CouponSpecification(param));
    }

    Specification<Coupon> result = specs.get(0);
    for (int i = 1; i < specs.size(); i++) {
      result = result.and(specs.get(i));
    }
    return result;
  }
}
