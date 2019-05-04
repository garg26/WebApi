package com.neostencil.controllers;

import com.neostencil.requests.UserExamSegmentRequest;
import com.neostencil.responses.NeoCashHistoryResponse;
import com.neostencil.services.NeoCashService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.neostencil.model.entities.Message;
import com.neostencil.model.entities.User;
import com.neostencil.projections.CourseProjection;
import com.neostencil.projections.UserProjection;
import com.neostencil.projections.UserUnitLinkageProjection;
import com.neostencil.requests.LoginRequest;
import com.neostencil.requests.MessageBroadcastRequest;
import com.neostencil.requests.ParentUnitsRequest;
import com.neostencil.requests.UpdatePasswordRequest;
import com.neostencil.requests.UpdateProfileRequest;
import com.neostencil.responses.AllMessagesResponse;
import com.neostencil.responses.CustomCourse;
import com.neostencil.responses.EarningsResponse;
import com.neostencil.responses.LoginResponse;
import com.neostencil.responses.MessageThreadsResponse;
import com.neostencil.responses.ParentCourseResponse;
import com.neostencil.responses.ResetPasswordResponse;
import com.neostencil.responses.SendMessageResponse;
import com.neostencil.responses.SignUpResponse;
import com.neostencil.responses.TeacherCourseResponse;
import com.neostencil.responses.TeacherStudentResponse;
import com.neostencil.responses.TeacherSummaryResponse;
import com.neostencil.responses.UpdatePasswordResponse;
import com.neostencil.responses.UpdateProfileResponse;
import com.neostencil.responses.UserUnitTopicWiseResponse;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.services.EcommerceService;
import com.neostencil.services.EmailService;
import com.neostencil.services.UserService;

@RestController
@RequestMapping({"/api/v1"})
public class UserController<T> {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  @Autowired
  EmailService emailService;
  @Value("${jwt.header}")
  private String tokenHeader;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private UserService userManagementService;
  @Autowired
  private EcommerceService ecommerceService;
  @Autowired
  private NeoCashService neoCashService;

  @Bean
  private PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<LoginResponse> login(RequestEntity<LoginRequest> request) {

    LOGGER.info("Entering login service");
    ResponseEntity<LoginResponse> response = null;

    List<String> messages = new ArrayList<String>();
    LoginResponse loginResponse = null;
    if (request != null) {

      LoginRequest loginRequest = request.getBody();
      loginResponse = userManagementService.login(loginRequest);
      if (loginResponse.isLoginSucces()) {
        response = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
      } else {
        response = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.BAD_REQUEST);
      }
    } else {
      loginResponse = new LoginResponse();
      loginResponse.setLoginSucces(false);
      response = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.BAD_REQUEST);
    }

    LOGGER.info(response.toString());
    return response;
  }

  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  public ResponseEntity<SignUpResponse> signup(RequestEntity<User> request) {

    SignUpResponse signUpResponse = null;
    ResponseEntity<SignUpResponse> response = null;

    if (request != null) {

      User signupUser = request.getBody();
      List<String> messages = new ArrayList<String>();

      if (signupUser != null && signupUser.getEmailId() != null && signupUser.getPassword() != null
          && signupUser.getFullName() != null) {
        final String name = signupUser.getFullName();
        final String email = signupUser.getEmailId();
        final String password = signupUser.getPassword();

        LOGGER.info(
            "Signup Request: Name :" + name + ", EmailId: " + email + ", Password: " + password);

        signUpResponse = userManagementService.signUp(signupUser);
        userManagementService.hubspotSignUpData(signupUser.getEmailId(),"Website Registration/ First Login");

        if (signUpResponse.isLoginSucces()) {

          response = new ResponseEntity<SignUpResponse>(signUpResponse, HttpStatus.CREATED);
          emailService.sendEmailOnSignUp(signupUser);
        } else {
          response = new ResponseEntity<SignUpResponse>(signUpResponse, HttpStatus.BAD_REQUEST);
        }

      } else {
        signUpResponse = new SignUpResponse();
        signUpResponse.setLoginSucces(false);
        response = new ResponseEntity<SignUpResponse>(signUpResponse, HttpStatus.BAD_REQUEST);
      }
    } else {
      signUpResponse = new SignUpResponse();
      signUpResponse.setLoginSucces(false);
      response = new ResponseEntity<SignUpResponse>(signUpResponse, HttpStatus.BAD_REQUEST);
    }
    LOGGER.info(response.toString());
    return response;
  }

  /**
   * For fetching all the courses of a student
   */
  @RequestMapping(value = "/user/courses", method = RequestMethod.GET)
  @ResponseBody
  Set<CustomCourse> fetchUserCourses() {

    long userID = jwtTokenUtil.getLoggedInUserID();
    Set<CustomCourse> response = userManagementService.fetchUserCourses(userID);
    return response;
  }
  
  @RequestMapping(value = "/user/courses/{status}", method = RequestMethod.GET)
  @ResponseBody
  Set<CustomCourse> fetchUserCoursesWithStatus(@PathVariable("status")String status) {

    long userID = jwtTokenUtil.getLoggedInUserID();
    Set<CustomCourse> response = userManagementService.fetchUserCoursesWithStatus(userID,status);
    return response;
  }

  @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
  @ResponseBody
  ResetPasswordResponse createPasswordResetRequest(@Valid @RequestBody String emailId) {

    // ResponseEntity<ResetPasswordResponse> res = null;
    ResetPasswordResponse response = userManagementService.generatePasswordResetToken(emailId);
    return response;
  }

  @RequestMapping(value = "/update-password", method = RequestMethod.POST)
  @ResponseBody
  UpdatePasswordResponse createPasswordResetRequest(
      @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

    boolean validateToken =
        userManagementService.validatePasswordResetToken(updatePasswordRequest.getResetToken());
    if (validateToken) {
      return userManagementService.updatePassword(updatePasswordRequest);
    }
    // TODO handle this properly
    return null;
  }

  @RequestMapping(value = {"/user/messages","/messages"}, method = RequestMethod.POST)
  @ResponseBody
  SendMessageResponse createMessage(@Valid @RequestBody Message newMessage,@RequestParam(value = "mobileNo", required = false) String mobileNo,
      @RequestParam(value = "emailId", required = false) String emailId) {
    boolean userAlreadySignedUp = userManagementService.isUserAlreadySignedUp(emailId,mobileNo);
    SendMessageResponse response = userManagementService.createMessage(newMessage,emailId,mobileNo);
    return response;
  }

  @RequestMapping(value = "/user/messages", method = RequestMethod.GET)
  @ResponseBody
  MessageThreadsResponse fetchAllMessages() {
    MessageThreadsResponse response = userManagementService.fetchAllMessageThreads();
    return response;
  }

  @RequestMapping(value = "/user/messages/sent", method = RequestMethod.GET)
  @ResponseBody
  AllMessagesResponse fetchSentMessages() {
    AllMessagesResponse response = userManagementService.fetchSentMessages();
    return response;
  }

  @RequestMapping(value = "/user/messages/received", method = RequestMethod.GET)
  @ResponseBody
  AllMessagesResponse fetchReceivedMessages() {
    AllMessagesResponse response = userManagementService.fetchReceivedMessages();
    return response;
  }

  @RequestMapping(value = "/user/batches/{batchId}/units", method = RequestMethod.GET)
  @ResponseBody
  UserUnitTopicWiseResponse getUserUnitsByBatch(@PathVariable("batchId") int batchId) {
    long userId = jwtTokenUtil.getLoggedInUserID();
    UserUnitTopicWiseResponse response = userManagementService.getUserUnitsByBatch(batchId, userId);
    return response;
  }
  
  @RequestMapping(value="/parent_admin/units",method=RequestMethod.GET)
  @ResponseBody
  UserUnitTopicWiseResponse getParentUnits(ParentUnitsRequest request)
  {
    UserUnitTopicWiseResponse response=userManagementService.getParentUnits(request);
    return response;
  }


  @RequestMapping(value = "/units/{unit_id}/user", method = RequestMethod.GET)
  @ResponseBody
  List<UserUnitLinkageProjection> getUserByUnit(@PathVariable("unit_id") int unitId) {
    List<UserUnitLinkageProjection> response = userManagementService.getUserByUnit(unitId);
    return response;
  }


  @RequestMapping(value = "/instructor/teachers/earnings", method = RequestMethod.GET)
  @ResponseBody
  EarningsResponse getTeacherEarnings() {
    EarningsResponse response = userManagementService.getTeacherEarnings();
    return response;
  }

  @RequestMapping(value = "/instructor/teachers/students", method = RequestMethod.GET)
  @ResponseBody
  TeacherStudentResponse getTeacherStudents() {
    TeacherStudentResponse response = userManagementService.getTeacherStudents(1, 20);
    return response;
  }

  @RequestMapping(value = "/instructor/teachers/courses", method = RequestMethod.GET)
  @ResponseBody
  TeacherCourseResponse getTeacherCourses() {
    TeacherCourseResponse response = userManagementService.getTeacherCourses();
    return response;
  }
  
  @RequestMapping(value="/instructor/teacher-summary",method=RequestMethod.GET)
  @ResponseBody
  TeacherSummaryResponse getTeacherSummary()
  {
    TeacherSummaryResponse response=userManagementService.getTeacherSummary();
    return response;
  }

  /**
   * For sending messages in bulk
   *
   * @param request
   * @return
   */
  @RequestMapping(value = "/admin/broadcast", method = RequestMethod.POST)
  @ResponseBody
  public SendMessageResponse sendMessageToMany(
      @Valid @RequestBody MessageBroadcastRequest request) {
    SendMessageResponse response =
        userManagementService.sendMessageToMany(request.getMessage(), request.getReceiverList());
    return response;

  }

  /**
   * To fetch all the users currently registered.
   *
   * @return
   */
  @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
  @ResponseBody
  public List<UserProjection> fetchAllUsers() {
    List<UserProjection> response = userManagementService.fetchAllUsers();
    return response;
  }

  @RequestMapping(value = "/admin/users", method = RequestMethod.PUT)
  public User updateUsers(@Valid @RequestBody User user) {
    User updatedUser = userManagementService.updateUser(user);
    return updatedUser;
  }

  /* Update User Profile Details */
  // @RequestMapping(value = "/user/updateuserprofile", method = RequestMethod.POST)
  // @ResponseBody
  // UpdateProfileResponse updateProfileResponse(
  // @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
  // UpdateProfileResponse response = userManagementService.updateUserProfile(updateProfileRequest);
  // return response;
  // }

  @RequestMapping(value = "/user/updateuserfullname", method = RequestMethod.POST)
  @ResponseBody
  UpdateProfileResponse updateFullNameProfileResponse(
      @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
    UpdateProfileResponse response = userManagementService.updateUserFullName(updateProfileRequest);
    return response;
  }

  @RequestMapping(value = "/user/updateuserpassword", method = RequestMethod.POST)
  @ResponseBody
  UpdateProfileResponse updatePasswordProfileResponse(
      @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
    UpdateProfileResponse response = userManagementService.updateUserPassword(updateProfileRequest);
    return response;
  }

  @RequestMapping(value = "/user/updateusermobilenumber", method = RequestMethod.POST)
  @ResponseBody
  UpdateProfileResponse updateMobileNumber(
      @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
    UpdateProfileResponse response = userManagementService.updateUserMobileNumber(updateProfileRequest);
    return response;
  }

  @RequestMapping(value = "/user/updateuseraddressdetail", method = RequestMethod.POST)
  @ResponseBody
  UpdateProfileResponse updateAddressDetailProfileResponse(
      @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
    UpdateProfileResponse response =
        userManagementService.updateUserAddressDetail(updateProfileRequest);
    return response;
  }

  @RequestMapping(value = "/user/updatedefaultaddressdetail", method = RequestMethod.POST)
  @ResponseBody
  Boolean setDefaultAddressDetailProfileResponse(
      @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
    Boolean response = ecommerceService.changeDefaultAddress(jwtTokenUtil.getLoggedInUserID(),
        updateProfileRequest.getAddressId());
    return response;
  }

  @RequestMapping(value = "/user/createuseraddressdetail", method = RequestMethod.POST)
  @ResponseBody
  UpdateProfileResponse createAddressDetailProfileResponse(
      @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
    UpdateProfileResponse response =
        userManagementService.createUserAddressDetail(updateProfileRequest);
    return response;
  }
  
  @RequestMapping(value="/admin_instructor/batches/{batchId}/units",method=RequestMethod.GET)
  @ResponseBody
  UserUnitTopicWiseResponse getTeacherUnits(@PathVariable("batchId")int batchId)
  {
	  UserUnitTopicWiseResponse response=userManagementService.getTeacherUnitsByBatch(batchId);
	  return response;
  }
  
  /**
   * For updating the examSegment for a user if already does not exist
   * @param
   */
  @RequestMapping(value="/user/examsegment",method=RequestMethod.PUT)
  public void updateExamSegment(@Valid @RequestBody UserExamSegmentRequest userExamSegmentRequest)
  {
    userManagementService.updateExamSegment(userExamSegmentRequest);
  }
  
  @RequestMapping(value="/parent_admin/courses",method=RequestMethod.GET)
  @ResponseBody
  public ParentCourseResponse getParentCourses()
  {
    ParentCourseResponse response=userManagementService.getParentCourses();
    return response;
  }


  @RequestMapping(value="/user/updateavatar",method=RequestMethod.PUT)
  public String updateProfilePicture(@RequestParam("profilePicture") String profilePicture)
  {
    String response = userManagementService.setUserProfilePicture(profilePicture);
    return response;
  }
  
  @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.GET)
  @ResponseBody
  public UserProjection getUserById(@PathVariable("id") long id) {
    UserProjection response = userManagementService.fetchUserById(id);
    return response;
  }

  @RequestMapping(value = "/user/getuserneocash", method = RequestMethod.GET)
  public NeoCashHistoryResponse getUserNewCashResponse(){
    NeoCashHistoryResponse response=neoCashService.fetchNeoCashHistoryForLoggedInUser();
    return response;
  }

}
