package com.neostencil.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neostencil.common.enums.VideoRequestStatusType;
import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.AuthorityName;
import com.neostencil.model.entities.Lecture;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.VideoChunks;
import com.neostencil.model.entities.VideoRequest;
import com.neostencil.model.repositories.AuthorityRepository;
import com.neostencil.model.repositories.LectureRepository;
import com.neostencil.model.repositories.UnitRepository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.model.repositories.VideoChunkRepository;
import com.neostencil.model.repositories.VideoRequestRepository;
import com.neostencil.requests.UpdateVideoRequestObject;
import com.neostencil.requests.VideoRequestObject;
import com.neostencil.responses.VideoRequestDTO;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.utils.CommonAssembler;

@Service
public class VideoRequestService {

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  UserRepository userRepository;

  @Autowired
  VideoRequestRepository videoRequestRepository;

  @Autowired
  EmailService emailService;

  @Autowired
  VideoChunkRepository videoChunksRepository;

  @Autowired
  AuthorityRepository authorityRepository;
  
  @Autowired
  UnitRepository unitRepository;
  
  @Autowired
  LectureRepository lectureRepository;
  
  //TODO: To update the path before final deployment
  final String BASEPATH="/videolectures/mp4/";
  final String DESTINATION_BASEPATH="mp4/";

  public VideoRequest createVideoRequest(VideoRequestObject request) {
    VideoRequest response = null;

    VideoRequest newRequest = new VideoRequest();

    String email = jwtTokenUtil.getLoggedInUserEmail();

    newRequest.setChunks(request.getChunks());
    newRequest.setDeleteOriginal(request.isDeleteOriginal());


    newRequest.setFree(request.isFree());
    newRequest.setRequestCreatedBy(email);
    newRequest.setRequestCreationDate(Timestamp.valueOf(LocalDateTime.now()));
    newRequest.setRequestReason(request.getRequestReason());
    newRequest.setRequestStatus(VideoRequestStatusType.NEW);
    newRequest.setRequestType(request.getRequestType());
    newRequest.setUnitId(request.getUnitId());
    
    //Getting lecture details
    Unit unit=unitRepository.findByUnitId(request.getUnitId());
    
    Lecture lecture=lectureRepository.findById(unit.getTypeId());
    
    String finalTitle=WordUtils.wrap(unit.getTitle(), 30, null, false);
    newRequest.setTitle(finalTitle);
    
    //newRequest.setTitle(unit.getTitle());
    
    String finalCourseName=WordUtils.wrap(unit.getBatch().getCourse().getCourseTitle(),30,null,false);
    newRequest.setCourseName(finalCourseName);
    
    String finalBatchName=WordUtils.wrap(unit.getBatch().getClassTiming(), 30 ,null,false);
    newRequest.setBatchName(finalBatchName);
    
    String finalInstituteName=WordUtils.wrap(unit.getBatch().getCourse().getInstitute().getName(), 30 ,null,false);
    newRequest.setInstituteName(finalInstituteName);
    
    if (lecture.getName() != null) {
      newRequest.setTeacherName(lecture.getName());
    } else {
      String teacherName = "";
      Set<TeacherDetails> teachers = unit.getBatch().getCourse().getInstructors();
      Iterator<TeacherDetails> it = teachers.iterator();
      while (it.hasNext()) {
        teacherName = it.next().getTeacherName();
        break;
      }
      newRequest.setTeacherName(teacherName);
    }
    
    //newRequest.setUpdateUnitAuto(request.isUpdateUnitAuto());

    StringBuilder sourceVideoPath=new StringBuilder();
    StringBuilder destinationVideoPath=new StringBuilder();
    if (request.getSourceVideoPath() != null && !request.getSourceVideoPath().isEmpty()) {
      newRequest.setOldWowzaLink(request.getSourceVideoPath());
      String temp = request.getSourceVideoPath();
      sourceVideoPath.append(BASEPATH);

      StringBuilder folderStruct = new StringBuilder();
      folderStruct.append(temp.substring(0, temp.indexOf('_')));
      folderStruct.append("_courses/");
      String applicationName = temp.substring(0, temp.indexOf('/') + 1);
      folderStruct.append(applicationName);
      sourceVideoPath.append(folderStruct);
      String fileName = temp.substring(temp.indexOf(':')+1);
      sourceVideoPath.append(fileName);
    }

    if (request.getDestinationVideoPath() != null && !request.getDestinationVideoPath().isEmpty()) {
      newRequest.setNewWowzaLink(request.getDestinationVideoPath());
      String temp = request.getDestinationVideoPath();
      destinationVideoPath.append(DESTINATION_BASEPATH);

      StringBuilder folderStruct = new StringBuilder();
      folderStruct.append(temp.substring(0, temp.indexOf('_')));
      folderStruct.append("_courses/");
      String applicationName = temp.substring(0, temp.indexOf('/') + 1);
      folderStruct.append(applicationName);
      destinationVideoPath.append(folderStruct);
      String fileName = temp.substring(temp.indexOf(':')+1);
      destinationVideoPath.append(fileName);
    }

    newRequest.setSourceVideoPath(sourceVideoPath.toString());

    //TODO: To remove once the whole flow is complete
    // destinationVideoPath.append(BASEPATH);
    //destinationVideoPath.append(request.getDestinationVideoPath());

    newRequest.setDestinationVideoPath(destinationVideoPath.toString());

    response = videoRequestRepository.saveAndFlush(newRequest);

    for(VideoChunks vc:request.getChunks())
    {
      vc.setVideoRequest(response);
      videoChunksRepository.saveAndFlush(vc);
    }
    if(response!=null)
    {
      /*Set<Authority> authorities=new HashSet<Authority>();
      Authority auth=authorityRepository.findByName(AuthorityName.ROLE_APPROVER);
      // Authority auth2=authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
      authorities.add(auth);
      //authorities.add(auth2);
      List<User> approvers=userRepository.findByAuthorities(authorities);
      if (approvers != null && !approvers.isEmpty()) {
        for (User approver : approvers) {

        }
      }*/

      StringBuilder stb = new StringBuilder();
      stb.append("A new video edit request with type ");
      stb.append(response.getRequestType());
      stb.append(" for this source video ");
      stb.append(response.getOldWowzaLink());
      stb.append(" has been received.Click on the button below for details.");

      String subject="New Video Request received for "+ response.getOldWowzaLink();

      emailService.sendNewVideoRequestMail(stb.toString(), "videoedit@neostencil.com",subject);

    }

    return response;
  }

  /**
   * For approving any request.
   *
   * @param request
   */
  public void approveRequest(UpdateVideoRequestObject request) {
    VideoRequest savedRequest = videoRequestRepository.findById(request.getRequestId());
    String email = jwtTokenUtil.getLoggedInUserEmail();
    // User user = userRepository.findByEmailId(email);

    if (savedRequest != null) {
      savedRequest.setApproverComments(request.getApproverComment());
      savedRequest.setRequestStatus(VideoRequestStatusType.APPROVED);
      savedRequest.setRequestApprovalDate(Timestamp.valueOf(LocalDateTime.now()));
      savedRequest.setApprover(email);

      videoRequestRepository.saveAndFlush(savedRequest);
    }
  }

  public void rejectRequest(UpdateVideoRequestObject request) {
    VideoRequest savedRequest = videoRequestRepository.findById(request.getRequestId());
    String email = jwtTokenUtil.getLoggedInUserEmail();
    // User user = userRepository.findByEmailId(email);

    if (savedRequest != null) {
      savedRequest.setApproverComments(request.getApproverComment());
      savedRequest.setRequestStatus(VideoRequestStatusType.REJECTED);
      savedRequest.setRequestRejectionDate(Timestamp.valueOf(LocalDateTime.now()));
      savedRequest.setApprover(email);

      videoRequestRepository.saveAndFlush(savedRequest);
    }
  }

  public List<VideoRequestDTO> fetchAllVideoRequests() {
    List<VideoRequest> requests = videoRequestRepository.findAll();
    List<VideoRequestDTO> response = new LinkedList<VideoRequestDTO>();
    if (requests != null && !requests.isEmpty()) {
      for (VideoRequest rq : requests) {
        response.add(CommonAssembler.toVideoRequestDTO(rq));
      }
    }
    return response;

  }

  public VideoRequestDTO fetchVideoRequestById(long requestId) {
    VideoRequest request = videoRequestRepository.findById(requestId);
    VideoRequestDTO response = null;
    if (request != null) {
      response = CommonAssembler.toVideoRequestDTO(request);
    }
    return response;
  }

}
