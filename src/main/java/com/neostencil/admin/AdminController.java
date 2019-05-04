package com.neostencil.admin;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.neostencil.base.BaseService;
import com.neostencil.base.SearchService;
import com.neostencil.common.CommonUtil;
import com.neostencil.common.enums.UnitType;
import com.neostencil.dtos.CourseBatchDetailedDTO;
import com.neostencil.dtos.CourseDTO;
import com.neostencil.dtos.InstituteDetailedDTO;
import com.neostencil.dtos.TeacherDetailsDTO;
import com.neostencil.model.entities.Address;
import com.neostencil.model.entities.AnnouncementHeader;
import com.neostencil.model.entities.AuthorityName;
import com.neostencil.model.entities.BreadCrumb;
import com.neostencil.model.entities.Coupon;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.CourseSummary;
import com.neostencil.model.entities.CustomPost;
import com.neostencil.model.entities.ExtraInfo;
import com.neostencil.model.entities.Lecture;
import com.neostencil.model.entities.Order;
import com.neostencil.model.entities.OrderNotes;
import com.neostencil.model.entities.Post;
import com.neostencil.model.entities.PostSummary;
import com.neostencil.model.entities.Quiz;
import com.neostencil.model.entities.QuizTemplate;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.repositories.LectureRepository;
import com.neostencil.model.repositories.QuizRepository;
import com.neostencil.model.repositories.QuizTemplateRepository;
import com.neostencil.projections.AddressProjection;
import com.neostencil.projections.CouponProjection;
import com.neostencil.projections.CourseProjection;
import com.neostencil.projections.InstituteProjection;
import com.neostencil.projections.JWMacroProjection;
import com.neostencil.projections.ProductProjection;
import com.neostencil.projections.TeacherProjection;
import com.neostencil.projections.UnitProjection;
import com.neostencil.projections.UserProjection;
import com.neostencil.projections.WowzaMacroProjection;
import com.neostencil.requests.CustomOrderRequest;
import com.neostencil.requests.OrderSubmitRequest;
import com.neostencil.requests.SalesReportRequest;
import com.neostencil.requests.SaveAddressRequest;
import com.neostencil.responses.AllBreadcrumbResponse;
import com.neostencil.responses.CourseBatchResponse;
import com.neostencil.responses.CourseProductResponse;
import com.neostencil.responses.CustomUnitLectureResponse;
import com.neostencil.responses.OrderStatusSummaryResponse;
import com.neostencil.responses.OrderSummaryResponse;
import com.neostencil.responses.OrdersResponse;
import com.neostencil.responses.SalesReportResponse;
import com.neostencil.responses.StudentsPerBatchResponse;
import com.neostencil.responses.UnitResponse;
import com.neostencil.responses.ValidateCouponResponse;
import com.neostencil.responses.VideoRequestDTO;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.services.EcommerceService;
import com.neostencil.services.LectureService;
import com.neostencil.services.UserService;
import com.neostencil.services.VideoRequestService;

@Controller
public class AdminController {

  static String errorPage = "error";

  @Autowired
  private BaseService baseService;

  @Autowired
  private LectureService lectureService;

  @Autowired
  private EcommerceService ecommerceService;

  @Autowired
  private SearchService searchService;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  LectureRepository lectureRepository;

  @Autowired
  QuizRepository quizRepository;
  
  @Autowired
  VideoRequestService videoService;

  @Autowired
  QuizTemplateRepository quizTemplateRepository;

  public AdminController() {
  }

  /**
   * Home routes
   */

  @GetMapping("/admin/login")
  String adminLogin() {
    return "/admin/login";
  }

  @GetMapping("/permission-error")
  String permissionError() {
    return "permission-error";
  }

  /**
   * call GET all courses API and show all courses in a paginated manner on Admin dashboard
   */
  @GetMapping("/admin_lmsadmin/lms/courses")
  String adminLmsCourses(Model model) {

    Collection<CourseProjection> courses = baseService.fetchAllCourses();
    model.addAttribute("courses", courses);
    return "admin/lms/courses/index";
  }

  @GetMapping("/admin_techops_lmsadmin/lms/all-units")
  String adminLmsAllUnits(Model model) {

    Collection<UnitProjection> units = lectureService.fetchAllUnits();
    Collection<CourseProjection> courses = baseService.fetchCoursesNames();
    model.addAttribute("units", units);
    model.addAttribute("courses", courses);
    return "admin/lms/units/all-units";
  }

  @GetMapping("/admin_sales/ecommerce/coupons")
  String adminLmsCoupons(Model model) {
    List<CouponProjection> coupons = ecommerceService.fetchAllCoupons();
    model.addAttribute("coupons", coupons);

    return "admin/ecommerce/coupons/index";
  }
  
  @GetMapping("/admin/ecommerce/credit-neo-cash")
  String creditNeoCash(Model model) {
    return "admin/ecommerce/credit-neo-cash";
  }

  @GetMapping("/admin_sales/ecommerce/coupons/create-coupon")
  String adminLmsAddCoupons(Model model) {

    List<ProductProjection> products = ecommerceService.fetchAllProducts();
    model.addAttribute("products", products);

    // List<User> users = userService.fetchAllUsers();
    // model.addAttribute("users", users);
    return "admin/ecommerce/coupons/create-coupon";
  }
  
  @GetMapping("/approver_admin/video-requests")
  String getAllVideoRequests(Model model) {
    List<VideoRequestDTO> response = videoService.fetchAllVideoRequests();
    model.addAttribute("requests", response);

    return "admin/lms/units/video-requests";

  }
  
  @GetMapping("/admin_sales/ecommerce/coupons/{coupon_id}/update-coupon")
  String adminEcommerceUpdateCoupon(Model model, @PathVariable("coupon_id") int couponId) {
    Coupon coupon = ecommerceService.fetchCouponByCouponCode(couponId);
    model.addAttribute("coupon", coupon);
    List<ProductProjection> products = ecommerceService.fetchAllProducts();
    model.addAttribute("products", products);
    return "admin/ecommerce/coupons/update-coupon";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/add")
  String adminLmsAddNewCourse(Model model) {
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    List<CourseSummary> courseSummaries = baseService.fetchAllCourseSummaries();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    model.addAttribute("relatedCourses", courseSummaries);
    return "admin/lms/courses/add";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/{course_slug}/update-course")
  String adminLmsUpdateNewCourse(Model model, @PathVariable("course_slug") String courseSlug) {
    CourseDTO course = baseService.fetchCourseBySlug(courseSlug);
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    List<CourseSummary> courseSummaries = baseService.fetchAllCourseSummaries();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("course", course);
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    model.addAttribute("relatedCourses", courseSummaries);
    return "admin/lms/courses/update-course";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/jee/{course_slug}/update-course")
  String adminLmsUpdateNewCourseForJee(Model model, @PathVariable("course_slug") String courseSlug) {
    CourseDTO course = baseService.fetchCourseBySlug("jee/"+courseSlug);
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    List<CourseSummary> courseSummaries = baseService.fetchAllCourseSummaries();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("course", course);
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    model.addAttribute("relatedCourses", courseSummaries);
    return "admin/lms/courses/update-course";
  }


  @GetMapping("/admin_lmsadmin/lms/courses/{course_slug}/clone-course")
  String adminLmsCloneNewCourse(Model model, @PathVariable("course_slug") String courseSlug) {
    CourseDTO course = baseService.fetchCourseBySlug(courseSlug);
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    List<CourseSummary> courseSummaries = baseService.fetchAllCourseSummaries();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("course", course);
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    model.addAttribute("relatedCourses", courseSummaries);
    return "admin/lms/courses/clone-course";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/jee/{course_slug}/clone-course")
  String adminLmsCloneNewCourseForJee(Model model, @PathVariable("course_slug") String courseSlug) {
    CourseDTO course = baseService.fetchCourseBySlug("jee/"+courseSlug);
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    List<CourseSummary> courseSummaries = baseService.fetchAllCourseSummaries();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("course", course);
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    model.addAttribute("relatedCourses", courseSummaries);
    return "admin/lms/courses/clone-course";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/neet/{course_slug}/update-course")
  String adminLmsUpdateNewCourseForNeet(Model model, @PathVariable("course_slug") String courseSlug) {
    CourseDTO course = baseService.fetchCourseBySlug("neet/"+courseSlug);
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    List<CourseSummary> courseSummaries = baseService.fetchAllCourseSummaries();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("course", course);
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    model.addAttribute("relatedCourses", courseSummaries);
    return "admin/lms/courses/update-course";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/neet/{course_slug}/clone-course")
  String adminLmsCloneNewCourseForNeet(Model model, @PathVariable("course_slug") String courseSlug) {
    CourseDTO course = baseService.fetchCourseBySlug("neet/"+courseSlug);
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    List<CourseSummary> courseSummaries = baseService.fetchAllCourseSummaries();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("course", course);
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    model.addAttribute("relatedCourses", courseSummaries);
    return "admin/lms/courses/clone-course";
  }


  @GetMapping("/admin_lmsadmin/lms/courses/update/{slug}")
  String adminLmsUpdateCourse(@PathVariable("slug") String slug, Model model) {

    CourseDTO course = baseService.fetchCourseBySlug(slug);
    if (course == null) {
      return errorPage;
    }
    Set<CourseBatchDetailedDTO> batches = course.getBatches();

    model.addAttribute("title", "Update Course");
    model.addAttribute("course", course);
    // TODO: Add code to fetch corresponding reviews when the review API is
    // done
    return "admin/lms/courses/update";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/update/jee/{slug}")
  String adminLmsUpdateCourseForJee(@PathVariable("slug") String slug, Model model) {

    CourseDTO course = baseService.fetchCourseBySlug("jee/"+slug);
    if (course == null) {
      return errorPage;
    }
    Set<CourseBatchDetailedDTO> batches = course.getBatches();

    model.addAttribute("title", "Update Course");
    model.addAttribute("course", course);
    // TODO: Add code to fetch corresponding reviews when the review API is
    // done
    return "admin/lms/courses/update";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/update/neet/{slug}")
  String adminLmsUpdateCourseNeet(@PathVariable("slug") String slug, Model model) {

    CourseDTO course = baseService.fetchCourseBySlug("neet/"+slug);
    if (course == null) {
      return errorPage;
    }
    Set<CourseBatchDetailedDTO> batches = course.getBatches();

    model.addAttribute("title", "Update Course");
    model.addAttribute("course", course);
    // TODO: Add code to fetch corresponding reviews when the review API is
    // done
    return "admin/lms/courses/update";
  }



  @GetMapping("/admin_lmsadmin/lms/courses/{slug}/batches")
  String adminGetCourseBatches(@PathVariable("slug") String slug, Model model) {
    Course course = baseService.fetchAllCourseBatchBySlug(slug);
    Set<CourseBatch> batches = null;
    if (course != null) {
      batches = course.getBatches();
    }

    model.addAttribute("batches", batches);
    model.addAttribute("course", course);

    return "admin/lms/batches/index";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/jee/{slug}/batches")
  String adminGetCourseBatchesForJee(@PathVariable("slug") String slug, Model model) {
    Course course = baseService.fetchAllCourseBatchBySlug("jee/" + slug);
    Set<CourseBatch> batches = null;
    if (course != null) {
      batches = course.getBatches();
    }

    model.addAttribute("batches", batches);
    model.addAttribute("course", course);

    return "admin/lms/batches/index";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/neet/{slug}/batches")
  String adminGetCourseBatchesForNeet(@PathVariable("slug") String slug, Model model) {
    Course course = baseService.fetchAllCourseBatchBySlug("neet/"+slug);
    Set<CourseBatch> batches = null;
    if (course != null) {
      batches = course.getBatches();
    }

    model.addAttribute("batches", batches);
    model.addAttribute("course", course);

    return "admin/lms/batches/index";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/{slug}/batches/{id}/students")
  String getStudentsPerBatch(@PathVariable("slug") String slug, @PathVariable("id") int id,
      Model model) {
    StudentsPerBatchResponse response = userService.fetchUsersPerBatch(id);
    model.addAttribute("response", response);
    return "admin/users/usercourses";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/{slug}/batches/add-course-batch")
  String addCourseBatch(@PathVariable("slug") String slug, Model model) {
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    model.addAttribute("course", course);
    return "admin/lms/batches/add-course-batch";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/jee/{slug}/batches/add-course-batch")
  String addCourseBatchJee(@PathVariable("slug") String slug, Model model) {
    slug = "jee/"+slug;
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    model.addAttribute("course", course);
    return "admin/lms/batches/add-course-batch";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/neet/{slug}/batches/add-course-batch")
  String addCourseBatchNeet(@PathVariable("slug") String slug, Model model) {
    slug = "neet/"+slug;
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    model.addAttribute("course", course);
    return "admin/lms/batches/add-course-batch";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/{slug}/batches/{batch_id}/update")
  String updateCourseBatch(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batch_id, Model model) {
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    CourseBatchResponse courseBatchResponse = baseService.fetchCourseBatchById(batch_id);
    model.addAttribute("course", course);
    model.addAttribute("courseBatch", courseBatchResponse);
    return "admin/lms/batches/update-course-batch";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/jee/{slug}/batches/{batch_id}/update")
  String updateCourseBatchForJee(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batch_id, Model model) {
    CourseDTO course = baseService.fetchCourseBySlug("jee/"+slug);
    CourseBatchResponse courseBatchResponse = baseService.fetchCourseBatchById(batch_id);
    model.addAttribute("course", course);
    model.addAttribute("courseBatch", courseBatchResponse);
    return "admin/lms/batches/update-course-batch";
  }

  @GetMapping("/admin_lmsadmin/lms/courses/neet/{slug}/batches/{batch_id}/update")
  String updateCourseBatchForNeet(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batch_id, Model model) {
    CourseDTO course = baseService.fetchCourseBySlug("neet/"+slug);
    CourseBatchResponse courseBatchResponse = baseService.fetchCourseBatchById(batch_id);
    model.addAttribute("course", course);
    model.addAttribute("courseBatch", courseBatchResponse);
    return "admin/lms/batches/update-course-batch";
  }

  /*
   ** Units
   *
   */

  @GetMapping("/admin_techops_lmsadmin/lms/courses/{slug}/batches/{batch_id}/units")
  String adminGetUnit(@PathVariable("slug") String slug, @PathVariable("batch_id") int batchId,
      Model model) {

    CourseDTO course = baseService.fetchCourseBySlug(slug);
    Collection<CourseProjection> courses = baseService.fetchCoursesNames();
    List<CustomUnitLectureResponse> response = null;
    CourseBatch courseBatch = null;
    if (batchId != 0) {
      courseBatch = baseService.fetchBatchById(batchId);
    }
    if (courseBatch != null && courseBatch.getUnits() != null
        && courseBatch.getUnits().size() > 0) {

      response = new ArrayList<>();

      for (Unit unit : courseBatch.getUnits()) {

        if (unit != null) {
          if (unit.getType().equals(UnitType.LECTURE)) {
            Lecture lecture = lectureRepository.findById(unit.getTypeId());
            CustomUnitLectureResponse customUnitLectureResponse = new CustomUnitLectureResponse();
            customUnitLectureResponse.setUnit(unit);
            customUnitLectureResponse.setLecture(lecture);
            response.add(customUnitLectureResponse);

          } else {
            CustomUnitLectureResponse customUnitLectureResponse = new CustomUnitLectureResponse();
            customUnitLectureResponse.setUnit(unit);
            response.add(customUnitLectureResponse);
          }
        }
      }
    }

    model.addAttribute("batch", courseBatch);
    model.addAttribute("course", course);
    model.addAttribute("units", response);
    model.addAttribute("courses", courses);
    return "admin/lms/units/index";
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/jee/{slug}/batches/{batch_id}/units")
  String adminGetUnitForJee(@PathVariable("slug") String slug, @PathVariable("batch_id") int batchId,
      Model model) {

    CourseDTO course = baseService.fetchCourseBySlug("jee/"+slug);
    Collection<CourseProjection> courses = baseService.fetchCoursesNames();
    List<CustomUnitLectureResponse> response = null;
    CourseBatch courseBatch = null;
    if (batchId != 0) {
      courseBatch = baseService.fetchBatchById(batchId);
    }
    if (courseBatch != null && courseBatch.getUnits() != null
        && courseBatch.getUnits().size() > 0) {

      response = new ArrayList<>();

      for (Unit unit : courseBatch.getUnits()) {

        if (unit != null) {
          if (unit.getType().equals(UnitType.LECTURE)) {
            Lecture lecture = lectureRepository.findById(unit.getTypeId());
            CustomUnitLectureResponse customUnitLectureResponse = new CustomUnitLectureResponse();
            customUnitLectureResponse.setUnit(unit);
            customUnitLectureResponse.setLecture(lecture);
            response.add(customUnitLectureResponse);

          } else {
            CustomUnitLectureResponse customUnitLectureResponse = new CustomUnitLectureResponse();
            customUnitLectureResponse.setUnit(unit);
            response.add(customUnitLectureResponse);
          }
        }
      }
    }

    model.addAttribute("batch", courseBatch);
    model.addAttribute("course", course);
    model.addAttribute("units", response);
    model.addAttribute("courses", courses);
    return "admin/lms/units/index";
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/neet/{slug}/batches/{batch_id}/units")
  String adminGetUnitForNeet(@PathVariable("slug") String slug, @PathVariable("batch_id") int batchId,
      Model model) {

    CourseDTO course = baseService.fetchCourseBySlug("neet/"+slug);
    Collection<CourseProjection> courses = baseService.fetchCoursesNames();
    List<CustomUnitLectureResponse> response = null;
    CourseBatch courseBatch = null;
    if (batchId != 0) {
      courseBatch = baseService.fetchBatchById(batchId);
    }
    if (courseBatch != null && courseBatch.getUnits() != null
        && courseBatch.getUnits().size() > 0) {

      response = new ArrayList<>();

      for (Unit unit : courseBatch.getUnits()) {

        if (unit != null) {
          if (unit.getType().equals(UnitType.LECTURE)) {
            Lecture lecture = lectureRepository.findById(unit.getTypeId());
            CustomUnitLectureResponse customUnitLectureResponse = new CustomUnitLectureResponse();
            customUnitLectureResponse.setUnit(unit);
            customUnitLectureResponse.setLecture(lecture);
            response.add(customUnitLectureResponse);

          } else {
            CustomUnitLectureResponse customUnitLectureResponse = new CustomUnitLectureResponse();
            customUnitLectureResponse.setUnit(unit);
            response.add(customUnitLectureResponse);
          }
        }
      }
    }

    model.addAttribute("batch", courseBatch);
    model.addAttribute("course", course);
    model.addAttribute("units", response);
    model.addAttribute("courses", courses);
    return "admin/lms/units/index";
  }

  @GetMapping("/admin_techops_lmsadmin/lms/batch/{batch_id}/units")
  String adminViewUnitInBatch(@PathVariable("batch_id") int batchId, Model model) {

    List<CustomUnitLectureResponse> response = null;
    CourseBatch courseBatch = null;
    Collection<CourseProjection> courses = baseService.fetchCoursesNames();
    if (batchId != 0) {
      courseBatch = baseService.fetchBatchById(batchId);
    }
    Course course = null;
    if (courseBatch != null) {
      course = courseBatch.getCourse();
    }

    if (courseBatch != null && courseBatch.getUnits() != null
        && courseBatch.getUnits().size() > 0) {

      response = new ArrayList<>();

      for (Unit unit : courseBatch.getUnits()) {

        if (unit != null) {
          if (unit.getType().equals(UnitType.LECTURE)) {
            Lecture lecture = lectureRepository.findById(unit.getTypeId());
            CustomUnitLectureResponse customUnitLectureResponse = new CustomUnitLectureResponse();
            customUnitLectureResponse.setUnit(unit);
            customUnitLectureResponse.setLecture(lecture);
            response.add(customUnitLectureResponse);

          } else {
            CustomUnitLectureResponse customUnitLectureResponse = new CustomUnitLectureResponse();
            customUnitLectureResponse.setUnit(unit);
            response.add(customUnitLectureResponse);
          }
        }
      }
    }

    model.addAttribute("batch", courseBatch);
    model.addAttribute("course", course);
    model.addAttribute("units", response);
    model.addAttribute("courses", courses);
    return "admin/lms/units/index";
  }

  /**
   * <<<<<<< HEAD get single course data and populate in add unit page. allow user to add the course
   * batch unit.
   */
  @GetMapping("/admin_techops_lmsadmin/lms/courses/{batchId}/units/add-unit")
  String addLecture(@PathVariable("batchId") int batchId, Model model) {

    // Course course = baseService.fetchCourseBySlug(slug);
    CourseBatchResponse courseBatch = baseService.fetchCourseBatchById(batchId);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(batchId);
    model.addAttribute("courseBatch", courseBatch);
    model.addAttribute("topics", topics);

    return "admin/lms/units/add-unit";
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/{slug}/batches/{batch_id}/units/{unit_id}/update-unit")
  String adminLmsUpdateUnit(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batchId,
      @PathVariable("unit_id") int id, Model model) {

    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    CourseBatchResponse courseBatch = baseService.fetchCourseBatchById(batchId);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(batchId);
    model.addAttribute("topics", topics);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();

          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);
          model.addAttribute("unit", unit);
          path = "admin/lms/units/update-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/update-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/update-note";
          break;

        case QUIZ:
          Quiz quiz = quizRepository.findByQuizId(unit.getTypeId());
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("quiz", quiz);
          path = "admin/lms/units/update-quiz";
          break;

      }

    }

    return path;
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/jee/{slug}/batches/{batch_id}/units/{unit_id}/update-unit")
  String adminLmsUpdateUnitForJee(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batchId,
      @PathVariable("unit_id") int id, Model model) {

    slug = "jee/"+slug;

    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    CourseBatchResponse courseBatch = baseService.fetchCourseBatchById(batchId);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(batchId);
    model.addAttribute("topics", topics);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();

          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);
          model.addAttribute("unit", unit);
          path = "admin/lms/units/update-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/update-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/update-note";
          break;

        case QUIZ:
          Quiz quiz = quizRepository.findByQuizId(unit.getTypeId());
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("quiz", quiz);
          path = "admin/lms/units/update-quiz";
          break;

      }

    }

    return path;
  }


  @GetMapping("/admin_techops_lmsadmin/lms/courses/neet/{slug}/batches/{batch_id}/units/{unit_id}/update-unit")
  String adminLmsUpdateUnitForNeet(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batchId,
      @PathVariable("unit_id") int id, Model model) {

    slug = "neet/"+slug;

    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    CourseBatchResponse courseBatch = baseService.fetchCourseBatchById(batchId);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(batchId);
    model.addAttribute("topics", topics);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();

          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);
          model.addAttribute("unit", unit);
          path = "admin/lms/units/update-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/update-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/update-note";
          break;

        case QUIZ:
          Quiz quiz = quizRepository.findByQuizId(unit.getTypeId());
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("quiz", quiz);
          path = "admin/lms/units/update-quiz";
          break;

      }

    }

    return path;
  }



  @GetMapping("/admin_techops_lmsadmin/lms/courses/{slug}/batches/{batch_id}/units/{unit_id}/preview")
  String adminLmsShowPreview(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batchId,
      @PathVariable("unit_id") int id, Model model) {

    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    CourseBatchResponse courseBatch = baseService.fetchCourseBatchById(batchId);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(batchId);
    model.addAttribute("topics", topics);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();

          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);
          model.addAttribute("unit", unit);
          path = "admin/lms/units/update-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/preview-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/preview-note";
          break;

      }
    }
    return path;
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/jee/{slug}/batches/{batch_id}/units/{unit_id}/preview")
  String adminLmsShowPreviewForJee(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batchId,
      @PathVariable("unit_id") int id, Model model) {

    slug = "jee/"+slug;

    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    CourseBatchResponse courseBatch = baseService.fetchCourseBatchById(batchId);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(batchId);
    model.addAttribute("topics", topics);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();

          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);
          model.addAttribute("unit", unit);
          path = "admin/lms/units/update-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/preview-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/preview-note";
          break;

      }
    }
    return path;
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/neet/{slug}/batches/{batch_id}/units/{unit_id}/preview")
  String adminLmsShowPreviewForNeet(@PathVariable("slug") String slug,
      @PathVariable("batch_id") int batchId,
      @PathVariable("unit_id") int id, Model model) {

    slug = "neet/"+slug;

    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    CourseBatchResponse courseBatch = baseService.fetchCourseBatchById(batchId);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(batchId);
    model.addAttribute("topics", topics);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();

          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);
          model.addAttribute("unit", unit);
          path = "admin/lms/units/update-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/preview-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          model.addAttribute("course", course);
          model.addAttribute("batch", courseBatch);
          path = "admin/lms/units/preview-note";
          break;

      }
    }
    return path;
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/{slug}/units/{unit_id}/clone-unit")
  String adminLmsCloneUnit(@PathVariable("slug") String slug, @PathVariable("unit_id") int id,
      Model model) {

    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(unit.getBatch().getId());
    model.addAttribute("topics", topics);
    model.addAttribute("course", course);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();
          model.addAttribute("unit", unit);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);

          path = "admin/lms/units/clone-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          path = "admin/lms/units/clone-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          path = "admin/lms/units/clone-note";
          break;
      }

    }

    return path;
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/jee/{slug}/units/{unit_id}/clone-unit")
  String adminLmsCloneUnitForJee(@PathVariable("slug") String slug, @PathVariable("unit_id") int id,
      Model model) {

    slug = "jee/"+slug;
    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(unit.getBatch().getId());
    model.addAttribute("topics", topics);
    model.addAttribute("course", course);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();
          model.addAttribute("unit", unit);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);

          path = "admin/lms/units/clone-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          path = "admin/lms/units/clone-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          path = "admin/lms/units/clone-note";
          break;
      }

    }

    return path;
  }

  @GetMapping("/admin_techops_lmsadmin/lms/courses/neet/{slug}/units/{unit_id}/clone-unit")
  String adminLmsCloneUnitForNeet(@PathVariable("slug") String slug, @PathVariable("unit_id") int id,
      Model model) {

    slug = "neet/"+slug;

    Unit unit = lectureService.fetchUnitById(id);
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    Set<String> topics = baseService.getExistingTopicsOfTheBatch(unit.getBatch().getId());
    model.addAttribute("topics", topics);
    model.addAttribute("course", course);
    String path = null;

    if (unit != null && !TextUtils.isEmpty(unit.getType().getJsonValue())) {

      switch (unit.getType()) {
        case LECTURE:
          Lecture lecture = baseService.fetchLectureById(unit.getTypeId());
          List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
          List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();
          model.addAttribute("unit", unit);
          model.addAttribute("wowzaMacros", wowzaMacros);
          model.addAttribute("jwMacros", jwMacros);
          model.addAttribute("lecture", lecture);

          path = "admin/lms/units/clone-lecture";
          break;

        case ASSIGNMENT:
          model.addAttribute("unit", unit);
          path = "admin/lms/units/clone-assignment";
          break;

        case NOTES:
          model.addAttribute("unit", unit);
          path = "admin/lms/units/clone-note";
          break;
      }

    }

    return path;
  }

  @GetMapping("/admin_lmsadmin/lms/teachers/add-teacher")
  String adminLmsAddNewTeacher(Model model) {
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    return "admin/lms/teachers/add-teacher";
  }

  @GetMapping("/admin_lmsadmin/lms/teachers/{teacher_slug}/update-teacher")
  String adminLmsUpdateTeacher(Model model, @PathVariable("teacher_slug") String teacherSlug) {
    TeacherDetailsDTO teacherDetail = baseService.fetchTeacherDetailsBySlug(teacherSlug);
    model.addAttribute("teacher", teacherDetail);
    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("institutes", institutes);
    return "admin/lms/teachers/update-teacher";
  }

  @GetMapping("/admin/lms/jwmacro/create-jwmacro")
  String adminLmsAddNewJWMacro() {

    return "admin/lms/jwmacro/create-jwmacro";
  }

  @GetMapping("/admin/lms/wowzamacro/create-wowzamacro")
  String adminLmsAddNewWawzoMacro() {

    return "admin/lms/wowzamacro/create-wowzamacro";
  }

  @GetMapping("/admin_lmsadmin/lms/teachers")
  String adminLmsAllTeacher(Model model) {
    List<TeacherProjection> teachers = baseService.fetchAllTeachers();
    model.addAttribute("teachers", teachers);
    return "admin/lms/teachers/index";
  }

  @GetMapping("/admin_techops_lmsadmin/lms/jwmacro")
  String adminLmsAllJWMacro(Model model) {
    List<JWMacroProjection> jwMacros = baseService.fetchAllJWMacros();
    model.addAttribute("jwmacros", jwMacros);

    return "admin/lms/jwmacro/index";
  }

  @GetMapping("/admin_techops_lmsadmin/lms/wowzamacro")
  String adminLmsAllWawzoMacro(Model model) {
    List<WowzaMacroProjection> wowzaMacros = baseService.fetchAllWowzaMacros();
    model.addAttribute("wowzaMacros", wowzaMacros);

    return "admin/lms/wowzamacro/index";
  }

  @GetMapping("/admin_lmsadmin/lms/institutes")
  String adminLmsAllInstitute(Model model) {

    List<InstituteProjection> institutes = baseService.fetchAllInstitutes();
    model.addAttribute("institutes", institutes);
    return "admin/lms/institutes/index";
  }

  @GetMapping("/admin_lmsadmin_sales/lms/assignments")
  String adminLmsAssignment(Model model) {

    List<CourseProjection> courses = baseService.fetchAllPublishCourseWithoutPagination();
    model.addAttribute("courses", courses);
    return "admin/lms/assignments/add-student";
  }

  @GetMapping("/admin_lmsadmin/lms/institutes/add-institute")
  String adminLmsAddNewInstitue(Model model) {

    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();
    model.addAttribute("breadCrumbs", breadCrumbs);
    return "admin/lms/institutes/add-institute";
  }

  @GetMapping("/admin_lmsadmin/lms/institutes/{institute_slug}/update-institute")
  String adminLmsAddNewInstitue(Model model, @PathVariable("institute_slug") String instituteSlug) {

    InstituteDetailedDTO institute = baseService.fetchInstituteBySlug(instituteSlug);
    model.addAttribute("institute", institute);
    return "admin/lms/institutes/update-institute";
  }

  /**
   * get all posts and send to all posts page in admin dashboard
   */
  @GetMapping("/admin_writer_editor/posts")
  String adminLmsPosts(Model model) {

    List<Post> posts = baseService.fetchAllPosts(null, null, null, null);
    model.addAttribute("posts", posts);
    return "admin/posts/index";
  }

  @GetMapping("/admin_writer_editor/free-prep")
  String adminFreePrep(Model model) {

    List<Post> posts = baseService.fetchFreePrep();
    model.addAttribute("posts", posts);
    return "admin/freeprep/free-prep";
  }

  @GetMapping("/admin_writer/posts/add-post")
  String addPost(Model model) {

    Set<AuthorityName> userRole = jwtTokenUtil.getLoggedInUserRole();

    if (userRole.contains(AuthorityName.ROLE_EDITOR)) {
      model.addAttribute("enablePublishButton", true);
    } else {
      model.addAttribute("enablePublishButton", false);
    }

    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();

    model.addAttribute("breadCrumbs", breadCrumbs);
    return "admin/posts/add-post";
  }

  @GetMapping("/admin_writer/free-prep/add-free-prep")
  String addFreePrep(Model model) {

    Set<AuthorityName> userRole = jwtTokenUtil.getLoggedInUserRole();

    if (userRole.contains(AuthorityName.ROLE_EDITOR) || userRole
        .contains(AuthorityName.ROLE_ADMIN)) {
      model.addAttribute("enablePublishButton", true);
    } else {
      model.addAttribute("enablePublishButton", false);
    }

    LinkedHashSet<String> postCategory = baseService.fetchPostCategory();
    LinkedHashSet<String> postSubCategory = baseService.fetchPostSubCategory();
    List<CustomPost> customPosts = baseService.fetchAllCustomPosts();
    List<PostSummary> postSummaries = baseService.fetchAllPostSummaries();

    LinkedList<BreadCrumb> breadCrumbs = baseService.fetchAllBreadCrumb();

    model.addAttribute("breadCrumbs", breadCrumbs);
    model.addAttribute("relatedPosts", postSummaries);
    model.addAttribute("categories", postCategory);
    model.addAttribute("subcategories", postSubCategory);
    model.addAttribute("customPosts", customPosts);
    return "admin/freeprep/add-free-prep";
  }

  @GetMapping("/admin_writer/free-prep/{id}/update")
  String updateFreePrep(@PathVariable("id") int id, Model model) {

    Set<AuthorityName> userRole = jwtTokenUtil.getLoggedInUserRole();

    if (userRole.contains(AuthorityName.ROLE_EDITOR)) {
      model.addAttribute("enablePublishButton", true);
    } else {
      model.addAttribute("enablePublishButton", false);
    }

    Post post = baseService.fetchPostById(id);

    LinkedHashSet<String> postCategory = baseService.fetchPostCategory();
    LinkedHashSet<String> postSubCategory = baseService.fetchPostSubCategory();
    List<CustomPost> customPosts = baseService.fetchAllCustomPosts();
    List<PostSummary> postSummaries = baseService.fetchAllPostSummaries();

    model.addAttribute("post", post);
    model.addAttribute("relatedPosts", postSummaries);
    model.addAttribute("category", postCategory);
    model.addAttribute("subcategory", postSubCategory);
    model.addAttribute("customPosts", customPosts);
    return "admin/freeprep/update-free-prep";
  }

  /**
   * get single post data and populate in update post page. allow user to update the post data
   * through this update post page.
   */
  @GetMapping("/admin_writer/posts/{id}/update")
  String adminUpdatePost(@PathVariable("id") int id, Model model) {

    Set<AuthorityName> userRole = jwtTokenUtil.getLoggedInUserRole();

    if (userRole.contains(AuthorityName.ROLE_EDITOR) || userRole
        .contains(AuthorityName.ROLE_ADMIN)) {
      model.addAttribute("enablePublishButton", true);
    } else {
      model.addAttribute("enablePublishButton", false);
    }

    Post post = baseService.fetchPostById(id);

    model.addAttribute("post", post);

    return "admin/posts/update-post";
  }

  @GetMapping("/admin_writer/posts/{slug}/comments")
  String adminPostComments(@PathVariable("slug") String slug, Model model) {

    Set<AuthorityName> userRole = jwtTokenUtil.getLoggedInUserRole();

    if (userRole.contains(AuthorityName.ROLE_EDITOR) || userRole
        .contains(AuthorityName.ROLE_ADMIN)) {
      model.addAttribute("enablePublishButton", true);
    } else {
      model.addAttribute("enablePublishButton", false);
    }

    Page page = baseService.fetchAllCommentByPostName(slug, null, null);

    model.addAttribute("comments", page);
    return "admin/posts/post-comments";
  }

  @GetMapping("/admin_writer/posts/jee/{slug}/comments")
  String adminPostCommentsForJee(@PathVariable("slug") String slug, Model model) {

    slug = "jee/"+slug;
    Set<AuthorityName> userRole = jwtTokenUtil.getLoggedInUserRole();

    if (userRole.contains(AuthorityName.ROLE_EDITOR) || userRole
        .contains(AuthorityName.ROLE_ADMIN)) {
      model.addAttribute("enablePublishButton", true);
    } else {
      model.addAttribute("enablePublishButton", false);
    }

    Page page = baseService.fetchAllCommentByPostName(slug,null,null);

    model.addAttribute("comments", page);
    return "admin/posts/post-comments";
  }

  @GetMapping("/admin_writer/posts/{id}/clone-post")
  String adminClonePost(@PathVariable("id") int id, Model model) {

    Set<AuthorityName> userRole = jwtTokenUtil.getLoggedInUserRole();

    if (userRole.contains(AuthorityName.ROLE_EDITOR) || userRole
        .contains(AuthorityName.ROLE_ADMIN)) {
      model.addAttribute("enablePublishButton", true);
    } else {
      model.addAttribute("enablePublishButton", false);
    }

    Post post = baseService.fetchPostById(id);

    model.addAttribute("post", post);

    return "admin/posts/clone-post";
  }

  @RequestMapping(value = "/admin/rebuild-search-index", method = RequestMethod.GET)
  @ResponseBody
  String rebuildSearchIndex() {

    try {
      searchService.initializeSearch();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return "failure";
    }
    return "success";
  }

  @GetMapping("/admin_sales/ecommerce/orders")
  String fetchAllOrders(Model model,@RequestParam(value = "startDate",required = false) String startDate,@RequestParam(value = "endDate",required = false) String endDate) {
    if (startDate != null && endDate != null){
      Timestamp startDate1 = CommonUtil.convertStringFormatIntoTimeStamp(startDate);
      Timestamp endDate1 = CommonUtil.convertStringFormatIntoTimeStamp(endDate);

      OrdersResponse response = ecommerceService.fetchAllOrdersWithInRange(startDate1, endDate1);
      OrderStatusSummaryResponse summaryResponse = ecommerceService.fetchOrderStatusSummary(startDate1, endDate1);
      if (response != null) {
        model.addAttribute("orders", response.getOrders());
        model.addAttribute("summary", summaryResponse);
      }
    }else{

      Timestamp startDate1 = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
      Timestamp endDate1 = Timestamp.valueOf(LocalDateTime.now());

      OrdersResponse response = ecommerceService.fetchAllOrdersWithInRange(startDate1, endDate1);
      OrderStatusSummaryResponse summaryResponse = ecommerceService.fetchOrderStatusSummary(startDate1, endDate1);
      if (response != null) {
        model.addAttribute("orders", response.getOrders());
        model.addAttribute("summary", summaryResponse);
      }
    }

    return "admin/ecommerce/orders";
  }

  @GetMapping("/admin_sales/ecommerce/sales-report")
  String generateSalesReport(Model model) {
    SalesReportRequest request = new SalesReportRequest();
    // long
    // startMiliseconds=Timestamp.valueOf(LocalDateTime.now()).getTime();

    request.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
    LocalDateTime date=LocalDateTime.now();
    LocalDateTime startDate=date.with(TemporalAdjusters.firstDayOfMonth());
    request.setStartDate(Timestamp.valueOf(startDate));
    String[] examSegments = new String[10];
    examSegments[0] = "IAS";
    examSegments[1] = "JEE";
    examSegments[2] = "NEET";
    examSegments[3] = "IES";
    examSegments[4] = "SSC";
    examSegments[5] = "STATE_PSC";
    examSegments[6] = "GATE";
    examSegments[7] = "IES_GATE";
    examSegments[8] = "LAW";
    examSegments[9] = "RRB";

    String[] orderTypes = new String[2];
    request.setExamSegments(examSegments);
    orderTypes[0] = "REGULAR";
    orderTypes[1] = "MANUAL";

    request.setOrderTypes(orderTypes);

    SalesReportResponse response = ecommerceService.generateSalesReport(request);
    model.addAttribute("report", response);

    return "admin/ecommerce/new-sales-report";
  }

  /**
   * For fetching all the user details in admin dashbaord
   */
  @RequestMapping(value = "/admin_sales/users", method = RequestMethod.GET)
  String fetchAllUsers(Model model) {
    List<UserProjection> response = userService.fetchAllUsers();
    model.addAttribute("users", response);
    return "admin/users/index";
  }

  @RequestMapping(value = "/admin_sales/ecommerce/add-order", method = RequestMethod.GET)
  String createOrder(Model model) {

    List<UnitResponse> units = ecommerceService.fetchAllUnitProducts();
    List<CourseProductResponse> courses = ecommerceService.fetchAllCourseProducts();
    List<CouponProjection> coupons = ecommerceService.fetchAllCoupons();

    model.addAttribute("units", units);
    model.addAttribute("courses", courses);
    model.addAttribute("coupons", coupons);
    return "admin/ecommerce/add-order";
  }

  @RequestMapping(value = "/admin_sales/ecommerce/createorder", method = RequestMethod.POST)
  @ResponseBody
  Order submitOrder(@Valid @RequestBody OrderSubmitRequest request) {

    Order submittedOrder = ecommerceService.submitOrderFromAdminDashboard(request);
    // OrderResponse rv = new OrderResponse(submittedOrder);
    return submittedOrder;
  }

  @RequestMapping(value = "/admin_sales/ecommerce/submitorder", method = RequestMethod.POST)
  @ResponseBody

  public OrderSummaryResponse handleCustomPaymentVerification(
      @Valid @RequestBody CustomOrderRequest request) {
    ecommerceService.emptyCart(request.getUserId());
    /*
     * if (request.getActualAmount() != 0 && request.getOrder() != null) {
     * request.getOrder().setTotalAmount(request.getActualAmount());
     * request.getOrder().setPayableAmount(request.getActualAmount()); }
     */

    Order order = ecommerceService
        .handlePaymentVerifiedOrder(request.getUserId(), request.getOrder());
    OrderSummaryResponse response = new OrderSummaryResponse(order);
    return response;
  }

  @RequestMapping(value = "/admin_sales/addresses", method = RequestMethod.POST)
  @ResponseBody
  Address addAddress(@Valid @RequestBody SaveAddressRequest request) {
    Address response = ecommerceService.addAddress(request.getUserId(), request.getAddress());
    return response;
  }

  @RequestMapping(value = "/admin_sales/users/{userId}/address", method = RequestMethod.GET)
  @ResponseBody
  AddressProjection getDefaultAddress(@PathVariable("userId") long userId) {
    List<AddressProjection> response = ecommerceService.getAddresses(userId);
    if (response != null && response.size() > 0) {
      for (AddressProjection add : response) {
        if (add.isDefaultAddress()) {
          return add;
        }
      }
    }
    return null;
  }

  @RequestMapping(value = "/admin_sales/users/{userId}/couponcode/{code}", method = RequestMethod.GET)
  @ResponseBody
  ValidateCouponResponse validateCoupon(@PathVariable("userId") long userId,
      @PathVariable("code") String code) {
    ValidateCouponResponse response = ecommerceService.validateCoupon(userId, code);
    return response;
  }

  @RequestMapping(value = "/admin_sales/users/{userId}/addresses", method = RequestMethod.GET)
  @ResponseBody
  List<AddressProjection> getUserAddresses(@PathVariable("userId") long userId) {
    List<AddressProjection> response = ecommerceService.getAddresses(userId);
    return response;
  }

  @RequestMapping(value = "/admin_sales/users/{userId}/addresses/{addressId}/makedefault", method = RequestMethod.PATCH)
  @ResponseBody
  boolean changeDefaultAddress(@PathVariable("userId") long userId,
      @PathVariable("addressId") int addressId) {
    boolean response = ecommerceService.changeDefaultAddress(userId, addressId);
    return response;
  }

  @RequestMapping(value = "/admin_sales/addresses/{addressId}", method = RequestMethod.DELETE)
  @ResponseBody
  boolean deleteAddress(@PathVariable("addressId") int addressId) {
    boolean success = ecommerceService.deleteAddress(addressId);
    return success;

  }

  @RequestMapping(value = "/admin_techops_lmsadmin/lms/units/create", method = RequestMethod.GET)
  String createUnit(Model model) {
    Collection<CourseProjection> courses = baseService.fetchCoursesNames();
    Set<String> topics = baseService.getAllExistingTopics();
    model.addAttribute("topics", topics);
    model.addAttribute("courses", courses);
    return "admin/lms/units/add-new-unit";
  }

  @RequestMapping(value = "/admin_sales/ecommerce/orders-export", method = RequestMethod.GET)
  String exportData(Model model) {
    return "admin/ecommerce/orders-export";
  }

  @RequestMapping(value = "/admin_writer_editor/answer-sheet", method = RequestMethod.GET)
  String addAnswerSheet(Model model) {

    Set<String> categories = baseService.fetchAllAnswerSheetCategories();
    model.addAttribute("categories", categories);
    return "admin/freeprep/add-answersheet";
  }

  @RequestMapping(value = "admin_writer_editor/update-announcement-header", method = RequestMethod.GET)
  String announcementHeader(Model model) {
    List<AnnouncementHeader> response = baseService.fetchAllAnnouncementHeader();
    model.addAttribute("headers", response);
    return "admin/posts/updateHeader";
  }

  @GetMapping("/admin_lmsadmin/lms/reviews")
  String adminLmsReviews(Model model) {
    return "admin/lms/reviews/create-review";
  }

  @RequestMapping(value = "/admin_sales/orders/{orderId}/ordernotes", method = RequestMethod.POST)
  @ResponseBody
  boolean addOrderNotes(@Valid @RequestBody OrderNotes newNote,
      @PathVariable("orderId") int orderId) {
    ecommerceService.addNoteToOrder(orderId, newNote);
    return true;
  }

  @RequestMapping(value = "/linktoall/{userId}", method = RequestMethod.PUT)
  void linkToAll(@PathVariable("userId") long userId) {
    ecommerceService.linkUserToAllBatches(userId);
  }

  @RequestMapping(value = "/admin_writer_editor/create-breadcrumb", method = RequestMethod.GET)
  String createBreadcrumb(Model model) {
    return "admin/posts/create-breadcrumb";
  }

  @RequestMapping(value = "/admin_writer_editor/breadcrumb", method = RequestMethod.GET)
  String fetchAllBreadcrumb(Model model) {
    List<AllBreadcrumbResponse> breadCrumbLinkedList = baseService.allBreadCrumb();
    model.addAttribute("breadcrumbs", breadCrumbLinkedList);
    return "admin/posts/breadcrumb";
  }

  @GetMapping(value = "/admin_writer_editor/quiz/all-quiz")
  String fetchAllQuiz(Model model){
    List<QuizTemplate> quizTemplates = baseService.fetchAllQuizTemplate();
    model.addAttribute("quizTemplates",quizTemplates);
    return "admin/quiztemplate/allquiz";
  }
  @GetMapping(value = "/admin_writer_editor/quiz/create-quiz")
  String createQuizTemplate(Model model){

    return "admin/quiztemplate/create-quiz-template";
  }

  @GetMapping(value = "/admin_writer_editor/quiz/{slug}/update")
  String updateQuizTemplate(@PathVariable("slug") String slug, Model model){
    QuizTemplate quizTemplate = quizTemplateRepository.findQuizTemplateBySlug(slug);
    model.addAttribute("quiztemplate",quizTemplate);
    return "admin/quiztemplate/update-quiz-template";
  }

  @GetMapping(value = "/admin_techops_lmsadmin_sales/extrainfo")
  String fetchExtraInfo(Model model){

    List<ExtraInfo> extraInfos = baseService.fetchAllExtraInfo();
    model.addAttribute("extraInfos",extraInfos);

    return "admin/ecommerce/extra-info-template";
  }

}

