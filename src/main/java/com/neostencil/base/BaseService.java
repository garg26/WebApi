package com.neostencil.base;

import static org.zwobble.mammoth.internal.util.Base64Encoding.streamToBase64;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.Objects;
import com.google.api.services.storage.model.StorageObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neostencil.common.CommonUtil;
import com.neostencil.common.StorageFactory;
import com.neostencil.common.enums.AnswerSheetCategory;
import com.neostencil.common.enums.CommentType;
import com.neostencil.common.enums.CourseCategory;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.PostCategory;
import com.neostencil.common.enums.PostSubCategory;
import com.neostencil.common.enums.ProductType;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.Subjects;
import com.neostencil.common.enums.TeacherCategory;
import com.neostencil.common.enums.TeacherSubjects;
import com.neostencil.common.enums.UnitType;
import com.neostencil.common.enums.UserCourseLinkageStatus;
import com.neostencil.dtos.CommentDTO;
import com.neostencil.dtos.CourseDTO;
import com.neostencil.dtos.InstituteDetailedDTO;
import com.neostencil.dtos.TeacherDetailsDTO;
import com.neostencil.exceptions.InvalidInputException;
import com.neostencil.exceptions.ResourceNotFoundException;
import com.neostencil.model.entities.*;
import com.neostencil.model.repositories.*;
import com.neostencil.model.specifications.CourseSpecificationBuilder;
import com.neostencil.model.specifications.TeacherSpecificationBuilder;
import com.neostencil.projections.CommentProjection;
import com.neostencil.projections.CourseProjection;
import com.neostencil.projections.InstituteProjection;
import com.neostencil.projections.JWMacroProjection;
import com.neostencil.projections.LectureProjection;
import com.neostencil.projections.TeacherProjection;
import com.neostencil.projections.WowzaMacroProjection;
import com.neostencil.requests.AnnouncementHeaderRequest;
import com.neostencil.requests.BatchNameRequest;
import com.neostencil.requests.CreateBreadCrumbRequest;
import com.neostencil.requests.Filter;
import com.neostencil.requests.LoginRequest;
import com.neostencil.requests.PayLaterRequest;
import com.neostencil.requests.PostCommentsRequest;
import com.neostencil.requests.ShuffleUnitsRequest;
import com.neostencil.requests.UnitPositionAndMacroRequest;
import com.neostencil.requests.UnitBatchLinkageRequest;
import com.neostencil.requests.UserUnitRequest;
import com.neostencil.responses.AllBreadcrumbResponse;
import com.neostencil.responses.BatchNameDTO;
import com.neostencil.responses.BatchUnitResponse;
import com.neostencil.responses.CourseBatchResponse;
import com.neostencil.responses.CourseFiltersResponse;
import com.neostencil.responses.CustomBreadCrumResponse;
import com.neostencil.responses.ExtraInfoResponse;
import com.neostencil.responses.NewsletterSubscribeResponse;
import com.neostencil.responses.QuizTemplateResponse;
import com.neostencil.responses.ReviewResponse;
import com.neostencil.responses.ReviewStatistics;
import com.neostencil.responses.SignUpResponse;
import com.neostencil.responses.TeacherFilterResponse;
import com.neostencil.responses.TopicUnitResponse;
import com.neostencil.responses.UnitDTO;
import com.neostencil.responses.UserExistResponse;
import com.neostencil.responses.UserUnitResponse;
import com.neostencil.responses.quizresponse.ExportQuizStats;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.services.EcommerceService;
import com.neostencil.services.EmailService;
import com.neostencil.services.SMSService;
import com.neostencil.services.UserService;
import com.neostencil.tools.GoogleSheetToQuestion;
import com.neostencil.utils.CSVUtility;
import com.neostencil.utils.CommonAssembler;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.WordUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

@Service
public class BaseService {

  @Autowired
  TeacherDetailsRepository teacherDetailsRepository;

  @Autowired
  InstituteRepository instituteRepository;

  @Autowired
  JWMacroRepository jwMacroRepository;

  @Autowired
  WowzaMacroRepository wowzaMacroRepository;

  @Autowired
  LectureRepository lectureRepository;

  @Autowired
  CourseBatchRepository courseBatchRepository;

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  PostRepository postRepository;

  @Autowired
  TestimonialRepository testimonialRepository;

  @Autowired
  AnnouncementHeaderRepository announcementHeaderRepository;

  @Autowired
  CommentRepository commentRepository;

  @Autowired
  TestRepository testRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  CustomPostRepository customPostRepository;

  @Autowired
  PostSummaryRepository postSummaryRepository;

  @Autowired
  CourseSummaryRepository courseSummaryRepository;

  @Autowired
  AnswerSheetRepository answerSheetRepository;
  @Autowired
  NewsletterSubscriberRepository newsletterSubscriberRepository;
  @Autowired
  NewsletterSubscriberListRepository newsletterSubscriberListRepository;

  @Autowired
  ImageUrlsRepository imageUrlsRepository;

  @Autowired
  PostTagRepository postTagRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  UserCourseBatchLinkageRepository userCourseBatchLinkageRepository;

  @Autowired
  UserExamSegmentRepository userExamSegmentRepository;

  @Autowired
  UserQuizSubmissionRepository userQuizSubmissionRepository;
  /*
   * @Autowired LogRecordRepository logger;
   */
  @Autowired
  private EmailService emailService;
  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SearchService searchService;

  @Autowired
  private BreadCrumbRepository breadCrumbRepository;

  @Autowired
  private EcommerceService ecommerceService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private QuizRepository quizRepository;
  @Autowired
  private GoogleSheetToQuestion googleSheetToQuestion;

  @Autowired
  private CustomQuestionRepository customQuestionRepository;

  @Autowired
  private QuizTemplateRepository quizTemplateRepository;


  @Autowired
  public SMSService smsService;

  @Autowired
  private ExtraInfoRepository extraInfoRepository;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private UserMetaDataRespository userMetaDataRespository;


  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  public static List<StorageObject> listBucket(String bucketName)
      throws IOException, GeneralSecurityException {

    com.google.api.services.storage.Storage client = StorageFactory.getService();
    com.google.api.services.storage.Storage.Objects.List listRequest = client.objects()
        .list(bucketName);

    List<StorageObject> results = new ArrayList<StorageObject>();

    Objects objects;
    // Iterate through each page of results, and add them to our results
    // list.
    do {
      objects = listRequest.execute();
      // Add the items in this page of results to the list we'll return.
      results.addAll(objects.getItems());

      // Get the next page, in the next iteration of this loop.
      listRequest.setPageToken(objects.getNextPageToken());
    } while (null != objects.getNextPageToken());

    return results;
  }

  /**
   * For creating a single teacher
   */
  public void createTeacher(TeacherDetails newTeacher) {
    if (newTeacher != null) {
      try {
        if (newTeacher.getBreadCrumb().getBreadcrumbId() != 0) {
          BreadCrumb breadcrumbId = breadCrumbRepository
              .findByBreadcrumbId(newTeacher.getBreadCrumb().getBreadcrumbId());

          newTeacher.setBreadCrumb(breadcrumbId);
        }

        newTeacher.setPosition(99999);
        teacherDetailsRepository.save(newTeacher);

      } catch (Exception e) {
        LOGGER.error(TeacherDetails.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
        throw e;
      }
    } else {
      throw new InvalidInputException(TeacherDetails.class.getName(), null, null);
    }

  }

  /**
   * For fetching a single teacher details
   */
  public TeacherDetailsDTO fetchTeacher(int id) {
    TeacherDetailsDTO response = null;

    if (id != 0) {
      try {
        TeacherDetails td = teacherDetailsRepository.findById(id);
        response = CommonAssembler.toTeacherDetailsDTO(td);
      } catch (Exception e) {
        LOGGER.error(TeacherDetails.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));

      }
    } else {
      throw new InvalidInputException(TeacherDetails.class.getName(), null, null);
    }
    return response;
  }

  public Page<TeacherProjection> fetchAllPublishedTeachers(Integer page, Integer size,
      String search, String sortBy) {
    Page<TeacherProjection> response = null;
    JsonParser jsonParser = new JsonParser();
    Specification<TeacherDetails> spec = null;

    LinkedList<Filter> filters = new LinkedList<>();
    LinkedList<Filter> subjectFilters = new LinkedList<>();
    try {
      Pageable pageable = null;
      Sort sort = null;

      if (search != null && !search.isEmpty()) {
        JsonObject jsonObject = (JsonObject) jsonParser.parse(search);

        if (jsonObject != null) {

          JsonArray jsonArray = (JsonArray) jsonObject.get("filters");

          for (int i = 0; i < jsonArray.size(); i++) {
            Filter filter = new Filter();
            filter.setFilterName(
                ((JsonObject) jsonArray.get(i)).get("filterName").toString().replaceAll("\"", ""));
            filter.setValue(
                ((JsonObject) jsonArray.get(i)).get("value").toString().replaceAll("\"", ""));
            if (filter.getFilterName().equalsIgnoreCase("subjects")) {
              subjectFilters.add(filter);
            } else {
              filters.add(filter);
            }
          }

          filters.add(Filter.addDeafultFilter());

        }
        if (filters != null && filters.size() > 0) {
          TeacherSpecificationBuilder builder = new TeacherSpecificationBuilder();
          spec = builder.build(filters, subjectFilters);

        }

      }
      if (sortBy != null && !sortBy.isEmpty()) {

        switch (sortBy) {
          case "atoz":
            sort = new Sort(Direction.ASC, new String[]{"teacherName"});
            break;
          case "ztoa":
            sort = new Sort(Direction.DESC, new String[]{"teacherName"});
            break;
          case "popular":
            sort = new Sort(Direction.ASC, "position");
            break;
        }
      }

      if (spec != null) {
        if (sort != null && page != null && size != null) {
          pageable = PageRequest.of(page, size, sort);
          response = teacherDetailsRepository.findAll(spec, TeacherProjection.class, pageable);

        } else {
          pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
          response = teacherDetailsRepository.findAll(spec, TeacherProjection.class, pageable);

        }
      }

    } catch (Exception e) {

      LOGGER.error(TeacherDetails.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public List<TeacherProjection> fetchAllPublishTeacherForHomePage(Integer page, Integer size,
      String search,
      String sortBy) {

    List<TeacherProjection> response = null;

    try {
      Pageable pageable = null;
      Sort sort = null;
      if (sortBy != null && !sortBy.isEmpty()) {

        switch (sortBy) {
          case "atoz":
            sort = new Sort(Direction.ASC, new String[]{"teacherName"});
            break;
          case "ztoa":
            sort = new Sort(Direction.DESC, new String[]{"teacherName"});
            break;
        }
      }
      if (page == null && size == null && sortBy == null) {
        response = teacherDetailsRepository
            .findAllProjectedByStatusAndPositionIsGreaterThanOrderByPosition(PublishStatus.publish,
                0);
      } else if (sort != null && page != null && size != null) {
        pageable = PageRequest.of(page, size, sort);
        Page<TeacherProjection> tPage = teacherDetailsRepository
            .findPagedProjectedByStatusAndPositionIsGreaterThanOrderByPosition(
                PublishStatus.publish,
                pageable, 0);
        response = tPage.getContent();
      } else {
        pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
        Page<TeacherProjection> tPage = teacherDetailsRepository
            .findPagedProjectedByStatusAndPositionIsGreaterThanOrderByPosition(
                PublishStatus.publish,
                pageable, 0);
        response = tPage.getContent();
      }
    } catch (Exception e) {

      LOGGER.error(TeacherDetails.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public List<TeacherProjection> fetchAllTeachers() {
    List<TeacherProjection> response = null;

    try {

      response = teacherDetailsRepository.findAllProjectedBy();

    } catch (Exception e) {

      LOGGER.error(TeacherDetails.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public List<InstituteProjection> fetchAllInstitutes() {
    List<InstituteProjection> response = null;

    try {

      response = instituteRepository.findAllProjectedBy();

    } catch (Exception e) {

      LOGGER.error(Institute.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public void updateTeacher(int id, TeacherDetails updatedTeacher) {

    if (updatedTeacher != null) {
      try {
        updatedTeacher.setId(id);
        teacherDetailsRepository.save(updatedTeacher);
      } catch (Exception e) {
        LOGGER.error(Institute.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(TeacherDetails.class.getName(), null, null);
    }
  }

  public TeacherDetailsDTO fetchTeacherDetailsBySlug(String slug) {
    TeacherDetailsDTO response = null;
    TeacherDetails teacherDetails = null;
    if (slug != null && !slug.isEmpty()) {
      teacherDetails = teacherDetailsRepository.findBySlug(slug);
      if (teacherDetails == null) {
        throw new ResourceNotFoundException(TeacherDetails.class.getName(), "slug", slug);
      }
      response = CommonAssembler.toTeacherDetailsDTO(teacherDetails);

    } else {
      throw new InvalidInputException(TeacherDetails.class.getName(), "slug", slug);
    }
    return response;
  }

  public void createInstitute(Institute newInstitute) {

    if (newInstitute != null) {
      try {

        if (newInstitute.getBreadCrumb().getBreadcrumbId() != 0) {
          BreadCrumb breadcrumbId = breadCrumbRepository
              .findByBreadcrumbId(newInstitute.getBreadCrumb().getBreadcrumbId());

          newInstitute.setBreadCrumb(breadcrumbId);
        }
        instituteRepository.save(newInstitute);
      } catch (Exception e) {
        LOGGER.error(Institute.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Institute.class.getName(), null, null);
    }

  }

  public InstituteDetailedDTO fetchInstituteById(int id) {
    InstituteDetailedDTO response = null;
    Institute institute = null;
    if (id != 0) {
      try {
        institute = instituteRepository.findById(id);
        response = CommonAssembler.toInstituteDetailedDTO(institute);
      } catch (Exception e) {
        LOGGER.error(Institute.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Institute.class.getName(), null, null);
    }
    return response;
  }

  public InstituteDetailedDTO fetchInstituteBySlug(String slug) {
    InstituteDetailedDTO response = null;
    Institute institute = null;
    if (slug != null) {
      try {
        institute = instituteRepository.findByInstituteSlug(slug);

        response = CommonAssembler.toInstituteDetailedDTO(institute);
      } catch (Exception e) {

        LOGGER.error(Institute.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Institute.class.getName(), null, null);
    }
    return response;
  }

  public List<Institute> fetchAllInstitutes(Integer page, Integer size, String search,
      String sortBy) {
    List<Institute> response = null;

    try {
      Pageable pageable = null;
      Sort sort = null;
      if (sortBy != null && !sortBy.isEmpty()) {

        switch (sortBy) {
          case "atoz":

            sort = new Sort(Direction.ASC, new String[]{"name"});
            break;
          case "ztoa":
            sort = new Sort(Direction.DESC, new String[]{"name"});
            break;
        }
      }
      if (page == null && size == null && sortBy == null) {
        return response = instituteRepository.findAll();
      } else if (sort != null && page != null && size != null) {
        pageable = PageRequest.of(page, size, sort);
      } else {
        pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
      }

      Page<Institute> iPage = instituteRepository.findAll(pageable);
      response = iPage.getContent();
    } catch (Exception e) {

      LOGGER.error(Institute.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  /**
   * Following are the CRUD APIs for Unit and its Macros
   */

  public void updateInstitute(int id, Institute updateInstitute) {

    if (updateInstitute != null) {
      try {

        updateInstitute.setId(id);
        instituteRepository.save(updateInstitute);

      } catch (Exception e) {
        LOGGER.error(Institute.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Institute.class.getName(), null, null);
    }
  }

  /**
   * CRUD for JWMacro
   */

  public void createJWMacro(JWMacro newMacro) {
    if (newMacro != null) {
      try {
        jwMacroRepository.save(newMacro);
      } catch (Exception e) {
        LOGGER.error(JWMacro.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(JWMacro.class.getName(), null, null);
    }

  }

  public JWMacroProjection fetchMacroByName(String name) {
    JWMacroProjection response = null;
    if (name != null) {
      try {
        response = jwMacroRepository.findAllProjectedByName(name);
      } catch (Exception e) {
        LOGGER.error(JWMacro.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(JWMacro.class.getName(), null, null);
    }
    return response;
  }

  public List<JWMacroProjection> fetchAllJWMacros() {
    List<JWMacroProjection> response = null;

    try {
      response = jwMacroRepository.findAllProjectedBy();
    } catch (Exception e) {
      LOGGER.error(JWMacro.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public void updateJWMacro(String name, JWMacro updatedMacro) {

    if (updatedMacro != null) {
      try {
        JWMacro temp = jwMacroRepository.findByName(name);

        if (temp != null) {

          updatedMacro.setName(name);
          jwMacroRepository.save(updatedMacro);
        } else {
          throw new ResourceNotFoundException(JWMacro.class.getName(), "id", name);
        }
      } catch (Exception e) {
        LOGGER.error(JWMacro.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(JWMacro.class.getName(), null, null);
    }
  }

  /**
   * CRUD for Wowza Macro
   */

  public void createWowzaMacro(WowzaMacro newMacro) {
    if (newMacro != null) {
      try {
        wowzaMacroRepository.save(newMacro);
      } catch (Exception e) {
        LOGGER.error(WowzaMacro.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(WowzaMacro.class.getName(), null, null);
    }

  }

  public WowzaMacroProjection fetchWowzaMacroByName(String name) {
    WowzaMacroProjection response = null;
    if (name != null) {
      try {
        response = wowzaMacroRepository.findAllProjectedByName(name);
      } catch (Exception e) {

        LOGGER.error(WowzaMacro.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(WowzaMacro.class.getName(), null, null);
    }
    return response;
  }

  public List<WowzaMacroProjection> fetchAllWowzaMacros() {
    List<WowzaMacroProjection> response = null;

    try {
      response = wowzaMacroRepository.findAllProjectedBy();
    } catch (Exception e) {

      LOGGER.error(WowzaMacro.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public void updateWowzaMacro(String name, WowzaMacro updatedMacro) {
    if (updatedMacro != null) {
      try {
        WowzaMacro temp = wowzaMacroRepository.findByName(name);

        if (temp != null) {

          updatedMacro.setName(name);
          wowzaMacroRepository.save(updatedMacro);
        } else {
          throw new ResourceNotFoundException(WowzaMacro.class.getName(), "id", name);
        }
      } catch (Exception e) {
        LOGGER.error(WowzaMacro.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(WowzaMacro.class.getName(), null, null);
    }
  }

  /**
   * CRUD for Lectures
   */

  public Lecture createLecture(Lecture newLecture) {
    Lecture response = null;
    if (newLecture != null) {
      try {
        response = lectureRepository.save(newLecture);
      } catch (Exception e) {
        LOGGER.error(Lecture.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Lecture.class.getName(), null, null);
    }

    return response;

  }

  public Lecture fetchLectureById(int id) {
    Lecture response = null;
    if (id != 0) {
      try {
        response = lectureRepository.findById(id);
      } catch (Exception e) {

        LOGGER.error(Lecture.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Lecture.class.getName(), null, null);
    }
    return response;
  }

  public List<LectureProjection> fetchAllLectures() {
    List<LectureProjection> response = null;

    try {
      response = lectureRepository.findAllProjectedBy();
    } catch (Exception e) {

      LOGGER.error(Lecture.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public Lecture updateLecture(int id, Lecture updatedLecture) {
    Lecture response = null;
    if (updatedLecture != null && id != 0) {
      try {
        Lecture temp = lectureRepository.findById(id);

        if (temp != null) {
          updatedLecture.setCreatedAt(temp.getCreatedAt());
          updatedLecture.setId(id);
          updatedLecture.setCourseBatch(temp.getCourseBatch());
          response = lectureRepository.save(updatedLecture);
        } else {
          throw new ResourceNotFoundException(Lecture.class.getName(), "id", id);
        }
      } catch (Exception e) {
        LOGGER.error(Lecture.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Lecture.class.getName(), null, null);
    }

    return response;
  }

  @Override
  public String toString() {
    return super.toString();
  }

  /**
   * For playing lectures @throws
   */

  private Product addProductForACourseBatch(CourseBatch batch) {
    Product p = new Product();
    try {
      p.setCommodityId(batch.getId());
      p.setType(ProductType.COURSE);
      p.setPrice(batch.getSalePrice());
      p.setRegularPrice(batch.getRegularPrice());
      p.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
      p.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

      if (batch.getCourse() != null) {
        p.setImageUrl(batch.getCourse().getCourseImageUrl());
        p.setProductSlug(batch.getCourse().getCourseOldSlug());
        p.setProductTitle(batch.getCourse().getCourseTitle() + "|" + batch.getBatchName());
      }
      p = productRepository.save(p);
    } catch (Exception e) {
      LOGGER.error(Product.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return p;
  }

  /**
   * CRUD CourseBatch
   */
  public void createCourseBatch(CourseBatch newBatch) {
    if (newBatch != null) {
      try {
        int courseId = newBatch.getCourse().getId();
        if (courseId != 0) {
          Course course = courseRepository.findById(courseId);

          if (course != null) {
            newBatch.setCourse(course);
            courseBatchRepository.save(newBatch);

            addProductForACourseBatch(newBatch);

            // Updating the price of course and course summary
            // entities
            // Course updatedCourse = fetchCourseById(courseId);
            Set<CourseBatch> batches = course.getBatches();
            batches.add(newBatch);
            course.setBatches(batches);
            CourseSummary cs = courseSummaryRepository.findByCourseSummaryId(courseId);
            if (course != null && course.getBatches() != null && course.getBatches().size() > 0) {
              Double price = minPriceCourseCalculate(course.getBatches());
              course.setPrice(price);
              cs.setPrice(price);

              courseRepository.save(course);
              courseSummaryRepository.save(cs);
            }
          }
        }
      } catch (Exception e) {
        LOGGER.error(CourseBatch.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(CourseBatch.class.getName(), null, null);
    }

  }

  public CourseBatchResponse fetchCourseBatchById(int id) {
    CourseBatchResponse response = null;
    if (id != 0) {
      try {
        CourseBatch courseBatch = courseBatchRepository.findById(id);
        if (courseBatch != null) {
          response = new CourseBatchResponse();
          response.setId(courseBatch.getId());
          response.setDisplaySize(courseBatch.getDisplaySize());
          response.setBatchName(courseBatch.getBatchName());
          response.setClassTiming(courseBatch.getClassTiming());
          response.setDiscount(courseBatch.getDiscount());
          response.setDuration(courseBatch.getDuration());
          response.setEndDate(courseBatch.getEndDate());
          response.setStartDate(courseBatch.getStartDate());
          response.setEndDate(courseBatch.getEndDate());
          response.setEndTime(courseBatch.getEndTime());
          response.setValidity(courseBatch.getValidity());
          response.setStatus(courseBatch.getStatus());
          response.setSalePrice(courseBatch.getSalePrice());
          response.setRegularPrice(courseBatch.getRegularPrice());
          response.setNoOfAvailableSeats(courseBatch.getNoOfAvailableSeats());
          response.setNoOfSession(courseBatch.getNoOfSession());
          response.setValidityDisplay(courseBatch.getValidityDisplay());

          if (courseBatch.getUnits() != null && courseBatch.getUnits().size() > 0) {
            Set<Unit> unitList = new LinkedHashSet<Unit>();
            for (Unit u : courseBatch.getUnits()) {
              if (u != null) {
                unitList.add(u);
              }
            }

            response.setUnits(unitList);
          }

        }
      } catch (Exception e) {

        LOGGER.error(CourseBatch.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(CourseBatch.class.getName(), null, null);
    }
    return response;
  }

  public CourseBatch fetchBatchById(int batchId) {
    CourseBatch response = null;
    try {
      if (batchId != 0) {
        response = courseBatchRepository.findById(batchId);
      }
    } catch (Exception e) {
      LOGGER.error(CourseBatch.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  /**
   * Fetch course batch for public facing website(with published units only)
   */
  public CourseBatchResponse fetchCourseBatchByIdPublishedOnly(int id) {
    CourseBatchResponse response = null;
    if (id != 0) {
      try {
        CourseBatch courseBatch = courseBatchRepository.findById(id);
        if (courseBatch != null) {
          response = new CourseBatchResponse();
          response.setId(courseBatch.getId());
          response.setDisplaySize(courseBatch.getDisplaySize());
          response.setBatchName(courseBatch.getBatchName());
          response.setClassTiming(courseBatch.getClassTiming());
          response.setDiscount(courseBatch.getDiscount());
          response.setDuration(courseBatch.getDuration());
          response.setEndDate(courseBatch.getEndDate());
          response.setStartDate(courseBatch.getStartDate());
          response.setEndDate(courseBatch.getEndDate());
          response.setEndTime(courseBatch.getEndTime());
          response.setValidity(courseBatch.getValidity());
          response.setStatus(courseBatch.getStatus());
          response.setSalePrice(courseBatch.getSalePrice());
          response.setRegularPrice(courseBatch.getRegularPrice());
          response.setNoOfAvailableSeats(courseBatch.getNoOfAvailableSeats());
          response.setNoOfSession(courseBatch.getNoOfSession());
          response.setValidityDisplay(courseBatch.getValidityDisplay());

          if (courseBatch.getUnits() != null && courseBatch.getUnits().size() > 0) {
            Set<Unit> unitList = new LinkedHashSet<Unit>();
            for (Unit u : courseBatch.getUnits()) {
              if (u != null && u.getStatus() != null && u.getStatus()
                  .equals(PublishStatus.publish)) {
                unitList.add(u);
              }
            }

            response.setUnits(unitList);
          }

        }
      } catch (Exception e) {

        LOGGER.error(CourseBatch.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(CourseBatch.class.getName(), null, null);
    }
    return response;
  }

  /*
   * public List<CourseBatch> fetchAllCourseBatches(Integer page, Integer
   * size, String search) { List<CourseBatch> response = null;
   *
   * try { Pageable pageable = null;
   *
   * if (page == null && size == null) { response =
   * courseBatchRepository.findAll(); } else if (page != null && size != null)
   * { pageable = PageRequest.of(page, size); } else { pageable =
   * PageRequest.of((page != null ? page : 0), (size != null ? size : 0)); }
   *
   * Page<CourseBatch> iPage = courseBatchRepository.findAll(pageable);
   * response = iPage.getContent(); } catch (Exception e) {
   *
   * throw new GeneralException(e.getMessage(), CourseBatch.class.getName());
   * }
   *
   * return response; }
   */
  public void updateCourseBatch(int id, CourseBatch updatedBatch) {
    if (updatedBatch != null && id != 0) {
      try {
        CourseBatch temp = courseBatchRepository.findById(id);

        if (temp != null) {

          updatedBatch.setUserCourseBatchLinkages(temp.getUserCourseBatchLinkages());
          updatedBatch.setId(id);
          updatedBatch.setUnits(temp.getUnits());
          updatedBatch.setCreatedAt(temp.getCreatedAt());
          updatedBatch.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
          CourseBatch courseBatch = courseBatchRepository.save(updatedBatch);

          if (courseBatch != null) {
            updateCourseBatchProduct(courseBatch, ProductType.COURSE);
          }

          if (temp.getCourse() != null && temp.getCourse().getId() != 0) {
            Course course = courseRepository.findById(temp.getCourse().getId());

            if (course != null) {
              CourseSummary cs = courseSummaryRepository.findByCourseSummaryId(course.getId());

              if (course != null && course.getBatches() != null && course.getBatches().size() > 0) {
                Double price = minPriceCourseCalculate(course.getBatches());
                course.setPrice(price);
                cs.setPrice(price);
                courseRepository.save(course);
                courseSummaryRepository.save(cs);
              }
            }

          }

        } else {

          throw new ResourceNotFoundException(CourseBatch.class.getName(), "id", id);
        }
      } catch (Exception e) {
        LOGGER.error(CourseBatch.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(CourseBatch.class.getName(), null, null);
    }
  }

  void updateCourseProduct(Course course, ProductType type) {

    if (course != null && course.getBatches() != null && course.getBatches().size() > 0) {
      for (CourseBatch cb : course.getBatches()) {
        Product pr = productRepository.findByCommodityIdAndType(cb.getId(), ProductType.COURSE);
        boolean updateProduct = false;
        if (pr != null) {
          if (!pr.getImageUrl().equals(course.getCourseImageUrl())) {
            pr.setImageUrl(course.getCourseImageUrl());
            updateProduct = true;
          }
          if (!pr.getProductSlug().equals(course.getCourseOldSlug())) {
            pr.setProductSlug(course.getCourseOldSlug());
            updateProduct = true;
          }
          if (!pr.getProductTitle().equals(course.getCourseTitle() + "|" + cb.getBatchName())) {
            pr.setProductTitle(course.getCourseTitle() + "|" + cb.getBatchName());
            updateProduct = true;
          }

          if (updateProduct) {
            productRepository.saveAndFlush(pr);
          }
        }

      }
    }
  }

  void updateCourseBatchProduct(CourseBatch cb, ProductType type) {
    // For updating corresponding product
    Product pr = productRepository.findByCommodityIdAndType(cb.getId(), ProductType.COURSE);

    if (pr != null) {
      if (pr.getPrice() != cb.getSalePrice()) {
        pr.setPrice(cb.getSalePrice());
        pr.setRegularPrice(cb.getRegularPrice());
        productRepository.saveAndFlush(pr);
      }

    }
  }

  /**
   * CRUD for Course
   */

  public void createCourse(Course newCourse) {
    Course response = null;
    if (newCourse != null) {
      try {

        if (newCourse.getBreadCrumb().getBreadcrumbId() != 0) {
          BreadCrumb breadcrumbId = breadCrumbRepository
              .findByBreadcrumbId(newCourse.getBreadCrumb().getBreadcrumbId());

          newCourse.setBreadCrumb(breadcrumbId);
        }

        newCourse.setPosition(99999);
        newCourse.setDemoUnitID(0);
        response = courseRepository.save(newCourse);

        if (response != null) {
          createOrUpdateCourseSummary(response);
        }
      } catch (Exception e) {
        LOGGER.error(CourseBatch.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
        throw new InvalidInputException(Course.class.getName(), null, null);

      }
    } else {
      throw new InvalidInputException(Course.class.getName(), null, null);
    }
  }

  private void createOrUpdateCourseSummary(Course response) {
    try {
      CourseSummary cs = new CourseSummary();

      cs.setCourseImageUrl(response.getCourseImageUrl());
      cs.setCourseRating(response.getCourseRating());
      cs.setCourseSlug(response.getCourseOldSlug());
      cs.setCourseTitle(response.getCourseTitle());
      if (response.getInstitute() != null) {
        Institute institute = instituteRepository.findById(response.getInstitute().getId());
        cs.setInstituteSlug(institute.getInstituteSlug());
        cs.setInstituteName(institute.getName());
      }

      if (response.getInstructors() != null && response.getInstructors().size() > 0) {
        Iterator<TeacherDetails> it = response.getInstructors().iterator();
        if (it.hasNext()) {
          TeacherDetails td = it.next();
          TeacherDetails teacherDetails = teacherDetailsRepository.findById(td.getId());
          cs.setInstructorSlug(teacherDetails.getSlug());
          cs.setInstructorName(teacherDetails.getTeacherName());
        }
      }
      cs.setNoOfReviewers(response.getNoOfReviewers());
      cs.setPrice(response.getPrice());
      cs.setStartDate(response.getStartDate());
      cs.setStudentsEnrolled(response.getStudentsEnrolled());
      cs.setStatus(response.getStatus());
      cs.setCourseSummaryId(response.getId());
      courseSummaryRepository.save(cs);
    } catch (Exception e) {
      LOGGER.error(CourseSummary.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  public CourseDTO fetchCourseById(int id) {
    CourseDTO response = null;
    Course course = courseRepository.findById(id);
    if (course == null) {
      return null;
    }
    response = CommonAssembler.toCourseDTO(course);
    int demoUnitID = getDemoUnit(course);
    response.setDemoUnitID(demoUnitID);
    return response;
  }

  public Course fetchAllCourseBatchBySlug(String slug) {
    Course response = null;
    if (slug != null && !slug.isEmpty()) {
      try {
        response = courseRepository.findByCourseOldSlug(slug);

      } catch (Exception e) {

        LOGGER.error(Course.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Course.class.getName(), null, null);
    }
    return response;
  }

  public CourseDTO fetchCourseBySlug(String slug) {
    CourseDTO response = null;
    Course course = courseRepository.findByCourseOldSlug(slug);
    if (course == null) {
      return null;
    }

    response = CommonAssembler.toCourseDTO(course);
    int demoUnitID = getDemoUnit(course);
    response.setDemoUnitID(demoUnitID);
    return response;
  }

  /**
   * For fetching courses with any filter.
   */

  public List<AllCustomCourseWithBatch> fetchFilteredFreeCourses() {
    List<AllCustomCourseWithBatch> response = null;
    try {

      response = courseRepository
          .findAllProjectedByCourseExamSegmentAndPriceAndStatus(ExamSegmentTypes.IAS, 0,
              PublishStatus.publish);
    } catch (Exception e) {
      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public Page<CourseProjection> fetchAllPublishCourses(Integer page, Integer size, String search,
      String sortBy) {
    Page<CourseProjection> response = null;
    JsonParser jsonParser = new JsonParser();
    Specification<Course> spec = null;
    LinkedList<Filter> subjectFilters = new LinkedList<>();
    LinkedList<Filter> subCategoryFilters = new LinkedList<>();
    LinkedList<Filter> examSegmentFilters = new LinkedList<>();
    LinkedList<Filter> categoryFilters = new LinkedList<>();
    LinkedList<Filter> otherFilters = new LinkedList<>();

    try {
      Pageable pageable = null;
      Sort sort = null;
      if (search != null && !search.isEmpty()) {
        JsonObject jsonObject = (JsonObject) jsonParser.parse(search);
        if (jsonObject != null) {

          JsonArray jsonArray = (JsonArray) jsonObject.get("filters");

          for (int i = 0; i < jsonArray.size(); i++) {
            Filter filter = new Filter();
            filter.setFilterName(
                ((JsonObject) jsonArray.get(i)).get("filterName").toString().replaceAll("\"", ""));
            filter.setValue(
                ((JsonObject) jsonArray.get(i)).get("value").toString().replaceAll("\"", ""));

            if (filter.getFilterName().equalsIgnoreCase("courseSubject")) {
              subjectFilters.add(filter);
            } else if (filter.getFilterName().equalsIgnoreCase("courseSubCategory")) {
              subCategoryFilters.add(filter);
              } else if (filter.getFilterName().equalsIgnoreCase("courseExamSegment")) {
              examSegmentFilters.add(filter);
            } else if (filter.getFilterName().equalsIgnoreCase("courseCategory")) {

              if (filter.getValue().equalsIgnoreCase("current affairs")) {
                Filter currentAffairsFilter = new Filter();
                currentAffairsFilter.setFilterName("courseSubject");
                currentAffairsFilter.setValue("Current Affairs");
                subjectFilters.add(currentAffairsFilter);
              } else {
                categoryFilters.add(filter);
              }

            } else {
              otherFilters.add(filter);
            }
          }
          otherFilters.add(Filter.addDeafultFilter());

        }
        if (otherFilters != null && otherFilters.size() > 0) {
          /*
           * if (sortBy.equalsIgnoreCase("popular")) { Filter filter =
           * new Filter(); filter.setFilterName("popular");
           * filter.setValue("true"); filters.add(filter); }
           */
          CourseSpecificationBuilder builder = new CourseSpecificationBuilder();
          spec = builder.build(otherFilters, subjectFilters, subCategoryFilters, examSegmentFilters,
              categoryFilters);

        }

      }
      if (sortBy != null && !sortBy.isEmpty()) {

        switch (sortBy) {
          case "atoz":
            sort = new Sort(Direction.ASC, new String[]{"courseTitle"});
            break;
          case "ztoa":
            sort = new Sort(Direction.DESC, new String[]{"courseTitle"});
            break;
          case "popular":
            sort = new Sort(Direction.ASC, "position");
            break;
        }
      }

      if (spec != null) {
        if (page != null && size != null) {

          if (sort != null) {
            pageable = PageRequest.of(page, size, sort);
            response = courseRepository.findAll(spec, CourseProjection.class, pageable);
          } else {
            pageable = PageRequest.of(page, size);
            response = courseRepository.findAll(spec, CourseProjection.class, pageable);
          }

        } else {
          pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
          response = courseRepository.findAll(spec, CourseProjection.class, pageable);

        }
      }

    } catch (Exception e) {
      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public List<CourseProjection> fetchAllCourses() {
    List<CourseProjection> response = null;
    try {
      response = courseRepository.findAllProjectedBy();
    } catch (Exception e) {

      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public List<CourseProjection> fetchAllPublishCourseWithoutPagination() {
    List<CourseProjection> response = null;
    try {

      response = courseRepository.findAllProjectedByStatus(PublishStatus.publish);

    } catch (Exception e) {

      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public Double minPriceCourseCalculate(Set<CourseBatch> batches) {
    CourseBatch minPriceBatch = null;
    if (batches != null && batches.size() > 0) {
      minPriceBatch = batches.stream().min(Comparator.comparing(CourseBatch::getSalePrice))
          .orElseThrow(NoSuchElementException::new);
      return minPriceBatch.getSalePrice();
    } else {
      return 0.0;
    }

  }

  /*
   * public CourseFiltersResponse getCourseFilters() {
   *
   * }
   */

  public void updateCourse(int id, Course updatedCourse) {
    Course response = null;
    if (updatedCourse != null && id != 0) {
      try {
        Course temp = courseRepository.findById(id);
        updatedCourse.setId(id);

        if (temp != null) {
          updatedCourse.setId(id);
          if (updatedCourse.getBreadCrumb() != null
              && updatedCourse.getBreadCrumb().getBreadcrumbId() != 0) {
            BreadCrumb breadCrumb = breadCrumbRepository
                .findByBreadcrumbId(updatedCourse.getBreadCrumb().getBreadcrumbId());
            updatedCourse.setBreadCrumb(breadCrumb);

          }

          if (temp.getRelatedCourses() != null && temp.getRelatedCourses().size() > 0) {
            updatedCourse.setRelatedCourses(temp.getRelatedCourses());
          }

          if (temp.getBoughtTogetherCourses() != null
              && temp.getBoughtTogetherCourses().size() > 0) {
            updatedCourse.setBoughtTogetherCourses(temp.getBoughtTogetherCourses());
          }

          updatedCourse.setPosition(temp.getPosition());
          updatedCourse.setCreatedAt(temp.getCreatedAt());
          updatedCourse.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
          updatedCourse.setPrice(temp.getPrice());

          response = courseRepository.save(updatedCourse);
          if (response != null) {
            createOrUpdateCourseSummary(response);
          }

        } else {
          throw new ResourceNotFoundException(Course.class.getName(), "id", id);
        }
      } catch (Exception e) {
        LOGGER.error(Course.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Course.class.getName(), null, null);
    }
  }

  public CourseFiltersResponse getCourseFilters(String examSegment, String category) {
    CourseFiltersResponse response = new CourseFiltersResponse();
    try {
      if (examSegment != null && !examSegment.isEmpty() && category != null && !category
          .isEmpty()) {

        LinkedHashMap<String, LinkedList<LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>>>> filters = new LinkedHashMap<>();

        if (examSegment.equalsIgnoreCase("all") && category.equalsIgnoreCase("all")) {

          for (ExamSegmentTypes examSegmentTypes : ExamSegmentTypes.values()) {

            if (!examSegment.equals(ExamSegmentTypes.IES_GATE)) {
              LinkedList<LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>>> categoryList = new LinkedList<>();
              LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>> categoryHashMap = new LinkedHashMap<>();

              for (CourseCategory courseCategory : CourseCategory.values()) {

                if (examSegmentTypes.jsonValue
                    .equalsIgnoreCase(courseCategory.getExamSegmentType().jsonValue)) {

                  categoryHashMap.put(courseCategory.jsonValue, null);
                }

              }
              categoryList.add(categoryHashMap);
              filters.put(examSegmentTypes.jsonValue, categoryList);
            }

          }

          response.setFilter(filters);
        } else if (category.equalsIgnoreCase("all")) {

          LinkedList<LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>>> categoryList = new LinkedList<>();
          LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>> categoryHashMap = new LinkedHashMap<>();

          for (ExamSegmentTypes examSegmentTypes : ExamSegmentTypes.values()) {

            if (!examSegmentTypes.jsonValue.equalsIgnoreCase(examSegment)
                && !examSegmentTypes.equals(ExamSegmentTypes.IES_GATE)) {
              filters.put(examSegmentTypes.jsonValue, null);
            }
          }

          for (CourseCategory courseCategory : CourseCategory.values()) {

            if (examSegment.equalsIgnoreCase(courseCategory.getExamSegmentType().jsonValue)) {

              categoryHashMap.put(courseCategory.jsonValue, null);
            }
          }

          categoryList.add(categoryHashMap);
          filters.put(ExamSegmentTypes.fromString(examSegment).jsonValue, categoryList);

          response.setFilter(filters);
        } else {

          LinkedList<LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>>> categoryList = new LinkedList<>();
          LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>> categoryHashMap = new LinkedHashMap<>();

          for (ExamSegmentTypes examSegmentTypes : ExamSegmentTypes.values()) {

            if (!examSegmentTypes.jsonValue.equalsIgnoreCase(examSegment)
                && !examSegmentTypes.equals(ExamSegmentTypes.IES_GATE)) {
              filters.put(examSegmentTypes.jsonValue, null);
            }
          }

          for (CourseCategory courseCategory : CourseCategory.values()) {
            if (examSegment.equalsIgnoreCase(courseCategory.getExamSegmentType().jsonValue)
                && !category.equalsIgnoreCase(courseCategory.jsonValue)) {

              categoryHashMap.put(courseCategory.jsonValue, null);
            }
          }
          // LinkedList<String> mediumList = new LinkedList<String>();
          // mediumList.add("English");
          // mediumList.add("Hindi");
          LinkedHashMap<String, LinkedList<String>> subCategoryHashMap = new LinkedHashMap<>();
          for (CourseSubCategory courseSubCategory : CourseSubCategory.values()) {

            if (courseSubCategory.getExamSegmentType().jsonValue.equalsIgnoreCase(examSegment)
                && courseSubCategory.getCourseCategory().jsonValue.equalsIgnoreCase(category)) {

              LinkedList<String> subjectList = new LinkedList<String>();
              for (Subjects sub : Subjects.values()) {

                if ((sub.getCourseSubCategory() != null) && courseSubCategory.toString()
                    .equalsIgnoreCase(sub.getCourseSubCategory().toString())) {
                  subjectList.add(sub.getJsonValue());
                }
              }
              subCategoryHashMap.put(courseSubCategory.jsonValue(), subjectList);
            }
          }
          // subCategoryHashMap.put("Medium", mediumList);

          categoryHashMap.put(CourseCategory.fromString(category).jsonValue, subCategoryHashMap);
          categoryList.add(categoryHashMap);
          filters.put(ExamSegmentTypes.fromString(examSegment).jsonValue, categoryList);
          response.setFilter(filters);

        }

      }
    } catch (Exception e) {
      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public TeacherFilterResponse getTeacherFilter(String examSegment, String category) {
    TeacherFilterResponse response = null;

    try {
      if (examSegment != null && !examSegment.isEmpty() && category != null && !category
          .isEmpty()) {

        LinkedHashMap<String, LinkedList<LinkedHashMap<String, LinkedList<String>>>> filters = new LinkedHashMap<>();

        if (examSegment.equalsIgnoreCase("all") && category.equalsIgnoreCase("all")) {

          for (ExamSegmentTypes examSegmentTypes : ExamSegmentTypes.values()) {
            if (!examSegmentTypes.equals(ExamSegmentTypes.IES_GATE)) {
              if (!examSegmentTypes.jsonValue.equalsIgnoreCase(examSegment)) {
                filters.put(examSegmentTypes.jsonValue, null);
              }
            }
          }
          response = new TeacherFilterResponse();
          response.setFilters(filters);

        } else if (category.equalsIgnoreCase("all")) {

          LinkedList<LinkedHashMap<String, LinkedList<String>>> categoryList = new LinkedList<>();

          for (ExamSegmentTypes examSegmentTypes : ExamSegmentTypes.values()) {
            if (!examSegmentTypes.equals(ExamSegmentTypes.IES_GATE)) {
              if (!examSegmentTypes.jsonValue.equalsIgnoreCase(examSegment)) {
                filters.put(examSegmentTypes.jsonValue, null);
              }
            }
          }

          for (TeacherCategory teacherCategory : TeacherCategory.values()) {

            LinkedHashMap<String, LinkedList<String>> categoryMap = new LinkedHashMap<>();
            if (teacherCategory.getExamSegmentTypes().jsonValue.equalsIgnoreCase(examSegment)) {

              LinkedList<String> subjectList = new LinkedList<String>();
              for (TeacherSubjects sub : TeacherSubjects.values()) {

                if (sub.getExamSegmentTypes() != null
                    && sub.getExamSegmentTypes().jsonValue.equalsIgnoreCase(examSegment)
                    && sub.getTeacherCategory() != null
                    && sub.getTeacherCategory().equals(teacherCategory)) {
                  subjectList.add(sub.getJsonValue());
                }
              }
              categoryMap.put(teacherCategory.jsonValue(), subjectList);
              categoryList.add(categoryMap);

            }

          }
          response = new TeacherFilterResponse();
          filters.put(ExamSegmentTypes.fromString(examSegment).jsonValue, categoryList);
          response.setFilters(filters);

        }
      }
    } catch (Exception e) {
      LOGGER.error(TeacherDetails.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  /**
   * CRUD for posts
   */
  public void createPost(Post newPost, long userID) {
    User user = userRepository.findByUserId(userID);
    newPost.setPostDate(LocalDateTime.now().getDayOfMonth());
    newPost.setPostMonth(LocalDateTime.now().getMonth().name());
    newPost.setPostYear(LocalDateTime.now().getYear());

    if (user != null) {
      newPost.setCreatedBy(user.getFullName());
    }

    if (newPost.getBreadCrumb().getBreadcrumbId() != 0) {
      BreadCrumb breadcrumbId = breadCrumbRepository
          .findByBreadcrumbId(newPost.getBreadCrumb().getBreadcrumbId());

      newPost.setBreadCrumb(breadcrumbId);
    }

    String replacement = newPost.getText();

//    String insertionString =
     //   "<div class=\"make-table-responsive\"><table";

 //   replacement = replacement.replaceAll("<table", insertionString);
   // replacement = replacement.replaceAll("</table>", "</table></div>");

    newPost.setText(replacement);

    postRepository.save(newPost);

    CustomPost cp = createOrUpdateCustomPost(new CustomPost(), newPost);
    customPostRepository.save(cp);

    PostSummary ps = createOrUpdatePostSummary(new PostSummary(), newPost);
    postSummaryRepository.save(ps);

  }

  public CustomPost createOrUpdateCustomPost(CustomPost cp, Post newPost) {
    cp.setCreatedAt(newPost.getCreatedAt());
    cp.setUpdatedAt(newPost.getUpdatedAt());
    cp.setSlug(newPost.getPostSlug());
    cp.setCustomPostId(newPost.getPostId());
    cp.setPostDate(newPost.getPostDate());
    cp.setPostMonth(newPost.getPostMonth());
    cp.setPostYear(newPost.getPostYear());
    cp.setPostName(newPost.getTitle());
    cp.setStatus(newPost.getStatus());

    // This is comment by kartikeya garg
    // if (newPost.getPreviousPost() != null &&
    // newPost.getPreviousPost().getSlug() != null) {
    // Post prev =
    // postRepository.findByPostId(newPost.getPreviousPost().getSlug());
    // cp.setPreviousParentPost(prev);
    // }
    // if (newPost.getNextPost() != null && newPost.getNextPost().getSlug()
    // != null) {
    // Post next =
    // postRepository.findByPostId(newPost.getNextPost().getSlug());
    // cp.setNextParentPost(next);
    // }

    return cp;

  }

  public PostSummary createOrUpdatePostSummary(PostSummary ps, Post newPost) {

    ps.setCreatedAt(newPost.getCreatedAt());
    ps.setUpdatedAt(newPost.getUpdatedAt());
    ps.setTitle(newPost.getTitle());
    ps.setPostSummaryId(newPost.getPostId());
    ps.setPostSummarySlug(newPost.getPostSlug());
    ps.setBrief(newPost.getBrief());
    ps.setCategory(newPost.getCategory());
    ps.setCreatedAt(newPost.getCreatedAt());
    ps.setPostDate(newPost.getPostDate());
    ps.setPostMonth(newPost.getPostMonth());
    ps.setPostYear(newPost.getPostYear());
    ps.setReadTime(newPost.getReadTime());
    ps.setStatus(newPost.getStatus());
    ps.setUpdatedAt(newPost.getUpdatedAt());
    ps.setPostSubCategory(newPost.getPostSubCategory());

    return ps;

  }

  public List<PostSummary> fetchAllPostSummaries() {
    List<PostSummary> response = null;
    try {
      response = postSummaryRepository.findAll();
    } catch (Exception e) {
      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public List<CustomPost> fetchAllCustomPosts() {
    List<CustomPost> response = null;
    try {
      response = customPostRepository.findAll();
    } catch (Exception e) {
      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public Post fetchPostById(int id) {
    Post response = null;
    response = postRepository.findByPostId(id);
    response = this.setPostMetaInformation(response);
    // Set<Post> posts = response.getRelatedPosts();
    // response.setRelatedPosts(posts);
    return response;
  }

  public Post fetchPostBySlug(String postSlug) {
    Post response = null;
    response = postRepository.findByPostSlug(postSlug);
    response = this.setPostMetaInformation(response);
    return response;
  }

  private Post setPostMetaInformation(Post post) {

    if (post != null) {
      List<PostMetaInformation> metaList = new LinkedList<PostMetaInformation>();
      if (post.getMetaList() == null || post.getMetaList().size() == 0) {

        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType("name");
        metaInfo.setType("description");
        metaInfo.setContent(post.getBrief());
        PostMetaInformation postMeta = new PostMetaInformation();
        postMeta.setMetaInformation(metaInfo);
        metaList.add(postMeta);
        post.setMetaList(metaList);

      } else {

        boolean foundDesc = false;
        for (PostMetaInformation meta : post.getMetaList()) {

          if (meta != null && meta.getMetaInformation() != null) {
            switch (meta.getMetaInformation().getType()) {
              case "description":
                foundDesc = true;
                break;

            }
          }
        }

        if (!foundDesc) {
          MetaInformation metaInfo = new MetaInformation();
          metaInfo.setAttributeType("name");
          metaInfo.setType("description");
          metaInfo.setContent(post.getBrief());
          PostMetaInformation postMeta = new PostMetaInformation();
          postMeta.setMetaInformation(metaInfo);
          metaList.add(postMeta);
        }

        post.getMetaList().addAll(metaList);
      }

      if (post.getTitleTag() == null || post.getTitleTag().isEmpty()) {
        post.setTitleTag(post.getTitle());

      }

    }
    return post;
  }

  public CustomPost fetchCustomPostById(String id) {
    CustomPost response = null;
    if (id != null) {
      try {
        response = customPostRepository.findBySlug(id);
        // Set<Post> posts = response.getRelatedPosts();
        // response.setRelatedPosts(posts);
      } catch (Exception e) {

        LOGGER.error(Post.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(CustomPost.class.getName(), null, null);
    }
    return response;
  }

  /*
   * public List<CustomPost> fetchAllCustomPosts() { List<CustomPost> response
   * = customPostRepository.findAll(); return response; }
   */

  public List<Post> fetchAllPosts(Integer page, Integer size, String search, String sortBy) {
    List<Post> response = null;

    try {
      Pageable pageable = null;
      Sort sort = null;
      if (sortBy != null && !sortBy.isEmpty()) {

        switch (sortBy) {
          case "atoz":
            sort = new Sort(Direction.ASC, new String[]{"id"});
            break;
          case "ztoa":
            sort = new Sort(Direction.DESC, new String[]{"id"});
            break;
        }
      }
      if (page == null && size == null && sortBy == null) {
        return response = postRepository.findAll();
      } else if (sort != null && page != null && size != null) {
        pageable = PageRequest.of(page, size, sort);
      } else {
        pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
      }

      Page<Post> iPage = postRepository.findAll(pageable);
      response = iPage.getContent();
    } catch (Exception e) {

      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  /**
   * For fetching all the published posts for public facing pages.
   */
  public Page<Post> fetchPublishedPosts(Integer page, Integer size, String search, String sortBy) {
    Page<Post> response = null;

    try {
      Pageable pageable = null;
      pageable = PageRequest.of(page, size);
      response = postRepository.findByStatusOrderByCreatedAtDesc(PublishStatus.publish, pageable);

    } catch (Exception e) {

      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public Page<Post> fetchPublishedPostsByTag(Integer page, Integer size, String search) {
    Page<Post> response = null;

    try {
      Pageable pageable = null;
      if (page != null && size != null) {
        pageable = PageRequest.of(page, size);
        response = postRepository.findOtherTagsByTag("%" + search + "%", pageable);
      } else {
        pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
        response = postRepository.findOtherTagsByTag("%" + search + "%", pageable);
      }

    } catch (Exception e) {

      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public HashSet<String> fetchOtherTagsByTag(String search) {
    HashSet<String> response = null;

    try {
      if (!TextUtils.isEmpty(search)) {
        List<String> allByTags = postRepository
            .findOtherTagsByTag("%" + search.toLowerCase() + "%");
        if (allByTags != null && allByTags.size() > 0) {
          String s = StringUtils.join(allByTags, ',');
          response = new HashSet<String>(Arrays.asList(s.split(",")));

          for (String tmp : response) {
            if (tmp.equalsIgnoreCase(search)) {
              response.remove(search);
              break;
            }
          }
        }

      }

    } catch (Exception e) {

      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public Post updatePost(int id, Post updatedPost, long userID) {
    Post response = null;
    if (updatedPost != null) {
      try {
        User user = userRepository.findByUserId(userID);
        Post temp = postRepository.findByPostId(id);

        if (user != null) {
          updatedPost.setCreatedBy(user.getFullName());
        }

        String replacement = updatedPost.getText();

        int tableTag = replacement.indexOf("<table");
        //String insertionString =
         //   "<div class=\"make-table-responsive\"><table";

        //replacement = replacement.replaceAll("<table", insertionString);
       // replacement = replacement.replaceAll("</table>", "</table></div>");

        updatedPost.setText(replacement);

        LocalDateTime publishDate = new java.sql.Timestamp(temp.getCreatedAt().getTime())
            .toLocalDateTime();
        updatedPost.setPostDate(publishDate.getDayOfMonth());
        updatedPost.setPostMonth(publishDate.getMonth().name());
        updatedPost.setPostYear(publishDate.getYear());

        updatedPost.setCreatedAt(temp.getCreatedAt());

        if (updatedPost.getBreadCrumb() != null
            && updatedPost.getBreadCrumb().getBreadcrumbId() != 0) {
          BreadCrumb breadcrumbId = breadCrumbRepository
              .findByBreadcrumbId(updatedPost.getBreadCrumb().getBreadcrumbId());

          updatedPost.setBreadCrumb(breadcrumbId);
        }

        if (temp != null) {
          updatedPost.setPostId(id);
          response = postRepository.save(updatedPost);
        }

      } catch (Exception e) {
        LOGGER.error(Post.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Post.class.getName(), null, null);
    }
    return response;
  }

  /**
   * CRUD Testimonial
   */
  public Testimonial createTestimonial(Testimonial newPost) {
    Testimonial response = null;
    if (newPost != null) {
      try {
        response = testimonialRepository.save(newPost);
      } catch (Exception e) {
        LOGGER.error(Testimonial.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Testimonial.class.getName(), null, null);
    }

    return response;
  }

  public Testimonial fetchTestimonialById(int id) {
    Testimonial response = null;
    if (id != 0) {
      try {
        response = testimonialRepository.findById(id);
      } catch (Exception e) {

        LOGGER.error(Testimonial.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Testimonial.class.getName(), null, null);
    }
    return response;
  }

  public List<Testimonial> fetchAllTestimonials(Integer page, Integer size, String search,
      String sortBy) {
    List<Testimonial> response = null;

    try {
      Pageable pageable = null;
      Sort sort = null;
      if (sortBy != null && !sortBy.isEmpty()) {

        switch (sortBy) {
          case "atoz":
            sort = new Sort(Direction.ASC, new String[]{"id"});
            break;
          case "ztoa":
            sort = new Sort(Direction.DESC, new String[]{"id"});
            break;
        }
      }
      if (page == null && size == null && sortBy == null) {
        response = testimonialRepository.findAll();
        return response;

      } else if (sort != null && page != null && size != null) {
        pageable = PageRequest.of(page, size, sort);
      } else {
        pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
      }

      Page<Testimonial> iPage = testimonialRepository.findAll(pageable);
      response = iPage.getContent();
    } catch (Exception e) {

      LOGGER.error(Testimonial.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public List<Testimonial> fetchAllTestimonialOrderByPosition() {

    List<Testimonial> response = null;

    try {
      response = testimonialRepository.findAllByPositionIsGreaterThanOrderByPosition(0);

    } catch (Exception e) {
      LOGGER.error(Testimonial.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public List<Testimonial> fetchAllTestimonialOrderByPositionForHomePage() {

    List<Testimonial> response = null;

    try {
      response = testimonialRepository
          .findAllByPositionIsGreaterThanAndPositionIsLessThanOrderByPosition(0, 7);
    } catch (Exception e) {
      LOGGER.error(Testimonial.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public AnnouncementHeader fetchAllAnnouncementHeaderOrderByPosition(String slug) {

    AnnouncementHeader response = null;
    List<AnnouncementHeader> allHeaders = announcementHeaderRepository.findAll();

    int bestMatchLength = -1;
    for (AnnouncementHeader header : allHeaders) {
      if (slug.toLowerCase().contains(header.getOnPageToDisplay().toLowerCase())) {
        if (header.getOnPageToDisplay().length() > bestMatchLength) {
          bestMatchLength = header.getOnPageToDisplay().length();
          response = header;
        }

      }
    }

    if (response == null) {

      response = announcementHeaderRepository.findByHeaderId(1);
    }
    return response;
  }

  public List<AnnouncementHeader> fetchAllAnnouncementHeader() {

    List<AnnouncementHeader> response = null;

    try {
      response = announcementHeaderRepository.findAll();
    } catch (Exception e) {
      LOGGER.error(AnnouncementHeader.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }


  public String deleteHeaderById(int id) {
    try {
      announcementHeaderRepository.deleteById(id);
    } catch (Exception e) {
      LOGGER.error(AnnouncementHeader.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return "success";
  }


  public AnnouncementHeader updateHeader(AnnouncementHeaderRequest announcementHeaderRequest) {

    AnnouncementHeader announcementHeaderResponse = new AnnouncementHeader();
    if (announcementHeaderRequest != null) {
      try {

        announcementHeaderResponse.setHeaderId(announcementHeaderRequest.getHeaderId());
        announcementHeaderResponse.setHeaderText(announcementHeaderRequest.getHeaderText());
        announcementHeaderResponse.setHeaderUrl(announcementHeaderRequest.getHeaderUrl());
        announcementHeaderResponse
            .setOnPageToDisplay(announcementHeaderRequest.getOnPageToDisplay());
        announcementHeaderResponse.setCreatedAt(announcementHeaderRequest.getCreatedAt());
        announcementHeaderResponse.setUpdatedAt(announcementHeaderRequest.getUpdatedAt());
        announcementHeaderResponse.setBtnText(announcementHeaderRequest.getBtnText());
        announcementHeaderRepository.save(announcementHeaderResponse);

      } catch (Exception e) {
        LOGGER.error(AnnouncementHeader.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Institute.class.getName(), null, null);
    }
    return announcementHeaderResponse;
  }

  public Comment changePostsCommentStatus(PostCommentsRequest postCommentsRequest) {

    Comment postCommentResponse = null;

    if (postCommentsRequest != null) {
      try {
        int commentId = postCommentsRequest.getCommentId();
        Comment comment = commentRepository.findByCommentId(commentId);
        comment.setCommentId(postCommentsRequest.getCommentId());
        comment.setStatus(postCommentsRequest.getStatus());
        postCommentResponse = commentRepository.save(comment);

      } catch (Exception e) {
        LOGGER.error(Comment.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Comment.class.getName(), null, null);
    }
    return postCommentResponse;
  }

  public Testimonial updateTestimonial(int id, Testimonial updatedTestimonial) {
    Testimonial response = null;
    if (updatedTestimonial != null) {
      try {
        Testimonial temp = testimonialRepository.findById(id);

        if (temp != null) {
          updatedTestimonial.setId(id);
          response = testimonialRepository.save(temp);
        } else {
          throw new ResourceNotFoundException(Testimonial.class.getName(), "id", id);
        }
      } catch (Exception e) {
        LOGGER.error(Testimonial.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Testimonial.class.getName(), null, null);
    }
    return response;
  }

  /**
   * Comment and Review APIs
   */

  public CommentDTO createComment(Comment newComment) {
    CommentDTO response = null;
    if (newComment != null) {
      try {
        Comment comment = commentRepository.save(newComment);
        response = CommonAssembler.toCommentDTO(comment);
        emailService.sendNotificationToContentTeam(response.getCommentId(),newComment.getText(),newComment.getEmail(),newComment.getSlug());
      } catch (Exception e) {
        LOGGER.error(Comment.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Comment.class.getName(), null, null);
    }

    return response;
  }

  public Page<CommentProjection> fetchCommentByParentId(int parentId, Integer page, Integer size) {
    Page<CommentProjection> commentReplies = null;
    if (parentId != 0) {
      try {
        Pageable pageable = null;

        if (page == null && size == null) {
          commentReplies = commentRepository.findAllProjectedByParentIdOrderByCommentIdAsc(parentId,
              pageable);
        } else if (page != null && size != null) {
          pageable = PageRequest.of(page, size);
          commentReplies = commentRepository.findAllProjectedByParentIdOrderByCommentIdAsc(parentId,
              pageable);

        } else {
          pageable = PageRequest
              .of((page != null ? page : 0), (size != null ? size : 0), Direction.ASC,
                  "date");
          commentReplies = commentRepository.findAllProjectedByParentIdOrderByCommentIdAsc(parentId,
              pageable);
        }

      } catch (Exception e) {

        LOGGER.error(Comment.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Comment.class.getName(), null, null);
    }
    return commentReplies;
  }

  public Page<CommentProjection> fetchCommentByPostName(String slug, Integer page, Integer size) {

    Page<CommentProjection> commentsBySlug = null;

    Pageable pageable = null;

    if (slug != null) {
      try {

        if (page == null && size == null) {
          commentsBySlug = commentRepository
              .findAllProjectedBySlug(slug, CommentType.COMMENT, pageable);
        } else if (page != null && size != null) {
          pageable = PageRequest.of(page, size);
          commentsBySlug = commentRepository
              .findAllProjectedBySlug(slug, CommentType.COMMENT, pageable);

        } else {
          pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
          commentsBySlug = commentRepository
              .findAllProjectedBySlug(slug, CommentType.COMMENT, pageable);
        }

      } catch (Exception e) {

        LOGGER.error(Comment.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Comment.class.getName(), null, null);
    }

    return commentsBySlug;
  }

  public Page<CommentProjection> fetchAllCommentByPostName(String slug, Integer page,
      Integer size) {

    Page<CommentProjection> commentsBySlug = null;

    Pageable pageable = null;

    if (slug != null) {
      try {

        if (page == null && size == null) {
          commentsBySlug = commentRepository
              .findAllByProjectedByOrderByCommentId(slug, CommentType.COMMENT, pageable);
        } else if (page != null && size != null) {
          pageable = PageRequest.of(page, size);
          commentsBySlug = commentRepository
              .findAllByProjectedByOrderByCommentId(slug, CommentType.COMMENT, pageable);

        } else {
          pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
          commentsBySlug = commentRepository
              .findAllByProjectedByOrderByCommentId(slug, CommentType.COMMENT, pageable);
        }

      } catch (Exception e) {

        LOGGER.error(Comment.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Comment.class.getName(), null, null);
    }

    return commentsBySlug;
  }

  public List<ReviewResponse> fetchReviewsByPostName(String slug, Integer page, Integer size) {
    List<ReviewResponse> response = new ArrayList<ReviewResponse>();
    Page<Comment> reviews = null;
    List<Comment> comments = null;
    if (slug != null) {
      try {

        Pageable pageable = null;

        if (page == null && size == null) {
          comments = commentRepository.findBySlugAndStatusAndTypeOrderByUpdatedAtAsc(slug,
              PublishStatus.publish, CommentType.REVIEW);
        } else if (page != null && size != null) {
          pageable = PageRequest.of(page, size);
          reviews = commentRepository.findCommentBySlug(slug, CommentType.REVIEW, pageable);

        } else {
          pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
          reviews = commentRepository.findCommentBySlug(slug, CommentType.REVIEW, pageable);
        }

        if (comments == null || comments.size() == 0 && reviews != null) {
          comments = reviews.getContent();
        }
        if (comments != null && comments.size() > 0) {
          for (Comment comment : comments) {
            ReviewResponse review = new ReviewResponse();
            review.setId(comment.getCommentId());
            review.setSlug(comment.getSlug());
            review.setSlugType(comment.getSlugType());

            if (comment.getDate() != null) {
              review.setDate(CommonUtil.convertEpochTimeIntoDateFormat(comment.getDate()));
            }
            review.setReviewTitle(comment.getReviewTitle());
            review.setReviewRating(comment.getReviewRating());
            review.setText(comment.getText());
            if (!TextUtils.isEmpty(review.getReviewTitle())) {
              review.setName(comment.getName() + " " + "(" + review.getReviewTitle() + ")");
            } else {
              review.setName(comment.getName());
            }

            review.setEmail(comment.getEmail());

            if (comment.getCommentedBy() != null) {
              String avatar = comment.getCommentedBy().getAvatar();
              if (avatar != null && avatar.length() > 0) {
                review.setAvatar(avatar);
              }
            }

            response.add(review);
          }
        }
      } catch (Exception e) {

        LOGGER.error(Comment.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Comment.class.getName(), null, null);
    }
    return response;
  }

  public ReviewStatistics getReviewStatisticsBySlug(String slug) {
    List<Comment> reviewResponses = null;
    ReviewStatistics reviewStatistics = new ReviewStatistics();

    try {
      reviewResponses = commentRepository.findCommentBySlug(slug, CommentType.REVIEW);
      float sum = 0f;
      HashMap<String, Integer> map = new HashMap<String, Integer>();
      map.put("One", 0);
      map.put("Two", 0);
      map.put("Three", 0);
      map.put("Four", 0);
      map.put("Five", 0);
      if (reviewResponses != null && reviewResponses.size() > 0) {
        reviewStatistics.setTotal((int) reviewResponses.size());
        for (Comment reviewResponse : reviewResponses) {
          if (reviewResponse.getReviewRating() == 1) {
            if (map.containsKey("One")) {
              map.put("One", map.get("One") + 1);
            }

          } else if (reviewResponse.getReviewRating() == 2) {
            if (map.containsKey("Two")) {
              map.put("Two", map.get("Two") + 1);
            }

          } else if (reviewResponse.getReviewRating() == 3) {
            if (map.containsKey("Three")) {
              map.put("Three", map.get("Three") + 1);
            }

          } else if (reviewResponse.getReviewRating() == 4) {
            if (map.containsKey("Four")) {
              map.put("Four", map.get("Four") + 1);
            }

          } else if (reviewResponse.getReviewRating() == 5) {
            if (map.containsKey("Five")) {
              map.put("Five", map.get("Five") + 1);
            }

          }
        }
      }
      reviewStatistics.setResponse(map);
      for (String str : map.keySet()) {
        if (str.equals("One")) {
          sum += map.get(str);
        }
        if (str.equals("Two")) {
          sum += 2 * map.get(str);
        }
        if (str.equals("Three")) {
          sum += 3 * map.get(str);
        }
        if (str.equals("Four")) {
          sum += 4 * map.get(str);
        }
        if (str.equals("Five")) {
          sum += 5 * map.get(str);
        }
      }
      float avg = 0;
      if (reviewStatistics.getTotal() > 0) {
        avg = sum / reviewStatistics.getTotal();
      }
      if (avg - (int) avg < 0.5) {
        reviewStatistics.setAvg((int) avg);
      } else if (avg - (int) avg > 0.5) {
        reviewStatistics.setAvg((int) avg + 1);
      } else {
        reviewStatistics.setAvg(avg);

      }
    } catch (Exception e) {
      LOGGER.error(Comment.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return reviewStatistics;
  }

  public Comment updateComment(int id, Comment updatedComment) {
    Comment response = null;
    if (updatedComment != null) {
      try {
        Comment temp = commentRepository.findByCommentId(id);

        if (temp != null) {

          updatedComment.setCommentId(id);
          response = commentRepository.save(temp);
        } else {
          throw new ResourceNotFoundException(Comment.class.getName(), "commentId", id);
        }
      } catch (Exception e) {
        LOGGER.error(Comment.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Comment.class.getName(), null, null);
    }
    return response;
  }

  public UserUnitResponse fetchUnitsByBatchID(int batchID, long userId) {
    UserUnitResponse response = new UserUnitResponse();
    LinkedHashMap<String, Set<Object>> unitMap = new LinkedHashMap<String, Set<Object>>();
    Set<Unit> units = new LinkedHashSet<Unit>();
    int totalUnits = 0;
    int watchedUnits = 0;
    int inprogressUnits = 0;
    int notstartedUnits = 0;
    try {
      if (batchID != 0) {
        CourseBatch cb = courseBatchRepository.findById(batchID);
        if (cb != null && cb.getUnits() != null && cb.getUnits().size() > 0) {
          // && cb.getUserCourseBatchLinkages()!=null &&
          // cb.getUserCourseBatchLinkages().size()>0
          // for()
          units = cb.getUnits();
          totalUnits = units.size();
        }

        if (units.size() > 0) {
          for (Unit unit : units) {
            if (unit.getType() != null) {
              UnitDTO dto = CommonAssembler.toUnitDTO(unit);
              UserLectureStats stats = userMetaDataRespository
                  .findByIdUserUserIdAndIdUnitUnitId(userId, unit.getUnitId());
              if (dto.getType().equals(UnitType.LECTURE)) {
                if (stats != null) {
                  float watchPercent = 0;
                  float watched = stats.getChunkCount() * 10;
                  if (stats.getDuration() > 0) {
                    watchPercent = watched / stats.getDuration();
                  }
                  if (watchPercent > 98 && watchPercent <= 100) {
                    watchedUnits++;
                  } else if (watchPercent > 0) {
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
              if (unitMap.get(unit.getType().getJsonValue()) != null) {
                Set<Object> tempSet = unitMap.get(unit.getType().getJsonValue());
                tempSet.add(dto);
                unitMap.put(unit.getType().getJsonValue(), tempSet);
              } else {
                Set<Object> newSet = new HashSet<Object>();
                newSet.add(dto);
                unitMap.put(unit.getType().getJsonValue(), newSet);
              }
            }
          }
          response.setUnitMap(unitMap);
          float inProgressPercent = inprogressUnits / totalUnits;
          float watchedPercent = watchedUnits / totalUnits;
          float notStartedPercent = notstartedUnits / totalUnits;
          response.setInProgressPercent(inProgressPercent);
          response.setNotStartedPercent(notStartedPercent);
          response.setWatchedPercent(watchedPercent);
        }
      } else {
        throw new InvalidInputException(CourseBatch.class.getName(), null, null);
      }
    } catch (Exception e) {
      LOGGER.error(UserUnitLinkage.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  /**
   * CRUD Test
   */
  public Test createTest(Test newTest) {
    Test response = null;
    if (newTest != null) {
      try {
        response = testRepository.save(newTest);
      } catch (Exception e) {
        LOGGER.error(Test.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Test.class.getName(), null, null);
    }

    return response;
  }

  public Test fetchTestById(int id) {
    Test response = null;
    if (id != 0) {
      try {
        response = testRepository.findById(id);
      } catch (Exception e) {

        LOGGER.error(Test.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Test.class.getName(), null, null);
    }
    return response;
  }

  public List<Test> fetchAllTests(Integer page, Integer size, String search, String sortBy) {
    List<Test> response = null;

    try {
      Pageable pageable = null;
      Sort sort = null;
      if (sortBy != null && !sortBy.isEmpty()) {

        switch (sortBy) {
          case "atoz":
            sort = new Sort(Direction.ASC, new String[]{"id"});
            break;
          case "ztoa":
            sort = new Sort(Direction.DESC, new String[]{"id"});
            break;
        }
      }
      if (page == null && size == null && sortBy == null) {
        response = testRepository.findAll();
      } else if (sort != null && page != null && size != null) {
        pageable = PageRequest.of(page, size, sort);
      } else {
        pageable = PageRequest.of((page != null ? page : 0), (size != null ? size : 0));
      }

      Page<Test> iPage = testRepository.findAll(pageable);
      response = iPage.getContent();
    } catch (Exception e) {

      LOGGER.error(Test.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public Test updateTest(int id, Test updatedTest) {
    Test response = null;
    if (updatedTest != null) {
      try {
        Test temp = testRepository.findById(id);

        if (temp != null) {
          updatedTest.setId(id);
          response = testRepository.save(temp);
        } else {
          throw new ResourceNotFoundException(Test.class.getName(), "id", id);
        }
      } catch (Exception e) {
        LOGGER.error(Test.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Test.class.getName(), null, null);
    }
    return response;
  }

  public static String fetchHTTPURL(String url) throws IOException {
    URL yahoo = new URL(url);
    URLConnection yc = yahoo.openConnection();
    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
    String inputLine;

    String rv = "";

    while ((inputLine = in.readLine()) != null) {
      // System.out.println(inputLine);
      rv = rv + inputLine;
    }
    in.close();
    return rv;
  }

  private String fetchServingUrl(String pathName) throws IOException {
    String url = "https://ns-web-storage.appspot.com/getImageUrl?fileName=" + URLEncoder
        .encode(pathName, "utf-8");
    String newPath = fetchHTTPURL(url);
    return newPath;
  }

  public String uploadFileOnGCP(MultipartFile file, String type, String contentType) {
    String bucketName = "ns-web-storage.appspot.com";
    String uploadFileName = null;

    try {
      Date date = new Date();
      LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      String month = String.valueOf(localDate.getMonth());
      String year = String.valueOf(localDate.getYear());
      String filePath = month + "-" + year + "/";
      uploadFileName = uploadFile(filePath, file.getOriginalFilename(), contentType,
          multipartToFile(file),
          bucketName, type);

      deleteFile(file);

      if (contentType.contains("image")) {
        ImageUrls imageUrl = new ImageUrls();
        imageUrl.setImagePath(uploadFileName);
        imageUrl.setImageServingUrl(fetchServingUrl(uploadFileName));
        imageUrlsRepository.save(imageUrl);
      }
    } catch (IOException e) {
      LOGGER.error(Unit.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      deleteFile(file);
    } catch (Throwable t) {
      t.printStackTrace();
      deleteFile(file);
    }

    return uploadFileName;
  }

  public String uploadFileOnGCP(File file, String type, String contentType) {
    String bucketName = "ns-web-storage.appspot.com";
    String uploadFileName = null;

    try {
      Date date = new Date();
      LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      String month = String.valueOf(localDate.getMonth());
      String year = String.valueOf(localDate.getYear());
      String filePath = month + "-" + year + "/";
      uploadFileName = uploadFile(filePath, file.getName(), contentType,
          file, bucketName, type);

      deleteFile(file);

    } catch (IOException e) {
      LOGGER.error(Unit.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      deleteFile(file);
    } catch (Throwable t) {
      t.printStackTrace();
      deleteFile(file);
    }

    return uploadFileName;
  }

  public String readWordFile(MultipartFile multipartFile) {

    if (multipartFile != null) {
      File file = null;
      try {
        file = multipartToFile(multipartFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
      DocumentConverter converter = new DocumentConverter()
          .imageConverter(image -> {
            String base64 = streamToBase64(image::getInputStream);
            String src = "data:" + image.getContentType() + ";base64," + base64;

            byte[] imageByte = Base64.getDecoder().decode(base64);

            File imageFile = File.createTempFile("fig", "." + CommonUtil.getImageType(src));
            //File imageFile = File.createTempFile("fig", "." + CommonUtil.getImageType(src));
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageByte);
            fos.flush();
            fos.close();

            String uploadFileName = uploadFileOnGCP(imageFile, "quiz/figures",
                image.getContentType());

            String imageUrlFromAppEngine = fetchServingUrl(uploadFileName);

            Map<String, String> attributes = new HashMap<>();
            attributes.put("src", imageUrlFromAppEngine);
            return attributes;
          });
      Result<String> result = null;
      try {
        result = converter.convertToHtml(file);
      } catch (IOException e) {
        e.printStackTrace();
      }

      String html = result.getValue();
      return html;
    }

    return null;
  }

  public Quiz updateQuiz(int id, Quiz updateQuiz) {
    Quiz saveQuiz = quizRepository.findByQuizId(id);
    if (updateQuiz != null && saveQuiz != null) {

      updateQuiz.setQuizId(id);
      updateQuiz.setCreatedAt(saveQuiz.getCreatedAt());
      updateQuiz.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      return quizRepository.save(updateQuiz);
    }
    return null;
  }

  public Quiz createQuiz(String quiz, MultipartFile multipartFile) {

    String html = readWordFile(multipartFile);

    Gson gson = new Gson();
    Quiz fromJson = gson.fromJson(quiz, Quiz.class);

    return createQuiz(fromJson, html);
  }


  public Quiz createQuiz(Quiz quiz, String html) {
    List<Question> questionList = null;
    Quiz response = null;
    if (quiz != null && !TextUtils.isEmpty(html)) {
      String decodeHTML = null;
      try {
        html = html.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        html = html.replaceAll("\\+", "%2B");
        decodeHTML = URLDecoder.decode(html, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      Document doc = Jsoup.parse(decodeHTML);
      Elements pTagHTML = doc.select("p");

      try {
        quiz.addMetadata();
        response = quizRepository.saveAndFlush(quiz);
        questionList = createQuizQuestionFromHTML(response, pTagHTML);
        saveListOfQuestion(questionList);

      } catch (Exception e) {
        LOGGER.error(Quiz.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
        throw new InvalidInputException(Quiz.class.getName(), null, null);
      }
    } else {
      throw new InvalidInputException(Quiz.class.getName(), null, null);
    }
    return response;
  }


  private void saveListOfQuestion(List<Question> questionList) {
    customQuestionRepository.save(questionList);
  }

  private List<Question> createQuizQuestionFromHTML(Quiz response, Elements pTagHTML) {
    int nextQuestionCount = 0;
    StringBuilder topic = null;
    for (int i = 0; i < pTagHTML.size(); i++) {

      String body = pTagHTML.get(i).text();

      if (body.contains("Q.2)")) {
        break;
      }
      if (body.toLowerCase().contains("topic:-") && topic == null) {
        topic = new StringBuilder();
        topic.append(body.toLowerCase().replace("topic:-", "").trim());

      }
      nextQuestionCount++;
    }
    return createQuizNextQuestionFromHTML(pTagHTML, 0, nextQuestionCount, 2, new Question(),
        new ArrayList<>(), response, topic);

  }

  public List<Question> createQuizNextQuestionFromHTML(Elements html, int startCounter,
      int nextQuestionCounter,
      int nextQuestionNumber, Question question, List<Question> questionList,
      Quiz quiz, StringBuilder topic) {


    try {
      StringBuilder questionTopic = topic;

      question.addMetadata();
      question.setPosition(nextQuestionNumber - 1);
      question.setQuiz(quiz);
      question.setPositivePoints(quiz.getPositiveMarkPerQuestion());
      question.setNegativePoints(quiz.getNegativeMarkPerQuestion());

      if (questionTopic != null) {
        question.setTopic(WordUtils.capitalizeFully(questionTopic.toString()));
      }

      if (startCounter == html.size()) {
        return questionList;
      }

      for (int i = startCounter; i < nextQuestionCounter; i++) {

        String text = html.get(i).text().trim();

        if (!TextUtils.isBlank(html.get(i).text()) && text.toLowerCase().contains("topic:-")) {
          questionTopic = new StringBuilder();
          questionTopic.append(text.toLowerCase().replace("topic:-", "").trim());

        } else if (!TextUtils.isBlank(html.get(i).text()) && text
            .contains("Q." + (nextQuestionNumber - 1) + ")")) {
          JsonArray jsonArray = new JsonArray();
          jsonArray.add(text.replace("Q." + (nextQuestionNumber - 1) + ")", "").trim());
          question.setQuestionText(jsonArray.toString());
        } else if (!html.get(i).select("img").isEmpty()) {
          Elements img = html.get(i).select("img");

          JsonArray figureJsonArray = new JsonArray();

          if (question.getFigureJson() != null) {
            JSONArray jsonArray = new JSONArray(question.getFigureJson());

            for (int k = 0; k < jsonArray.length(); k++) {
              figureJsonArray.add(jsonArray.getString(k));
            }
          }

          for (Element elm : img) {
            figureJsonArray.add(elm.attr("src"));
          }

          question.setFigureJson(figureJsonArray.toString());

        } else if (!TextUtils.isBlank(html.get(i).text()) && (text.contains("(a)."))) {
          if (question.getOptionJson() == null) {
            JsonArray optionJsonArray = new JsonArray();
            optionJsonArray.add(text.replace("(a).", "").trim());
            question.setOptionJson(optionJsonArray.toString());
          }
        } else if (!TextUtils.isBlank(html.get(i).text()) && (text.contains("(b)."))) {
          JSONArray optionJsonArray = new JSONArray(question.getOptionJson());
          optionJsonArray.put(text.replace("(b).", "").trim());
          question.setOptionJson(optionJsonArray.toString());

        } else if (!TextUtils.isBlank(html.get(i).text()) && (text.contains("(c)."))) {
          JSONArray optionJsonArray = new JSONArray(question.getOptionJson());
          optionJsonArray.put(text.replace("(c).", "").trim());
          question.setOptionJson(optionJsonArray.toString());

        } else if (!TextUtils.isBlank(html.get(i).text()) && (text.contains("(d)."))) {

          JSONArray optionJsonArray = new JSONArray(question.getOptionJson());
          optionJsonArray.put(text.replace("(d).", "").trim());
          question.setOptionJson(optionJsonArray.toString());

        } else if (!TextUtils.isBlank(html.get(i).text()) && (text.contains("Answer:-"))) {
          question.setAnswer(text.replace("Answer:-", "").trim());
        } else if (!TextUtils.isBlank(html.get(i).text()) && question.getAnswer() == null) {

          String questionText = question.getQuestionText();
          JSONArray jsonArray = new JSONArray(questionText);

          if (!TextUtils.isBlank(html.get(i).text())) {
            jsonArray.put(html.get(i).text().trim());
          }

          question.setQuestionText(jsonArray.toString());

        } else {

          JsonArray answerExplanation = new JsonArray();

          if (question.getAnswerExplanation() != null) {
            JSONArray jsonArray = new JSONArray(question.getAnswerExplanation());

            for (int k = 0; k < jsonArray.length(); k++) {
              answerExplanation.add(jsonArray.getString(k));
            }
          }

          answerExplanation.add(text);
          question.setAnswerExplanation(answerExplanation.toString());
        }


      }

      questionList.add(question);

      for (int i = nextQuestionCounter; i < html.size(); i++) {

        if (!TextUtils.isBlank(html.get(i).text())) {
          String text = html.get(i).text();

          if (text.contains("Q." + (nextQuestionNumber + 1) + ")")) {
            createQuizNextQuestionFromHTML(html, nextQuestionCounter, i, nextQuestionNumber + 1,
                new Question(),
                questionList, quiz, questionTopic);
          }


        }

      }

      // To read last question
      if (nextQuestionNumber == quiz.getTotalNoOfQuestion()) {
        createQuizNextQuestionFromHTML(html, nextQuestionCounter, html.size(),
            nextQuestionNumber + 1,
            new Question(),
            questionList, quiz, questionTopic);
      }
    }catch(Exception e){
      emailService.sendErrorLogsToDevelopers(nextQuestionNumber-1 + "\n" + e.getStackTrace());
      e.printStackTrace();
    }

    return questionList;
  }


  public String uploadFile(String filePath, String originalFilename, String contentType, File file,
      String bucketName,
      String type) throws IOException, GeneralSecurityException {

    InputStreamContent contentStream = new InputStreamContent(contentType,
        new FileInputStream(file));
    // Setting the length improves upload performance
    contentStream.setLength(file.length());

    com.google.api.services.storage.Storage client = StorageFactory.getService();

    StorageObject storageObject = new StorageObject();
    if (contentType.indexOf("pdf") != -1) {
      // Set the access control list to publicly read-only
      storageObject
          .setAcl(Arrays.asList(new ObjectAccessControl().setEntity("allUsers").setRole("READER")));
      storageObject.setName(filePath + type + "/" + originalFilename);
    } else if (contentType.indexOf("csv") != -1) {
      storageObject
          .setAcl(Arrays.asList(new ObjectAccessControl().setEntity("allUsers").setRole("READER")));
      storageObject.setName(filePath + "/" + originalFilename);
    } else {
      storageObject.setName(filePath + type + "/" + originalFilename);
    }

    // Do the insert
    Storage.Objects.List list = client.objects().list(storageObject.getName());

    if (list != null && list.size() > 0) {
      String existingName = list.getBucket();

      if (existingName.equals(filePath + type + "/" + originalFilename)) {

        String[] split = contentType.split("/");
        String generatedString = RandomStringUtils.random(12, true, true);

        storageObject.setName(filePath + type + "/" + generatedString + "." + split[1]);

      }
    }

    com.google.api.services.storage.Storage.Objects.Insert insertRequest = client.objects()
        .insert(bucketName,
            storageObject, contentStream);

    StorageObject execute = insertRequest.execute();

    if (contentType.indexOf("pdf") != -1) {
      return "https://storage.googleapis.com/" + execute.getBucket() + "/" + execute.getName();
    } else if (contentType.indexOf("csv") != -1) {
      return execute.getMediaLink();
    } else {
      return execute.getName();
    }

  }

  public boolean checkFileIsExist(List<StorageObject> items, String name) {

    if (items != null && items.size() > 0) {
      for (StorageObject object : items) {
        if (name == object.getName()) {
          return true;
        }
      }
    }
    return false;
  }

  public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
    File convFile = new File(multipart.getOriginalFilename());
    convFile.createNewFile();

    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(multipart.getBytes());
    fos.close();
    return convFile;
  }

  public void deleteFile(File file) {
    try {
      if (file.exists()) {
        file.delete();
      }
    } catch (Exception e) {
      LOGGER.error(String.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  public void deleteFile(MultipartFile multipart) {
    try {
      File file = new File(multipart.getOriginalFilename());
      if (file.exists()) {
        file.delete();
      }
    } catch (Exception e) {
      LOGGER.error(String.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  public Bucket getBucket(String bucketName) throws IOException, GeneralSecurityException {
    com.google.api.services.storage.Storage client = StorageFactory.getService();

    com.google.api.services.storage.Storage.Buckets.Get bucketRequest = client.buckets()
        .get(bucketName);
    // Fetch the full set of the bucket's properties (e.g. include the ACLs
    // in the response)
    bucketRequest.setProjection("full");
    return bucketRequest.execute();
  }

  public LinkedHashMap<String, String> fetchAllExamSegment() {
    LinkedHashMap<String, String> examSegmentList = new LinkedHashMap<>();
    ExamSegmentTypes[] values = ExamSegmentTypes.values();

    for (ExamSegmentTypes examSegmentType : values) {
      examSegmentList.put(examSegmentType.toString(), examSegmentType.jsonValue);
    }

    return examSegmentList;
  }

  public LinkedHashSet<String> fetchAllTeacherExamSegment() {
    LinkedHashSet<String> examSegmentList = new LinkedHashSet<>();
    ExamSegmentTypes[] values = ExamSegmentTypes.values();

    for (ExamSegmentTypes examSegmentType : values) {
      examSegmentList.add(examSegmentType.toString());
    }

    return examSegmentList;
  }

  public LinkedHashMap<String, String> fetchCategoryByExamSegment(String category) {
    LinkedHashMap<String, String> categoryList = null;
    try {
      if (category != null && !category.isEmpty()) {

        ExamSegmentTypes examSegmentTypes = ExamSegmentTypes.valueOf(category.toUpperCase());
        CourseCategory[] values = CourseCategory.values();
        categoryList = new LinkedHashMap<>();

        if (examSegmentTypes != null) {
          for (CourseCategory courseCategory : values) {

            if (courseCategory.getExamSegmentType().equals(examSegmentTypes)) {
              categoryList.put(courseCategory.toString(), courseCategory.jsonValue);
            }
          }
        } else {
          throw new InvalidInputException(null, null, null);
        }
      } else {
        throw new InvalidInputException(null, null, null);
      }

    } catch (Exception e) {
      LOGGER.error("Category Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return categoryList;
  }

  public LinkedHashSet<String> fetchTeacherCategoryByExamSegment(String category) {
    LinkedHashSet<String> categoryList = null;
    try {
      if (category != null && !category.isEmpty()) {

        ExamSegmentTypes examSegmentTypes = ExamSegmentTypes.fromString(category);
        TeacherCategory[] values = TeacherCategory.values();
        categoryList = new LinkedHashSet<>();

        if (examSegmentTypes != null) {
          for (TeacherCategory teacherCategory : values) {

            if (teacherCategory.getExamSegmentTypes().equals(examSegmentTypes)) {
              categoryList.add(teacherCategory.jsonValue());
            }
          }
        } else {
          throw new InvalidInputException(null, null, null);
        }
      } else {
        throw new InvalidInputException(null, null, null);
      }
    } catch (Exception e) {
      LOGGER.error("Teacher Category Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return categoryList;
  }

  public LinkedHashSet<String> fetchTeacherSubjectByCategory(String category, String examsegment) {

    LinkedHashSet<String> subjectList = null;
    try {
      if (!TextUtils.isEmpty(examsegment) && !TextUtils.isEmpty(category)) {

        String[] categoryArray = category.split(",");

        if (categoryArray != null && categoryArray.length > 0) {

          subjectList = new LinkedHashSet<>();
          for (String categoryString : categoryArray) {

            for (TeacherSubjects subjects : TeacherSubjects.values()) {

              if (subjects.getTeacherCategory().jsonValue().equalsIgnoreCase(categoryString)
                  && subjects.getExamSegmentTypes().jsonValue.equalsIgnoreCase(examsegment)) {
                subjectList.add(subjects.getJsonValue());
              }
            }

          }

        }
      } else {
        throw new InvalidInputException(null, null, null);
      }
    } catch (Exception e) {
      LOGGER.error("Teacher Subject Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return subjectList;
  }

  public LinkedHashMap<String, String> fetchSubCategoryByExamSegment(String examSegment,
      String subCategory) {

    LinkedHashMap<String, String> subCategoryList = null;

    try {
      if (examSegment != null && !examSegment.isEmpty() && subCategory != null && !subCategory
          .isEmpty()) {

        CourseSubCategory[] values = CourseSubCategory.values();
        subCategoryList = new LinkedHashMap<>();

        ExamSegmentTypes examSegmentType = ExamSegmentTypes.valueOf(examSegment.toUpperCase());
        CourseCategory courseCategory = CourseCategory
            .getCourseCategory(examSegmentType, subCategory);

        for (CourseSubCategory courseSubCategory : values) {

          if (courseSubCategory.getExamSegmentType() == examSegmentType
              && courseSubCategory.getCourseCategory() == courseCategory) {
            subCategoryList.put(courseSubCategory.toString(), courseSubCategory.jsonValue());
          }
        }
      } else {
        throw new InvalidInputException(null, null, null);
      }
    } catch (Exception e) {
      LOGGER.error("Sub Category Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return subCategoryList;
  }

  public LinkedHashSet<String> fetchSubSubCategoryByExamSegment(String examSegment, String category,
      String subCategory) {

    LinkedHashSet<String> subjectsList = null;
    try {
      if (examSegment != null && !examSegment.isEmpty() && category != null && !category.isEmpty()
          && subCategory != null && !subCategory.isEmpty()) {

        subjectsList = new LinkedHashSet<>();

        Subjects[] values = Subjects.values();

        ExamSegmentTypes examSegmentTypes = ExamSegmentTypes.valueOf(examSegment.toUpperCase());
        CourseCategory courseCategory = CourseCategory.valueOf(category.toUpperCase());
        CourseSubCategory courseSubCategory = CourseSubCategory.valueOf(subCategory.toUpperCase());

        for (Subjects subjects : values) {

          if (subjects.getExamSegmentTypes() == examSegmentTypes
              && subjects.getCourseCategory() == courseCategory
              && subjects.getCourseSubCategory() == courseSubCategory) {
            subjectsList.add(subjects.jsonValue);
          }
        }
      } else {
        throw new InvalidInputException(null, null, null);
      }
    } catch (Exception e) {
      LOGGER.error("Category Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return subjectsList;
  }

  public void makeUnitProduct(Unit unit) {
    Product p = new Product();
    Product temp = productRepository.findByCommodityIdAndType(unit.getUnitId(), ProductType.UNIT);

    if (temp != null) {
      p.setId(temp.getId());
    }
    p.setImageUrl(unit.getBatch().getCourse().getCourseImageUrl());
    p.setCommodityId(unit.getUnitId());
    p.setPrice(unit.getPrice());
    p.setRegularPrice(unit.getPrice());
    p.setProductTitle(unit.getTitle());
    p.setType(ProductType.UNIT);
    productRepository.save(p);
  }

  public Unit addLectureToBatch(int batchId, Unit unit) {

    Unit response = null;
    if (batchId == 0) {
      throw new InvalidInputException(CourseBatch.class.getName(), null, null);
    }
    CourseBatch courseBatch = courseBatchRepository.findById(batchId);
    if (courseBatch == null) {
      throw new ResourceNotFoundException(CourseBatch.class.getName(), "id", batchId);
    }

    Set<Unit> unitSet = courseBatch.getUnits();

    if (unitSet == null) {
      unitSet = new HashSet<>();
    }
    int pos = maxPosition(unitSet);
    unit.setPosition(pos + 1);
    unitSet.add(unit);
    int typeId = unit.getTypeId();

    if (unit.getType().equals(UnitType.LECTURE)) {
      Lecture lecture = lectureRepository.findById(typeId);
      if (lecture != null) {
        lecture.setCourseBatch(courseBatch);
        lectureRepository.save(lecture);
      } else {
        throw new ResourceNotFoundException(Lecture.class.getName(), "id", lecture);
      }
    }
    CourseBatch cb = courseBatchRepository.save(courseBatch);
    if (cb != null && cb.getUnits() != null && cb.getUnits().size() > 0) {
      int max = 0;
      for (Unit u : cb.getUnits()) {
        if (u.getUnitId() > max) {
          max = u.getUnitId();
        }
        if (u.getTypeId() != 0 && u.getTypeId() == typeId) {
          return u;
        }
      }

      for (Unit u : cb.getUnits()) {
        if (u.getUnitId() == max && u.getType() != null && !u.getType().equals(UnitType.LECTURE)) {
          return u;
        }
      }

    }
    return response;
  }

  /**
   * For fetching all the featured blog posts
   */
  public List<Post> fetchFeaturedPosts(Integer page, Integer size) {
    List<Post> response = new LinkedList<Post>();
    try {
      response = postRepository.fetchFeaturedPosts();
    } catch (Exception e) {
      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  /**
   * To fetch all the popular posts for the blog home page.
   */
  public List<Post> fetchPopularPosts(Integer page, Integer size) {
    List<Post> response = new LinkedList<Post>();
    try {
      response = postRepository.fetchPopularPosts();
    } catch (Exception e) {
      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public List<Post> fetchFeaturedVideoBlogs(Integer page, Integer size) {
    List<Post> response = new LinkedList<Post>();
    try {
      response = postRepository.fetchFeaturedVideoBlogs();
    } catch (Exception e) {
      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public List<CourseProjection> fetchPopularCourses(Integer page, Integer size) {
    List<CourseProjection> response = null;
    try {
      if (page == null || size == null) {
        response = courseRepository.findAllProjectedByPopularTrueAndStatus(PublishStatus.publish);
      } else {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseProjection> tPage = courseRepository
            .findAllProjectedByPopularTrueAndStatus(PublishStatus.publish, pageable);
        response = tPage.getContent();
      }
    } catch (Exception e) {
      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;

  }

  public Page<CourseProjection> fetchCoursesByPagination(Integer page, Integer size) {
    Page<CourseProjection> tPage = null;

    try {
      if (page != null && size != null) {
        Pageable pageable = PageRequest.of(page, size);
        tPage = courseRepository.findAllProjectedBy(pageable);
      }
    } catch (Exception e) {
      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return tPage;

  }

  public LinkedHashMap<String, List<CourseProjection>> fetchPopularCourseByCategory() {

    LinkedHashMap<String, List<CourseProjection>> linkedHashMap = new LinkedHashMap<>();

    List<CourseProjection> iasCourses = courseRepository
        .findAllProjectedByPopularTrueAndStatusAndCourseExamSegmentAndPositionIsGreaterThanAndPositionIsLessThanEqualOrderByPosition(
            PublishStatus.publish, ExamSegmentTypes.IAS, 0, 15);

    linkedHashMap.put("IAS", iasCourses);

    List<CourseProjection> iesCourses = courseRepository
        .findAllProjectedByPopularTrueAndStatusAndCourseExamSegmentAndPositionIsGreaterThanAndPositionIsLessThanEqualOrderByPosition(
            PublishStatus.publish, ExamSegmentTypes.IES, 0, 15);
    linkedHashMap.put("IES", iesCourses);

    List<CourseProjection> gateCourses = courseRepository
        .findAllProjectedByPopularTrueAndStatusAndCourseExamSegmentAndPositionIsGreaterThanAndPositionIsLessThanEqualOrderByPosition(
            PublishStatus.publish, ExamSegmentTypes.GATE, 0, 15);
    linkedHashMap.put("GATE", gateCourses);

    List<CourseProjection> iesGateCourses = courseRepository
        .findAllProjectedByPopularTrueAndStatusAndCourseExamSegmentAndPositionIsGreaterThanAndPositionIsLessThanEqualOrderByPosition(
            PublishStatus.publish, ExamSegmentTypes.IES_GATE, 0, 15);

    // linkedHashMap.put("IES_GATE", iesGateCourses);
    linkedHashMap.get("IES").addAll(iesGateCourses);
    linkedHashMap.get("GATE").addAll(iesGateCourses);

    List<CourseProjection> statePcsCourses = courseRepository
        .findAllProjectedByPopularTrueAndStatusAndCourseExamSegmentAndPositionIsGreaterThanAndPositionIsLessThanEqualOrderByPosition(
            PublishStatus.publish, ExamSegmentTypes.STATE_PSC, 0, 15);

    linkedHashMap.put("STATE_PSC", statePcsCourses);

    return linkedHashMap;

  }

  public LinkedHashSet<String> fetchPostCategory() {

    LinkedHashSet<String> postCategoryList = new LinkedHashSet<>();
    PostCategory[] values = PostCategory.values();

    if (values != null && values.length > 0) {
      for (PostCategory postCategory : values) {
        postCategoryList.add(postCategory.jsonValue());
      }
    }

    return postCategoryList;

  }

  public LinkedHashSet<String> fetchPostSubCategory() {

    LinkedHashSet<String> postSubCategoryList = new LinkedHashSet<>();
    PostSubCategory[] values = PostSubCategory.values();

    if (values != null && values.length > 0) {
      for (PostSubCategory postSubCategory : values) {
        postSubCategoryList.add(postSubCategory.jsonValue());
      }
    }

    return postSubCategoryList;

  }

  /**
   * For publishing a post publicly
   */
  public boolean publishPost(int id) {
    try {
      if (id != 0) {
        Post post = postRepository.findByPostId(id);
        if (post != null) {
          post.setStatus(PublishStatus.publish);
          postRepository.save(post);
          CustomPost cp = customPostRepository.findByCustomPostId(id);
          if (cp != null) {
            cp.setStatus(PublishStatus.publish);
            customPostRepository.save(cp);
          }
          PostSummary ps = postSummaryRepository.findByPostSummaryId(id);
          if (ps != null) {
            ps.setStatus(PublishStatus.publish);
            postSummaryRepository.save(ps);
          }

        }
      } else {
        return false;
      }
    } catch (Exception e) {
      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return true;
  }

  public Course updateCourseStatus(int id, @Valid String courseStatus) {
    Course course = null;
    try {
      if (id != 0 && courseStatus != null && !courseStatus.isEmpty()) {
        course = courseRepository.findById(id);
        if (course != null) {
          PublishStatus publishStatus = PublishStatus.valueOf(courseStatus);
          course.setStatus(publishStatus);
          course.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
          Course updatedCourse = courseRepository.save(course);

          return updatedCourse;
        } else {
          return course;
        }

      } else {
        return null;
      }
    } catch (Exception e) {
      LOGGER.error(Course.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return course;
  }

  /**
   * For returning list of topics
   */
  public Set<String> getExistingTopicsOfTheBatch(int id) {
    Set<String> response = new LinkedHashSet<String>();
    try {
      CourseBatch cb = courseBatchRepository.findById(id);
      if (cb != null && cb.getUnits() != null) {
        for (Unit u : cb.getUnits()) {
          if (u != null && u.getTopic() != null) {
            response.add(u.getTopic());
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(UserUnitLinkage.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;

  }

  public Unit updateUnit(Unit updateUnit, int id) {
    Unit response = null;
    if (updateUnit != null && id != 0) {
      Unit temp = unitRepository.findByUnitId(id);
      if (temp != null) {
        boolean product = temp.isProduct();
        updateUnit.setUnitId(id);
        updateUnit.setBatch(temp.getBatch());
        updateUnit.setUserUnitLinkages(temp.getUserUnitLinkages());
        updateUnit.setCreatedAt(temp.getCreatedAt());
        updateUnit.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        response = unitRepository.save(updateUnit);
        if (updateUnit.isProduct()) {
          if (!product) {
            makeUnitProduct(response);
          }
        }

      } else {
        throw new ResourceNotFoundException(Unit.class.getName(), "id", id);
      }
    } else {
      throw new InvalidInputException(Unit.class.getName(), null, null);
    }
    return response;
  }

  public Set<String> getAllExistingTopics() {
    Set<String> response = new LinkedHashSet<String>();
    try {
      List<Unit> units = unitRepository.findAll();

      if (units != null && units.size() > 0) {
        for (Unit u : units) {
          if (u != null && u.getTopic() != null && !u.getTopic().isEmpty()) {
            response.add(u.getTopic());
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(Unit.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public TopicUnitResponse getUnitsByBatch(int batchId) {
    TopicUnitResponse response = new TopicUnitResponse();
    Map<String, Set<Unit>> unitMap = new LinkedHashMap<String, Set<Unit>>();
    try {
      if (batchId != 0) {
        CourseBatch cb = courseBatchRepository.findById(batchId);
        if (cb != null) {
          Set<Unit> units = cb.getUnits();
          if (units != null && units.size() > 0) {
            for (Unit u : units) {
              if (u != null && u.getTopic() != null) {
                if (unitMap.get(u.getTopic()) != null && unitMap.get(u.getTopic()).size() > 0) {
                  Set<Unit> temp = unitMap.get(u.getTopic());
                  temp.add(u);
                  unitMap.put(u.getTopic(), temp);
                } else {
                  Set<Unit> temp = new LinkedHashSet<Unit>();
                  temp.add(u);
                  unitMap.put(u.getTopic(), temp);
                }
              }
            }
            response.setUnitMap(unitMap);
            response.setBatchId(batchId);
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(TopicUnitResponse.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public List<CourseProjection> fetchCoursesNames() {
    // TODO:To fix issue with this code snippet
    // List<Course> courses = courseRepository.fetchCourseNames();

    List<CourseProjection> courses = courseRepository
        .findAllProjectedByStatus(PublishStatus.publish);

    return courses;
  }

  public List<BatchNameDTO> fetchBatchNamesForCourses(BatchNameRequest request) {
    List<BatchNameDTO> response = new LinkedList<BatchNameDTO>();

    try {
      if (request != null) {
        List<CourseBatch> batches = courseBatchRepository
            .fetchCourseBatchNames(request.getCourseIds());
        if (batches != null && batches.size() > 0) {
          for (CourseBatch cb : batches) {
            if (cb != null) {
              BatchNameDTO dto = new BatchNameDTO();
              dto.setBatchId(cb.getId());
              dto.setBatchName(cb.getBatchName());
              dto.setCourseId(cb.getCourse().getId());
              dto.setCourseName(cb.getCourse().getCourseName());
              response.add(dto);
            }
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(CourseBatch.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  /**
   * Linking multiple units to multiple batches
   */
  public boolean linkUnitsToBatches(UnitBatchLinkageRequest request) {

    try {
      if (request != null && request.getBatches() != null && request.getBatches().size() > 0
          && request.getUnits() != null && request.getUnits().size() > 0) {
        for (int unitId : request.getUnits()) {
          if (unitId != 0) {
            for (int cbId : request.getBatches()) {
              if (cbId != 0) {
                CourseBatch cb = courseBatchRepository.findById(cbId);
                Set<Unit> units = null;
                if (cb.getUnits() != null && cb.getUnits().size() > 0) {
                  units = cb.getUnits();
                } else {
                  units = new LinkedHashSet<Unit>();
                }

                int position = maxPosition(units);

                Unit temp = unitRepository.findByUnitId(unitId);
                Unit newUnit = new Unit(temp);
                newUnit.setUnitId(0);
                newUnit.setPosition(position + 1);
                newUnit.setBatch(cb);
                newUnit.addMetadata();
                Unit savedUnit = unitRepository.save(newUnit);
                units.add(savedUnit);

                // For linking the unit with the users who have
                // already purchased the course
                List<UserCourseBatchLinkage> ucbLinkages = userCourseBatchLinkageRepository
                    .findByCourseBatch(cb.getId());

                if (ucbLinkages != null && ucbLinkages.size() > 0) {
                  for (UserCourseBatchLinkage linkage : ucbLinkages) {
                    if (linkage != null) {
                      User user = linkage.getUser();

                      if (user != null) {
                        ecommerceService.linkSingleUnitToUser(user, savedUnit);
                      }
                    }
                  }
                }

              }
            }

          }
        }

      }
    } catch (Exception e) {
      LOGGER.error(CourseBatch.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    for (int unitId : request.getUnits()) {

      Unit unit = unitRepository.findByUnitId(unitId);
      if (unit.getBatch() == null) {
        unitRepository.deleteById(unitId);
      }

    }

    return true;

  }

  private int maxPosition(Set<Unit> units) {
    int maxPos = 0;
    for (Unit u : units) {
      if (u.getPosition() > maxPos) {
        maxPos = u.getPosition();
      }
    }

    return maxPos;
  }

  /**
   * For creating a new unit without linking it with any batch.
   */
  public Unit saveUnit(Unit unit) {
    Unit response = null;
    unit.addMetadata();
    response = unitRepository.save(unit);
    if (unit.isProduct()) {
      makeUnitProduct(response);
    }

    return response;

  }

  public boolean shuffleUnits(ShuffleUnitsRequest request) {
    boolean response = false;
    try {
      if (request != null && request.getUnitId() != 0) {
        Unit unit = unitRepository.findByUnitId(request.getUnitId());
        if (unit != null) {
          CourseBatch cb = unit.getBatch();
          Set<Unit> units = cb.getUnits();
          if (units != null && units.size() > 0) {
            for (Unit u : units) {
              if (u != null && u.getPosition() == request.getNewPosition()) {
                // Swap
                int temp = unit.getPosition();
                unit.setPosition(request.getNewPosition());
                u.setPosition(temp);
                unitRepository.save(unit);
                unitRepository.save(u);
                response = true;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(Unit.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public boolean shuffleUnitsPositions(List<UnitPositionAndMacroRequest> request) {
    for (UnitPositionAndMacroRequest request1 : request) {

      if (request != null && request1.getUnitId() != 0) {
        Unit unit = unitRepository.findByUnitId(request1.getUnitId());
        unit.setPosition(request1.getNewPosition());
        unitRepository.save(unit);

        if (unit.getType() == UnitType.LECTURE) {
          Lecture lecture = lectureRepository.findById(unit.getTypeId());
          JWMacro macro = jwMacroRepository.findByName(request1.getJwMacro());
          lecture.setJwMacro(macro);
          lectureRepository.save(lecture);

        }
      }
    }

    return true;
  }

  /**
   * Fetch all the course summaries
   */

  public List<CourseSummary> fetchAllCourseSummaries() {
    List<CourseSummary> response = null;
    try {
      response = courseSummaryRepository.findAll();
    } catch (Exception e) {
      LOGGER.error(CourseSummary.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public BatchUnitResponse getBatchUnitById(int batchId) {
    BatchUnitResponse batchUnitResponse = null;
    LinkedHashMap<String, LinkedList<Unit>> tempUnitMap = null;
    long userId = jwtTokenUtil.getLoggedInUserID();
    String freeTopic = "Free Videos";
    int freeVideoCount = 0;
    CourseBatch courseBatch = courseBatchRepository.findById(batchId);

    tempUnitMap = new LinkedHashMap<>();
    LinkedList<Unit> freeUnits = new LinkedList<>();
    for (Unit unit : courseBatch.getUnits()) {

      if (unit != null && unit.getStatus() != null
          && unit.getStatus().equals(PublishStatus.publish)) {
        if (!TextUtils.isEmpty(unit.getTopic())) {

          if (unit.getType() == UnitType.LECTURE && unit.isFree()) {
            Unit temp = new Unit();
            temp.setUnitId(unit.getUnitId());
            freeVideoCount += 1;
            temp.setTitle("Free Video " + freeVideoCount);
            temp.setPosition(-100 + freeVideoCount);
            temp.setFree(true);
            freeUnits.add(temp);

          }

          if (tempUnitMap.get(unit.getTopic()) == null) {
            LinkedList<Unit> units = new LinkedList<>();
            units.add(unit);
            tempUnitMap.put(unit.getTopic(), units);
          } else {
            LinkedList<Unit> units = tempUnitMap.get(unit.getTopic());
            units.add(unit);
            tempUnitMap.put(unit.getTopic(), units);
          }
        }
      }
    }

    //repopulating hashmap by adding freetopic on top

    LinkedHashMap<String, LinkedList<Unit>> rvUnitMap = new LinkedHashMap<>();
    if (freeUnits.size() > 0) {
      rvUnitMap.put(freeTopic, freeUnits);
    }

    for (String key : tempUnitMap.keySet()) {
      rvUnitMap.put(key, tempUnitMap.get(key));
    }

    if (tempUnitMap != null) {
      batchUnitResponse = new BatchUnitResponse();
      batchUnitResponse.setResponse(rvUnitMap);
      UserCourseBatchLinkage linkage = userCourseBatchLinkageRepository
          .findByUserAndBatchAndStatus(userId, courseBatch.getId(),
              UserCourseLinkageStatus.ACTIVE);
      if (linkage != null) {
        batchUnitResponse.setAlreadyBought(true);
      }

    }

    return batchUnitResponse;
  }

  public List<Post> fetchPostByKeyword(String search, int page, int size) {

    List<Post> response = null;
    PostCategory category = PostCategory.valueOf(search);
    try {
      if (category != null) {
        response = postRepository.findByCategory(category);
      } else {
        PostSubCategory subCategory = PostSubCategory.valueOf(search);
        if (subCategory != null) {
          response = postRepository.findByPostSubCategory(subCategory);
        }
        // TODO
        /*
         * else { response = searchService.searchPosts(search, page,
         * size); }
         */
      }
    } catch (Exception e) {
      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public List<Post> fetchFreePrep() {
    List<Post> response = null;
    try {
      response = postRepository.fetchFreePrep();
    } catch (Exception e) {
      LOGGER.error(Post.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;

  }

  public List<AnswerSheet> fetchAnswerSheetsByCategory(AnswerSheetCategory category) {
    List<AnswerSheet> response = null;
    try {
      if (category != null) {
        response = answerSheetRepository.findByCategory(category);

      }
    } catch (Exception e) {
      LOGGER.error(AnswerSheet.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  /**
   * For fetching all the categories of answer sheet
   */
  public Set<String> fetchAllAnswerSheetCategories() {
    Set<String> response = new LinkedHashSet<String>();
    AnswerSheetCategory[] values = AnswerSheetCategory.values();
    for (AnswerSheetCategory c : values) {
      response.add(c.jsonValue());
    }
    return response;
  }

  public AnswerSheet createAnswerSheet(AnswerSheet answerSheet) {
    answerSheet.addMetadata();
    AnswerSheet response = null;
    try {

      answerSheetRepository.save(answerSheet);
    } catch (Exception e) {
      LOGGER.error(AnswerSheet.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public NewsletterSubscribeResponse createSubscriber(NewsletterSubscriber newsletterSubscriber) {
    NewsletterSubscribeResponse response = new NewsletterSubscribeResponse();
    if (newsletterSubscriber != null) {
      try {

        if (newsletterSubscriber.getEmailId() != null && !newsletterSubscriber.getEmailId()
            .isEmpty()) {

          NewsletterSubscriber newsletterSubscriberByEmailId = newsletterSubscriberRepository
              .findByEmailId(newsletterSubscriber.getEmailId());

          if (newsletterSubscriberByEmailId != null) {
            response.setSubscribe(false);
            response.setErrorMessage("User has already subscribed");

            return response;
          }

          if (newsletterSubscriber.getSubscriberList() != null
              && newsletterSubscriber.getSubscriberList().size() > 0) {

            for (NewsletterSubscriberList newsletterSubscriberList : newsletterSubscriber
                .getSubscriberList()) {

              if (newsletterSubscriberList.getName() != null
                  && !newsletterSubscriberList.getName().isEmpty()) {

                NewsletterSubscriberList subscriberList = newsletterSubscriberListRepository
                    .findByName(newsletterSubscriberList.getName());

                if (subscriberList != null) {
                  newsletterSubscriberList.setId(subscriberList.getId());
                } else {
                  NewsletterSubscriberList subscriberList1 = newsletterSubscriberListRepository
                      .save(newsletterSubscriberList);

                  if (subscriberList1 != null) {
                    newsletterSubscriberList.setId(subscriberList1.getId());
                  }
                }
              }

            }
          }
        }
        NewsletterSubscriber subscriber = newsletterSubscriberRepository.save(newsletterSubscriber);
        if (subscriber != null && subscriber.getId() != 0) {
          response.setSubscribe(true);
          response.setErrorMessage("You have successfully subscribed to our newsletter");
          response.setEmailId(newsletterSubscriber.getEmailId());

          emailService.sendEmailToNewsletterSubscriber(subscriber);
        } else {
          response.setSubscribe(false);
          response.setErrorMessage("Some error occurred. Please try again after sometime");

        }
      } catch (Exception e) {

        LOGGER.error(AnswerSheet.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
        response.setSubscribe(false);
        response.setErrorMessage("Some error occurred. Please try again after sometime");

      }
    } else {
      throw new InvalidInputException(NewsletterSubscriber.class.getName(), null, null);
    }

    return response;

  }

  public void createDefaultEntities() {
    CustomPost cp = new CustomPost();
  }

  public BreadCrumb createBreadCrumb(BreadCrumb request) {
    BreadCrumb response = null;
    if (request != null) {
      try {
        response = breadCrumbRepository.save(request);
      } catch (Exception e) {
        LOGGER.error(BreadCrumb.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(BreadCrumb.class.getName(), null, null);
    }

    return response;
  }

  public String sendEmailToRedcarpet(@Valid PayLaterRequest request) {

    if (request != null) {
      try {
        if (!request.getEmail().isEmpty() && request.getEmail() != null) {
          emailService.sendEmailToRedcarpet(request);
        }

      } catch (NullPointerException e) {
        LOGGER.error(BreadCrumb.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    }

    return "Email Sent Successfully";

  }

  public LinkedList<BreadCrumb> fetchBreadCrumbById(int id) {
    LinkedList<BreadCrumb> breadCrumbLinkedList = new LinkedList<>();
    try {
      BreadCrumb breadCrumb = breadCrumbRepository.findByBreadcrumbId(id);
      if (breadCrumb != null) {

        breadCrumbLinkedList.add(breadCrumb);
        findBreadCrumbById(breadCrumb.getParentId(), breadCrumbLinkedList);

        if (breadCrumbLinkedList.size() > 0) {
          Collections.reverse(breadCrumbLinkedList);
        }

      } else {
        return null;
      }
    } catch (Exception e) {
      LOGGER.error(BreadCrumb.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return breadCrumbLinkedList;
  }

  public LinkedList<BreadCrumb> findBreadCrumbById(int id,
      LinkedList<BreadCrumb> breadCrumbLinkedList) {

    if (id == 0) {
      return breadCrumbLinkedList;
    }

    BreadCrumb breadCrumbByParentId = breadCrumbRepository.findByBreadcrumbId(id);

    if (breadCrumbByParentId != null) {
      breadCrumbLinkedList.add(breadCrumbByParentId);

    } else {
      return breadCrumbLinkedList;
    }

    return findBreadCrumbById(breadCrumbByParentId.getParentId(), breadCrumbLinkedList);

  }

  public LinkedList<BreadCrumb> fetchBreadCrumbByParentId(int id) {
    LinkedList<BreadCrumb> response = null;
    try {
      if (id == 0) {
        throw new InvalidInputException(BreadCrumb.class.getName(), null, null);
      }
      response = breadCrumbRepository.findByParentId(id);
    } catch (Exception e) {
      LOGGER.error(BreadCrumb.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public LinkedList<BreadCrumb> fetchAllBreadCrumb() {
    LinkedList<BreadCrumb> response = null;
    try {

      response = breadCrumbRepository.findByParentId(0);
    } catch (Exception e) {
      LOGGER.error(BreadCrumb.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;

  }

  public List<AllBreadcrumbResponse> allBreadCrumb() {
    List<BreadCrumb> response = null;
    List<AllBreadcrumbResponse> allBreadcrumbResponses = null;
    try {

      response = breadCrumbRepository.findAllBy();

      if (response != null && response.size() > 0) {
        allBreadcrumbResponses = new ArrayList<>();

        for (BreadCrumb childBreadcrumb : response) {

          AllBreadcrumbResponse allBreadcrumbResponse = new AllBreadcrumbResponse();
          int parentId = childBreadcrumb.getParentId();
          BreadCrumb parentBreadcrumb = breadCrumbRepository.findByBreadcrumbId(parentId);

          allBreadcrumbResponse.setChild(childBreadcrumb);
          allBreadcrumbResponse.setParent(parentBreadcrumb);

          allBreadcrumbResponses.add(allBreadcrumbResponse);
        }
      }

    } catch (Exception e) {
      LOGGER.error(BreadCrumb.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return allBreadcrumbResponses;

  }

  public boolean slugExistence(String slug, String type) {

    boolean flag = false;
    if (type.equalsIgnoreCase("course")) {
      Course course = courseRepository.findByCourseOldSlug(slug);
      if (course != null) {
        flag = true;
      }
    } else if (type.equalsIgnoreCase("teacher")) {
      TeacherDetails teacher = teacherDetailsRepository.findBySlug(slug);
      if (teacher != null) {
        flag = true;
      }
    } else if (type.equalsIgnoreCase("institute")) {
      TeacherDetails teacher = teacherDetailsRepository.findBySlug(slug);
      if (teacher != null) {
        flag = true;
      }
    }

    return flag;

  }

  public List<ReviewResponse> getTeacherTestimonial(String slug) {

    Set<ReviewResponse> reviewResponses = new HashSet<>();

    try {
      TeacherDetails teacher = teacherDetailsRepository.findBySlug(slug);
      if (teacher != null) {
        Set<Course> courses = teacher.getCourses();
        if (courses != null && courses.size() > 0) {
          for (Course course : courses) {
            List<Comment> comments = commentRepository
                .findBySlugAndStatusAndReviewTitleContainsAndTypeAndReviewRatingGreaterThanEqualOrderByCommentIdDesc(
                    course.getCourseOldSlug(), PublishStatus.publish, "AIR", CommentType.REVIEW, 3);

            if (comments != null && comments.size() > 0) {
              for (Comment comment : comments) {

                if (!TextUtils.isEmpty(comment.getReviewTitle())) {
                  ReviewResponse review = new ReviewResponse();
                  review.setId(comment.getCommentId());
                  review.setSlug(comment.getSlug());
                  review.setSlugType(comment.getSlugType());

                  review.setReviewRating(comment.getReviewRating());
                  review.setText(comment.getText());

                  review.setName(comment.getName() + " " + "(" + comment.getReviewTitle() + ")");
                  review.setDummyName(comment.getName());

                  review.setEmail(comment.getEmail());

                  if (comment.getCommentedBy() != null) {
                    String avatar = comment.getCommentedBy().getAvatar();
                    if (avatar != null && avatar.length() > 0) {
                      review.setAvatar(avatar);
                    }
                  }

                  reviewResponses.add(review);
                }
              }

            }

          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(BreadCrumb.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    List<ReviewResponse> response = null;
    if (reviewResponses != null && reviewResponses.size() > 0) {
      response = reviewResponses.stream().filter(distinctByKey(review -> review.getDummyName()))
          .collect(Collectors.toList());
    }
    return response;
  }

  public List<ReviewResponse> getInstituteTestimonial(String slug) {

    Set<ReviewResponse> reviewResponses = new HashSet<>();

    try {
      Institute institute = instituteRepository.findByInstituteSlug(slug);
      if (institute != null) {
        Set<Course> courses = institute.getCourses();
        if (courses != null && courses.size() > 0) {
          for (Course course : courses) {
            List<Comment> comments = commentRepository
                .findBySlugAndStatusAndReviewTitleContainsAndTypeAndReviewRatingGreaterThanEqualOrderByCommentIdDesc(
                    course.getCourseOldSlug(), PublishStatus.publish, "AIR", CommentType.REVIEW, 3);

            if (comments != null && comments.size() > 0) {
              for (Comment comment : comments) {

                if (!TextUtils.isEmpty(comment.getReviewTitle())) {
                  ReviewResponse review = new ReviewResponse();
                  review.setId(comment.getCommentId());
                  review.setSlug(comment.getSlug());
                  review.setSlugType(comment.getSlugType());

                  review.setReviewRating(comment.getReviewRating());
                  review.setText(comment.getText());

                  review.setName(comment.getName() + " " + "(" + comment.getReviewTitle() + ")");
                  review.setDummyName(comment.getName());

                  review.setEmail(comment.getEmail());

                  if (comment.getCommentedBy() != null) {
                    String avatar = comment.getCommentedBy().getAvatar();
                    if (avatar != null && avatar.length() > 0) {
                      review.setAvatar(avatar);
                    }
                  }

                  reviewResponses.add(review);
                }
              }

            }

          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(BreadCrumb.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    List<ReviewResponse> response = null;
    if (reviewResponses != null && reviewResponses.size() > 0) {

      response = reviewResponses.stream().filter(distinctByKey(review -> review.getName()))
          .collect(Collectors.toList());
    }
    return response;
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

  public Unit createUnit(Unit unit) {
    Unit response = null;
    if (unit.getBatch() != null && unit.getBatch().getId() != 0) {
      response = addLectureToBatch(unit.getBatch().getId(), unit);
      if (unit.isProduct()) {
        makeUnitProduct(response);
      }

      if (unit.getBatch() != null) {
        List<UserCourseBatchLinkage> ucbLinkages = userCourseBatchLinkageRepository
            .findByCourseBatch(unit.getBatch().getId());
        if (ucbLinkages != null && ucbLinkages.size() > 0) {
          for (UserCourseBatchLinkage linkage : ucbLinkages) {
            if (linkage != null && linkage.getUser() != null && !linkage.isRestrictedAccess()) {
              User user = linkage.getUser();
              ecommerceService.linkSingleUnitToUser(user, response);
            }
          }
        }

      }
    }
    return response;
  }

  public CustomBreadCrumResponse createCustomBreadCrumb(CreateBreadCrumbRequest request) {

    CustomBreadCrumResponse response = new CustomBreadCrumResponse();
    if (request != null && !TextUtils.isEmpty(request.getChildText()) && !TextUtils
        .isEmpty(request.getChildUrl()) && !TextUtils.isEmpty(request.getParentUrl())) {

      String childText = request.getChildText().trim();
      String childUrl = request.getChildUrl().trim();
      String parentUrl = request.getParentUrl().trim();

      int index = childUrl.indexOf(".com/");
      String slug = childUrl.substring(index + 4);
      String parentSlug = parentUrl.substring(index + 4);

      BreadCrumb breadCrumb = null;

      if (slug.substring(slug.length() - 1).equals("/")) {
        breadCrumb = breadCrumbRepository.findByLink(slug);
      } else {
        breadCrumb = breadCrumbRepository.findByLink(slug + "/");
      }

      BreadCrumb parentBreadCrumb = null;
      if (parentSlug.substring(parentSlug.length() - 1).equals("/")) {
        parentBreadCrumb = breadCrumbRepository.findByLink(parentSlug);
      } else {
        parentBreadCrumb = breadCrumbRepository.findByLink(parentSlug + "/");
      }

      if (breadCrumb != null) {
        breadCrumb.setText(childText);
      } else {
        breadCrumb = new BreadCrumb();
        breadCrumb.setText(childText);
        breadCrumb.setLink(slug);


      }

      if (parentBreadCrumb != null) {
        breadCrumb.setParentId(parentBreadCrumb.getBreadcrumbId());
      }

      BreadCrumb updateBreadCrumb = breadCrumbRepository.save(breadCrumb);

      if (slug.contains("/course/")) {
        int i = slug.lastIndexOf("/course/");
        String courseSlug = slug.substring(i + 8, slug.length() - 1);

        Course course = courseRepository.findByCourseOldSlug(courseSlug);
        if (course != null) {
          course.setBreadCrumb(updateBreadCrumb);

          courseRepository.save(course);
        } else {
          response.setErrorMessage("Course does not exist with this url");

        }


      } else if (slug.contains("/teacher/")) {
        int i = slug.lastIndexOf("/teacher/");
        String teacherSlug = slug.substring(i + 9, slug.length() - 1);

        TeacherDetails teacher = teacherDetailsRepository.findBySlug(teacherSlug);
        if (teacher != null) {
          teacher.setBreadCrumb(updateBreadCrumb);

          teacherDetailsRepository.save(teacher);
        } else {

          response.setErrorMessage("Teacher does not exist with this url");

        }

      } else if (slug.contains("/institute/")) {
        int i = slug.lastIndexOf("/institute/");
        String instituteSlug = slug.substring(i + 11, slug.length() - 1);

        Institute institute = instituteRepository.findByInstituteSlug(instituteSlug);

        if (institute != null) {
          institute.setBreadCrumb(updateBreadCrumb);

          instituteRepository.save(institute);
        } else {

          response.setErrorMessage("Institute does not exist with this url");
        }
      } else {
        String postSlug = slug.substring(1, slug.length());
        Post post = postRepository.findByPostSlug(postSlug);
        if (post != null) {
          post.setBreadCrumb(updateBreadCrumb);

          postRepository.save(post);
        } else {

          response.setErrorMessage("Post does not exist with this url");
        }
      }


    } else {

      response.setErrorMessage("Either Text or Url or Parent Url is empty");
    }

    return response;

  }

  public int getDemoUnit(Course course) {
    if (course.getDemoUnitID() != 0) {
      Unit u = unitRepository.findByUnitId(course.getDemoUnitID());
      if (u != null) {
        return u.getUnitId();
      }
    } else {

      Set<CourseBatch> batches = course.getBatches();

      CourseBatch latestBatchWithFreeVideos = null;
      Date latestBatchWithFreeVideoDate = new Date(0);
      int returnUnitID = 0;

      //get first unit from latest batch
      for (CourseBatch batch : batches) {
        if (batch.getStartDate().after(latestBatchWithFreeVideoDate)
            || latestBatchWithFreeVideos == null) {
          Set<Unit> batchUnits = batch.getUnits();

          for (Unit unit : batchUnits) {
            if (unit.getType() == UnitType.LECTURE && unit.isFree()) {
              returnUnitID = unit.getUnitId();
              latestBatchWithFreeVideoDate = batch.getStartDate();
              latestBatchWithFreeVideos = batch;
              break;
            }
          }
        }
      }
      return returnUnitID;
    }
    return 0;

  }

  public ExtraInfoResponse createExtraInfo(ExtraInfo extraInfo) {
    ExtraInfoResponse extraInfoResponse = new ExtraInfoResponse();
    if (extraInfo != null) {

      String mobileNumber = extraInfo.getMobileNumber();

      extraInfo.addMetadata();
      ExtraInfo save = extraInfoRepository.save(extraInfo);

      extraInfoResponse.setExtraInfo(save);
      extraInfoResponse.setMessage("Success");
      extraInfoResponse.setValid(true);


    } else {
      extraInfoResponse.setMessage("Failed");
      extraInfoResponse.setValid(false);
    }

    return extraInfoResponse;
  }


  public List<ExtraInfo> fetchAllExtraInfo() {

    List<ExtraInfo> all = extraInfoRepository.findAll();

    return all;
  }

  public QuizTemplate createQuizTemplate(QuizTemplate quizTemplate) {

    if (quizTemplate != null && quizTemplate.getQuiz().getQuizId() != 0) {
      quizTemplate.addMetadata();
      Quiz quiz = quizRepository.findByQuizId(quizTemplate.getQuiz().getQuizId());
      quizTemplate.getQuiz().addMetadata();
      quizTemplate.setQuiz(quiz);
      quizTemplate = quizTemplateRepository.save(quizTemplate);

    }

    return quizTemplate;
  }

  public QuizTemplate updateQuizTemplate(int id, QuizTemplate quizTemplate) {

    QuizTemplate saveQuizTemplate = quizTemplateRepository.findQuizTemplateById(id);
    if (quizTemplate != null && saveQuizTemplate != null) {
      quizTemplate.setId(id);
      quizTemplate.setQuiz(saveQuizTemplate.getQuiz());
      quizTemplate.setCreatedAt(saveQuizTemplate.getCreatedAt());
      quizTemplate.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      quizTemplate = quizTemplateRepository.save(quizTemplate);

    }

    return quizTemplate;
  }

  public QuizTemplate fetchQuizTemplateBySlug(String slug) {
    return quizTemplateRepository.findQuizTemplateBySlug(slug);
  }


  public QuizTemplateResponse fetchQuizTemplateByQuizId(int quizId) {
    QuizTemplate quizTemplate = quizTemplateRepository.findByQuizQuizId(quizId);

    List<Question> questions = customQuestionRepository
        .findQuestionsByQuizId(quizTemplate.getQuiz());

    QuizTemplateResponse quizTemplateResponse = new QuizTemplateResponse();

    quizTemplateResponse.setQuizTemplate(quizTemplate);

    if (quizTemplate.getQuiz().isTopicWiseQuestion()) {
      LinkedHashMap<String, List<Question>> quizResponseWithTopic = createQuizResponseWithTopic(
          questions);

      quizTemplateResponse.setQuestionMap(quizResponseWithTopic);
    } else {
      quizTemplateResponse.setQuestions(questions);
    }

    return quizTemplateResponse;
  }

  private LinkedHashMap<String, List<Question>> createQuizResponseWithTopic(
      List<Question> questionList) {

    LinkedHashMap<String, List<Question>> questionMap = new LinkedHashMap<>();

    if (questionList != null && questionList.size() > 0) {

      for (Question question : questionList) {
        if (question != null && question.getTopic() != null) {
          if (questionMap.get(question.getTopic()) != null
              && questionMap.get(question.getTopic()).size() > 0) {
            List<Question> temp = questionMap.get(question.getTopic());
            temp.add(question);
            questionMap.put(question.getTopic(), temp);
          } else {
            List<Question> temp = new ArrayList<>();
            temp.add(question);
            questionMap.put(question.getTopic(), temp);
          }
        }
      }

    }

    return questionMap;
  }

  public List<QuizTemplate> fetchAllQuizTemplate() {
    List<QuizTemplate> all = quizTemplateRepository.findAll();
    return all;
  }

  public UserExistResponse checkUserExistOnTestSubmit(String name, String emailId,
      String password, String mobile,
      String city, String examSegment, boolean isLogin) {

    UserExistResponse userExistResponse = new UserExistResponse();
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    Long userId = null;
    String token = null;

    User user = new User();
    user.setFullName(name);
    user.setEmailId(emailId);
    user.setPassword(password);
    user.setMobileNumber(mobile);
    user.setCity(city);
    user.setExamSegment(examSegment);

    if (!isLogin) {
      SignUpResponse signUpResponse = userService.signUp(user);

      if (signUpResponse.isLoginSucces()) {

        User fetchUserByEmailId = userService.fetchUserByEmailId(emailId);
        userId = fetchUserByEmailId.getUserId();
        token = signUpResponse.getAccessToken();
      } else {

        boolean flag = false;

        User existUser = userService.fetchUserByEmailId(emailId);
        if (existUser.getMobileNumber() == null || existUser.getMobileNumber().isEmpty()) {
          flag = true;
          existUser.setMobileNumber(mobile);
        }

        if (existUser.getExamSegment() == null || existUser.getExamSegment().isEmpty()) {
          flag = true;
          existUser.setExamSegment(examSegment);
        }

        if (existUser.getCity() == null || existUser.getCity().isEmpty()) {
          flag = true;
          existUser.setCity(city);
        }

        if (flag) {
          existUser = userService.updateUser(existUser);
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailId(emailId);
        loginRequest.setPassword(password);

        token = userService.genearteToken(existUser.getUserName());
        userId = existUser.getUserId();

      }
    } else {
      boolean flag = false;
      User existUser = userService.fetchUserByEmailId(emailId);
      if (existUser.getMobileNumber() == null || existUser.getMobileNumber().isEmpty()) {
        flag = true;
        existUser.setMobileNumber(mobile);
      }

      if (existUser.getExamSegment() == null || existUser.getExamSegment().isEmpty()) {
        flag = true;
        existUser.setExamSegment(examSegment);
      }

      if (existUser.getCity() == null || existUser.getCity().isEmpty()) {
        flag = true;
        existUser.setCity(city);
      }

      if (flag) {
        existUser = userService.updateUser(existUser);
        token = userService.genearteToken(existUser.getUserName());
      }

      userId = existUser.getUserId();
    }

    userExistResponse.setUserId(userId);
    userExistResponse.setToken(token);

    return userExistResponse;

  }

  public void sendTestDataToHubSpot(String name, String phoneNumber, String emailId, String city,
      String exam, String source) {

  }

  public String downloadQuizData(int id) {
    List<ExportQuizStats> exportQuizStatsList = new ArrayList<>();
    String downloadFileUrl = null;
    if (id != 0) {
      Quiz byQuizId = quizRepository.findByQuizId(id);
      List<UserQuizSubmission> userQuizSubmissions = userQuizSubmissionRepository
          .findByQuizQuizIdOrderByMarksObtainedDesc(id);

      String fileNamePath = "/tmp/" + byQuizId.getTitle() + LocalDate.now().toString() + ".csv";
      Writer writer = null;
      try {
        writer = Files.newBufferedWriter(Paths.get(fileNamePath));
      } catch (IOException e) {
        e.printStackTrace();
      }

      for (UserQuizSubmission userQuizSubmission : userQuizSubmissions) {

        ExportQuizStats exportQuizStats = new ExportQuizStats();

        exportQuizStats.setId(userQuizSubmission.getUser().getUserId().toString());
        exportQuizStats.setEmail(userQuizSubmission.getUser().getEmailId());
        exportQuizStats.setFullName(userQuizSubmission.getUser().getFullName());
        exportQuizStats.setTimeTaken(userQuizSubmission.getTimeTaken());
        exportQuizStats.setMarksObtained(String.valueOf(userQuizSubmission.getMarksObtained()));
        exportQuizStats.setSubmittedDate(userQuizSubmission.getUpdatedAt().toString());
        exportQuizStats.setMobile(userQuizSubmission.getUser().getMobileNumber());

        exportQuizStatsList.add(exportQuizStats);

      }

      CSVUtility.writeUserQuizSubmission(writer, exportQuizStatsList, null);
      downloadFileUrl = ecommerceService.uploadCSVToCloud(fileNamePath);

      File file = new File(fileNamePath);
      if (file.exists()) {
        file.delete();
      }

    }
    return downloadFileUrl;
  }

  public void saveUserUnitStats(UserUnitRequest userUnitRequest, long userId, int unitId) {

    UserLectureStats userLectureStats = userMetaDataRespository
        .findByIdUserUserIdAndIdUnitUnitId(userId, unitId);

    if (userLectureStats == null) {
      userLectureStats = new UserLectureStats();
      UserLectureCompositeKey userLectureCompositeKey = new UserLectureCompositeKey();

      User user = userRepository.findByUserId(userId);
      Unit unit = unitRepository.findByUnitId(unitId);
      int batchId = unit.getBatch().getId();

      userLectureStats.setBatchId(batchId);
      userLectureStats.setDuration(userUnitRequest.getDuration());
      userLectureCompositeKey.setUser(user);
      userLectureCompositeKey.setUnit(unit);

      userLectureStats.setId(userLectureCompositeKey);
      userLectureStats.addMetadata();
    }

    userLectureStats.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    userLectureStats.setChunkCount(userLectureStats.getChunkCount()+userUnitRequest.getCount());
    userLectureStats.setResumeFrom(userUnitRequest.getTime());

    String type = userUnitRequest.getType();
    String currentState = userUnitRequest.getCurrentState();

    if (currentState.equalsIgnoreCase("idle")) {
      type = "onIdle";
    }

    JWPlayerEventRequest jwPlayerEventRequest = null;

    switch (type) {

      case "onFirstFrame":
        jwPlayerEventRequest = new Gson()
            .fromJson(userLectureStats.getOnFirstFrame(), JWPlayerEventRequest.class);
        if (jwPlayerEventRequest == null) {
          jwPlayerEventRequest = new JWPlayerEventRequest();
        }
        jwPlayerEventRequest.setCount(jwPlayerEventRequest.getCount() + 1);
        jwPlayerEventRequest.setDate(Timestamp.valueOf(LocalDateTime.now()));
        jwPlayerEventRequest.setSeek(userUnitRequest.getTime());
        jwPlayerEventRequest.setState(userUnitRequest.getCurrentState());
        String toJson = new Gson().toJson(jwPlayerEventRequest);
        userLectureStats.setOnFirstFrame(toJson);

        break;

      case "onIdle":
        jwPlayerEventRequest = new Gson()
            .fromJson(userLectureStats.getOnIdle(), JWPlayerEventRequest.class);
        if (jwPlayerEventRequest == null) {
          jwPlayerEventRequest = new JWPlayerEventRequest();
        }
        jwPlayerEventRequest.setCount(jwPlayerEventRequest.getCount() + 1);
        jwPlayerEventRequest.setDate(Timestamp.valueOf(LocalDateTime.now()));
        jwPlayerEventRequest.setSeek(userUnitRequest.getTime());
        jwPlayerEventRequest.setState(userUnitRequest.getCurrentState());
        userLectureStats.setOnIdle(new Gson().toJson(jwPlayerEventRequest));
        break;

      case "onPause":
        jwPlayerEventRequest = new Gson()
            .fromJson(userLectureStats.getOnPause(), JWPlayerEventRequest.class);
        if (jwPlayerEventRequest == null) {
          jwPlayerEventRequest = new JWPlayerEventRequest();
        }
        jwPlayerEventRequest.setCount(jwPlayerEventRequest.getCount() + 1);
        jwPlayerEventRequest.setDate(Timestamp.valueOf(LocalDateTime.now()));
        jwPlayerEventRequest.setSeek(userUnitRequest.getTime());
        jwPlayerEventRequest.setState(userUnitRequest.getCurrentState());
        userLectureStats.setOnPause(new Gson().toJson(jwPlayerEventRequest));
        break;

      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }

    userMetaDataRespository.save(userLectureStats);

  }
}

