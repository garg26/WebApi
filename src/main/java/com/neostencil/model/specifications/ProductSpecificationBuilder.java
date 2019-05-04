package com.neostencil.model.specifications;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.neostencil.model.entities.Product;
import com.neostencil.requests.DataFieldRequest;

public class ProductSpecificationBuilder {

  private List<DataFieldRequest> params;

  public ProductSpecificationBuilder() {
    this.params = new ArrayList<DataFieldRequest>();
  }

  public ProductSpecificationBuilder with(String key, String value) {
    params.add(new DataFieldRequest(key, key, value));
    return this;
  }

  public Specification<Product> build() {
    if (params.size() == 0) {
      return null;
    }

    List<Specification<Product>> specs = new ArrayList<Specification<Product>>();
    for (DataFieldRequest param : params) {
      specs.add(new ProductSpecification(param));
    }

    Specification<Product> result = specs.get(0);
    for (int i = 1; i < specs.size(); i++) {
      result = result.and(specs.get(i));
    }
    return result;
  }
}
