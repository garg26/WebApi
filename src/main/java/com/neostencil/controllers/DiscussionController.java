package com.neostencil.controllers;


import com.neostencil.model.entities.Discussion;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.neostencil.responses.AllDiscussionsResponse;
import com.neostencil.responses.DiscussionDTO;
import com.neostencil.services.DiscussionService;

/**
 * 
 * @author shilpa
 *
 */
@RestController
@RequestMapping(value = "/api/v1")
public class DiscussionController {
  
  @Autowired
  DiscussionService service;
  
  @RequestMapping(value="/discussions",method=RequestMethod.GET)
  @ResponseBody
  public List<Discussion> fetchAllDiscussions()
  {
    List<Discussion> response=service.fetchAllDiscussions();
    return response;
  }
  
  
 @RequestMapping(value="/discussions/{slug}",method=RequestMethod.GET)
 @ResponseBody
 public DiscussionDTO fetchDiscussionBySlug(@PathVariable("slug")String slug)
 {
   DiscussionDTO response=service.fetchDiscussionBySlug(slug);
   return response;
 }
 
 @RequestMapping(value="/user/discussions",method=RequestMethod.POST)
 public void createDiscussion(@Valid @RequestBody DiscussionDTO request)
 {
   service.createDiscussion(request);
 }
 
 @RequestMapping(value="/user/discussions/{id}/like",method=RequestMethod.PUT)
 public long likeDiscussion(@PathVariable("id")long id)
 {
   long response=service.likeDiscussion(id);
   return response;
   
 }
 
 @RequestMapping(value="/user/discussions",method=RequestMethod.GET)
 public List<Discussion> fetchUserDiscussions()
 {
   List<Discussion> response=service.fetchUserDiscussions();
   return response;
 }

}
