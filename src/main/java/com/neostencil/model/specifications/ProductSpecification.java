package com.neostencil.model.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.ProductType;
import com.neostencil.model.entities.Product;
import com.neostencil.requests.DataFieldRequest;

public class ProductSpecification implements Specification<Product> {


  private DataFieldRequest criteria;

  public ProductSpecification(DataFieldRequest criteria) {
    super();
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {

    if (root.get(criteria.getFieldName()).getJavaType() == String.class) {
      return criteriaBuilder.like(root.get(criteria.getFieldName()),
          "%" + criteria.getFieldValue() + "%");
    } else if (root.get(criteria.getFieldName()).getJavaType() == ProductType.class) {
      return criteriaBuilder.equal(root.get(criteria.getFieldName()),
          ProductType.valueOf(criteria.getFieldValue()));
    } else {
      return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getFieldValue());
    }

  }

}
