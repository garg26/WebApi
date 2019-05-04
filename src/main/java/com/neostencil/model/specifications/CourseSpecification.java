package com.neostencil.model.specifications;

import com.neostencil.common.enums.PublishStatus;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import com.neostencil.common.enums.CourseCategory;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.model.entities.Course;
import com.neostencil.requests.Filter;

public class CourseSpecification implements Specification<Course> {

  private Filter criteria;
  private String jsonValue;

  public CourseSpecification(Filter criteria) {
    super();
    this.criteria = criteria;
  }

  public CourseSpecification(Filter criteria,String courseCategory) {
    super();
    this.criteria = criteria;
    this.jsonValue = courseCategory;
  }



  public CourseSpecification() {
    super();
  }

  @Override
  public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


    if (root.get(criteria.getFilterName()).getJavaType() == String.class) {
      return cb.like(cb.upper(root.<String>get(criteria.getFilterName())), "%" + criteria.getValue().toUpperCase() + "%");
    } else if (root.get(criteria.getFilterName()).getJavaType() == CourseCategory.class) {
      return cb.equal(root.get(criteria.getFilterName()),
          CourseCategory.getCourseCategory(jsonValue,criteria.getValue()));
    }
    else if (root.get(criteria.getFilterName()).getJavaType() == CourseSubCategory.class) {

      return cb.equal(root.get(criteria.getFilterName()),
          CourseSubCategory.getCourseSubCategory(jsonValue,criteria.getValue()));
    }
    else if (root.get(criteria.getFilterName()).getJavaType() == ExamSegmentTypes.class) {
      return cb.equal(root.get(criteria.getFilterName()),
          ExamSegmentTypes.fromString(criteria.getValue()));
    }
    else if (root.get(criteria.getFilterName()).getJavaType() == PublishStatus.class) {
      return cb.equal(root.get(criteria.getFilterName()),
          PublishStatus.valueOf(criteria.getValue()));
    }
    else if (root.get(criteria.getFilterName()).getJavaType().getName()
        .equalsIgnoreCase("boolean")) {
      return cb.equal(root.get(criteria.getFilterName()), Boolean.TRUE);
    } else {
      return cb.equal(root.get(criteria.getFilterName()), criteria.getValue());
    }
  }

  public Filter getCriteria() {
    return criteria;
  }

}
