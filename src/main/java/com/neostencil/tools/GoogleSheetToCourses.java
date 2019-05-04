package com.neostencil.tools;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.neostencil.base.BaseService;
import com.neostencil.common.CommonUtil;
import com.neostencil.common.enums.CourseCategory;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.CourseType;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.Subjects;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.Highlights;
import com.neostencil.model.entities.Institute;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.repositories.CourseRepository;
import com.neostencil.model.repositories.InstituteRepository;
import com.neostencil.model.repositories.TeacherDetailsRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GoogleSheetToCourses {

  @Autowired
  CourseRepository courseRepository;
  @Autowired
  TeacherDetailsRepository teacherDetailsRepository;
  @Autowired
  InstituteRepository instituteRepository;
  @Autowired
  BaseService baseService;


  private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
   * your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES =
      Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
  private static final String CREDENTIALS_FILE_PATH = "/google-credentials.json";

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetToCourses.class);

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = GoogleSheetToCourses.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline").build();
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  public List<HashMap<String, String>> getCourseDetails()
      throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> courseDetails = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1GzgtpApEBSWFgKm4Zzv5a-lzTtHmUZQI5A6IABXZxl8";

//    final String spreadsheetId = "1HLQ9PtmPwLr7lvFbDlQsXFmjxG7mtO5YZOTSOlSHJo8";
    final String range = "Content Work Sheet!A1:AW";

    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME).build();
    ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    List<List<Object>> values = response.getValues();

    int index1 = 0;

    for (List row : values) {
      index1++;
      int index2 = 0;
      HashMap<String, String> singleCourse = new HashMap<>();
      for (Object temp : row) {

        if (index1 == 1) {
          columnIndexMapping.put(index2, temp.toString());
        } else {
          singleCourse.put(columnIndexMapping.get(index2), temp.toString());
        }

        index2++;
      }
      courseDetails.add(singleCourse);

    }
    return courseDetails;
  }

  public void updateCourses(List<HashMap<String, String>> courseDetailsList) {
    List<Course> toUpdateCourses = new ArrayList<>();
    for (HashMap<String, String> course : courseDetailsList) {
      if (course.size() > 0) {
        if (course.containsKey("Course ID") && course.get("Course ID") != "") {
          try {

            String courseID = course.get("Course ID");
           /* if(courseID.equals("31437"))
            {

            }
            else{
              continue;
            }
            */

            Course toUpdate = courseRepository.findById(Integer.parseInt(courseID));
            /*Set<CourseInclusion> courseInclusions = new HashSet<>();
            Set<CourseAdditional> additionals = new HashSet<>();*/
            String ch1 = course.get("CH1");
            String chText1 = course.get("CHText1");
            String ch2 = course.get("CH2");
            String chText2 = course.get("CHText2");
            String ch3 = course.get("CH3");
            String chText3 = course.get("CHText3");
            String ch4 = course.get("CH4");
            String chText4 = course.get("CHText4");

            String ci1 = course.get("CI1");
            String ciText1 = course.get("CIText1");
            String ci2 = course.get("CI2");
            String ciText2 = course.get("CIText2");
            String ci3 = course.get("CI3");
            String ciText3 = course.get("CIText3");
            String ci4 = course.get("CI4");
            String ciText4 = course.get("CIText4");

            String ranking = course.get("Ranking");
            if (!TextUtils.isEmpty(ranking)) {
              toUpdate.setPosition(Integer.parseInt(ranking));
              toUpdate.setPopular(true);
            } else {
              toUpdate.setPosition(99999);
              toUpdate.setPopular(false);
            }
            String courseType = course.get("Course Type");
            if (!TextUtils.isEmpty(courseType)) {
              CourseType type = null;
              if (courseType.equals("ALL_INLCUSIVE;")) {

                type = CourseType.ALL_INLCUSIVE;

              } else {

                type = CourseType.valueOf(courseType);
              }
              if (type != null) {
                toUpdate.setCourseType(type);
              } else {
                toUpdate.setCourseType(CourseType.ALL_INLCUSIVE);
                LOGGER.error("S. No " + course.get("Course ID") + "  " + "in course type");
              }
            }

            String rating = course.get("Course Rating");
            if (!TextUtils.isEmpty(rating)) {
              toUpdate.setCourseRating(Float.parseFloat(rating));
            } else {
              toUpdate.setCourseRating(5);
            }

            String validity = course.get("Course Validity");
            if (!TextUtils.isEmpty(validity)) {
              toUpdate.setCourseValidity(validity);
            } else {
              toUpdate.setCourseValidity(null);
            }

            if (toUpdate.getCourseType().equals(CourseType.LECTURES_ONLY)) {
              String session = course.get("No of sessions");
              toUpdate.setNoOfSessions(session);
            } else if (toUpdate.getCourseType().equals(CourseType.TEST_SERIES_ONLY)) {
              String test = course.get("No of Tests");
              toUpdate.setNoOfSessions(test);
            } else if (toUpdate.getCourseType().equals(CourseType.LECTURES_TESTSERIES)) {
              String session = course.get("No of sessions");
              String test = course.get("No of Tests");
              toUpdate.setNoOfSessions(session + "," + test);
            } else {
              String session = course.get("No of sessions");
              toUpdate.setNoOfSessions(session);
            }

            Set<CourseBatch> batchesList = new LinkedHashSet<CourseBatch>();

            String batchValidity = course.get("Batch Validity");
            if (!TextUtils.isEmpty(batchValidity)) {
              Set<CourseBatch> batches = toUpdate.getBatches();
              CourseBatch next = batches.iterator().next();
              String date = CommonUtil.convertStringIntoDateFormat(next.getStartDate());
              toUpdate.setStartDate(date);
              if (batches != null && batches.size() > 0) {
                for (CourseBatch courseBatch : batches) {

                  courseBatch.setValidity(Integer.parseInt(batchValidity));
                  courseBatch.setValidityDisplay(validity);
                  if (courseBatch != null && courseBatch.getStatus() != null
                      && courseBatch.getStatus().equals(PublishStatus.publish)) {

                    batchesList.add(courseBatch);
                  }
                }
              }
            }

            if (batchesList != null && batchesList.size() > 0) {
              Double coursePrice = baseService.minPriceCourseCalculate(batchesList);
              if (coursePrice != null) {
                toUpdate.setPrice(coursePrice);
              }
            }

            String demoVideo = course.get("Demo Video (Vijay)");
            if (!TextUtils.isEmpty(demoVideo)) {
              toUpdate.setDemoLecture(demoVideo);
            } else {
              toUpdate.setDemoLecture(null);
              LOGGER.error("S. No " + course.get("Course ID") + "  " + "in course demo");

            }

            String course_examSegment = course.get("Course ExamSegment");
            if (!TextUtils.isEmpty(course_examSegment)) {
              ExamSegmentTypes examSegmentTypes = ExamSegmentTypes.valueOf(course_examSegment);
              if (examSegmentTypes != null) {
                toUpdate.setCourseExamSegment(course_examSegment);
              } else {
                LOGGER.error("S. No " + course.get("Course ID") + "  " + "in course exam segment");
              }
            }

            String course_category = course.get("Course Category");
            if (!TextUtils.isEmpty(course_category)) {
              CourseCategory courseCategory = CourseCategory.valueOf(course_category);
              if (courseCategory != null) {
                toUpdate.setCourseCategory(course_category);
              } else {
                LOGGER.error("S. No " + course.get("Course ID") + "  " + "in course category");
              }
            }

            String course_sub_category = course.get("Course Sub Category");
            if (!TextUtils.isEmpty(course_sub_category)) {
              CourseSubCategory courseSubCategory = CourseSubCategory.valueOf(course_sub_category);

              if (courseSubCategory != null) {
                toUpdate.setCourseSubCategory(course_sub_category);
              } else {
                LOGGER.error("S. No " + course.get("Course ID") + "  " + "in course sub category");
              }
            }
            String course_subject = course.get("Course Subject ");

            if (!TextUtils.isEmpty(course_subject)) {
              String[] split = course_subject.split(",");
              List<String> subjectsList = new ArrayList<>();
              for (String s : split) {
                Subjects subjects = Subjects.valueOf(s);
                if (subjects != null) {
                  String jsonValue = subjects.getJsonValue();
                  subjectsList.add(jsonValue);
                } else {
                  LOGGER.error("S. No " + course.get("Course ID") + "  " + "in course subject");
                }

              }
              String subjects = StringUtils.join(subjectsList, ',');
              toUpdate.setCourseSubject(subjects);
            } else {
              LOGGER.warn("S. No " + course.get("Course ID") + "  " + "in course subject");
            }

            String courseTitle = course.get("Course Title (Shantanu)");
            if (!TextUtils.isEmpty(courseTitle)) {
              toUpdate.setCourseTitle(courseTitle);
              toUpdate.setCourseName(courseTitle);
            } else {
              toUpdate.setCourseTitle(null);
              LOGGER.warn("S. No " + course.get("Course ID") + "  " + "in course title");
            }

            String teacherName = course.get("Teacher (Shantanu)");
            if (!TextUtils.isEmpty(teacherName)) {
              TeacherDetails byTeacherName =
                  teacherDetailsRepository.findByTeacherName(teacherName);
              if (byTeacherName != null && byTeacherName.getId() != 0) {
                Set<TeacherDetails> teacherDetails = new HashSet<>();
                TeacherDetails teacherDetails1 = new TeacherDetails();
                teacherDetails1.setId(byTeacherName.getId());
                teacherDetails.add(teacherDetails1);
                toUpdate.setInstructors(teacherDetails);
              } else {
                LOGGER.error("S. No " + course.get("Course ID") + "  " + "in teacher");
              }
            } else {
              LOGGER.warn("S. No " + course.get("Course ID") + "  " + "in teacher");
            }

            String instituteName = course.get("Institute (Shantanu)");
            if (!TextUtils.isEmpty(instituteName)) {
              Institute instituteRepositoryByName = instituteRepository.findByName(instituteName);
              if (instituteRepositoryByName != null && instituteRepositoryByName.getId() != 0) {
                Institute institute = new Institute();
                institute.setId(instituteRepositoryByName.getId());
                toUpdate.setInstitute(institute);
              } else {
                LOGGER.error("S. No " + course.get("Course ID") + "  " + "in institute");
              }
            } else {
              Institute instituteRepositoryByName = instituteRepository
                  .findByInstituteSlug("neostencil");
              if (instituteRepositoryByName != null && instituteRepositoryByName.getId() != 0) {
                Institute institute = new Institute();
                institute.setId(instituteRepositoryByName.getId());
                toUpdate.setInstitute(institute);
              }
              LOGGER.warn("S. No " + course.get("Course ID") + "  " + "in institute");
            }

            String about = course.get("Course About (Anna/Gautam)");
            if (!TextUtils.isEmpty(about)) {
              toUpdate.setCourseAbout(about);
            } else {
              toUpdate.setCourseAbout(null);
              LOGGER.warn("S. No " + course.get("Course ID") + "  " + "in course about");
            }

            String suitable = course.get("Suitable for ");
            if (!TextUtils.isEmpty(suitable)) {
              toUpdate.setSuitableFor(suitable);
            } else {
              toUpdate.setSuitableFor(null);
              LOGGER.warn("S. No " + course.get("Course ID") + "  " + "in course suitable");
            }

            String medium = course.get("Course Medium");
            if (!TextUtils.isEmpty(medium)) {
              toUpdate.setCourseMedium(medium);
            } else {
              toUpdate.setCourseMedium(null);
              LOGGER.warn("S. No " + course.get("Course ID") + "  " + "in course medium");
            }

            String aboutInstructor = course.get("About Instructor");
            if (!TextUtils.isEmpty(aboutInstructor)) {
              toUpdate.setAboutInstructor(aboutInstructor);
            } else {
              toUpdate.setAboutInstructor(null);
              LOGGER
                  .warn("S. No " + course.get("Course ID") + "  " + "in course about instructor");
            }

            /*if (!TextUtils.isEmpty(ch1) && !TextUtils.isEmpty(chText1)) {
              CourseAdditional courseAdditonalTmp = new CourseAdditional();
              courseAdditonalTmp.setCourse(toUpdate);
              Highlights highlights = new Highlights();
              courseAdditonalTmp.setHighlights(highlights);
              courseAdditonalTmp.getHighlights().setText(ch1);
              courseAdditonalTmp.getHighlights().setDescription(chText1);
              additionals.add(courseAdditonalTmp);
            } else {
              LOGGER
                  .error("S. No " + course.get("Course ID") + "  " + "in course ch1 and chtext1");
            }

            if (!TextUtils.isEmpty(ch2) && !TextUtils.isEmpty(chText2)) {
              CourseAdditional courseAdditonalTmp = new CourseAdditional();
              courseAdditonalTmp.setCourse(toUpdate);
              Highlights highlights = new Highlights();
              courseAdditonalTmp.setHighlights(highlights);
              courseAdditonalTmp.getHighlights().setText(ch2);
              courseAdditonalTmp.getHighlights().setDescription(chText2);
              additionals.add(courseAdditonalTmp);
            } else {
              LOGGER
                  .error("S. No " + course.get("Course ID") + "  " + "in course ch12and chtext2");
            }

            if (!TextUtils.isEmpty(ch3) && !TextUtils.isEmpty(chText3)) {
              CourseAdditional courseAdditonalTmp = new CourseAdditional();
              courseAdditonalTmp.setCourse(toUpdate);
              Highlights highlights = new Highlights();
              courseAdditonalTmp.setHighlights(highlights);
              courseAdditonalTmp.getHighlights().setText(ch3);
              courseAdditonalTmp.getHighlights().setDescription(chText3);
              additionals.add(courseAdditonalTmp);
            } else {
              LOGGER
                  .error("S. No " + course.get("Course ID") + "  " + "in course ch3 and chtext3");
            }

            if (!TextUtils.isEmpty(ch4) && !TextUtils.isEmpty(chText4)) {
              CourseAdditional courseAdditonalTmp = new CourseAdditional();
              courseAdditonalTmp.setCourse(toUpdate);
              Highlights highlights = new Highlights();
              courseAdditonalTmp.setHighlights(highlights);
              courseAdditonalTmp.getHighlights().setText(ch4);
              courseAdditonalTmp.getHighlights().setDescription(chText4);
              additionals.add(courseAdditonalTmp);
            } else {
              LOGGER
                  .error("S. No " + course.get("Course ID") + "  " + "in course ch4 and chtext4");
            }

            if (!TextUtils.isEmpty(ci1) && !TextUtils.isEmpty(ciText1)) {
              CourseInclusion inclusion = new CourseInclusion();
              inclusion.setCourse(toUpdate);
              Highlights highlights = new Highlights();
              inclusion.setHighlights(highlights);
              inclusion.getHighlights().setText(ci1);
              inclusion.getHighlights().setDescription(ciText1);
              courseInclusions.add(inclusion);
            } else {
              LOGGER
                  .error("S. No " + course.get("Course ID") + "  " + "in course ci1 and citext1");
            }

            if (!TextUtils.isEmpty(ci2) && !TextUtils.isEmpty(ciText2)) {
              CourseInclusion inclusion = new CourseInclusion();
              inclusion.setCourse(toUpdate);
              Highlights highlights = new Highlights();
              inclusion.setHighlights(highlights);
              inclusion.getHighlights().setText(ci2);
              inclusion.getHighlights().setDescription(ciText2);
              courseInclusions.add(inclusion);

            } else {
              LOGGER
                  .error("S. No " + course.get("Course ID") + "  " + "in course ci2 and citext2");
            }

            if (!TextUtils.isEmpty(ci3) && !TextUtils.isEmpty(ciText3)) {
              CourseInclusion inclusion = new CourseInclusion();
              Highlights highlights = new Highlights();
              inclusion.setHighlights(highlights);
              inclusion.setCourse(toUpdate);
              inclusion.getHighlights().setText(ci3);
              inclusion.getHighlights().setDescription(ciText3);
              courseInclusions.add(inclusion);
            } else {
              LOGGER
                  .error("S. No " + course.get("Course ID") + "  " + "in course ci3 and citext3");
            }

            if (!TextUtils.isEmpty(ci4) && !TextUtils.isEmpty(ciText4)) {
              CourseInclusion inclusion = new CourseInclusion();
              Highlights highlights = new Highlights();
              inclusion.setHighlights(highlights);
              inclusion.setCourse(toUpdate);
              inclusion.getHighlights().setText(ci4);
              inclusion.getHighlights().setDescription(ciText4);
              courseInclusions.add(inclusion);

            } else {
              LOGGER
                  .error("S. No " + course.get("Course ID") + "  " + "in course ci4 and citext4");
            }

            *//*if (courseInclusions.size() > 0) {
              toUpdate.getCourseInclusions().clear();
              toUpdate.getCourseInclusions().addAll(courseInclusions);
            }
            if (additionals.size() > 0) {
              toUpdate.getCourseAdditional().clear();
              toUpdate.getCourseAdditional().addAll(additionals);
            }*//*
*/
            try {

              baseService.updateCourse(toUpdate.getId(), toUpdate);


            } catch (Exception e) {
              System.err.println(
                  "something wrong with course " + toUpdate.getId() + "  " + toUpdate.toString());
            }


          } catch (Exception e) {
            e.printStackTrace();
            System.err.println("exception in " + course.get("Course ID"));
          }
        } else {
          System.err.println(
              "something wrong with course " + course.get("Course ID") + "  " + course.toString());
        }

        System.out.println();

      }
    }

    // for(Course c: toUpdateCourses)
    // {
    // Course course = baseService.updateCourse(c.getId(), c);
    // if(course==null){
    // System.err.println("something wrong with course " + c.getId() + " " + course.toString());
    // }
    //
    // }

  }

  /**
   * Prints the names and majors of students in a sample spreadsheet:
   * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
   */
  // public static void main(String... args) throws IOException, GeneralSecurityException {
  // // Build a new authorized API client service.
  // SpringApplication.run(GoogleSheetToCourses.class, args);
  // List<HashMap<String, String>> courseDetails = getCourseDetails();
  // updateCourses(courseDetails);
  // }
}
