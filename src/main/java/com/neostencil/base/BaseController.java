package com.neostencil.base;

import com.neostencil.model.entities.AnnouncementHeader;
import com.neostencil.model.entities.ExtraInfo;
import com.neostencil.model.entities.Question;
import com.neostencil.model.entities.Quiz;
import com.neostencil.model.entities.QuizTemplate;
import com.neostencil.model.repositories.PostRepository;
import com.neostencil.projections.CommentProjection;
import com.neostencil.requests.AnnouncementHeaderRequest;
import com.neostencil.requests.CreateBreadCrumbRequest;
import com.neostencil.requests.CustomUnitUpdateRequest;
import com.neostencil.requests.PostCommentsRequest;
import com.neostencil.requests.QuizRequest;
import com.neostencil.requests.QuizSubmissionRequest;
import com.neostencil.requests.UserUnitRequest;
import com.neostencil.responses.AssignmentSubmissionRequest;
import com.neostencil.requests.UnitPositionAndMacroRequest;
import com.neostencil.responses.CustomBreadCrumResponse;
import com.neostencil.responses.ExtraInfoResponse;
import com.neostencil.responses.QuizLeaderBoardResponse;
import com.neostencil.responses.QuizTemplateResponse;
import com.neostencil.responses.UserExistResponse;
import com.neostencil.responses.UserQuizSubmissionResponse;
import com.neostencil.services.LectureService;
import com.neostencil.tools.GoogleSheetToCourseMeta;
import com.neostencil.tools.GoogleSheetToInstituteMeta;
import com.neostencil.tools.GoogleSheetToPostMeta;
import com.neostencil.tools.GoogleSheetToPostTag;
import com.neostencil.tools.GoogleSheetToQuestion;
import com.neostencil.tools.GoogleSheetToTeacherMeta;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neostencil.common.enums.AnswerSheetCategory;
import com.neostencil.dtos.CommentDTO;
import com.neostencil.dtos.CourseDTO;
import com.neostencil.dtos.InstituteDetailedDTO;
import com.neostencil.dtos.TeacherDetailsDTO;
import com.neostencil.model.entities.AnswerSheet;
import com.neostencil.model.entities.BreadCrumb;
import com.neostencil.model.entities.Comment;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.Institute;
import com.neostencil.model.entities.JWMacro;
import com.neostencil.model.entities.Lecture;
import com.neostencil.model.entities.NewsletterSubscriber;
import com.neostencil.model.entities.Post;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.Testimonial;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.UserExamSegment;
import com.neostencil.model.entities.UserUnitLinkage;
import com.neostencil.model.entities.WowzaMacro;
import com.neostencil.projections.CourseProjection;
import com.neostencil.projections.InstituteProjection;
import com.neostencil.projections.JWMacroProjection;
import com.neostencil.projections.LectureProjection;
import com.neostencil.projections.TeacherProjection;
import com.neostencil.projections.WowzaMacroProjection;
import com.neostencil.requests.BatchNameRequest;
import com.neostencil.requests.PayLaterRequest;
import com.neostencil.requests.ShuffleUnitsRequest;
import com.neostencil.requests.UnitBatchLinkageRequest;
import com.neostencil.requests.UserExamSegmentRequest;
import com.neostencil.responses.BatchNameDTO;
import com.neostencil.responses.BatchUnitResponse;
import com.neostencil.responses.CourseBatchResponse;
import com.neostencil.responses.CourseFiltersResponse;
import com.neostencil.responses.NewsletterSubscribeResponse;
import com.neostencil.responses.ReviewResponse;
import com.neostencil.responses.ReviewStatistics;
import com.neostencil.responses.SearchResponse;
import com.neostencil.responses.TeacherFilterResponse;
import com.neostencil.responses.TopicUnitResponse;
import com.neostencil.responses.UserUnitResponse;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.tools.GoogleSheetToBreadCrumb;
import com.neostencil.tools.GoogleSheetToCourses;
import com.neostencil.tools.GoogleSheetToInstitute;
import com.neostencil.tools.GoogleSheetToTeachers;

@RestController
@RequestMapping(value = "/api/v1")
public class BaseController {

  @Autowired
  BaseService service;

  @Autowired
  LectureService lectureService;

  @Autowired
  SearchService searchService;
  @Value("${jwt.header}")
  private String tokenHeader;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  GoogleSheetToCourses googleSheetToCourses;

  @Autowired
  GoogleSheetToBreadCrumb googleSheetToBreadCrumb;

  @Autowired
  GoogleSheetToInstitute googleSheetToInstitute;

  @Autowired
  GoogleSheetToTeachers googleSheetToTeachers;

  @Autowired
  GoogleSheetToPostTag googleSheetToPostTag;

  @Autowired
  PostRepository postRepository;

  @Autowired
  GoogleSheetToInstituteMeta googleSheetToInstituteMeta;

  @Autowired
  GoogleSheetToTeacherMeta googleSheetToTeacherMeta;

  @Autowired
  GoogleSheetToPostMeta googleSheetToPostMeta;

  @Autowired
  GoogleSheetToCourseMeta googleSheetToCourseMeta;

  @Autowired
  GoogleSheetToQuestion googleSheetToQuestion;

  @RequestMapping(value = "/admin_lmsadmin/teachers", method = RequestMethod.POST)
  void createTeacher(@Valid @RequestBody TeacherDetails newTeacher) {
    newTeacher.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    newTeacher.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    service.createTeacher(newTeacher);

  }

  @RequestMapping(value = "/teachers/{id}", method = RequestMethod.GET)
  @ResponseBody
  TeacherDetailsDTO fetchTeacher(@PathVariable("id") int id) {
    TeacherDetailsDTO response = service.fetchTeacher(id);
    return response;
  }

  @RequestMapping(value = "/teachers", method = RequestMethod.GET)
  @ResponseBody
  Page<TeacherProjection> fetchAllTeachers(
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "sort", required = false) String sortBy) {
    Page<TeacherProjection> response = service
        .fetchAllPublishedTeachers(page, size, search, sortBy);
    return response;
  }

  @RequestMapping(value = "/update-header/{slug}", method = RequestMethod.GET)
  @ResponseBody
  AnnouncementHeader announcementHeader(@PathVariable("slug") String slug) {
    AnnouncementHeader announcementHeader = service.fetchAllAnnouncementHeaderOrderByPosition(slug);
    return announcementHeader;
  }

  @RequestMapping(value = "/getquizbyid/{slug}", method = RequestMethod.GET)
  @ResponseBody
  QuizTemplateResponse getquizbyid(@PathVariable("slug") String slug) {
    QuizTemplate quizTemplate = service.fetchQuizTemplateBySlug(slug);
    QuizTemplateResponse quizTemplateResponse = service
        .fetchQuizTemplateByQuizId(quizTemplate.getQuiz().getQuizId());
    return quizTemplateResponse;
  }

  @RequestMapping(value = "/getquizbyid/jee/{slug}", method = RequestMethod.GET)
  @ResponseBody
  QuizTemplateResponse getjeequizbyid(@PathVariable("slug") String slug) {
    QuizTemplate quizTemplate = service.fetchQuizTemplateBySlug("jee/" + slug);
    QuizTemplateResponse quizTemplateResponse = service
        .fetchQuizTemplateByQuizId(quizTemplate.getQuiz().getQuizId());
    return quizTemplateResponse;
  }

  @RequestMapping(value = "/getquizbyid/neet/{slug}", method = RequestMethod.GET)
  @ResponseBody
  QuizTemplateResponse getneetquizbyid(@PathVariable("slug") String slug) {
    QuizTemplate quizTemplate = service.fetchQuizTemplateBySlug("neet/" + slug);
    QuizTemplateResponse quizTemplateResponse = service
        .fetchQuizTemplateByQuizId(quizTemplate.getQuiz().getQuizId());
    return quizTemplateResponse;
  }

  @RequestMapping(value = "quizsubmission/{quiz_slug}", method = RequestMethod.PUT)
  @ResponseBody
  UserQuizSubmissionResponse calculateUserQuizStats(@PathVariable("quiz_slug") String quizSlug,
      @RequestParam("timetaken") String timeTaken, @RequestBody
      QuizRequest quizRequest) {

    QuizTemplate quizTemplate = service.fetchQuizTemplateBySlug(quizSlug);

    long loggedInUserID = jwtTokenUtil.getLoggedInUserID();
    UserExistResponse userExistResponse;
    if (loggedInUserID != 0) {
      userExistResponse = service
          .checkUserExistOnTestSubmit(quizRequest.getName(), quizRequest.getEmailId(),
              quizRequest.getPassword(), quizRequest.getMobile(), quizRequest.getCity(),
              quizTemplate.getExamSegment(), true);
    } else {
      userExistResponse = service
          .checkUserExistOnTestSubmit(quizRequest.getName(), quizRequest.getEmailId(),
              quizRequest.getPassword(), quizRequest.getMobile(), quizRequest.getCity(),
              quizTemplate.getExamSegment(), false);
    }

    UserQuizSubmissionResponse userQuizSubmissionResponse = lectureService
        .calculateUserQuizStats(userExistResponse.getUserId(), 1,
            quizTemplate.getQuiz().getQuizId(), timeTaken, quizRequest.getList());

    userQuizSubmissionResponse.setToken(userExistResponse.getToken());

    return userQuizSubmissionResponse;


  }

  @RequestMapping(value = "quizsubmission/jee/{quiz_slug}", method = RequestMethod.PUT)
  @ResponseBody
  UserQuizSubmissionResponse calculateUserQuizStatsJee(@PathVariable("quiz_slug") String quizSlug,
      @RequestParam("timetaken") String timeTaken, @RequestBody
      QuizRequest quizRequest) {

    QuizTemplate quizTemplate = service.fetchQuizTemplateBySlug("jee/" + quizSlug);

    long loggedInUserID = jwtTokenUtil.getLoggedInUserID();
    UserExistResponse userExistResponse;
    if (loggedInUserID != 0) {
      userExistResponse = service
          .checkUserExistOnTestSubmit(quizRequest.getName(), quizRequest.getEmailId(),
              quizRequest.getPassword(), quizRequest.getMobile(), quizRequest.getCity(),
              quizTemplate.getExamSegment(), true);
    } else {
      userExistResponse = service
          .checkUserExistOnTestSubmit(quizRequest.getName(), quizRequest.getEmailId(),
              quizRequest.getPassword(), quizRequest.getMobile(), quizRequest.getCity(),
              quizTemplate.getExamSegment(), false);
    }

    UserQuizSubmissionResponse userQuizSubmissionResponse = lectureService
        .calculateUserQuizStats(userExistResponse.getUserId(), 1,
            quizTemplate.getQuiz().getQuizId(), timeTaken, quizRequest.getList());

    userQuizSubmissionResponse.setToken(userExistResponse.getToken());

    return userQuizSubmissionResponse;


  }

  @RequestMapping(value = "quizsubmission/neet/{quiz_slug}", method = RequestMethod.PUT)
  @ResponseBody
  UserQuizSubmissionResponse calculateUserQuizStatsNeet(@PathVariable("quiz_slug") String quizSlug,
      @RequestParam("timetaken") String timeTaken, @RequestBody
      QuizRequest quizRequest) {

    QuizTemplate quizTemplate = service.fetchQuizTemplateBySlug("neet/" + quizSlug);

    long loggedInUserID = jwtTokenUtil.getLoggedInUserID();
    UserExistResponse userExistResponse;
    if (loggedInUserID != 0) {
      userExistResponse = service
          .checkUserExistOnTestSubmit(quizRequest.getName(), quizRequest.getEmailId(),
              quizRequest.getPassword(), quizRequest.getMobile(), quizRequest.getCity(),
              quizTemplate.getExamSegment(), true);
    } else {
      userExistResponse = service
          .checkUserExistOnTestSubmit(quizRequest.getName(), quizRequest.getEmailId(),
              quizRequest.getPassword(), quizRequest.getMobile(), quizRequest.getCity(),
              quizTemplate.getExamSegment(), false);
    }

    UserQuizSubmissionResponse userQuizSubmissionResponse = lectureService
        .calculateUserQuizStats(userExistResponse.getUserId(), 1,
            quizTemplate.getQuiz().getQuizId(), timeTaken, quizRequest.getList());

    userQuizSubmissionResponse.setToken(userExistResponse.getToken());

    return userQuizSubmissionResponse;


  }

  @RequestMapping(value = "/admin_editor/change-header", method = RequestMethod.POST)
  @ResponseBody
  AnnouncementHeader updateHeader(
      @Valid @RequestBody AnnouncementHeaderRequest announcementHeaderRequest) {
    AnnouncementHeader announcementHeaderResponse = service.updateHeader(announcementHeaderRequest);
    return announcementHeaderResponse;

  }

  @RequestMapping(value = "/admin_editor/delete-header/{header_id}", method = RequestMethod.DELETE)
  String deleteHeader(@PathVariable("header_id") int id) {
    String response = service.deleteHeaderById(id);
    return response;
  }

  @RequestMapping(value = "/admin_lmsadmin/teachers/{teacherId}", method = RequestMethod.PUT)
  void updateTeacher(@PathVariable("teacherId") int teacherId,
      @Valid @RequestBody TeacherDetails updatedTeacher) {
    service.updateTeacher(teacherId, updatedTeacher);
  }

  @RequestMapping(value = "/admin_lmsadmin/institutes", method = RequestMethod.POST)
  void createInstitute(@Valid @RequestBody Institute newInstitute) {

    newInstitute.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    newInstitute.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    newInstitute.getBreadCrumb().addMetadata();
    service.createInstitute(newInstitute);
  }

  @RequestMapping(value = "/institutes/{id}", method = RequestMethod.GET)
  @ResponseBody
  InstituteDetailedDTO fetchInstitute(@PathVariable("id") int id) {
    InstituteDetailedDTO response = service.fetchInstituteById(id);
    return response;
  }

  @GetMapping
  @RequestMapping(value = "/institutes/{slug}")
  @ResponseBody
  InstituteDetailedDTO fetchInstituteBySlug(@PathVariable("slug") String slug) {
    InstituteDetailedDTO response = service.fetchInstituteBySlug(slug);
    return response;
  }

  /*
   * @RequestMapping(value = "/institutes", method = RequestMethod.GET)
   *
   * @ResponseBody List<Institute> fetchAllInstitutes(@RequestParam(value =
   * "page", required = false) Integer page,
   *
   * @RequestParam(value = "size", required = false) Integer size,
   *
   * @RequestParam(value = "search", required = false) String search,
   *
   * @RequestParam(value = "sort", required = false) String sortBy) {
   * List<Institute> response = service.fetchAllInstitutes(page, size, search,
   * sortBy); return response; }
   */

  @RequestMapping(value = "/admin_lmsadmin/institutes/{instituteId}", method = RequestMethod.PUT)
  void updateInstitute(@PathVariable("instituteId") int instituteId,
      @Valid @RequestBody Institute updateInstitute) {
    service.updateInstitute(instituteId, updateInstitute);

  }

  /**
   * CRUD for JWMacros
   */

  @RequestMapping(value = "/admin/jwmacros", method = RequestMethod.POST)
  void createJWMacro(@Valid @RequestBody JWMacro newMacro) {
    newMacro.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    newMacro.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    service.createJWMacro(newMacro);
  }

  @RequestMapping(value = "/admin_techops/jwmacros/{name}", method = RequestMethod.GET)
  @ResponseBody
  JWMacroProjection fetchJWMacro(@PathVariable("name") String name) {
    JWMacroProjection response = service.fetchMacroByName(name);
    return response;
  }

  @RequestMapping(value = "/admin_techops/jwmacros", method = RequestMethod.GET)
  @ResponseBody
  List<JWMacroProjection> fetchAllJWMacros() {
    List<JWMacroProjection> response = service.fetchAllJWMacros();
    return response;
  }

  @RequestMapping(value = "/admin/jwmacros/{name}", method = RequestMethod.PUT)
  void updateJWMacro(@PathVariable("name") String name, @Valid @RequestBody JWMacro updatedMacro) {
    service.updateJWMacro(name, updatedMacro);
  }

  /**
   * CRUD for WowzaMacro
   */

  @RequestMapping(value = "/admin/wowzamacros", method = RequestMethod.POST)
  void createWowzaMacro(@Valid @RequestBody WowzaMacro newMacro) {
    newMacro.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    newMacro.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    service.createWowzaMacro(newMacro);

  }

  @RequestMapping(value = "/admin_techops/wowzamacros/{name}", method = RequestMethod.GET)
  @ResponseBody
  WowzaMacroProjection fetchWowzaMacro(@PathVariable("name") String name) {
    WowzaMacroProjection response = service.fetchWowzaMacroByName(name);
    return response;
  }

  @RequestMapping(value = "/admin_techops/wowzamacros", method = RequestMethod.GET)
  @ResponseBody
  List<WowzaMacroProjection> fetchAllWowzaMacros() {
    List<WowzaMacroProjection> response = service.fetchAllWowzaMacros();
    return response;
  }

  @RequestMapping(value = "/admin/wowzamacros/{name}", method = RequestMethod.PUT)
  void updateWowzaMacro(@PathVariable("name") String name,
      @Valid @RequestBody WowzaMacro updatedMacro) {
    service.updateWowzaMacro(name, updatedMacro);
  }

  /**
   * CRUD for Lectures
   */

  @RequestMapping(value = "/admin_techops/lectures", method = RequestMethod.POST)
  Lecture createLecture(@Valid @RequestBody Lecture newMacro) {
    newMacro.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    newMacro.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    Lecture lecture = service.createLecture(newMacro);

    return lecture;
  }

  @RequestMapping(value = "/admin_techops/units/{id}", method = RequestMethod.GET)
  @ResponseBody
  Unit fetchUnitById(@PathVariable("id") int id) {
    Unit response = lectureService.fetchUnitById(id);
    return response;
  }

  @RequestMapping(value = "/admin_techops_student/lectures/{id}", method = RequestMethod.GET)
  @ResponseBody
  Lecture fetchLectureById(@PathVariable("id") int id) {
    Lecture response = service.fetchLectureById(id);
    return response;
  }

  @RequestMapping(value = "/admin_techops/lectures", method = RequestMethod.GET)
  @ResponseBody
  List<LectureProjection> fetchAllLectures() {
    List<LectureProjection> response = service.fetchAllLectures();
    return response;
  }

  @RequestMapping(value = "/admin_techops/lectures/{id}", method = RequestMethod.PUT)
  Lecture updateLecture(@PathVariable("id") int id, @Valid @RequestBody Lecture updatedLecture) {
    updatedLecture.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    return service.updateLecture(id, updatedLecture);
  }

  /**
   * CourseBatch CRUD
   */
  @RequestMapping(value = "/admin_lmsadmin/coursebatches", method = RequestMethod.POST)
  void createCourseBatch(@Valid @RequestBody CourseBatch newBatch) {
    newBatch.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    newBatch.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    service.createCourseBatch(newBatch);
  }

  @RequestMapping(value = "/coursebatches/{id}", method = RequestMethod.GET)
  @ResponseBody
  CourseBatchResponse fetchCourseBatchById(@PathVariable("id") int id) {
    CourseBatchResponse response = service.fetchCourseBatchById(id);
    return response;
  }

  /*
   * @RequestMapping(value = "/coursebatches", method = RequestMethod.GET)
   *
   * @ResponseBody List<CourseBatch> fetchAllCourseBatches(
   *
   * @RequestParam(value = "page", required = false) Integer page,
   *
   * @RequestParam(value = "size", required = false) Integer size,
   *
   * @RequestParam(value = "search", required = false) String search) {
   * List<CourseBatch> response = service.fetchAllCourseBatches(page, size,
   * search); return response; }
   */

  @RequestMapping(value = "/admin_lmsadmin/coursebatches/{id}", method = RequestMethod.PUT)
  void updateCourseBatch(@PathVariable("id") int id, @Valid @RequestBody CourseBatch updatedBatch) {
    service.updateCourseBatch(id, updatedBatch);
  }

  /**
   * Course CRUD
   */
  @RequestMapping(value = "/admin_lmsadmin/courses", method = RequestMethod.POST)
  void createCourse(@Valid @RequestBody Course newBatch) {
    newBatch.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    newBatch.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    service.createCourse(newBatch);
  }

  /**
   * Course CRUD
   */
  @RequestMapping(value = "/admin_techops_lmsadmin/units", method = RequestMethod.POST)
  @ResponseBody
  Unit createUnit(@Valid @RequestBody Unit unit) {
    Unit response = service.createUnit(unit);
    return response;
  }

  @RequestMapping(value = "/admin_techops_lmsadmin/units/{unit_id}", method = RequestMethod.PUT)
  @ResponseBody
  Unit updateUnit(@PathVariable("unit_id") int id, @Valid @RequestBody Unit unit) {
    Unit response = service.updateUnit(unit, id);
    return response;
  }

  @RequestMapping(value = "/courses/{id}", method = RequestMethod.GET)
  @ResponseBody
  CourseDTO fetchCourseById(@PathVariable("id") int id) {
    CourseDTO response = service.fetchCourseById(id);
    return response;
  }

  @RequestMapping(value = "/courses", method = RequestMethod.GET)
  @ResponseBody
  Page<CourseProjection> fetchAllPublishCourses(
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size,
      @RequestParam(value = "sort", required = false) String sortBy,
      @RequestParam(value = "search", required = false) String search) {
    Page<CourseProjection> response = service.fetchAllPublishCourses(page, size, search, sortBy);
    return response;
  }

  // @RequestMapping(value = "/admin_lmsadmin/courses/{id}", method =
  // RequestMethod.PATCH)
  // @ResponseBody
  // Course updateCourseStatus(@PathVariable("id") int id, @Valid @RequestBody
  // String courseStatus)
  // {
  // Course response = service.updateCourseStatus(id, courseStatus);
  // return response;
  // }

  @RequestMapping(value = "/admin_lmsadmin/courses/{id}", method = RequestMethod.PUT)
  void updateCourse(@PathVariable("id") int id, @Valid @RequestBody Course updatedCourse) {

    service.updateCourse(id, updatedCourse);
  }

  @GetMapping
  @RequestMapping(value = "/admin_lmsadmin/courses/{slug}")
  @ResponseBody
  CourseDTO updateCourse(@PathVariable("slug") String slug) {
    CourseDTO response = service.fetchCourseBySlug(slug);
    return response;
  }

  @RequestMapping(value = "/courses/filters/exam/{examSegment}/category/{category}", method = RequestMethod.GET)
  @ResponseBody
  CourseFiltersResponse getCourseFilters(@PathVariable("examSegment") String examSegment,
      @PathVariable("category") String category) {
    CourseFiltersResponse response = service.getCourseFilters(examSegment, category);
    return response;
  }

  @RequestMapping(value = "/teachers/filters/exam/{examSegment}/category/{category}", method = RequestMethod.GET)
  @ResponseBody
  TeacherFilterResponse getTeacherFilter(@PathVariable("examSegment") String examSegment,
      @PathVariable("category") String category) {
    TeacherFilterResponse response = service.getTeacherFilter(examSegment, category);
    return response;
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  @ResponseBody
  SearchResponse performSearch(@RequestParam("search") String searchString,
      @RequestParam("page") int page,
      @RequestParam("size") int size) {
    SearchResponse response = searchService.performSearch(searchString, page, size);
    return response;
  }

  @RequestMapping(value = "/search-user", method = RequestMethod.POST)
  @ResponseBody
  List<Object> performUserSearch(@Valid @RequestBody String searchString) {

    List<Object> response = searchService.searchUsers(searchString, 0, 0);
    return response;
  }

  /**
   * CRUD Post
   */

  @RequestMapping(value = "/admin_writer/posts", method = RequestMethod.POST)
  void createPost(@Valid @RequestBody Post newPost) {
    newPost.addMetadata();
    long userID = jwtTokenUtil.getLoggedInUserID();
    service.createPost(newPost, userID);

  }

  @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
  @ResponseBody
  Post fetchPostById(@PathVariable("id") int id) {
    Post response = service.fetchPostById(id);
    return response;
  }

  @RequestMapping(value = "/admin_writer_editor/posts", method = RequestMethod.GET)
  @ResponseBody
  List<Post> fetchAllPosts(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "sort", required = false) String sortBy) {
    List<Post> response = service.fetchAllPosts(page, size, search, sortBy);
    return response;
  }

  @RequestMapping(value = "/admin_writer/posts/{id}", method = RequestMethod.PUT)
  @ResponseBody
  Post updatePost(@PathVariable("id") int id, @Valid @RequestBody Post updatedCourse) {
    long userID = jwtTokenUtil.getLoggedInUserID();
    updatedCourse.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    Post response = service.updatePost(id, updatedCourse, userID);
    return response;
  }

  /**
   * For publishing a post to the public platform
   */
  @RequestMapping(value = "/admin_editor/post/{id}/publish", method = RequestMethod.PATCH)
  @ResponseBody
  boolean publishPost(@PathVariable("id") int id) {
    boolean success = service.publishPost(id);
    return success;
  }

  @RequestMapping(value = "/posts/featured", method = RequestMethod.GET)
  @ResponseBody
  List<Post> fetchFeaturedPosts(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    List<Post> response = service.fetchFeaturedPosts(page, size);
    return response;
  }

  @RequestMapping(value = "/posts/popular", method = RequestMethod.GET)
  @ResponseBody
  List<Post> fetchPopularPosts(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    List<Post> response = service.fetchPopularPosts(page, size);
    return response;
  }

  /**
   * CRUD Testimonial
   */

  @RequestMapping(value = {"/admin/testimonials"}, method = RequestMethod.POST)
  @ResponseBody
  Testimonial createTestimonial(@Valid @RequestBody Testimonial newTestimonial) {
    Testimonial response = service.createTestimonial(newTestimonial);
    return response;
  }

  @RequestMapping(value = "/testimonials/{id}", method = RequestMethod.GET)
  @ResponseBody
  Testimonial fetchTestimonialById(@PathVariable("id") int id) {
    Testimonial response = service.fetchTestimonialById(id);
    return response;
  }

  @RequestMapping(value = "/testimonials", method = RequestMethod.GET)
  @ResponseBody
  List<Testimonial> fetchAllTestimonials(
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "sort", required = false) String sortBy) {
    List<Testimonial> response = service.fetchAllTestimonials(page, size, search, sortBy);
    return response;
  }


  @RequestMapping(value = {"/admin/testimonials/{id}"}, method = RequestMethod.PUT)
  @ResponseBody
  Testimonial updateTestimonial(@PathVariable("id") int id,
      @Valid @RequestBody Testimonial updatedTestimonial) {
    Testimonial response = service.updateTestimonial(id, updatedTestimonial);
    return response;
  }

  @RequestMapping(value = "/user/comments", method = RequestMethod.POST)
  CommentDTO createComment(@Valid @RequestBody Comment newComment) {
    newComment.addMetadata();
    CommentDTO response = service.createComment(newComment);
    return response;
  }

  @RequestMapping(value = "/comments/{parentId}", method = RequestMethod.GET)
  Page<CommentProjection> fetchCommentByParentId(@PathVariable("parentId") int parentId,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    Page<CommentProjection> response = service.fetchCommentByParentId(parentId, page, size);
    return response;
  }

  @RequestMapping(value = "/comments/slug/{slug}", method = RequestMethod.GET)
  Page<CommentProjection> fetchCommentsByPostName(@PathVariable("slug") String slug,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    Page<CommentProjection> response = service.fetchCommentByPostName(slug, page, size);
    return response;
  }

  @RequestMapping(value = "/comments/slug/jee/{slug}", method = RequestMethod.GET)
  Page<CommentProjection> fetchCommentsByPostNameForJee(@PathVariable("slug") String slug,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    slug = "jee/" + slug;
    Page<CommentProjection> response = service.fetchCommentByPostName(slug, page, size);
    return response;
  }

  @RequestMapping(value = "/comments/slug/neet/{slug}", method = RequestMethod.GET)
  Page<CommentProjection> fetchCommentsByPostNameForNeet(@PathVariable("slug") String slug,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    slug = "neet/" + slug;
    Page<CommentProjection> response = service.fetchCommentByPostName(slug, page, size);
    return response;
  }

  @RequestMapping(value = "/comments/slug/discussion/{slug}", method = RequestMethod.GET)
  Page<CommentProjection> fetchCommentsByPostNameForDiscussion(@PathVariable("slug") String slug,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    Page<CommentProjection> response = service.fetchCommentByPostName(slug, page, size);
    return response;
  }

  @RequestMapping(value = "/reviews/slug/{slug}", method = RequestMethod.GET)
  List<ReviewResponse> fetchReviewsByPostName(@PathVariable("slug") String slug,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    List<ReviewResponse> response = service.fetchReviewsByPostName(slug, page, size);
    return response;
  }

  @RequestMapping(value = "/reviews/{slug}/statistics")
  ReviewStatistics getReviewStatisticsBySlug(@PathVariable("slug") String slug) {
    ReviewStatistics response = service.getReviewStatisticsBySlug(slug);
    return response;
  }

  @RequestMapping(value = "/user/comments/{id}", method = RequestMethod.PUT)
  Comment updateComment(@PathVariable("id") int id, Comment updatedComment) {
    Comment response = service.updateComment(id, updatedComment);
    return response;
  }

  /**
   * Test APIs
   */
  /*
   * @RequestMapping(value = "/testimonials", method = RequestMethod.POST)
   * >>>>>>> complete create institute + teacher + post + login check point +
   * logout in admin dashboard
   *
   * @ResponseBody Testimonial createTestimonial(@Valid @RequestBody
   * Testimonial newTestimonial) { Testimonial response =
   * service.createTestimonial(newTestimonial); return response; }
   *
   * @RequestMapping(value = "/testimonials/{id}", method = RequestMethod.GET)
   *
   * @ResponseBody Testimonial fetchTestimonialById(@PathVariable("id") int
   * id) { Testimonial response = service.fetchTestimonialById(id); return
   * response; }
   *
   * @RequestMapping(value = "/testimonials", method = RequestMethod.GET)
   *
   * @ResponseBody List<Testimonial> fetchAllTestimonials(@RequestParam(value
   * = "page", required = false) Integer page,
   *
   * @RequestParam(value = "size", required = false) Integer size,
   *
   * @RequestParam(value = "search", required = false) String search,
   *
   * @RequestParam(value = "sort", required = false) String sortBy) {
   * List<Testimonial> response = service.fetchAllTestimonials(page, size,
   * search, sortBy); return response; }
   *
   * @RequestMapping(value = "/testimonials/{id}", method = RequestMethod.PUT)
   *
   * @ResponseBody Testimonial updateTestimonial(@PathVariable("id") int
   * id, @Valid @RequestBody Testimonial updatedTestimonial) { Testimonial
   * response = service.updateTestimonial(id, updatedTestimonial); return
   * response; }
   *
   * @RequestMapping(value = "/comments", method = RequestMethod.POST) Comment
   * createComment(@Valid @RequestBody Comment newComment) { Comment response
   * = service.createComment(newComment); return response; }
   *
   * @RequestMapping(value = "/comments/{parentId}", method =
   * RequestMethod.GET) Comment
   * fetchCommentByParentId(@PathVariable("parentId") int parentId) { Comment
   * response = service.fetchCommentByParentId(parentId); return response; }
   *
   * @RequestMapping(value = "/comments/slug/{slug}", method =
   * RequestMethod.GET) List<Comment>
   * fetchCommentsByPostName(@PathVariable("slug") String slug) {
   * List<Comment> response = service.fetchCommentByPostName(slug); return
   * response; }
   *
   * @RequestMapping(value = "/reviews/slug/{slug}", method =
   * RequestMethod.GET) List<ReviewResponse>
   * fetchReviewsByPostName(@PathVariable("slug") String slug) {
   * List<ReviewResponse> response = service.fetchReviewsByPostName(slug);
   * return response; }
   *
   * @RequestMapping(value = "/comments/{id}", method = RequestMethod.PUT)
   * Comment updateComment(@PathVariable("id") int id, Comment updatedComment)
   * { Comment response = service.updateComment(id, updatedComment); return
   * response; }
   *
   * /** Test APIs
   */
  /*
   * @RequestMapping(value = "/testimonials", method = RequestMethod.POST)
   *
   * @ResponseBody Testimonial createTestimonial(@Valid @RequestBody
   * Testimonial newTestimonial) { Testimonial response =
   * service.createTestimonial(newTestimonial); return response; }
   *
   * @RequestMapping(value = "/testimonials/{id}", method = RequestMethod.GET)
   *
   * @ResponseBody Testimonial fetchTestimonialById(@PathVariable("id") int
   * id) { Testimonial response = service.fetchTestimonialById(id); return
   * response; }
   *
   * @RequestMapping(value = "/testimonials", method = RequestMethod.GET)
   *
   * @ResponseBody List<Testimonial> fetchAllTestimonials(@RequestParam(value
   * = "page", required = false) Integer page,
   *
   * @RequestParam(value = "size", required = false) Integer size,
   *
   * @RequestParam(value = "search", required = false) String search,
   *
   * @RequestParam(value = "sort", required = false) String sortBy) {
   * List<Testimonial> response = service.fetchAllTestimonials(page, size,
   * search, sortBy); return response; }
   *
   * @RequestMapping(value = "/testimonials/{id}", method = RequestMethod.PUT)
   *
   * @ResponseBody Testimonial updateTestimonial(@PathVariable("id") int
   * id, @Valid @RequestBody Testimonial updatedTestimonial) { Testimonial
   * response = service.updateTestimonial(id, updatedTestimonial); return
   * response; }
   */

  /**
   * upload single file
   *
   * @return filename
   */
  @PostMapping("/user/uploadfile")
  @ResponseBody
  String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String type,
      @RequestParam("contentType") String contentType) {
    return service.uploadFileOnGCP(file, type, contentType);
  }

  @RequestMapping(value = "/admin/readwordfile", method = RequestMethod.POST)
  String getHTMLFromDocFile(@RequestParam("file") MultipartFile file) {
    return service.readWordFile(file);
  }

  @RequestMapping(value = "/admin_techops_lmsadmin/createquiz", method = RequestMethod.POST)
  Quiz createQuiz(@RequestParam("file") MultipartFile file, @RequestParam("quiz") String quiz) {
    return service.createQuiz(quiz, file);
  }


  @RequestMapping(value = "/admin_techops_lmsadmin_writer_editor/createquiztemplate", method = RequestMethod.POST)
  QuizTemplate createQuizTemplate(@RequestBody QuizTemplate quizTemplate) {
    return service.createQuizTemplate(quizTemplate);
  }

  @RequestMapping(value = "/admin_techops_lmsadmin_writer_editor/updatequiztemplate/{id}", method = RequestMethod.PATCH)
  QuizTemplate updateQuizTemplate(@PathVariable("id") int id,
      @RequestBody QuizTemplate quizTemplate) {
    return service.updateQuizTemplate(id, quizTemplate);
  }

  @RequestMapping(value = "/admin_techops_lmsadmin_writer_editor/exportquizstats/{quiz_id}", method = RequestMethod.GET)
  String downloadQuizData(@PathVariable("quiz_id") int id) {
    String downloadQuizData = service.downloadQuizData(id);
    return downloadQuizData;
  }

  @RequestMapping(value = "/admin_techops_lmsadmin_writer_editor/updatequiz/{quiz_id}", method = RequestMethod.PATCH)
  Quiz updateQuiz(@PathVariable("quiz_id") int quizId, @RequestBody Quiz quiz) {
    return service.updateQuiz(quizId, quiz);
  }


  @RequestMapping(value = "/batches/{batchId}/units")
  @ResponseBody
  UserUnitResponse fetchUnitsByBatchId(@PathVariable("batchId") int batchId) {
    long userID = jwtTokenUtil.getLoggedInUserID();
    UserUnitResponse response = service.fetchUnitsByBatchID(batchId, userID);
    return response;
  }

  @GetMapping(value = "/examsegments")
  LinkedHashMap<String, String> fetchAllExamSegment() {
    LinkedHashMap<String, String> fetchAllExamSegment = service.fetchAllExamSegment();
    return fetchAllExamSegment;
  }

  @GetMapping(value = "/examsegments/category/{category}")
  LinkedHashMap<String, String> fetchCategoryByExamSegment(
      @PathVariable("category") String category) {

    LinkedHashMap<String, String> fetchCategoryByExamSegment = service
        .fetchCategoryByExamSegment(category);
    return fetchCategoryByExamSegment;
  }

  @GetMapping(value = "/teacherexamsegments")
  LinkedHashSet<String> fetchAllTeacherExamSegment() {
    LinkedHashSet<String> fetchAllExamSegment = service.fetchAllTeacherExamSegment();
    return fetchAllExamSegment;
  }

  @GetMapping(value = "/teacherexamsegments/teachercategory/{category}")
  LinkedHashSet<String> fetchTeacherCategoryCategoryByExamSegment(
      @PathVariable("category") String category) {

    LinkedHashSet<String> fetchCategoryByExamSegment = service
        .fetchTeacherCategoryByExamSegment(category);
    return fetchCategoryByExamSegment;
  }

  @GetMapping(value = "/teacherexamsegments/{examsegment}/teachercategory/{category}/teachersubject")
  LinkedHashSet<String> fetchTeacherSubjectByCategory(@PathVariable("category") String category,
      @PathVariable("examsegment") String examsegment) {

    LinkedHashSet<String> subjects = service.fetchTeacherSubjectByCategory(category, examsegment);
    return subjects;
  }

  @GetMapping(value = "/examsegments/{exam_segment}/category/{category}")
  LinkedHashMap<String, String> fetchSubCategoryByExamSegment(
      @PathVariable("exam_segment") String examSegment,
      @PathVariable("category") String subCategory) {

    LinkedHashMap<String, String> fetchSubCategoryByExamSegment = service
        .fetchSubCategoryByExamSegment(examSegment,
            subCategory);
    return fetchSubCategoryByExamSegment;
  }

  @GetMapping(value = "/examsegments/{exam_segment}/category/{category}/subcatgory/{sub_category}")
  LinkedHashSet<String> fetchSubSubCategoryByExamSegment(
      @PathVariable("exam_segment") String examSegment,
      @PathVariable("category") String category, @PathVariable("sub_category") String subCategory) {

    LinkedHashSet<String> fetchSubCategoryByExamSegment = service
        .fetchSubSubCategoryByExamSegment(examSegment,
            category, subCategory);
    return fetchSubCategoryByExamSegment;
  }

  @GetMapping(value = "/postcategory")
  LinkedHashSet<String> fetchPostCategory() {
    LinkedHashSet<String> postCategory = service.fetchPostCategory();
    return postCategory;

  }

  @GetMapping(value = "/postsubcategory")
  LinkedHashSet<String> fetchPostSubCategory() {
    LinkedHashSet<String> postSubCategory = service.fetchPostSubCategory();
    return postSubCategory;

  }


  @RequestMapping(value = "/admin_sales/updateuserassignment", method = RequestMethod.PUT)

  @ResponseBody
  String updateUserToAssignmentUnit(@Valid @RequestBody List<UserUnitLinkage> request) {
    String response = lectureService.updateUserToAssignmentUnit(request);
    return response;

  }

  @RequestMapping(value = "/admin_sales/course/{course_id}/sendassignmentemails", method = RequestMethod.PUT)
  @ResponseBody
  String sendEmailsWithAttachment(@PathVariable("course_id") int courseId,
      @Valid @RequestBody List<UserUnitLinkage> request) {
    String response = lectureService.sendAssignmentEmails(request, courseId);
    return response;
  }

  /**
   *
   */
  @RequestMapping(value = "/user/assignmentsubmission", method = RequestMethod.PATCH)
  @ResponseBody
  UserUnitLinkage userAssignmentSubmission(
      @Valid @RequestBody AssignmentSubmissionRequest request) {
    long userID = jwtTokenUtil.getLoggedInUserID();
    UserUnitLinkage response = lectureService.assignmentSubmissionByUser(request, userID);
    return response;
  }

  /**
   * This will return the topicwise list of units.
   */
  @RequestMapping(value = "/coursebatches/{batchId}/units")
  @ResponseBody
  public TopicUnitResponse getUnitsByBatch(@PathVariable("batchId") int batchId) {

    TopicUnitResponse response = service.getUnitsByBatch(batchId);
    return response;

  }

  @RequestMapping(value = "/courses/names", method = RequestMethod.GET)
  @ResponseBody
  public List<CourseProjection> fetchAllCoursesNames() {
    List<CourseProjection> response = service.fetchCoursesNames();
    return response;
  }

  @RequestMapping(value = "/teachers/names", method = RequestMethod.GET)
  @ResponseBody
  public List<TeacherProjection> fetchAllTeacherNames() {
    List<TeacherProjection> response = service.fetchAllTeachers();
    return response;
  }

  @RequestMapping(value = "/institutes/names", method = RequestMethod.GET)
  @ResponseBody
  public List<InstituteProjection> fetchAllInstituteNames() {
    List<InstituteProjection> response = service.fetchAllInstitutes();
    return response;
  }

  @RequestMapping(value = "/coursebatches/names", method = RequestMethod.GET)
  @ResponseBody
  public List<BatchNameDTO> fetchBatchNamesForCourses(
      @RequestParam(value = "batchNameRequest", required = true) String batchNameRequest) {
    List<BatchNameDTO> response = null;
    JsonParser jsonParser = new JsonParser();
    if (batchNameRequest != null && !batchNameRequest.isEmpty()) {
      JsonObject jsonObject = (JsonObject) jsonParser.parse(batchNameRequest);
      BatchNameRequest request = new BatchNameRequest();
      JsonArray jsonArray = (JsonArray) jsonObject.get("courseIds");

      List<Integer> courseIds = new LinkedList<Integer>();

      for (int i = 0; i < jsonArray.size(); i++) {
        courseIds.add(jsonArray.get(i).getAsInt());
      }
      /*
       * for(int i=0;i<jsonArray.size();i++) { courseIds.add((JsonObject)
       * jsonArray.get(i)); }
       */
      request.setCourseIds(courseIds);
      response = service.fetchBatchNamesForCourses(request);
    }

    return response;
  }

  /**
   * For linking multiple units with multiple batches.
   */
  @RequestMapping(value = "/coursebatches/units/link", method = RequestMethod.PUT)
  @ResponseBody
  public boolean linkUnitsToBatches(@Valid @RequestBody UnitBatchLinkageRequest request) {
    boolean response = service.linkUnitsToBatches(request);
    return response;

  }

  @RequestMapping(value = "/admin_techops_lmsadmin/units/create", method = RequestMethod.POST)
  @ResponseBody
  public Unit saveUnit(@Valid @RequestBody Unit unit) {
    Unit response = service.saveUnit(unit);
    return response;
  }

  @RequestMapping(value = "/admin_techops_lmsadmin/units/reshuffle", method = RequestMethod.PATCH)
  @ResponseBody
  public boolean reshuffleUnits(@Valid @RequestBody ShuffleUnitsRequest request) {
    boolean response = service.shuffleUnits(request);
    return response;
  }

  @RequestMapping(value = "/admin_techops_lmsadmin/units/reshuffleunitpositions", method = RequestMethod.PATCH)
  @ResponseBody
  public boolean reshuffleUnitPositions(@Valid @RequestBody CustomUnitUpdateRequest request) {
    boolean response = service.shuffleUnitsPositions(request.getRequest());
    return response;
  }

  /**
   * For searching post(s) by category or subcategory or any other parameter
   */
  @RequestMapping(value = "/posts/category/{search}", method = RequestMethod.GET)
  @ResponseBody
  public List<Post> fetchPostByKeyword(@PathVariable("search") String search,
      @RequestParam("page") int page,
      @RequestParam("size") int size) {
    List<Post> response = service.fetchPostByKeyword(search, page, size);
    return response;
  }

  @RequestMapping(value = "/batch/{batch_id}/units", method = RequestMethod.GET)
  @ResponseBody
  public BatchUnitResponse getBatchUnitById(@PathVariable("batch_id") int batchId) {
    BatchUnitResponse batchUnitResponse = service.getBatchUnitById(batchId);
    return batchUnitResponse;
  }

  @RequestMapping(value = "/freeprep/answersheets/{category}", method = RequestMethod.GET)
  @ResponseBody
  public List<AnswerSheet> fetchAnswerSheetsByCategory(@PathVariable("category") String category) {
    List<AnswerSheet> response = service
        .fetchAnswerSheetsByCategory(AnswerSheetCategory.valueOf(category));
    return response;
  }

  @RequestMapping(value = "admin_writer/freeprep/answersheets", method = RequestMethod.POST)
  @ResponseBody
  public AnswerSheet createAnswerSheet(@Valid @RequestBody AnswerSheet answerSheet) {
    AnswerSheet response = service.createAnswerSheet(answerSheet);
    return response;
  }

  @RequestMapping(value = "newsletter/subscribers", method = RequestMethod.POST)
  @ResponseBody
  public NewsletterSubscribeResponse createSubscriber(
      @Valid @RequestBody NewsletterSubscriber newsletterSubscriber) {
    newsletterSubscriber.addMetadata();
    NewsletterSubscribeResponse subscriber = service.createSubscriber(newsletterSubscriber);
    return subscriber;
  }

  @RequestMapping(value = "admin_lmsadmin_writer/breadcrumbs", method = RequestMethod.POST)
  @ResponseBody
  public BreadCrumb createBreadCrumb(@Valid @RequestBody BreadCrumb request) {
    request.addMetadata();
    BreadCrumb response = service.createBreadCrumb(request);
    return response;
  }

  @RequestMapping(value = "breadcrumbs/{id}/parents", method = RequestMethod.GET)
  @ResponseBody
  public LinkedList<BreadCrumb> fetchBreadCrumbByParentId(@PathVariable("id") int id) {
    LinkedList<BreadCrumb> response = service.fetchBreadCrumbById(id);
    return response;
  }

  @RequestMapping(value = "breadcrumbs/{id}/child", method = RequestMethod.GET)
  @ResponseBody
  public LinkedList<BreadCrumb> fetchBreadCrumbById(@PathVariable("id") int id) {
    LinkedList<BreadCrumb> response = service.fetchBreadCrumbByParentId(id);
    return response;
  }

  @RequestMapping(value = "breadcrumbs", method = RequestMethod.GET)
  @ResponseBody
  public LinkedList<BreadCrumb> fetchAllBreadCrumb() {
    LinkedList<BreadCrumb> response = service.fetchAllBreadCrumb();
    return response;
  }

  @RequestMapping(value = "slug/{slug}/type/{slug_type}/exist", method = RequestMethod.GET)
  @ResponseBody
  public boolean slugExistence(@PathVariable("slug") String slug,
      @PathVariable("slug_type") String type) {
    boolean b = service.slugExistence(slug, type);
    return b;
  }

  @RequestMapping(value = "/paylater", method = RequestMethod.POST)
  @ResponseBody
  String payLaterRedcarpet(@Valid @RequestBody PayLaterRequest request) {
    String response = service.sendEmailToRedcarpet(request);
    return response;

  }

  @RequestMapping(value = "/readgooglesheetcourses", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetCourses() throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> courseDetails = googleSheetToCourses.getCourseDetails();
    googleSheetToCourses.updateCourses(courseDetails);
  }

  @RequestMapping(value = "/readgooglesheetbreadcrumbs", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetBreadCrumbs() throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> breadCrumbDetails = googleSheetToBreadCrumb.getBreadCrumbs();
    googleSheetToBreadCrumb.processBreadCrumbs(breadCrumbDetails);
  }

  @RequestMapping(value = "/readgooglesheetinstitute", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetInstitute() throws GeneralSecurityException, IOException {
    List<HashMap<String, String>> breadCrumbDetails = googleSheetToInstitute.getInstituteDetails();
    googleSheetToInstitute.updateInstitute(breadCrumbDetails);
  }

  @RequestMapping(value = "/readgooglesheetinstitutemeta", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetInstituteMeta() throws GeneralSecurityException, IOException {
    List<HashMap<String, String>> breadCrumbDetails = googleSheetToInstituteMeta
        .getInstituteDetails();
    googleSheetToInstituteMeta.updateInstituteMeta(breadCrumbDetails);
  }

  @RequestMapping(value = "/readgooglesheetteachermeta", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetTeacherMeta() throws GeneralSecurityException, IOException {
    List<HashMap<String, String>> breadCrumbDetails = googleSheetToTeacherMeta
        .getInstituteDetails();
    googleSheetToTeacherMeta.updateTeacherMeta(breadCrumbDetails);
  }

  @RequestMapping(value = "/readgooglesheetpostmeta", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetPostMeta() throws GeneralSecurityException, IOException {
    List<HashMap<String, String>> breadCrumbDetails = googleSheetToPostMeta.getInstituteDetails();
    googleSheetToPostMeta.updatePostMeta(breadCrumbDetails);
  }

  @RequestMapping(value = "/readgooglesheetteacher", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetTeacher() throws GeneralSecurityException, IOException {
    List<HashMap<String, String>> breadCrumbDetails = googleSheetToTeachers.getTeacherDetails();
    googleSheetToTeachers.updateTeachers(breadCrumbDetails);
  }

  @RequestMapping(value = "/readgooglesheetposttag", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetPostTag() throws GeneralSecurityException, IOException {
    List<HashMap<String, String>> tags = googleSheetToPostTag.getPostTags();
    googleSheetToPostTag.updatePostTags(tags);
  }

  @RequestMapping(value = "/readgooglesheetcoursemeta", method = RequestMethod.GET)
  @ResponseBody
  void readGoogleSheetMetaTag() throws GeneralSecurityException, IOException {
    List<HashMap<String, String>> tags = googleSheetToCourseMeta.getInstituteDetails();
    googleSheetToCourseMeta.updateCourseMeta(tags);
  }


  @RequestMapping(value = "/admin_writer/updatepostcommentstatus", method = RequestMethod.PUT)
  @ResponseBody
  Comment updatepoststatus(@Valid @RequestBody PostCommentsRequest request) {
    Comment comment = service.changePostsCommentStatus(request);
    return comment;
  }

  /**
   * For migrating the post data into new table
   */
  @RequestMapping(value = "/migrate-post-data", method = RequestMethod.GET)
  @ResponseBody
  void migratePostData() {

  }


  /**
   *
   */
  @RequestMapping(value = "/admin_writer_editor/breadcrumb", method = RequestMethod.POST)
  @ResponseBody
  private CustomBreadCrumResponse createBreadCrumb(@RequestBody CreateBreadCrumbRequest request) {
    return service.createCustomBreadCrumb(request);
  }


  @Value("${onesignal.appid}")
  private String oneSignalAppID;

  @RequestMapping(value = "/getonesignalappid", method = RequestMethod.GET)
  @ResponseBody
  public String getOneSignalAppID() {
    return oneSignalAppID;
  }


  @Value("${googleanalytics.id}")
  private String googleAnalyticsId;

  @RequestMapping(value = "/getgaid", method = RequestMethod.GET)
  @ResponseBody
  public String getGAID() {
    return googleAnalyticsId;
  }


  @RequestMapping(value = "/extrainfo", method = RequestMethod.POST)
  @ResponseBody
  public ExtraInfoResponse createExtraInfo(@RequestBody ExtraInfo extraInfo) {
    return service.createExtraInfo(extraInfo);
  }

  @RequestMapping(value = "/user-unit-stats/{unit_id}", method = RequestMethod.POST)
  @ResponseBody
  public void saveUserUnitStats(@PathVariable("unit_id") int unitId,@RequestBody UserUnitRequest userUnitRequest) {

    long loggedInUserID = jwtTokenUtil.getLoggedInUserID();
    if (loggedInUserID ==0 )
    {
      return;
    }
    service.saveUserUnitStats(userUnitRequest,loggedInUserID,unitId);

  }


}

