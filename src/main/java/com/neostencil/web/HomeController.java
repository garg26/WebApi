package com.neostencil.web;

import com.neostencil.model.entities.Discussion;
import com.neostencil.model.entities.QuizTemplate;
import com.neostencil.responses.AllDiscussionsResponse;
import com.neostencil.responses.DiscussionDTO;
import com.neostencil.responses.NeoCashHistoryResponse;
import com.neostencil.responses.QuizLeaderBoardResponse;
import com.neostencil.responses.QuizTemplateResponse;
import com.neostencil.rss.RssFeedView;
import com.neostencil.services.LectureService;
import com.neostencil.services.NeoCashService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.neostencil.base.BaseService;
import com.neostencil.base.SearchService;
import com.neostencil.common.enums.CourseType;
import com.neostencil.dtos.CourseDTO;
import com.neostencil.dtos.InstituteDetailedDTO;
import com.neostencil.dtos.TeacherDetailsDTO;
import com.neostencil.model.entities.AllCustomCourseWithBatch;
import com.neostencil.model.entities.AuthorityName;
import com.neostencil.model.entities.BreadCrumb;
import com.neostencil.model.entities.MetaInformation;
import com.neostencil.model.entities.Post;
import com.neostencil.model.entities.Testimonial;
import com.neostencil.projections.AddressProjection;
import com.neostencil.projections.CourseProjection;
import com.neostencil.projections.TeacherProjection;
import com.neostencil.projections.UserProjection;
import com.neostencil.responses.CourseBatchResponse;
import com.neostencil.responses.ReviewResponse;
import com.neostencil.responses.ReviewStatistics;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.services.EmailService;
import com.neostencil.services.UserService;
import com.neostencil.utils.CSVUtility;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import com.neostencil.services.DiscussionService;

@Controller
public class HomeController {

  /*
   * @Autowired private CourseService courseService;
   *
   * @Autowired private TeacherService teacherService;
   *
   * @Autowired private TestimonialService testimonialService;
   *
   * @Autowired private CommentService commentService;
   *
   * @Autowired private InstituteService instituteService;
   *
   * @Autowired private PostService postService;
   */
  static String errorPage = "error";
  @Value("${jwt.header}")
  private String tokenHeader;

  @Autowired
  private BaseService baseService;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  SearchService searchService;

  @Autowired
  EmailService emailService;

  @Autowired
  LectureService lectureService;

  @Autowired
  DiscussionService discussionService;

  @Autowired
  NeoCashService neoCashService;
  
  public static Map<String, String> redirectUrlMap = CSVUtility.getRedirectionMap();

  public HomeController() {
  }

  /**
   * This method will fetch all the courses based on the search filters applied.
   */
  @RequestMapping(value = "/courses", method = RequestMethod.GET)
  public String fetchAllCourses(Model model,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size,
      @RequestParam(value = "sort", required = false) String sortBy,
      @RequestParam(value = "search", required = false) String search) {
    return "courses/courses";
  }

  /*
   * @RequestMapping(value="/courses/{cat}") public String
   * fetchCoursesCategoryWise(@PathVariable)
   */

  @RequestMapping(value = "/", method = RequestMethod.GET)
  String index(Model model) {

    // For setting courses
    /*LinkedHashMap<String, List<CourseProjection>> courses = baseService
        .fetchPopularCourseByCategory();*/

    // For setting teacherDetails
    /*List<TeacherProjection> teachers = baseService
        .fetchAllPublishTeacherForHomePage(null, null, null, null);
    if (courses == null || teachers == null) {
      return "we-are-sorry";
    }*/

    List<Testimonial> testimonials = baseService.fetchAllTestimonialOrderByPositionForHomePage();

    /*model.addAttribute("teachers", teachers);
    model.addAttribute("courses", courses);*/
    model.addAttribute("testimonials", testimonials);
    return "index";
  }

  /*
   * @RequestMapping(value = "**") String redirecUnmappedURls() { return
   * "redirect:/courses"; }
   */

  private String getCourseModelData(String slug, Model model) {
    CourseDTO course = baseService.fetchCourseBySlug(slug);
    if (course == null)
    {
      return "we-are-sorry";
    }
    CourseType courseType = null;
    if (course != null) {
      courseType = course.getCourseType();

      if (courseType != null && course.getNoOfSessions() != null) {

        if (courseType.equals(CourseType.LECTURES_ONLY)
            || courseType.equals(CourseType.ALL_INLCUSIVE)) {
          model.addAttribute("nofOfSessionText", "Lectures: " + course.getNoOfSessions());
        } else if (courseType.equals(CourseType.TEST_SERIES_ONLY)) {
          model.addAttribute("nofOfSessionText", "Tests: " + course.getNoOfSessions());
        } else {
          String noOfSessions = course.getNoOfSessions();
          String[] split = noOfSessions.split(",");

          if (split.length > 1) {
            model.addAttribute("nofOfSessionText",
                "Lectures: " + split[0] + "<br/>" + "Tests: " + split[1]);
          } else {
            model.addAttribute("nofOfSessionText", "Lectures: " + split[0]);
          }

        }
      }

      List<MetaInformation> list = course.getMetaList();
      list.size();
      for (MetaInformation m : list) {
        if (m != null) {

        }
      }

      if (course.getBreadCrumb() != null) {
        LinkedList<BreadCrumb> breadCrumbs =
            baseService.fetchBreadCrumbById(course.getBreadCrumb().getBreadcrumbId());
        model.addAttribute("breadCrumbs", breadCrumbs);
      }
      ReviewStatistics statistics = baseService.getReviewStatisticsBySlug(slug);
      List<ReviewResponse> reviews = baseService.fetchReviewsByPostName(slug, null, null);

      if (statistics.getAvg() == 0) {
        model.addAttribute("courserating", 5);
      } else {
        model.addAttribute("courserating", statistics.getAvg());
      }

      model.addAttribute("course", course);
      model.addAttribute("statistics", statistics);
      model.addAttribute("reviews", reviews);
    }
    if(course.getPrice() !=0){
      return "courses/paidCourse";
    }else{
      return "courses/freeCourse";
    }
  }

  @GetMapping
  @RequestMapping(value = "/course/{slug}")
  String course(@PathVariable("slug") String slug, Model model) {
    // fetch single course data given course ID
    /*
     * Course course = courseService.fetchCourseByID(slug); List<SingleReviewResponse> reviews =
     * commentService.fetchReviewByPostName(slug); model.addAttribute("reviews", reviews);
     * model.addAttribute("course", course);
     */

    String url = "course/" + slug;
    if (redirectUrlMap.get(url) != null) {
      return "redirect:/" + redirectUrlMap.get(url);
    }


    return getCourseModelData(slug, model);
  }

  @GetMapping
  @RequestMapping(value = "/course/jee/{slug}")
  String jeeCourse(@PathVariable("slug") String slug, Model model) {
    // fetch single course data given course ID
    /*
     * Course course = courseService.fetchCourseByID(slug); List<SingleReviewResponse> reviews =
     * commentService.fetchReviewByPostName(slug); model.addAttribute("reviews", reviews);
     * model.addAttribute("course", course);
     */

    String url = "course/jee/" + slug;
    slug = "jee/" + slug;
    if (redirectUrlMap.get(url) != null) {
      return "redirect:/" + redirectUrlMap.get(url);
    }

    return getCourseModelData(slug, model);
  }

  @GetMapping
  @RequestMapping(value = "/course/neet/{slug}")
  String neetCourse(@PathVariable("slug") String slug, Model model) {
    // fetch single course data given course ID
    /*
     * Course course = courseService.fetchCourseByID(slug); List<SingleReviewResponse> reviews =
     * commentService.fetchReviewByPostName(slug); model.addAttribute("reviews", reviews);
     * model.addAttribute("course", course);
     */

    String url = "course/neet/" + slug;
    slug = "neet/" + slug;
    if (redirectUrlMap.get(url) != null) {
      return "redirect:/" + redirectUrlMap.get(url);
    }

    return getCourseModelData(slug, model);
  }

  @RequestMapping(value = "/coursebatches/{id}", method = RequestMethod.GET)
  String fetchCourseBatchWithId(@PathVariable("id") int id, Model model) {
    CourseBatchResponse batch = baseService.fetchCourseBatchById(id);
    model.addAttribute("batch", batch);
    return "courses/course";
  }

  @RequestMapping(value = "/teachers", method = RequestMethod.GET)
  String teachers(Model model, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size,
      @RequestParam(value = "sort", required = false) String sortBy,
      @RequestParam(value = "search", required = false) String search) {

    return "teachers/teachers";
  }

  @RequestMapping(value = "/teacher/{id}", method = RequestMethod.GET)
  String teacher(@PathVariable("id") String id, Model model) {

    String url = "teacher/" + id;
    if (redirectUrlMap.get(url) != null) {
      return "redirect:/" + redirectUrlMap.get(url);
    }
    TeacherDetailsDTO teacher = baseService.fetchTeacherDetailsBySlug(id);
    if (teacher == null) {
      return "we-are-sorry";
    }
    if (teacher.getBreadCrumb() != null) {
      LinkedList<BreadCrumb> breadCrumbs = baseService
          .fetchBreadCrumbById(teacher.getBreadCrumb().getBreadcrumbId());
      model.addAttribute("breadCrumbs", breadCrumbs);
    }

    List<ReviewResponse> reviews = baseService.getTeacherTestimonial(id);

    model.addAttribute("teacher", teacher);
    model.addAttribute("reviews", reviews);

    return "teachers/teacher";
  }

  @RequestMapping(value = "/institute/{slug}", method = RequestMethod.GET)
  String institute(@PathVariable("slug") String slug, Model model) {
    InstituteDetailedDTO institute = baseService.fetchInstituteBySlug(slug);
    if (institute == null) {
      return "we-are-sorry";
    }

    if (institute.getBreadCrumb() != null) {
      LinkedList<BreadCrumb> breadCrumbs = baseService
          .fetchBreadCrumbById(institute.getBreadCrumb().getBreadcrumbId());
      model.addAttribute("breadCrumbs", breadCrumbs);

    }

    List<ReviewResponse> reviews = baseService.getInstituteTestimonial(slug);

    model.addAttribute("institute", institute);
    model.addAttribute("reviews", reviews);

    return "institutes/institute";
  }


  @RequestMapping(value = "/reset", params = {"token"}, method = RequestMethod.GET)
  String resetPassword(@RequestParam("token") String token) {

    if (userService.validatePasswordResetToken(token)) {
      return "common/resetPassword";
    } else {
      return errorPage;
    }
  }

  @RequestMapping(value = "/create-course", method = RequestMethod.GET)
  String createCourse() {
    return "cpp/createCourse";
  }

  @RequestMapping(value = "exam/{slug}", method = RequestMethod.GET)
  String ias() {
    return "exams/examSegment";
  }

  /**
   * Company pages routes
   */
  @RequestMapping(value = "/about-us", method = RequestMethod.GET)
  String about() {
    return "company/about";
  }

  @RequestMapping(value = "/we-are-sorry", method = RequestMethod.GET)
  String weAreSorry() {
    return "we-are-sorry";
  }

  @RequestMapping(value = {"/contact", "/contact-us"}, method = RequestMethod.GET)
  String contact() {
    return "company/contact";
  }

  @RequestMapping(value = "/frequently-asked-questions", method = RequestMethod.GET)
  String faqs() {
    return "company/faqs";
  }

  @GetMapping("/faqs-more")
  @RequestMapping(value = "/faqs-more", method = RequestMethod.GET)
  String faqsSeeMore() {
    return "company/faqsSeeMore";
  }

  @RequestMapping(value = "/faqs-answer", method = RequestMethod.GET)
  String faqsAnswer() {
    return "company/faqsAnswer";
  }

  @RequestMapping(value = "/terms", method = RequestMethod.GET)
  String terms() {
    return "company/terms";
  }

  @RequestMapping(value = "/privacy", method = RequestMethod.GET)
  String privacy() {
    return "company/privacy";
  }

  @RequestMapping(value = "/commonhead", method = RequestMethod.GET)
  String commonHead() {
    return "common/commonHead";
  }

  @RequestMapping(value = "/commonjs", method = RequestMethod.GET)
  String commonJS() {
    return "common/commonJS";
  }

  @RequestMapping(value = "/cart", method = RequestMethod.GET)
  String cart() {
    return "cart/cart";
  }

  @RequestMapping(value = "/checkout", method = RequestMethod.GET)
  String checkout() {
    return "cart/checkout";
  }

  /**
   * Blog home page route handler.
   */
  @RequestMapping(value = {"/blog", "/blog/{tag}"}, method = RequestMethod.GET)
  String blogHomePage(Model model, @PathVariable(value = "tag", required = false) String tag,
      @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

    AtomicInteger currentPage = new AtomicInteger(1);
    AtomicInteger pageSize = new AtomicInteger(12);

    page.ifPresent(p -> currentPage.set(p));
    size.ifPresent(s -> pageSize.set(s));

    /*
     * AllPostResponse allPostResponse = postService.getAllPost(1, 10);
     * Set<Post> posts = allPostResponse.getPosts();
     * model.addAttribute("posts", posts);
     */

    Page<Post> posts = null;
    HashSet<String> tags = null;
    if (!TextUtils.isEmpty(tag)) {
      tag = tag.replaceAll("-", " ");
      posts = baseService.fetchPublishedPostsByTag(currentPage.get() - 1, pageSize.get(), tag);
      tags = baseService.fetchOtherTagsByTag(tag.toLowerCase());
    } else {
      posts = baseService.fetchPublishedPosts(currentPage.get() - 1, pageSize.get(), null, null);
    }

    int totalPages = posts.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
          .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }

    model.addAttribute("tag", tag);
    model.addAttribute("posts", posts);
    model.addAttribute("tags", tags);
    return "blog/home";
  }

  @RequestMapping(value = {"/blog/*", "/blog/*/*"})
  String redirectBlogPages() {
    return "redirect:/blog";
  }

  /**
   * For Redirecting unmapped URLs
   */
  @RequestMapping("/testimonial/{id}")
  String getUrl(@PathVariable("id") String id) {

    String url = "/testimonial/" + id;
    if (redirectUrlMap.get(url) != null) {
      return "redirect:/" + redirectUrlMap.get(id);
    }
    return "redirect:/testimonials";
  }

  @RequestMapping(value = "/product/{slug}")
  String redirectProductURLs(@PathVariable("slug") String slug) {
    return "redirect:/course/" + slug;
  }

  @RequestMapping(value = "/courses/{slug}")
  String redirectCourseCategoryPages(@PathVariable("slug") String slug) {
    return "redirect:/courses";
  }


  /*
   * @RequestMapping(value="/members/{slug}") String
   * redirectMemberURLs(@PathVariable("slug")String slug) { //TODO:To replace
   * with the actual 404 error page return "redirect:/errorPage"; }
   */

  /**
   * For redirecting blog category pages of V1
   */
  /*
   * @GetMapping
   *
   * @RequestMapping("/{post_category}/{post_id}") String
   * redirectPosts(@PathVariable("post_category") String postCategory,
   *
   * @PathVariable("post_id") String postId) { String url = postCategory + "/"
   * + postId + "/"; if (redirectUrlMap.get(url) != null) { return
   * "redirect:/" + redirectUrlMap.get(url); } else { return
   * "redirect:/we-are-sorry"; }
   *
   * }
   */

  /**
   * Blog post page route handler.
   */
  @RequestMapping("/{post_id}")
  String getPost(@PathVariable("post_id") String post_id, Model model) {

    if (redirectUrlMap.get(post_id) != null) {
      return "redirect:/" + redirectUrlMap.get(post_id);
    }

    QuizTemplate quizTemplate = lectureService.fetchQuizTemplateBySlug(post_id);
    if(quizTemplate!=null){
      int quizId = quizTemplate.getQuiz().getQuizId();
      LinkedList<QuizLeaderBoardResponse> quizLeaderBoardResponses = lectureService
          .fetchLeaderboardbyQuizId(quizId);
      model.addAttribute("quizTemplate",quizTemplate);
//    model.addAttribute("response",quizTemplateResponse);
      model.addAttribute("leaderbard",quizLeaderBoardResponses);
      return "quizzes/quiz";
    }



    Post post = baseService.fetchPostBySlug(post_id);
    if (post == null) {
      return "we-are-sorry";
    }

    if (post.getBreadCrumb() != null) {
      LinkedList<BreadCrumb> breadCrumbs = baseService
          .fetchBreadCrumbById(post.getBreadCrumb().getBreadcrumbId());
      model.addAttribute("breadCrumbs", breadCrumbs);

    }

    String postContent = post.getText();

    String insertionString =
        "<div class=\"make-table-responsive\"><table";

    postContent = postContent.replaceAll("<table", insertionString);
    postContent = postContent.replaceAll("</table>", "</table></div>");


    int postHref = postContent.indexOf("<a href");

    while (postHref > -1) {

      int endingPosition = postContent.indexOf(">", postHref);
      String hrefAttributes = postContent.substring(postHref + 2, endingPosition);
      int checkRelExists = hrefAttributes.indexOf("rel=");
      int checkIfInternalLink = hrefAttributes.indexOf("neostencil.com");
      if (checkRelExists > -1 || checkIfInternalLink > -1) {
        postHref = postContent.indexOf("<a href", postHref + 1);
        continue;
      }

      hrefAttributes = hrefAttributes + " rel=\"nofollow\" ";
      String hrefReplacement = "<a " + hrefAttributes + " >";

      postContent = postContent
          .replace(postContent.substring(postHref, endingPosition + 1), hrefReplacement);
      postHref = postContent.indexOf("<a href", postHref + 1);
    }

    post.setText(postContent);
    model.addAttribute("post", post);

    return "blog/post";
  }

  @RequestMapping("/jee/{post_id}")
  String getJeePost(@PathVariable("post_id") String post_id, Model model) {

    post_id = "jee/"+post_id;

    if (redirectUrlMap.get(post_id) != null) {
      return "redirect:/" + redirectUrlMap.get(post_id);
    }
    QuizTemplate quizTemplate = lectureService.fetchQuizTemplateBySlug(post_id);
    if(quizTemplate!=null){
      int quizId = quizTemplate.getQuiz().getQuizId();
      LinkedList<QuizLeaderBoardResponse> quizLeaderBoardResponses = lectureService
          .fetchLeaderboardbyQuizId(quizId);
      model.addAttribute("quizTemplate",quizTemplate);
//    model.addAttribute("response",quizTemplateResponse);
      model.addAttribute("leaderbard",quizLeaderBoardResponses);
      return "quizzes/quiz";
    }


    Post post = baseService.fetchPostBySlug(post_id);
    if (post == null) {
      return "we-are-sorry";
    }

    if (post.getBreadCrumb() != null) {
      LinkedList<BreadCrumb> breadCrumbs = baseService
          .fetchBreadCrumbById(post.getBreadCrumb().getBreadcrumbId());
      model.addAttribute("breadCrumbs", breadCrumbs);

    }

    String postContent = post.getText();

    String insertionString =
        "<div class=\"make-table-responsive\"><table";

    postContent = postContent.replaceAll("<table", insertionString);
    postContent = postContent.replaceAll("</table>", "</table></div>");


    int postHref = postContent.indexOf("<a href");

    while (postHref > -1) {

      int endingPosition = postContent.indexOf(">", postHref);
      String hrefAttributes = postContent.substring(postHref + 2, endingPosition);
      int checkRelExists = hrefAttributes.indexOf("rel=");
      int checkIfInternalLink = hrefAttributes.indexOf("neostencil.com");
      if (checkRelExists > -1 || checkIfInternalLink > -1) {
        postHref = postContent.indexOf("<a href", postHref + 1);
        continue;
      }

      hrefAttributes = hrefAttributes + " rel=\"nofollow\" ";
      String hrefReplacement = "<a " + hrefAttributes + " >";

      postContent = postContent
          .replace(postContent.substring(postHref, endingPosition + 1), hrefReplacement);
      postHref = postContent.indexOf("<a href", postHref + 1);
    }

    post.setText(postContent);
    model.addAttribute("post", post);

    return "blog/post";
  }

  @RequestMapping("/neet/{post_id}")
  String getNeetPost(@PathVariable("post_id") String post_id, Model model) {

    post_id = "neet/"+post_id;

    if (redirectUrlMap.get(post_id) != null) {
      return "redirect:/" + redirectUrlMap.get(post_id);
    }

    QuizTemplate quizTemplate = lectureService.fetchQuizTemplateBySlug(post_id);
    if(quizTemplate!=null){
      int quizId = quizTemplate.getQuiz().getQuizId();
      LinkedList<QuizLeaderBoardResponse> quizLeaderBoardResponses = lectureService
          .fetchLeaderboardbyQuizId(quizId);
      model.addAttribute("quizTemplate",quizTemplate);
//    model.addAttribute("response",quizTemplateResponse);
      model.addAttribute("leaderbard",quizLeaderBoardResponses);
      return "quizzes/quiz";
    }

    Post post = baseService.fetchPostBySlug(post_id);
    if (post == null) {
      return "we-are-sorry";
    }

    if (post.getBreadCrumb() != null) {
      LinkedList<BreadCrumb> breadCrumbs = baseService
          .fetchBreadCrumbById(post.getBreadCrumb().getBreadcrumbId());
      model.addAttribute("breadCrumbs", breadCrumbs);

    }

    String postContent = post.getText();

    String insertionString =
        "<div class=\"make-table-responsive\"><table";

    postContent = postContent.replaceAll("<table", insertionString);
    postContent = postContent.replaceAll("</table>", "</table></div>");


    int postHref = postContent.indexOf("<a href");

    while (postHref > -1) {

      int endingPosition = postContent.indexOf(">", postHref);
      String hrefAttributes = postContent.substring(postHref + 2, endingPosition);
      int checkRelExists = hrefAttributes.indexOf("rel=");
      int checkIfInternalLink = hrefAttributes.indexOf("neostencil.com");
      if (checkRelExists > -1 || checkIfInternalLink > -1) {
        postHref = postContent.indexOf("<a href", postHref + 1);
        continue;
      }

      hrefAttributes = hrefAttributes + " rel=\"nofollow\" ";
      String hrefReplacement = "<a " + hrefAttributes + " >";

      postContent = postContent
          .replace(postContent.substring(postHref, endingPosition + 1), hrefReplacement);
      postHref = postContent.indexOf("<a href", postHref + 1);
    }

    post.setText(postContent);
    model.addAttribute("post", post);

    return "blog/post";
  }


  @RequestMapping(value = "/user-dashboard", method = RequestMethod.GET)
  String dashboard() {
    Set<AuthorityName> roles = jwtTokenUtil.getLoggedInUserRole();
    // Set<AuthorityName> roles =null;
    if (roles != null && (roles.contains(AuthorityName.ROLE_ADMIN) || roles
        .contains(AuthorityName.ROLE_EDITOR) || roles.contains(AuthorityName.ROLE_WRITER) || roles
        .contains(AuthorityName.ROLE_SALES)
        || roles.contains(AuthorityName.ROLE_LMS_ADMIN) || roles
        .contains(AuthorityName.ROLE_TECHOPS))) {
      return "admin/index";

    } else if (roles != null && roles.contains(AuthorityName.ROLE_INSTRUCTOR)) {
      return "teacherDashboard";
    }else if (roles != null && roles.contains(AuthorityName.ROLE_PARENT)) {
      return "parentDashboard";
    }
    else if (roles != null && (roles.contains(AuthorityName.ROLE_STUDENT) || roles
        .contains(AuthorityName.ROLE_USER))) {
      

      return "studentDashboard";
    }
    return "studentDashboard";
  }
  /*
   * @RequestMapping(value = "/user-dashboard", method = RequestMethod.GET)
   * String studentDashboard() { return "studentDashboard"; }
   */

  @RequestMapping(value = "/lms", method = RequestMethod.GET)
  String lms(Model model) {
    return "lms";
  }

  @RequestMapping(value = "/free-ias-preparation", method = RequestMethod.GET)
  String freePrepHome(Model model) {

    List<AllCustomCourseWithBatch> courses = baseService.fetchFilteredFreeCourses();

    model.addAttribute("courses", courses);
    return "freePrep/freePrep";
  }

  // @RequestMapping(value = "/freePrepHome", method = RequestMethod.GET)
  // String freePrep() {
  // return "freePrep/freePrep";
  // }

  @RequestMapping(value = "/freepreppost", method = RequestMethod.GET)
  String freePrepPost() {

    return "freePrep/freePrepPost";
  }

  @GetMapping("/all-quizzes")
  @RequestMapping(value = "/all-quizzes", method = RequestMethod.GET)
  String quizzes() {
    return "quizzes/quizzes";
  }

  @RequestMapping(value = "/all-test-series", method = RequestMethod.GET)
  String allTestSeries() {
    return "testseries/allTestSeries";
  }

  @RequestMapping(value = "/test-series", method = RequestMethod.GET)
  String singleTestSeries() {
    return "testseries/singleTestSeries";
  }

  @RequestMapping(value = "/single-quiz", method = RequestMethod.GET)
  String singleQuiz() {
    return "quizzes/quiz";
  }

  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  String profile(Model model) {

    if (jwtTokenUtil.getLoggedInUserID() <= 0) {
      return "redirect:/login";
    } else {
      List<AddressProjection> userAddress = userService.fetchAllAddressDetails();
      UserProjection userDetail = userService.fetchAllUserDetails();
      if (userDetail != null) {
        model.addAttribute("addresses", userAddress);
        model.addAttribute("user", userDetail);
      }
      return "profile";
    }
  }

  @RequestMapping(value = "/orders", method = RequestMethod.GET)
  String orders() {
    return "orders";
  }

  @RequestMapping(value = "/free-videos", method = RequestMethod.GET)
  String freeVideos() {
    return "freePrep/freeVideos";
  }

  @RequestMapping(value = "/thank-you", method = RequestMethod.GET)
  String thankyou() {
    return "cart/thankyou";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  String login() {
    return "login";
  }

  @RequestMapping(value = "/media-coverage", method = RequestMethod.GET)
  String mediaCoverage() {
    return "mediaCoverage";
  }

  @RequestMapping(value = "/quick-tour", method = RequestMethod.GET)
  String tutorials() {
    return "tutorials";
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  String search() {
    return "search";
  }

  /*
   * ;
   *
   * @GetMapping("/error") String error() { return "admin/index"; }
   */

  @RequestMapping(value = "/reviews-testimonials", method = RequestMethod.GET)
  String allTestimonials(Model model) {
    List<Testimonial> testimonials = baseService.fetchAllTestimonialOrderByPosition();
    model.addAttribute("testimonials", testimonials);
    return "testimonials/testimonials";
  }

  @RequestMapping(value = {"/robots", "/robot", "/robot.txt", "/robots.txt", "/null"})
  public void robots(HttpServletRequest request, HttpServletResponse response) {
    try {
      response.getWriter().write("User-agent: *\n");
      response.getWriter().write("Disallow: /checkout*\n");
      response.getWriter().write("Disallow: /cart*\n");
      response.getWriter().write("Disallow: /admin_writer_editor\n");
      response.getWriter().write("Disallow: /admin_sales\n");
      response.getWriter().write("Disallow: /admin_lmsadmin\n");
      response.getWriter().write("Disallow: *dashboard*\n");
      response.getWriter().write("Disallow: *?search=*\n");
      response.getWriter().write("Disallow: *?.pdf$\n");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/sitemap_index.xml", method = RequestMethod.GET)
  public void siteMapIndex(HttpServletRequest request, HttpServletResponse response) {
    try {

      String rv = getStringFromClassPath("/sitemap/sitemap_index.xml");
      response.getWriter().write(rv);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/post-sitemap.xml", method = RequestMethod.GET)
  public void postSiteMap(HttpServletRequest request, HttpServletResponse response) {
    try {

      String rv = getStringFromClassPath("/sitemap/post-sitemap.xml");
      response.getWriter().write(rv);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/course-sitemap.xml", method = RequestMethod.GET)
  public void courseSiteMap(HttpServletRequest request, HttpServletResponse response) {
    try {

      String rv = getStringFromClassPath("/sitemap/course-sitemap.xml");
      response.getWriter().write(rv);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @RequestMapping(value = "/academy-sitemap.xml", method = RequestMethod.GET)
  public void academySiteMap(HttpServletRequest request, HttpServletResponse response) {
    try {

      String rv = getStringFromClassPath("/sitemap/academy-sitemap.xml");
      response.getWriter().write(rv);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getStringFromClassPath(String classPath) {
    InputStream in = HomeController.class.getResourceAsStream(classPath);
    BufferedReader br = new BufferedReader(new InputStreamReader(in/*, "UTF-8"*/));
    StringBuilder builder = new StringBuilder();
    try {
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        builder.append(line);
        builder.append('\n');
      }
    } catch (IOException ignore) {
      ignore.printStackTrace();
      return null;
    }
    String text = builder.toString();
    return text;
  }

  @RequestMapping(value = "/facebook-redirect", method = RequestMethod.GET)
  String faceBookLoginRedirect(Model model) {
    return "facebook-redirect";
  }

  @RequestMapping(value = "/google-redirect", method = RequestMethod.GET)
  String googleLoginRedirect(Model model) {
    return "google-redirect";
  }

  @RequestMapping(value = "/demo-counselling-at-home", method = RequestMethod.GET)
  String demoSchedular() {
    return "demoSchedular";
  }


  @RequestMapping(value = "/manifest.json", method = RequestMethod.GET)
  public void getOneSignalManifestJson(HttpServletRequest request, HttpServletResponse response) {
    try {

      String rv = getStringFromClassPath("/static/js/onesignal/manifest.json");
      response.setContentType("application/json");
      response.getWriter().write(rv);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/OneSignalSDKUpdaterWorker.js", method = RequestMethod.GET)
  public void getOneSignalSDKUpdaterWorker(HttpServletRequest request, HttpServletResponse response) {
    try {

      String rv = getStringFromClassPath("/static/js/onesignal/OneSignalSDKUpdaterWorker.js");
      response.setContentType("application/javascript");
      response.getWriter().write(rv);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @RequestMapping(value = "/OneSignalSDKWorker.js", method = RequestMethod.GET)
  public void getOneSignalSDKWorker(HttpServletRequest request, HttpServletResponse response) {
    try {

      String rv = getStringFromClassPath("/static/js/onesignal/OneSignalSDKWorker.js");
      response.setContentType("application/javascript");
      response.getWriter().write(rv);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

//  @GetMapping(value = "/test/{quiz_id}")
//  @ResponseBody
//  private String getQuizTemplate(Model model,@PathVariable("quiz_id") int quizId){
//    QuizTemplateResponse quizTemplateResponse = baseService.fetchQuizTemplateByQuizId(quizId);
//    LinkedList<QuizLeaderBoardResponse> quizLeaderBoardResponses = lectureService
//        .fetchLeaderboardbyQuizId(quizId);
//    model.addAttribute("response",quizTemplateResponse);
//    model.addAttribute("leaderbard",quizLeaderBoardResponses);
//
//    return "admin/quiztemplate/create-quiz-template.html";
//  }

/*  @GetMapping
  @RequestMapping(value = "/test/{quiz_slug}", method = RequestMethod.GET)
  private String getQuizTemplate(Model model,@PathVariable("quiz_slug") String quizSlug){
//    QuizTemplateResponse quizTemplateResponse = baseService.fetchQuizTemplateByQuizId(quizId);

    QuizTemplate quizTemplate = lectureService.fetchQuizTemplateBySlug(quizSlug);

    int quizId = quizTemplate.getQuiz().getQuizId();
    LinkedList<QuizLeaderBoardResponse> quizLeaderBoardResponses = lectureService
        .fetchLeaderboardbyQuizId(quizId);
//    model.addAttribute("response",quizTemplateResponse);
    model.addAttribute("leaderbard",quizLeaderBoardResponses);
    return "quizzes/quiz";
  }*/

  @RequestMapping(value = "/neet", method = RequestMethod.GET)
  String neet() {
    return "jee/neet";
  }

  @RequestMapping(value = "/institute/motion", method = RequestMethod.GET)
  String instituteJee(Model model) {
    return "institutes/instituteJee";
  }

  @RequestMapping(value = "/jee/coaching-online", method = RequestMethod.GET)
  String jeeCoachingOnline() {
    return "jee/jeeCoachingOnline";
  }

  @RequestMapping(value = "/neet/coaching-online", method = RequestMethod.GET)
  String neetCoachingOnline() {
    return "jee/neetCoachingOnline";
  }

  @RequestMapping(value = "/jee/main", method = RequestMethod.GET)
  String jee() {
    return "jee/jee";
  }

  @RequestMapping(value = "/discussions", method = RequestMethod.GET)
  String allDiscussions() {
    return "discussion/allDiscussions";
  }

  @RequestMapping(value="/discussion/{slug}",method=RequestMethod.GET)
  String fetchDiscussionBySlug(@PathVariable("slug")String slug, Model model)
  {
    DiscussionDTO response=service.fetchDiscussionBySlug(slug);
    model.addAttribute("response",response);
    return "discussion/singleDiscussion";
  }

  @RequestMapping(value="/user-discussions",method=RequestMethod.GET)
  String userDiscussions(){return "discussion/userDiscussions";}

  @RequestMapping(value="/spin-n-win",method=RequestMethod.GET)
  String spinToWin(){return "spinToWin";}

  @Autowired
  private RssFeedView view;
  @Autowired
  DiscussionService service;

  @GetMapping("/rss")
  public View getFeed() {
    return view;
  }

  @GetMapping("/feed")
  public View getFeed2() {
    return view;
  }

}

