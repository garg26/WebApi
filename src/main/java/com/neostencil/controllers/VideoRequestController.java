package com.neostencil.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.neostencil.requests.UpdateVideoRequestObject;
import com.neostencil.requests.VideoRequestObject;
import com.neostencil.responses.VideoRequestDTO;
import com.neostencil.services.VideoRequestService;

/**
 * 
 */
@RestController
@RequestMapping({"/api/v1"})
public class VideoRequestController {

  @Autowired
  VideoRequestService service;

  @RequestMapping(value = "/techops_admin/video-requests", method = RequestMethod.POST)
  public void createRequest(@Valid @RequestBody VideoRequestObject request) {
    service.createVideoRequest(request);
  }

  @RequestMapping(value = "/approver_admin/video-requests/approve", method = RequestMethod.PUT)
  public void approveRequest(@Valid @RequestBody UpdateVideoRequestObject request) {
    service.approveRequest(request);
  }

  @RequestMapping(value = "/approver_admin/video-requests/reject", method = RequestMethod.PUT)
  public void rejectRequest(@Valid @RequestBody UpdateVideoRequestObject request) {
    service.rejectRequest(request);
  }

  @RequestMapping(value = "/approver_techops_admin/video-requests", method = RequestMethod.GET)
  public List<VideoRequestDTO> fetchAllRequests() {
    List<VideoRequestDTO> response = service.fetchAllVideoRequests();
    return response;
  }

  @RequestMapping(value = "/approver_techops_admin/video-requests/{id}", method = RequestMethod.GET)
  @ResponseBody
  public VideoRequestDTO fetchRequestById(@PathVariable("id") long id) {
    VideoRequestDTO response = service.fetchVideoRequestById(id);
    return response;
  }

}
