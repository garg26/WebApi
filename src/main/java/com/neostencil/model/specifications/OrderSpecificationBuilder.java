package com.neostencil.model.specifications;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.neostencil.model.entities.Order;
import com.neostencil.requests.DataFieldRequest;

public class OrderSpecificationBuilder {

  private List<DataFieldRequest> params;

  public OrderSpecificationBuilder() {
    this.params = new ArrayList<DataFieldRequest>();
  }

  public OrderSpecificationBuilder with(String key, String value) {
    params.add(new DataFieldRequest(key, key, value));
    return this;
  }

  public Specification<Order> build() {
    if (params.size() == 0) {
      return null;
    }

    List<Specification<Order>> specs = new ArrayList<Specification<Order>>();
    for (DataFieldRequest param : params) {
      specs.add(new OrderSpecification(param));
    }

    Specification<Order> result = specs.get(0);
    for (int i = 1; i < specs.size(); i++) {
      result = result.and(specs.get(i));
    }
    return result;
  }
}
