package com.neostencil.services;

import com.neostencil.common.enums.CourseCategory;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.Subjects;
import com.neostencil.responses.CourseFiltersResponse;
import com.neostencil.responses.CustomCategoryResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MobileService {

  public LinkedHashMap<String,List<CustomCategoryResponse>> getCourseFilters() {

    LinkedHashMap<String,List<CustomCategoryResponse>> examList = new LinkedHashMap<>();

    for (ExamSegmentTypes examSegmentTypes : ExamSegmentTypes.values()){

      List<CustomCategoryResponse> categoryList = new ArrayList<>();

      for (CourseCategory courseCategory : CourseCategory.values()) {


        if (courseCategory.getExamSegmentType() == examSegmentTypes) {

          CustomCategoryResponse customCategoryResponse = new CustomCategoryResponse();

          customCategoryResponse.setName(courseCategory.jsonValue());
          customCategoryResponse.setHavingSubjects(courseCategory.isHavingSubject());
          categoryList.add(customCategoryResponse);

        }

      }
      examList.put(examSegmentTypes.jsonValue(),categoryList);

    }

    return examList;
  }

  public LinkedHashMap<String,List<String>> getCourseFilters(String examSegment, String category) {

    LinkedHashMap<String,List<String>> subCategoryList = new LinkedHashMap<>();

    for(CourseSubCategory courseSubCategory : CourseSubCategory.values()){

      if(courseSubCategory.examSegmentType.jsonValue().equalsIgnoreCase(examSegment) && courseSubCategory.courseCategory.jsonValue().equalsIgnoreCase(category)){
        String jsonValue = courseSubCategory.jsonValue();

        List<String> subjectList = new ArrayList<>();
        for(Subjects subjects : Subjects.values()){

          if(subjects.courseSubCategory==courseSubCategory){
            subjectList.add(subjects.jsonValue);
          }
        }

        subCategoryList.put(jsonValue,subjectList);
      }
    }

    return subCategoryList;
  }
}
