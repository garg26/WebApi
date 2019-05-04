package com.neostencil.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.neostencil.requests.StudentInquiryRequest;
import com.neostencil.services.InquiryService;

@RestController
@RequestMapping(value = "/api/v1")
public class InquiryController {

  @Autowired
  InquiryService service;

  @RequestMapping(value = "/student_admin/send-query", method = RequestMethod.POST)
  String message(@RequestBody StudentInquiryRequest studentEnquryRequest) {
    String response = service.sendQuery(studentEnquryRequest);
    return response;
  }
}
