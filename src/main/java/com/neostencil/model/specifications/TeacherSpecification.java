package com.neostencil.model.specifications;

import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.TeacherCategory;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.requests.Filter;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TeacherSpecification implements Specification<TeacherDetails>{

  private Filter criteria;

  public TeacherSpecification(Filter criteria) {
    super();
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<TeacherDetails> root, CriteriaQuery<?> criteriaQuery,
      CriteriaBuilder criteriaBuilder) {

    if(root.get(criteria.getFilterName()).getJavaType() == String.class){
      return criteriaBuilder.like(root.get(criteria.getFilterName()), "%" + criteria.getValue() + "%");
    }
    else if (root.get(criteria.getFilterName()).getJavaType() == PublishStatus.class) {
      return criteriaBuilder.equal(root.get(criteria.getFilterName()),
          PublishStatus.valueOf(criteria.getValue()));
    }
    else {
      return criteriaBuilder.equal(root.get(criteria.getFilterName()), criteria.getValue());
    }

  }
}
