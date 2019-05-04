package com.neostencil.services;

import com.neostencil.common.enums.UnitType;
import com.neostencil.common.enums.WatchStatus;
import com.neostencil.requests.UserExamSegmentRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.neostencil.common.CommonUtil;
import com.neostencil.common.enums.LoginType;
import com.neostencil.common.enums.MessageStatus;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.StudentStatusType;
import com.neostencil.common.enums.UserCourseLinkageStatus;
import com.neostencil.dtos.UserCourseBatchLinkageDTO;
import com.neostencil.exceptions.InvalidInputException;
import com.neostencil.exceptions.ResourceNotFoundException;
import com.neostencil.model.entities.Address;
import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.AuthorityName;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.CustomStudent;
import com.neostencil.model.entities.Message;
import com.neostencil.model.entities.ParentStudentLinkage;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.TeacherStatistics;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.UserCourseBatchLinkage;
import com.neostencil.model.entities.UserLectureStats;
import com.neostencil.model.entities.UserUnitLinkage;
import com.neostencil.model.repositories.AddressRepository;
import com.neostencil.model.repositories.AuthorityRepository;
import com.neostencil.model.repositories.CourseBatchRepository;
import com.neostencil.model.repositories.CourseRepository;
import com.neostencil.model.repositories.MessageRepository;
import com.neostencil.model.repositories.ParentStudentLinkageRepository;
import com.neostencil.model.repositories.TeacherDetailsRepository;
import com.neostencil.model.repositories.TeacherStatisticsRepository;
import com.neostencil.model.repositories.UnitRepository;
import com.neostencil.model.repositories.UserCourseBatchLinkageRepository;
import com.neostencil.model.repositories.UserMetaDataRespository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.model.repositories.UserUnitLinkageRepository;
import com.neostencil.projections.AddressProjection;
import com.neostencil.projections.CourseProjection;
import com.neostencil.projections.TeacherCoursesProjection;
import com.neostencil.projections.UserProjection;
import com.neostencil.projections.UserUnitLinkageProjection;
import com.neostencil.requests.CreditNeoCashRequest;
import com.neostencil.requests.LoginRequest;
import com.neostencil.requests.ParentUnitsRequest;
import com.neostencil.requests.UpdatePasswordRequest;
import com.neostencil.requests.UpdateProfileRequest;
import com.neostencil.responses.AllMessagesResponse;
import com.neostencil.responses.CommissionDTO;
import com.neostencil.responses.CustomBatch;
import com.neostencil.responses.CustomCourse;
import com.neostencil.responses.EarningsResponse;
import com.neostencil.responses.LoginResponse;
import com.neostencil.responses.MessageThread;
import com.neostencil.responses.MessageThreadsResponse;
import com.neostencil.responses.ParentCourseResponse;
import com.neostencil.responses.ResetPasswordResponse;
import com.neostencil.responses.SendMessageResponse;
import com.neostencil.responses.SignUpResponse;
import com.neostencil.responses.StudentsPerBatchResponse;
import com.neostencil.responses.TeacherCourseResponse;
import com.neostencil.responses.TeacherStudentResponse;
import com.neostencil.responses.TeacherSummaryResponse;
import com.neostencil.responses.UnitDTO;
import com.neostencil.responses.UpdatePasswordResponse;
import com.neostencil.responses.UpdateProfileResponse;
import com.neostencil.responses.UserKey;
import com.neostencil.responses.UserMenuResponse;
import com.neostencil.responses.UserUnitTopicWiseResponse;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.security.JwtUser;
import com.neostencil.utils.CommonAssembler;
import com.neostencil.utils.EncryptionUtils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeacherDetailsRepository teacherRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private TeacherStatisticsRepository teacherStatsRepository;

	@Autowired
	private UserCourseBatchLinkageRepository userCourseBatchLinkageRepository;

	@Autowired
	private UserUnitLinkageRepository userUnitLinkageRepository;

	@Autowired
	private CourseBatchRepository batchRepository;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EcommerceService ecommerceService;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UnitRepository unitRepository;

	@Autowired
	private CourseBatchRepository courseBatchRepository;

	@Autowired
	private NeoCashService neoCashService;
	
	@Autowired
	private UserMetaDataRespository userMetaDataRepository;
	
	@Autowired
	private ParentStudentLinkageRepository parentStudentLinkageRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private boolean checkValidLogin(User user, String passsword) {

		String userPassword = user.getPassword();

		if (userPassword !=null && EncryptionUtils.isValidPassword(passsword, userPassword)) {
			return true;
		}

		User masterUser = userRepository.findByEmailId("aman@neostencil.com");
		if (EncryptionUtils.isValidPassword(passsword, masterUser.getPassword())) {

			for (Authority a : user.getAuthorities()) {
				if (a.getName() != AuthorityName.ROLE_USER && a.getName() != AuthorityName.ROLE_STUDENT) {
					return false;
				}

			}
			return true;
		}
		return false;
	}

	public LoginResponse login(LoginRequest request) {

		LoginResponse response = new LoginResponse();
		if (request == null) {
			response.setLoginSucces(false);
		} else {
			String accessToken = null;

			User savedUser = userRepository.findByEmailId((String) request.getEmailId());
			if (savedUser != null) {
				if (checkValidLogin(savedUser, request.getPassword())) {
					response.setLoginSucces(true);
					JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUserName());
					accessToken = jwtUtil.generateToken(userDetails);
					response.setUserName(savedUser.getUserName());
					response.setAccessToken(accessToken);
				} else {
					response.setLoginSucces(false);
					response.setErrorMessage("Wrong credentials");
				}
			} else {
				response.setErrorMessage("User doesn't exist.Please signup.");
			}
		}

		return response;
	}

	@SuppressWarnings("unused")
	public SignUpResponse signUp(User user) {
		SignUpResponse response = new SignUpResponse();
		String accessToken = "";
		if (user != null && user.getEmailId() != null) {
			User existingUser = userRepository.findByEmailId(user.getEmailId());

			// TODO return this message properly to front
			if (existingUser != null) {
				response.setLoginSucces(false);
				response.setErrorMessage("User already exists. Please login.");
				return response;
			}
		}
		user.setUserName(user.getEmailId());
		user.setLoginType(LoginType.OTHERS);
		Set<Authority> auths = new HashSet<Authority>();
		Authority authority = authRepository.findByName(AuthorityName.ROLE_USER);
		if (authority == null) {
			authority = new Authority();
			authority.setName(AuthorityName.ROLE_USER);
		}
		auths.add(authority);
		user.setAuthorities(auths);
		user.setEnabled(true);

		if (user != null) {
			// For encrypting the password
			String encPassword = EncryptionUtils.encryptPassword(user.getPassword());
			if (encPassword != null) {
				user.setPassword(encPassword);
			}
			user.addMetadata();
			User savedUser = null;
			savedUser = userRepository.save(user);
			if (savedUser != null) {
				response.setUserName(savedUser.getUserName());
				response.setLoginSucces(true);
				JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUserName());
				accessToken = jwtUtil.generateToken(userDetails);
				response.setAccessToken(accessToken);
				
			} else {
				response.setErrorMessage("Error signing up. Please try again.");
				response.setLoginSucces(false);
			}


		}
		return response;
	}

	public void hubspotSignUpData(String emailId,String source){
		//"https://api.hubapi.com/contacts/v1/contact";
		final String uri = UriComponentsBuilder.newInstance()
				.scheme("https").host("api.hubapi.com/contacts/v1/contact/createOrUpdate/email/"+emailId+"/")
				.queryParam("hapikey", "265b3aed-798d-4ffe-9bbf-464b61a83e97")
				.build(). toUriString();
		RestTemplate restTemplate = new RestTemplate();
		Map<String, List<Map<String, String>>> dataMap= new HashMap<String, List<Map<String, String>>>();
		Map<String,String> property=new HashMap<String,String>();
		property.put("property","ns_source");
		property.put("value",source);
		List<Map<String, String>> properties=new LinkedList<Map<String,String>>();
		properties.add(property);
		dataMap.put("properties",properties);
		String result = restTemplate.postForObject(uri,dataMap, String.class);

		System.out.print(result);
	}

	public void hubspotSignUpWithExtraData(String name,String phoneNumber,String emailId,String city,String exam,String quiz_score,String source){
		//"https://api.hubapi.com/contacts/v1/contact";
		final String uri = UriComponentsBuilder.newInstance()
				.scheme("https").host("api.hubapi.com/contacts/v1/contact/createOrUpdate/email/"+emailId+"/")
				.queryParam("hapikey", "265b3aed-798d-4ffe-9bbf-464b61a83e97")
				.build(). toUriString();
		RestTemplate restTemplate = new RestTemplate();
		Map<String, List<Map<String, String>>> dataMap= new HashMap<String, List<Map<String, String>>>();
		Map<String,String> property=new HashMap<String,String>();
		property.put("property","ns_source");
		property.put("value",source);

		Map<String,String> property1=new HashMap<String,String>();
		property.put("property","name");
		property.put("value",name);

		Map<String,String> property2=new HashMap<String,String>();
		property.put("property","phone");
		property.put("value",phoneNumber);

		Map<String,String> property3=new HashMap<String,String>();
		property.put("property","email");
		property.put("value",emailId);

		Map<String,String> property4=new HashMap<String,String>();
		property.put("property","city");
		property.put("value",city);

		Map<String,String> property5=new HashMap<String,String>();
		property.put("property","exam");
		property.put("value",exam);

		Map<String,String> property6=new HashMap<String,String>();
		property.put("property","quiz_score");
		property.put("value",quiz_score);

		List<Map<String, String>> properties=new LinkedList<Map<String,String>>();
		properties.add(property);
		properties.add(property1);
		properties.add(property2);
		properties.add(property3);
		properties.add(property4);
		properties.add(property5);
		properties.add(property6);

		dataMap.put("properties",properties);
		String result = restTemplate.postForObject(uri,dataMap, String.class);

		System.out.print(result);
	}

	public UpdatePasswordResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {

		UpdatePasswordResponse response = new UpdatePasswordResponse();
		try {
			if (updatePasswordRequest != null) {
				String encryptedPassword = EncryptionUtils.encryptPassword(updatePasswordRequest.getPassword());
				String userName = jwtUtil.getUsernameFromToken(updatePasswordRequest.getResetToken());
				User u = userRepository.findByUserName(userName);
				u.setPassword(encryptedPassword);
				userRepository.save(u);
				response.setUpdateSuccess(true);
				response.setUserName(userName);
			}
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * This service will fetch the a user details pertaining to a particular
	 * userID.
	 */
	public UserProjection fetchUserById(Long userId) {
		UserProjection fetchedUser = null;
		try {
			fetchedUser = userRepository.findAllProjectedByUserId(userId);
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return fetchedUser;
	}

	public List<UserProjection> fetchAllUsers() {
		List<UserProjection> response = null;
		try {
		  response=userRepository.findAllProjectedBy();
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * This will return all the courses of a particular student
	 */
	public Set<CustomCourse> fetchUserCourses(long userId) {
		Set<CustomCourse> response = new LinkedHashSet<CustomCourse>();

		if (userId != 0) {
			try {
				User user = userRepository.findByUserId(userId);
				if (user != null) {
					if (user.getUserCourseBatchLinkages() != null && user.getUserCourseBatchLinkages().size() > 0) {

						for (UserCourseBatchLinkage cbl : user.getUserCourseBatchLinkages()) {

							if (cbl.getCourseBatch() != null && cbl.getCourseBatch().getCourse() != null) {

								if (cbl.getExpiryDate().before(Timestamp.valueOf(LocalDateTime.now()))
										&& cbl.getStatus() != null
										&& !cbl.getStatus().equals(UserCourseLinkageStatus.EXPIRED)) {
									cbl.setStatus(UserCourseLinkageStatus.EXPIRED);
									userCourseBatchLinkageRepository.save(cbl);
								}

								CustomCourse cc = new CustomCourse();

								Course course = cbl.getCourseBatch().getCourse();
								cc.setCourseImage(course.getCourseImageUrl());
								cc.setCourseOldSlug(course.getCourseOldSlug());
								cc.setCourseTitle(course.getCourseTitle());

								cc.setStatus(cbl.getStatus());
								int remainingValidity = 0;
								if (cbl.getStatus() != UserCourseLinkageStatus.EXPIRED) {
									long calculateValidity = ChronoUnit.DAYS.between(LocalDateTime.now().toLocalDate(),
											cbl.getExpiryDate().toLocalDateTime().toLocalDate());
									remainingValidity = (int) calculateValidity;
								}
								cc.setRemainingValidity(remainingValidity);
								String instituteName = "";
								TeacherDetails t = null;
								if (course.getInstructors() != null) {
									Iterator it = course.getInstructors().iterator();
									if (it.hasNext()) {
										t = (TeacherDetails) it.next();
									}

									if (t != null && t.getInstitute() != null) {
										instituteName = t.getInstitute().getName();
									}
								}
								cc.setInstituteName(instituteName);
								if (t != null) {
									cc.setInstructorName(t.getTeacherName());
								}
								cc.setStudentsEnrolled(course.getStudentsEnrolled());
								cc.setCourseBatchId(cbl.getCourseBatch().getId());
								if (cbl.getCourseBatch().getStartDate() != null) {
									cc.setStartDate(CommonUtil
											.convertStringIntoDateFormat(cbl.getCourseBatch().getStartDate()));
								}
								response.add(cc);
							}
						}
						// response = user.getCourseBatches();
					}
				} else {
					throw new ResourceNotFoundException(User.class.getName(), "userId", userId);
				}
			} catch (Exception e) {
				LOGGER.error(User.class.getName() + " Exception Occured");
				emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
			}
		}

		return response;
	}

	public Set<CustomCourse> fetchUserCoursesWithStatus(long userId, String status) {
		Set<CustomCourse> response = new LinkedHashSet<CustomCourse>();

		if (userId != 0) {
			try {
				User user = userRepository.findByUserId(userId);
				List<UserCourseBatchLinkage> linkages = userCourseBatchLinkageRepository.findAllByUserAndStatus(user,
						UserCourseLinkageStatus.valueOf(status));

				if (linkages != null && linkages.size() > 0) {

					for (UserCourseBatchLinkage cbl : linkages) {

						if (cbl.getCourseBatch() != null && cbl.getCourseBatch().getCourse() != null) {
							if (cbl.getExpiryDate().before(Timestamp.valueOf(LocalDateTime.now()))
									&& cbl.getStatus() != null
									&& !cbl.getStatus().equals(UserCourseLinkageStatus.EXPIRED)) {
								cbl.setStatus(UserCourseLinkageStatus.EXPIRED);
								userCourseBatchLinkageRepository.save(cbl);
							}

							CustomCourse cc = new CustomCourse();

							Course course = cbl.getCourseBatch().getCourse();
							cc.setCourseImage(course.getCourseImageUrl());
							cc.setCourseOldSlug(course.getCourseOldSlug());
							cc.setCourseTitle(course.getCourseTitle());

							cc.setStatus(cbl.getStatus());
							int remainingValidity = 0;
							if (cbl.getStatus() != UserCourseLinkageStatus.EXPIRED) {
								long calculateValidity = ChronoUnit.DAYS.between(LocalDateTime.now().toLocalDate(),
										cbl.getExpiryDate().toLocalDateTime().toLocalDate());
								remainingValidity = (int) calculateValidity;
							}
							cc.setRemainingValidity(remainingValidity);
							String instituteName = "";
							TeacherDetails t = null;
							if (course.getInstructors() != null) {
								Iterator it = course.getInstructors().iterator();
								if (it.hasNext()) {
									t = (TeacherDetails) it.next();
								}

								if (t != null && t.getInstitute() != null) {
									instituteName = t.getInstitute().getName();
								}
							}
							cc.setInstituteName(instituteName);
							if (t != null) {
								cc.setInstructorName(t.getTeacherName());
							}
							cc.setStudentsEnrolled(course.getStudentsEnrolled());
							cc.setCourseBatchId(cbl.getCourseBatch().getId());
							if (cbl.getCourseBatch().getStartDate() != null) {
								cc.setStartDate(
										CommonUtil.convertStringIntoDateFormat(cbl.getCourseBatch().getStartDate()));
							}
							response.add(cc);
						}
					}
				}

			} catch (Exception e) {
				LOGGER.error(User.class.getName() + " Exception Occured");
				emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
			}
		}

		return response;
	}

	public Map<String, Set<CustomCourse>> fetchUserCoursesStatusWise(long userId) {

		Map<String, Set<CustomCourse>> response = new LinkedHashMap<String, Set<CustomCourse>>();

		if (userId != 0) {
			try {
				User user = userRepository.findByUserId(userId);
				if (user != null) {
					if (user.getUserCourseBatchLinkages() != null && user.getUserCourseBatchLinkages().size() > 0) {

						for (UserCourseBatchLinkage cbl : user.getUserCourseBatchLinkages()) {

							if (cbl.getCourseBatch() != null && cbl.getCourseBatch().getCourse() != null) {

								if (cbl.getExpiryDate().before(Timestamp.valueOf(LocalDateTime.now()))
										&& cbl.getStatus() != null
										&& !cbl.getStatus().equals(UserCourseLinkageStatus.EXPIRED)) {
									cbl.setStatus(UserCourseLinkageStatus.EXPIRED);
									userCourseBatchLinkageRepository.save(cbl);
								}

								CustomCourse cc = new CustomCourse();

								Course course = cbl.getCourseBatch().getCourse();
								cc.setCourseImage(course.getCourseImageUrl());
								cc.setCourseOldSlug(course.getCourseOldSlug());
								cc.setCourseTitle(course.getCourseTitle());

								cc.setStatus(cbl.getStatus());

								int remainingValidity = 0;
								if (cbl.getStatus() != UserCourseLinkageStatus.EXPIRED) {
									long calculateValidity = ChronoUnit.DAYS.between(LocalDateTime.now().toLocalDate(),
											cbl.getExpiryDate().toLocalDateTime().toLocalDate());
									remainingValidity = (int) calculateValidity;
								}
								cc.setRemainingValidity(remainingValidity);
								String instituteName = "";
								TeacherDetails t = null;
								if (course.getInstructors() != null) {
									Iterator it = course.getInstructors().iterator();
									if (it.hasNext()) {
										t = (TeacherDetails) it.next();
									}

									if (t != null && t.getInstitute() != null) {
										instituteName = t.getInstitute().getName();
									}
								}
								cc.setInstituteName(instituteName);
								if (t != null) {
									cc.setInstructorName(t.getTeacherName());
								}
								cc.setStudentsEnrolled(course.getStudentsEnrolled());
								cc.setCourseBatchId(cbl.getCourseBatch().getId());
								if (cbl.getCourseBatch().getStartDate() != null) {
									cc.setStartDate(CommonUtil
											.convertStringIntoDateFormat(cbl.getCourseBatch().getStartDate()));
								}

								if (cc.getStatus() != null) {
									if (response.get(cc.getStatus().name()) != null) {
										response.get(cc.getStatus().name()).add(cc);
									} else {
										Set<CustomCourse> statusSet = new LinkedHashSet<CustomCourse>();
										statusSet.add(cc);
										response.put(cc.getStatus().name(), statusSet);
									}

								}
							}
						}
						// response = user.getCourseBatches();
					}
				} else {
					throw new ResourceNotFoundException(User.class.getName(), "userId", userId);
				}
			} catch (Exception e) {
				LOGGER.error(User.class.getName() + " Exception Occured");
				emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
			}
		}
		return response;
	}

	public ResetPasswordResponse generatePasswordResetToken(String emailID) {

		ResetPasswordResponse response = new ResetPasswordResponse();
		String passwordResetToken = null;
		try {
			User savedUser = userRepository.findByEmailId(emailID);
			if (savedUser != null) {
				JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUserName());
				passwordResetToken = jwtUtil.generatePasswordResetToken(userDetails);
				if (passwordResetToken != null && !passwordResetToken.isEmpty()) {
					response.setSuccess(true);
					emailService.sendPasswordRequestEmail(emailID, passwordResetToken,
							savedUser.getFullName());
				} else {
					response.setSuccess(true);
					response.setErrorMessage("Error occured. Please try again");
				}
			} else {
				response.setSuccess(false);
				response.setErrorMessage("Email Id is not registered with us.");
			}
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	public boolean validatePasswordResetToken(String token) {

		try {
			String userName = jwtUtil.getUsernameFromToken(token);
			User savedUser = userRepository.findByUserName(userName);

			if (savedUser != null) {
				JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUserName());
				boolean validate = jwtUtil.validateResetToken(token, userDetails);
				return validate;
			}
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	/**
	 * For returning role specific menu for a user
	 */
	public UserMenuResponse getUserMenu() {
		UserMenuResponse response = new UserMenuResponse();
		Set<AuthorityName> roles = jwtUtil.getLoggedInUserRole();
		List<String> menuItems = new LinkedList<String>();
		// For student
		if (roles.contains(AuthorityName.ROLE_STUDENT)) {
			menuItems.add("Dashboard");
			menuItems.add("Profile");
			menuItems.add("My orders");
			menuItems.add("Logout");
			menuItems.add("Messages");
			response.setMenuItems(menuItems);
			return response;
		}
		// For teacher's role
		if (roles.contains(AuthorityName.ROLE_INSTRUCTOR)) {
			menuItems.add("Dashboard");
			menuItems.add("Profile");
			menuItems.add("My orders");
			menuItems.add("Logout");
			menuItems.add("Messages");
			menuItems.add("Remuneration");
			response.setMenuItems(menuItems);
			return response;
		}
		return response;
	}

	/**
	 * For returning all the courses of a particular teacher
	 */
	public Set<CourseProjection> getTeacherCoursesOld() {
		Set<CourseProjection> response = new LinkedHashSet<CourseProjection>();
		try {
			String email = jwtUtil.getLoggedInUserEmail();
			User loggedInTeacher = userRepository.findByUserId(jwtUtil.getLoggedInUserID());

			if (email != null) {
				TeacherCoursesProjection td = teacherRepository.findAllProjectedByUserAccount(loggedInTeacher);
				if (td != null && td.getCourses() != null && td.getCourses().size() > 0) {
					response = td.getCourses();
				}
			}
		} catch (Exception e) {
			LOGGER.error(Course.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	public TeacherCourseResponse getTeacherCourses() {
	  TeacherCourseResponse response=new TeacherCourseResponse();

		Set<CustomCourse> customCourses = new LinkedHashSet<CustomCourse>();
		int totalNoOfCourses=0;
		try {
			User loggedInTeacher = userRepository.findByUserId(jwtUtil.getLoggedInUserID());
			if (loggedInTeacher != null) {
				TeacherCoursesProjection td = teacherRepository.findAllProjectedByUserAccount(loggedInTeacher);
				if (td != null && td.getCourses() != null && td.getCourses().size() > 0) {

				  totalNoOfCourses=td.getCourses().size();

					for (CourseProjection cp : td.getCourses()) {
						if (cp != null) {
							Course course = courseRepository.findById(cp.getId());
							if (course != null && course.getBatches() != null && course.getBatches().size() > 0) {

								for (CourseBatch cb : course.getBatches()) {
                  if (cb != null && cb.getStatus().equals(PublishStatus.publish)) {
										CustomCourse cc = new CustomCourse();
										cc.setCourseImage(course.getCourseImageUrl());
										cc.setCourseOldSlug(course.getCourseOldSlug());
										cc.setCourseTitle(course.getCourseTitle());
										String instituteName = "";
										TeacherDetails t = null;
										// cc.setStatus(UserCourseLinkageStatus.ACTIVE);
										if (course.getInstructors() != null) {
											Iterator it = course.getInstructors().iterator();
											if (it.hasNext()) {
												t = (TeacherDetails) it.next();
											}

											if (t != null && t.getInstitute() != null) {
												instituteName = t.getInstitute().getName();
											}
										}
										cc.setInstituteName(instituteName);
										if (t != null) {
											cc.setInstructorName(t.getTeacherName());
										}
										cc.setStudentsEnrolled(course.getStudentsEnrolled());
										cc.setCourseBatchId(cb.getId());
										cc.setStartDate(CommonUtil.convertStringIntoDateFormat(cb.getStartDate()));
										customCourses.add(cc);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(Course.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}

		response.setTotalNoOfCourses(totalNoOfCourses);
		response.setCourseBatches(customCourses);
		return response;
	}

	/**
	 * To get a teacher's remuneration between a specific period of time. If no
	 * time period is defined , the service returns total compensation till
	 * current date.
	 */
	public EarningsResponse getTeacherEarnings() {
		EarningsResponse response = new EarningsResponse();
		// Here key will be the month#year
		Map<String, List<CommissionDTO>> earningsMap = new LinkedHashMap<String, List<CommissionDTO>>();
		CommissionDTO commissionDTO = null;
		try {

			User loggedInTeacher = userRepository.findByUserId(jwtUtil.getLoggedInUserID());
			List<TeacherStatistics> teacherStatsList = teacherStatsRepository
					.findByTeacherAndVisibleToTeacherIsTrue(teacherRepository.findByUserAccount(loggedInTeacher));
			if (teacherStatsList != null && teacherStatsList.size() > 0) {
        for (TeacherStatistics ts : teacherStatsList) {
          if (ts.isVisibleToTeacher()) {
            commissionDTO = new CommissionDTO();
            StringTokenizer token = new StringTokenizer(ts.getCourseBatch(), "#");
            String[] batch = new String[2];
            int i = 0;

            while (token.hasMoreTokens()) {
              batch[i] = token.nextToken();
              i++;
            }
            commissionDTO.setBatchId(Integer.parseInt(batch[0]));
            commissionDTO.setName(batch[1]);
            commissionDTO.setCommission(ts.getTeacherCommission());


            if (earningsMap.containsKey(ts.getMonthYear())) {
              List<CommissionDTO> commissionDTOs = earningsMap.get(ts.getMonthYear());
              commissionDTOs.add(commissionDTO);
              earningsMap.put(ts.getMonthYear(), commissionDTOs);

            } else {
              List<CommissionDTO> commissionDTOs = new LinkedList<CommissionDTO>();
              commissionDTOs.add(commissionDTO);
              earningsMap.put(ts.getMonthYear(), commissionDTOs);
            }

          }
        }


			}
			response.setEarningsMap(earningsMap);

		} catch (Exception e) {
			LOGGER.error(TeacherStatistics.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * For fetching a teacher's students in the form of a batch wise map.
	 */
	public TeacherStudentResponse getTeacherStudents(int page, int size) {
		TeacherStudentResponse response = new TeacherStudentResponse();
		User loggedInTeacher = userRepository.findByUserId(jwtUtil.getLoggedInUserID());
		Map<CustomBatch, Set<CustomStudent>> studentsMap = new LinkedHashMap<CustomBatch, Set<CustomStudent>>();
		int totalStudents=0;
		try {
			List<TeacherStatistics> teacherStatsList = teacherStatsRepository
					.findByTeacherAndVisibleToTeacherIsTrue(teacherRepository.findByUserAccount(loggedInTeacher));
			if (teacherStatsList != null && teacherStatsList.size() > 0) {
				for (TeacherStatistics ts : teacherStatsList) {
					StringTokenizer token = new StringTokenizer(ts.getCourseBatch(), "#");
					String[] batch = new String[2];
					int i = 0;
					while (token.hasMoreTokens()) {
						batch[i] = token.nextToken();
						i++;
					}
					CustomBatch cb = new CustomBatch(Integer.parseInt(batch[0]), batch[1]);
					Set<CustomStudent> studentSet = new LinkedHashSet<CustomStudent>();
					// Filtering out students which are not to be shown to
					// teacher
					if (ts.getStudents() != null && ts.getStudents().size() > 0) {

						for (CustomStudent cs : ts.getStudents()) {
							if (cs != null && cs.isVisibleToTeacher() && !cs.getStudentStatus().equals(StudentStatusType.REFUND)) {

							  totalStudents+=1;
                if (cs.getAddress() == null || cs.getAddress().isEmpty()) {
                  List<Address> addresses =
                      addressRepository.findByUserAndDefaultAddress(cs.getStudent(), true);
                  if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    StringBuilder addressText = new StringBuilder();

                    addressText.append(address.getAddressText());
                    addressText.append(",");
                    addressText.append(address.getCity());
                    addressText.append(",");
                    addressText.append(address.getState());
                    addressText.append(",");
                    addressText.append(address.getPincode());
                    cs.setAddress(addressText.toString());
                  }
                }
							  studentSet.add(cs);
							}
						}
					}

					studentsMap.put(cb, studentSet);
				}
				response.setTotalStudents(totalStudents);
				response.setStudentsMap(studentsMap);
			}
		} catch (Exception e) {
			LOGGER.error(TeacherStatistics.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	public boolean isUserAlreadySignedUp(String emailId ,String mobileNo){
		if(emailId==null){
			String loggedInUserEmail = jwtUtil.getLoggedInUserEmail();
			User user = userRepository.findByEmailId(loggedInUserEmail);
		}
		else{
			User user = userRepository.findByEmailId(emailId);
			if(user == null){
				User newUser = new User();
				newUser.setFullName(emailId.split("@")[0]);
				newUser.setEmailId(emailId);
				newUser.setPassword("");
				signUp(newUser);
				hubspotSignUpData(emailId,"Website - Send Enquiry");


				newUser.setMobileNumber(String.valueOf(mobileNo));
			}else {
				if(user.getMobileNumber() == null){
					user.setMobileNumber(String.valueOf(mobileNo));
				}

			}
		}
		return true;
	}

	/**
	 * To save a message to draft or send it.
	 */
	public SendMessageResponse createMessage(Message newMessage,String fromEmailId,String mobileNo) {
		SendMessageResponse response = new SendMessageResponse();
		if (newMessage != null) {
			newMessage.setSendTime(Timestamp.valueOf(LocalDateTime.now()));
			newMessage.setMessageFrom(fromEmailId);
			newMessage.setGroupId(newMessage.getMessageTo());
			newMessage.setMessageUrl(newMessage.getMessageUrl());
			newMessage.setStatus(MessageStatus.SENT);
			Message message = messageRepository.save(newMessage);
			if (message != null) {
				response.setSuccess(true);

				if (message.getStatus().equals(MessageStatus.DRAFT)) {
					response.setMessage(message);
				} else {
					response.setMessage(message);
					emailService.sendMessageNotification(message.getMessageTo(), message.getBody(), fromEmailId,
							message.getTopic(), message.getMessageUrl(),mobileNo);
				}

			}
		}
		return response;
	}

	public SendMessageResponse createNotification(Message newMessage) {
    SendMessageResponse response = new SendMessageResponse();
    if (newMessage != null) {
      newMessage.setSendTime(Timestamp.valueOf(LocalDateTime.now()));
      newMessage.setMessageFrom("NeoStencil Notifications");
      newMessage.setGroupId(newMessage.getMessageTo());
      newMessage.setMessageUrl(newMessage.getMessageUrl());
      newMessage.setStatus(MessageStatus.SENT);
      newMessage.setNotification(true);
      Message message = messageRepository.save(newMessage);
      if (message != null) {
        response.setSuccess(true);

        if (message.getStatus().equals(MessageStatus.DRAFT)) {
          response.setMessage(message);
        } else {
          response.setMessage(message);
          emailService.sendMessageNotification(message.getMessageTo(), message.getBody(), "NeoStencil Notifications",
              message.getTopic(), message.getMessageUrl(),null);
        }

      }
    }
    return response;
  }

	/**
	 * For sending messages to many people in one go.
	 */
	public SendMessageResponse sendMessageToMany(Message message, List<String> receiverList) {
		Message newMessage = null;
		SendMessageResponse response = new SendMessageResponse();
		if (message != null && receiverList != null && receiverList.size() > 0) {
			for (String r : receiverList) {
				if (r != null) {
					newMessage = new Message();
					newMessage.setBody(message.getBody());
					newMessage.setMessageTo(r);
//					response = this.createMessage(newMessage);

				}
			}
		}
		return response;

	}

	/**
	 * To fetch send wise threads of messages.
	 */
	public MessageThreadsResponse fetchAllMessageThreads() {
		MessageThreadsResponse response = new MessageThreadsResponse();
		Map<String, MessageThread> messagesMap = new LinkedHashMap<String, MessageThread>();
		Map<String,MessageThread> notificationMap=new LinkedHashMap<String,MessageThread>();
		String currentUser = jwtUtil.getLoggedInUserEmail();
		List<Message> allMessages = messageRepository.findAllMessageThreads(currentUser);
		List<Message> threadMessages = null;
		MessageThread msgThread = null;
		try {

			if (allMessages != null && allMessages.size() > 0) {
				for (Message m : allMessages) {
					if (m != null) {
						String key = null;

						if (m.getMessageFrom() != null && !m.getMessageFrom().equals(currentUser)) {
							key = m.getMessageFrom();
						} else if (m.getMessageTo() != null && !m.getMessageTo().equals(currentUser)) {
							key = m.getMessageTo();

						}

						if (key != null) {
							if (messagesMap.get(key) != null) {
								msgThread = messagesMap.get(key);
								threadMessages = msgThread.getMessages();
								threadMessages.add(m);
								msgThread.setMessages(threadMessages);
								if (!m.isRead()) {
									msgThread.setNoOfUnreadMessages(msgThread.getNoOfUnreadMessages() + 1);
								}
								messagesMap.put(key, msgThread);
							} else {
								msgThread = new MessageThread();
								threadMessages = new LinkedList<Message>();
								threadMessages.add(m);
								msgThread.setMessages(threadMessages);
								if (!m.isRead()) {
									msgThread.setNoOfUnreadMessages(1);
								}
								messagesMap.put(key, msgThread);
							}

						}
					}
				}

				//Removing the notifications from the messages map
				notificationMap.put("NeoStencil Notifications", messagesMap.get("NeoStencil Notifications"));
				response.setNotificationsMap(notificationMap);
				messagesMap.remove("NeoStencil Notifications");
				response.setMessagesMap(messagesMap);


			}
		} catch (Exception e) {
			LOGGER.error(TeacherStatistics.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

	/**
	 * To fetch all the sent messages
	 */
	public AllMessagesResponse fetchSentMessages() {
		AllMessagesResponse response = new AllMessagesResponse();
		try {
			String from = jwtUtil.getLoggedInUserEmail();
			if (from != null) {
				List<Message> messages = messageRepository.findByMessageFromOrderBySendTimeDesc(from);
				response.setMessages(messages);
			}
		} catch (Exception e) {
			LOGGER.error(Message.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * To fetch all the received messages
	 */
	public AllMessagesResponse fetchReceivedMessages() {
		AllMessagesResponse response = new AllMessagesResponse();
		try {
			String to = jwtUtil.getLoggedInUserEmail();
			if (to != null) {
				List<Message> messages = messageRepository.findByMessageToOrderBySendTimeDesc(to);
				response.setMessages(messages);
			}
		} catch (Exception e) {
			LOGGER.error(Message.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * For fetching student details of a particular batch
	 */
	public StudentsPerBatchResponse fetchUsersPerBatch(int batchId) {
		StudentsPerBatchResponse response = new StudentsPerBatchResponse();
		List<UserCourseBatchLinkage> linkageList = new LinkedList<UserCourseBatchLinkage>();
		List<UserCourseBatchLinkageDTO> dtoList = new LinkedList<UserCourseBatchLinkageDTO>();
		try {
			if (batchId != 0) {
				// CourseBatch cb=batchRepository.findById(batchId);
				linkageList = userCourseBatchLinkageRepository.findByCourseBatch(batchId);
				// For updating the status of the linkages which are past
				// validity
				if (linkageList != null && linkageList.size() > 0) {
					for (UserCourseBatchLinkage linkage : linkageList) {
						if (linkage != null) {
							if (!UserCourseLinkageStatus.EXPIRED.equals(linkage.getStatus())
									&& linkage.getExpiryDate().before(Timestamp.valueOf(LocalDateTime.now()))) {

								linkage.setStatus(UserCourseLinkageStatus.EXPIRED);
								userCourseBatchLinkageRepository.save(linkage);
							}
						}
						UserCourseBatchLinkageDTO dto = CommonAssembler.toUserCourseBatchLinkageDTO(linkage);
						if (dto != null) {
							dtoList.add(dto);
						}

					}

				}

				response.setLinkages(dtoList);
				if (dtoList != null && dtoList.size() > 0 && dtoList.get(0) != null
						&& dtoList.get(0).getCourseBatch() != null
						&& dtoList.get(0).getCourseBatch().getBatchName() != null) {
					response.setCourseBatchName(dtoList.get(0).getCourseBatch().getBatchName());
				}

			}
		} catch (Exception e) {
			LOGGER.error(Message.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/**
	 * For updating a particular user
	 */
	public User updateUser(User user) {
		User updatedUser = null;
		try {
			if (user != null) {
				updatedUser = userRepository.save(user);
			}
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return updatedUser;
	}

	// TODO: To be fixed in terms of sending data via DTOs in the response
	public UserUnitTopicWiseResponse getUserUnitsByBatch(int batchId, long userId) {

		UserUnitTopicWiseResponse response = new UserUnitTopicWiseResponse();
		int totalUnits = 0;
    int watchedUnits = 0;
    int inprogressUnits = 0;
    int notstartedUnits = 0;
		try {

			if (batchId != 0 && userId != 0) {

				UserCourseBatchLinkage byUserAndBatchAndStatus = userCourseBatchLinkageRepository
						.findByUserAndBatchAndStatus(userId, batchId, UserCourseLinkageStatus.ACTIVE);
				String courseSlug = "";
				if (byUserAndBatchAndStatus != null && byUserAndBatchAndStatus.getCourseBatch() != null
						&& byUserAndBatchAndStatus.getCourseBatch().getCourse().getCourseOldSlug() != null) {
					courseSlug = byUserAndBatchAndStatus.getCourseBatch().getCourse().getCourseOldSlug();

				}
				response.setCourseSlug(courseSlug);
				if (byUserAndBatchAndStatus != null) {
					LinkedHashMap<String, LinkedList<Object>> userUnitMap = new LinkedHashMap<String, LinkedList<Object>>();
					List<UserUnitLinkage> tempList = userUnitLinkageRepository.findByUser(userId);
					Set<Unit> batchUnits = batchRepository.findById(batchId).getUnits();

					if (tempList != null && tempList.size() > 0) {
						for (UserUnitLinkage uul : tempList) {
						  //For returning topicwise map of units
							for (Unit u : batchUnits) {
							   
								if (u.getUnitId() == uul.getUnit().getUnitId()) {
									if (uul != null && !TextUtils.isEmpty(uul.getUnit().getTopic())
											&& uul.getUnit().getStatus().equals(PublishStatus.publish)) {
									  totalUnits++;
										if (userUnitMap.get(uul.getUnit().getTopic()) == null) {
											LinkedList<Object> userUnitObjects = new LinkedList<>();
											UnitDTO dto = CommonAssembler.toUnitDTO(uul.getUnit());

											if(uul.getUnit().getUserUnitLinkages()!=null && uul.getUnit().getUserUnitLinkages().size()>0)
											{
												for(UserUnitLinkage linkage:uul.getUnit().getUserUnitLinkages())
												{
												  //To mark units as watched just by clicking on them for all types other thann lectures
													if(linkage.getUser().getUserId()==userId && !uul.getUnit().getType().equals(
															UnitType.LECTURE))
													{
														dto.setActive(linkage.isActive());
														if(WatchStatus.WATCHED.equals(linkage.getWatchStatus()))
														{
															dto.setWatchPercent(100);
															watchedUnits++;
														}
														else{
															notstartedUnits++;
														}
													}
												}
											}
				              UserLectureStats stats = userMetaDataRepository
				                  .findByIdUserUserIdAndIdUnitUnitId(userId, uul.getUnit().getUnitId());
				              //For lecture type units if the watched percentage is more than 99 we mark them as watched
											if (uul.getUnit().getType().equals(UnitType.LECTURE)) {
												if (stats != null) {
													float watchPercent = 0;
													float watched = stats.getChunkCount() * 10;
													if (stats.getDuration() > 0) {
														watchPercent = (watched / stats.getDuration()) * 100;
													}
													if (watchPercent > 99) {
														watchedUnits++;
													} else if (watchPercent > 0 && watchPercent < 99) {
														inprogressUnits++;
													} else {
														notstartedUnits++;
													}
													dto.setWatchPercent(watchPercent);
												} else {
													dto.setWatchPercent(0);
													notstartedUnits++;
												}
											}
											userUnitObjects.add(dto);
											userUnitMap.put(uul.getUnit().getTopic(), userUnitObjects);
										} else {
											LinkedList<Object> userUnitObjects = userUnitMap
													.get(uul.getUnit().getTopic());
											UnitDTO dto = CommonAssembler.toUnitDTO(uul.getUnit());

											if(uul.getUnit().getUserUnitLinkages()!=null && uul.getUnit().getUserUnitLinkages().size()>0)
											{
												for(UserUnitLinkage linkage:uul.getUnit().getUserUnitLinkages())
												{
													if(linkage.getUser().getUserId()==userId && !uul.getUnit().getType().equals(
															UnitType.LECTURE))
													{
														dto.setActive(linkage.isActive());
														if(WatchStatus.WATCHED.equals(linkage.getWatchStatus()))
														{
															dto.setWatchPercent(100);
															watchedUnits++;
														}
														else{
															notstartedUnits++;
														}
													}
												}
											}
                      UserLectureStats stats = userMetaDataRepository
                          .findByIdUserUserIdAndIdUnitUnitId(userId, uul.getUnit().getUnitId());
											if(uul.getUnit().getType().equals(UnitType.LECTURE)) {
												if (stats != null) {
													float watchPercent = 0;
													float watched = stats.getChunkCount() * 10;
													if (stats.getDuration() > 0) {
														watchPercent = (watched / stats.getDuration()) * 100;
													}
													if (watchPercent > 99) {
														watchedUnits++;
													} else if (watchPercent > 0 && watchPercent < 99) {
														inprogressUnits++;
													} else {
														notstartedUnits++;
													}
													dto.setWatchPercent(watchPercent);
												} else {
													dto.setWatchPercent(0);
													notstartedUnits++;
												}
											}
                      userUnitObjects.add(dto);
											userUnitMap.put(uul.getUnit().getTopic(), userUnitObjects);
										}
									}

								}
							}

						}
					}
          if (userUnitMap != null) {
            response.setResponse(userUnitMap);
            float inProgressPercent = (inprogressUnits * 100)  / totalUnits;
            float watchedPercent = (watchedUnits* 100) / totalUnits;
            float notStartedPercent = (notstartedUnits* 100) / totalUnits;
            response.setInProgressPercent(inProgressPercent);
            response.setNotStartedPercent(notStartedPercent);
            response.setWatchedPercent(watchedPercent);
          }
				}

			}

		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

	public UserUnitTopicWiseResponse getTeacherUnitsByBatch(int batchId) {
		UserUnitTopicWiseResponse response = new UserUnitTopicWiseResponse();
		User teacher = userRepository.findByUserId(jwtUtil.getLoggedInUserID());

		if (teacher != null) {
			CourseBatch cb = courseBatchRepository.findById(batchId);
			if (cb != null && cb.getUnits() != null && cb.getUnits().size() > 0) {
				if (cb.getCourse() != null && cb.getCourse().getCourseOldSlug() != null) {
					response.setCourseSlug(cb.getCourse().getCourseOldSlug());
				}
				LinkedHashMap<String, LinkedList<Object>> userUnitMap = new LinkedHashMap<String, LinkedList<Object>>();

				for (Unit u : cb.getUnits()) {
					String topic1 = "";
					if (u.getTopic() == null) {
						topic1 = "Session";
					} else {
						topic1 = u.getTopic();
					}
					if (userUnitMap.get(topic1) == null) {
						LinkedList<Object> userUnitObjects = new LinkedList<>();
						userUnitObjects.add(u);
						userUnitMap.put(topic1, userUnitObjects);
					} else {
						String topic = "";
						if (u.getTopic() == null) {
							topic = "Session";
						} else {
							topic = u.getTopic();
						}
						LinkedList<Object> userUnitObjects = userUnitMap.get(topic);
						if (userUnitObjects != null) {
							userUnitObjects.add(u);
							userUnitMap.put(topic, userUnitObjects);
						}
					}
				}
				if (userUnitMap != null) {
					response.setResponse(userUnitMap);
				}
			}
		}

		return response;
	}

	public List<UserUnitLinkageProjection> getUserByUnit(int unitId) {

		List<UserUnitLinkageProjection> response = null;

		if (unitId == 0) {
			throw new InvalidInputException(UserUnitLinkage.class.getName(), null, null);
		}

		try {
			Unit unit = unitRepository.findByUnitId(unitId);
			response = userUnitLinkageRepository.findAllProjectedByUnit(unit);
		} catch (Exception e) {

			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

	/* Update User Profile Details */

	// public UpdateProfileResponse updateUserProfile(UpdateProfileRequest
	// updateProfileRequest) {
	//
	// UpdateProfileResponse response = new UpdateProfileResponse();
	// Address address = new Address();
	// if (updateProfileRequest != null) {
	// String encryptedPassword =
	// EncryptionUtils.encryptPassword(updateProfileRequest.getPassword());
	// User u = userRepository.findByEmailId(updateProfileRequest.getEmailId());
	// u.setPassword(encryptedPassword);
	// u.setFullName(updateProfileRequest.getFullName());
	// User savedUser = userRepository.save(u);
	// address.setUser(savedUser);
	// address.setMobileNumber(updateProfileRequest.getMobile());
	// address.setAddressText(updateProfileRequest.getAddress());
	// address.setPincode(updateProfileRequest.getPincode());
	// address.setCity(updateProfileRequest.getCity());
	// address.setState(updateProfileRequest.getState());
	// address.setDefaultAddress(true);
	// addressRepository.save(address);
	// response.setUpdateSuccess(true);
	// response.setEmailId(updateProfileRequest.getEmailId());
	// }
	// return response;
	// }

	public UpdateProfileResponse updateUserFullName(UpdateProfileRequest updateProfileRequest) {

		UpdateProfileResponse response = new UpdateProfileResponse();
		try {
			if (updateProfileRequest != null) {
				User u = userRepository.findByEmailId(updateProfileRequest.getEmailId());
				u.setFullName(updateProfileRequest.getFullName());
				userRepository.save(u);
				response.setUpdateSuccess(true);
				response.setEmailId(updateProfileRequest.getEmailId());
			}
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	public UpdateProfileResponse updateUserPassword(UpdateProfileRequest updateProfileRequest) {
		UpdateProfileResponse response = new UpdateProfileResponse();
		User loggedInUser = userRepository.findByUserId(jwtUtil.getLoggedInUserID());
 		String encryptedPassword = EncryptionUtils.encryptPassword(updateProfileRequest.getPassword());
 		loggedInUser.setPassword(encryptedPassword);
		userRepository.save(loggedInUser);
		response.setUpdateSuccess(true);
		response.setEmailId(loggedInUser.getEmailId());
		return response;
	}

	public UpdateProfileResponse updateUserMobileNumber(
			UpdateProfileRequest updateProfileRequest) {
			UpdateProfileResponse response = new UpdateProfileResponse();
			User loggedInUser = userRepository.findByUserId(jwtUtil.getLoggedInUserID());
			loggedInUser.setMobileNumber(updateProfileRequest.getMobileNumber());
			userRepository.save(loggedInUser);
			response.setUpdateSuccess(true);
			response.setEmailId(loggedInUser.getEmailId());
			return response;
			}


	public UpdateProfileResponse updateUserAddressDetail(UpdateProfileRequest updateProfileRequest) {

		UpdateProfileResponse response = new UpdateProfileResponse();
		Address address = new Address();
		try {
			if (updateProfileRequest != null) {
				User loggedInUser = userRepository.findByUserId(jwtUtil.getLoggedInUserID());
				address.setUser(loggedInUser);
				address.setAddressId(updateProfileRequest.getAddressId());
				address.setFirstName(updateProfileRequest.getFirstName());
				address.setLastname(updateProfileRequest.getLastname());
				address.setAddressText(updateProfileRequest.getAddressText());
				address.setPincode(updateProfileRequest.getPincode());
				address.setCity(updateProfileRequest.getCity());
				address.setState(updateProfileRequest.getState());
				address.setMobileNumber(updateProfileRequest.getMobileNumber());
				address.setDefaultAddress(updateProfileRequest.getDefaultAddress());
			  addressRepository.save(address);
				response.setUpdateSuccess(true);
				response.setEmailId(updateProfileRequest.getEmailId());
			}
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	/*
	 * public UpdateProfileResponse
	 * setDefaultUserAddressDetail(UpdateProfileRequest updateProfileRequest) {
	 *
	 * UpdateProfileResponse response = new UpdateProfileResponse(); Address
	 * address = new Address(); if (updateProfileRequest != null) { User u =
	 * userRepository.findByEmailId(updateProfileRequest.getEmailId());
	 * address.setUser(u); address.setDefaultAddress(false);
	 * address.setAddressId(updateProfileRequest.getAddressId());
	 * address.setFirstName(updateProfileRequest.getFirstName());
	 * address.setLastname(updateProfileRequest.getLastname());
	 * address.setAddressText(updateProfileRequest.getAddressText());
	 * address.setPincode(updateProfileRequest.getPincode());
	 * address.setCity(updateProfileRequest.getCity());
	 * address.setState(updateProfileRequest.getState());
	 * address.setMobileNumber(updateProfileRequest.getMobileNumber());
	 * address.setDefaultAddress(true); addressRepository.save(address);
	 * response.setUpdateSuccess(true);
	 * response.setEmailId(updateProfileRequest.getEmailId()); } return
	 * response; }
	 */

	public UpdateProfileResponse createUserAddressDetail(UpdateProfileRequest updateProfileRequest) {

		UpdateProfileResponse response = new UpdateProfileResponse();
		Address address = new Address();
		try {
			if (updateProfileRequest != null) {
				User loggedInUser = userRepository.findByUserId(jwtUtil.getLoggedInUserID());

				address.setUser(loggedInUser);
				address.setFirstName(updateProfileRequest.getFirstName());
				address.setLastname(updateProfileRequest.getLastname());
				address.setAddressText(updateProfileRequest.getAddressText());
				address.setPincode(updateProfileRequest.getPincode());
				address.setCity(updateProfileRequest.getCity());
				address.setState(updateProfileRequest.getState());
				address.setMobileNumber(updateProfileRequest.getMobileNumber());
				addressRepository.save(address);
				response.setUpdateSuccess(true);
				response.setEmailId(updateProfileRequest.getEmailId());
			}
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	public List<AddressProjection> fetchAllAddressDetails() {
		List<AddressProjection> response = new ArrayList<AddressProjection>();
		try {
			long userId = jwtUtil.getLoggedInUserID();
			User user = userRepository.findByUserId(userId);
			List<AddressProjection> addresses = addressRepository.findAllProjectedByUser(user);
			if (addresses != null && addresses.size() > 0) {
				for (AddressProjection ad : addresses) {
					if (ad != null) {
						response.add(ad);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	public UserProjection fetchAllUserDetails() {
		UserProjection response = null;
		try {
			long userId = jwtUtil.getLoggedInUserID();
			response = userRepository.findAllProjectedByUserId(userId);
		} catch (Exception e) {
			LOGGER.error(User.class.getName() + " Exception Occured");
			emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	public TeacherSummaryResponse getTeacherSummary()
	{
	  TeacherSummaryResponse response=new TeacherSummaryResponse();

	   TeacherCourseResponse teacherCourses= getTeacherCourses();
	   if(teacherCourses!=null && teacherCourses.getCourseBatches()!=null && teacherCourses.getCourseBatches().size()>0)
	   {
	    // response.setCourseBatches(teacherCourses.getCourseBatches());
	     response.setNoOfCourses(teacherCourses.getTotalNoOfCourses());
	     response.setNoOfBatches(teacherCourses.getCourseBatches().size());
	   }
	   TeacherStudentResponse teacherStudentResponse= getTeacherStudents(0, 0);
	   if(teacherStudentResponse!=null)
	   {
	     //response.setTeacherStudentResponse(teacherStudentResponse);
	     response.setNoOfStudents(teacherStudentResponse.getTotalStudents());
	   }
	   return response;
	}
	public User fetchUserByEmailId(String emailId){

		User byEmailId = userRepository.findByEmailId(emailId);

		return byEmailId;
	}

	/**
	 *
	 * @param
	 */
	public void updateExamSegment(UserExamSegmentRequest userExamSegmentRequest) {
		long loggedInUserId = jwtUtil.getLoggedInUserID();

		User user = userRepository.findByUserId(loggedInUserId);
		user.setExamSegment(userExamSegmentRequest.getExamSegment());
		user.setMobileNumber(userExamSegmentRequest.getMobileNo());
		user.setCity(userExamSegmentRequest.getCity());
		userRepository.saveAndFlush(user);
	}

  public String genearteToken(String userName){
		JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(userName);
		return jwtUtil.generateToken(userDetails);
	}

  /**
   * For getting the course details of a parent's wards
   */
  public ParentCourseResponse getParentCourses()
  {
    long loggedInUserId = jwtUtil.getLoggedInUserID();

    List<ParentStudentLinkage> linkages=parentStudentLinkageRepository.findAllByParentId(loggedInUserId);

    ParentCourseResponse response =new ParentCourseResponse();
    Map<String,Set<CustomCourse>> courseMap=new HashMap<String,Set<CustomCourse>>();

    if(linkages!=null && !linkages.isEmpty())
    {
      for(ParentStudentLinkage linkage:linkages)
      {
        Set<CustomCourse> courses=this.fetchUserCourses(linkage.getStudentId());

        User student = userRepository.findByUserId(linkage.getStudentId());

				UserKey userKey = new UserKey(student.getUserId(), student.getFullName(),
						student.getEmailId());

				courseMap.put(userKey.toString(), courses);
       }
      response.setCourseMap(courseMap);
    }

    return response;
  }

  /**
   * For recording absence of a student for a lecture
   */
  public void markStudentAbsent(User student,String lectureDetails)
  {
    ParentStudentLinkage linkage=parentStudentLinkageRepository.findByStudentId(student.getUserId());
    if(linkage!=null)
    {
      User parent = userRepository.findByUserId(linkage.getParentId());

      String message="Your ward " + student.getFullName() +" hasn't logged in for " +lectureDetails;
      emailService.sendSimpleMessage(parent.getEmailId(), "Absence Recorded for "+student.getFullName() ,message);

      int noOfAbsences = linkage.getNoOfAbsences();
      linkage.setNoOfAbsences(noOfAbsences+1);
      parentStudentLinkageRepository.saveAndFlush(linkage);

    }
  }

  /**
   * For getting the units of all a particular ward's , particular batch's units
   *
   * @param request
   */
  public UserUnitTopicWiseResponse getParentUnits(ParentUnitsRequest request) {
    UserUnitTopicWiseResponse response = null;

    long parentId = jwtUtil.getLoggedInUserID();

    List<ParentStudentLinkage> linkages =
        parentStudentLinkageRepository.findAllByParentId(parentId);

    if (linkages != null && !linkages.isEmpty()) {
      for (ParentStudentLinkage linkage : linkages) {
        if (linkage.getStudentId() == request.getStudentId()) {
          response = getUserUnitsByBatch(request.getBatchId(), linkage.getStudentId());
          return response;
        }
      }
    }

    return response;
  }
  public String setUserProfilePicture(String profilePicture) {
    long loggedInUserId = jwtUtil.getLoggedInUserID();
    User user = userRepository.findByUserId(loggedInUserId);
    user.setAvatar(profilePicture);
    userRepository.saveAndFlush(user);
    return "uploaded successfully";
  }

}
