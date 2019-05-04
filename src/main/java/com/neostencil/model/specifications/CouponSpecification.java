package com.neostencil.model.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.neostencil.model.entities.Coupon;
import com.neostencil.requests.DataFieldRequest;

public class CouponSpecification implements Specification<Coupon> {

  private DataFieldRequest criteria;

  public CouponSpecification(DataFieldRequest criteria) {
    super();
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<Coupon> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {

    if (root.get(criteria.getFieldName()).getJavaType() == String.class) {
      return criteriaBuilder.like(root.get(criteria.getFieldName()),
          "%" + criteria.getFieldValue() + "%");
    } else {
      return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getFieldValue());
    }

  }

}
