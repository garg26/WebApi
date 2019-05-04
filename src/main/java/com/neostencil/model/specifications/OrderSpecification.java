package com.neostencil.model.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.neostencil.model.entities.Order;
import com.neostencil.requests.DataFieldRequest;


public class OrderSpecification implements Specification<Order> {

  private DataFieldRequest criteria;

  public OrderSpecification(DataFieldRequest criteria) {
    super();
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {

    if (root.get(criteria.getFieldName()).getJavaType() == String.class) {
      return criteriaBuilder.like(root.get(criteria.getFieldName()),
          "%" + criteria.getFieldValue() + "%");
    } else {
      return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getFieldValue());
    }

  }

}
