package com.neostencil.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.neostencil.web.HomeController;

@Controller
public class RedirectionController {

  @RequestMapping(value = "/rpsc-syllabus-ras-exam/{post_id}")
  String redirectRASExam(@PathVariable("post_id") String postId) {
    String url = "rpsc-syllabus-ras-exam/" + postId;
    if (HomeController.redirectUrlMap != null && HomeController.redirectUrlMap.get(url) != null) {
      return "redirect:/" + HomeController.redirectUrlMap.get(url);
    }
    return "redirect:/we-are-sorry";
  }

   @RequestMapping(value = "/upsc-syllabus/{post_id}")
  String redirectSyllabus(@PathVariable("post_id") String postId) {
    String url = "upsc-syllabus/" + postId;
    if (HomeController.redirectUrlMap != null && HomeController.redirectUrlMap.get(url) != null) {
      return "redirect:/" + HomeController.redirectUrlMap.get(url);
    }
    return "redirect:/we-are-sorry";
  }

  @RequestMapping(value = "/courses/*/*")
  String redirectCourseCategoryURLs() {
    return "redirect:/courses";
  }

  @RequestMapping(value="/course-status")
  String redirectToLMS()
  {
    return "redirect:/user-dashboard";
  }

  @RequestMapping(value="/jee-coaching-online")
  String redirectToJee()
  {
    return "redirect:/jee/coaching-online";
  }
}
