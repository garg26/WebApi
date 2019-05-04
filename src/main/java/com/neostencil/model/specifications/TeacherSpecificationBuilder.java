package com.neostencil.model.specifications;


import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.requests.Filter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class TeacherSpecificationBuilder {

  private final List<Filter> params;

  public TeacherSpecificationBuilder() {
    params = new ArrayList<Filter>();
  }

  public TeacherSpecificationBuilder with(String key, String value) {
    params.add(new Filter(key, value));
    return this;
  }

  public Specification<TeacherDetails> build(LinkedList<Filter> params, LinkedList<Filter> subjectFilters) {
    if (params.size() == 0) {
      return null;
    }

    LinkedList<Specification<TeacherDetails>> specs = new LinkedList<>();
    LinkedList<Specification<TeacherDetails>> subjectSpecs = new LinkedList<>();



    for (Filter param : params) {
      specs.add(new TeacherSpecification(param));
    }

    if (subjectFilters != null && subjectFilters.size() > 0) {
      for (Filter param : subjectFilters) {
        subjectSpecs.add(new TeacherSpecification(param));
      }
    }

    Specification<TeacherDetails> result = null;
    if (subjectSpecs != null && subjectSpecs.size() > 0) {
      result = subjectSpecs.get(0);

      if (subjectSpecs.size() > 1) {
        for (int i = 1; i < subjectSpecs.size(); i++) {
          result = result.or(subjectSpecs.get(i));
        }

      }
      result = result.and(specs.get(0));

    } else {
      result = specs.get(0);

      for (int i = 1; i < specs.size(); i++) {
        result = result.and(specs.get(i));
      }
    }


    return result;
  }
}
