package com.neostencil.model.specifications;

import com.neostencil.common.enums.CourseCategory;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.model.entities.Course;
import com.neostencil.requests.Filter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecificationBuilder {

  private final List<Filter> otherFilters;
  private String courseCategory;
  private String examSegment;

  public CourseSpecificationBuilder() {
    otherFilters = new ArrayList<Filter>();
  }

  public CourseSpecificationBuilder with(String key, String value) {
    otherFilters.add(new Filter(key, value));
    return this;
  }

  public Specification<Course> build(LinkedList<Filter> otherFilters, LinkedList<Filter> subjectFilters, LinkedList<Filter> subCategoryFilters,
      LinkedList<Filter> examSegmentFilters, LinkedList<Filter> categoryFilters) {

    if (otherFilters.size() == 0) {
      return null;
    }

    LinkedList<Specification<Course>> specs = new LinkedList<>();
    LinkedList<Specification<Course>> subjectSpecs = new LinkedList<>();
    LinkedList<Specification<Course>> subCategorySpecs = new LinkedList<>();
    LinkedList<Specification<Course>> examSegmentSpec = new LinkedList<>();
    LinkedList<Specification<Course>> categorySpec = new LinkedList<>();



    if(examSegmentFilters!=null && examSegmentFilters.size()>0){
      examSegment = examSegmentFilters.get(0).getValue();
      for (Filter param : examSegmentFilters) {
        param.setValue(ExamSegmentTypes.fromString(param.getValue()).toString());
        examSegmentSpec.add(new CourseSpecification(param));
      }
    }

    if(categoryFilters!=null && categoryFilters.size()>0){
      courseCategory = categoryFilters.get(0).getValue();
      for (Filter param : categoryFilters) {
        param.setValue(CourseCategory.fromString(param.getValue()).toString());
        categorySpec.add(new CourseSpecification(param,examSegment));
      }
    }

    if (subCategoryFilters != null && subCategoryFilters.size() > 0) {
      for (Filter param : subCategoryFilters) {
        param.setValue(CourseSubCategory.fromString(param.getValue()).toString());
        subCategorySpecs.add(new CourseSpecification(param, courseCategory));
      }
    }

    if (subjectFilters != null && subjectFilters.size() > 0) {
      for (Filter param : subjectFilters) {
        subjectSpecs.add(new CourseSpecification(param));
      }
    }

    if(otherFilters!=null && otherFilters.size()>0){
      for (Filter param : otherFilters) {
        specs.add(new CourseSpecification(param));
      }
    }

    Specification<Course> result = null;

    if (specs != null && specs.size() > 0) {
      result = specs.get(0);
      for (int i = 1; i < specs.size(); i++) {
        result = result.and(specs.get(i));
      }
    }

    if (subjectSpecs != null && subjectSpecs.size() > 0 && subCategorySpecs != null
        && subCategorySpecs.size() > 0) {
      result = subjectSpecs.get(0);

      if (subjectSpecs.size() > 1) {
        for (int i = 1; i < subjectSpecs.size(); i++) {
          result = result.or(subjectSpecs.get(i));
        }

      }

      Specification<Course> subCategorySpecification = subCategorySpecs.get(0);

      if(subCategorySpecs.size()>1) {
        for (int i = 1; i < subCategorySpecs.size(); i++) {
          subCategorySpecification = subCategorySpecification.or(subCategorySpecs.get(i));
        }
      }

      result = result.and(subCategorySpecification);

      result = result.and(specs.get(0));

    }
    else if(subCategorySpecs.size()==0 && subjectSpecs != null && subjectSpecs.size()>0){
      result = subjectSpecs.get(0);

      if (subjectSpecs.size() > 1) {
        for (int i = 1; i < subjectSpecs.size(); i++) {
          result = result.or(subjectSpecs.get(i));
        }

      }
      result = result.and(specs.get(0));
    }

    Specification<Course> examSpecs = null;
    if(examSegmentSpec !=null && examSegmentSpec.size()>0){

      examSpecs = examSegmentSpec.get(0);
      if(examSegmentSpec.size()>1){
        for(int i = 1;i<examSegmentSpec.size();i++){
          examSpecs = examSpecs.or(examSegmentSpec.get(i));
        }
      }
    }

    if(examSpecs!=null) {
      result = result.and(examSpecs);
    }

    Specification<Course> catSpecs = null;
    if(categorySpec!=null && categorySpec.size()>0){

      catSpecs = categorySpec.get(0);
      if(categorySpec.size()>1){
        for (int i=1;i<categorySpec.size();i++){
          catSpecs = catSpecs.or(categorySpec.get(i));
        }
      }
    }

    if(catSpecs!=null) {
      result = result.and(catSpecs);
    }



    return result;
  }
}
