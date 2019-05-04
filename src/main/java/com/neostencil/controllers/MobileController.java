package com.neostencil.controllers;

import com.neostencil.responses.CourseFiltersResponse;
import com.neostencil.responses.CustomCategoryResponse;
import com.neostencil.services.MobileService;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/v1/mobile")
public class MobileController {

  @Autowired
  MobileService mobileService;

  @RequestMapping(value = "/courses/filters", method = RequestMethod.GET)
  @ResponseBody
  LinkedHashMap<String,List<CustomCategoryResponse>> getCourseFilters() {
    LinkedHashMap<String, List<CustomCategoryResponse>> response = mobileService.getCourseFilters();
    return response;
  }

  @RequestMapping(value = "/courses/filters/exam/{examSegment}/category/{category}", method = RequestMethod.GET)
  @ResponseBody
  LinkedHashMap<String,List<String>> getCourseFilters(@PathVariable("examSegment") String examSegment,
      @PathVariable("category") String category) {
    LinkedHashMap<String,List<String>> response = mobileService.getCourseFilters(examSegment, category);
    return response;
  }

}
