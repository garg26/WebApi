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
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.Institute;
import com.neostencil.model.entities.MetaInformation;
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
import java.util.List;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleSheetToCourseMeta {

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

  private static final List<String> SCOPES = Collections
      .singletonList(SheetsScopes.SPREADSHEETS_READONLY);
  private static final String CREDENTIALS_FILE_PATH = "/google-credentials.json";

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetToInstitute.class);

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
    GoogleClientSecrets clientSecrets = GoogleClientSecrets
        .load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  public List<HashMap<String, String>> getInstituteDetails()
      throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> courseDetails = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1S4242yxI48hm82bl2YRchQl6QKducomHj21prXTqj8I";

//    final String spreadsheetId = "1HLQ9PtmPwLr7lvFbDlQsXFmjxG7mtO5YZOTSOlSHJo8";
    final String range = "Course Meta Tags!A:D";

    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
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

  public void updateCourseMeta(List<HashMap<String, String>> courseDetails) {
    List<Course> toUpdateCourse = new ArrayList<>();

    for (HashMap<String, String> course : courseDetails) {

      if (course.size() > 0) {

        if (course.containsKey("Post Name") && course.get("Post Name") != "") {

          try {
            String postName = course.get("Post Name");

            Course byCourseOldSlug = courseRepository.findByCourseOldSlug(postName);

            if (byCourseOldSlug != null) {
              List<MetaInformation> metaList = byCourseOldSlug.getMetaList();
              String meta_title = course.get("Meta Title");
              if (!TextUtils.isEmpty(meta_title) && !meta_title.equals("NULL")) {
                byCourseOldSlug.setTitleTag(meta_title + " | NeoStencil");
              } else {
                byCourseOldSlug.setTitleTag(byCourseOldSlug.getCourseTitle() + " | NeoStencil");
                LOGGER.error("id " + postName + "  " + "in title");
              }

              String meta_description = course.get("Meta Description");

              if (!TextUtils.isEmpty(meta_description)) {
                MetaInformation metaInformation = new MetaInformation();
                if (metaList != null && metaList.size() > 0) {

                  metaInformation.setAttributeType("name");
                  metaInformation.setContent(meta_description);
                  metaInformation.setType("description");

                } else {
                  metaList = new ArrayList<>();
                  metaInformation.setAttributeType("name");
                  metaInformation.setContent(meta_description);
                  metaInformation.setType("description");
                }

                metaList.add(metaInformation);
              }
              else{
                MetaInformation metaInformation = new MetaInformation();
                if (metaList != null && metaList.size() > 0) {

                  metaInformation.setAttributeType("name");
                  metaInformation.setContent(byCourseOldSlug.getCourseAbout());
                  metaInformation.setType("description");

                } else {
                  metaList = new ArrayList<>();
                  metaInformation.setAttributeType("name");
                  metaInformation.setContent(byCourseOldSlug.getCourseAbout());
                  metaInformation.setType("description");
                }

                metaList.add(metaInformation);
              }
              byCourseOldSlug.setMetaList(metaList);
              try {
                baseService.updateCourse(byCourseOldSlug.getId(), byCourseOldSlug);
              } catch (Exception e) {
                System.err.println(
                    "something wrong with course " + byCourseOldSlug.getId() + "  "
                        + byCourseOldSlug.getCourseOldSlug());
              }
            } else {
              System.err.println("Not Found " + postName);
            }


          } catch (Exception e) {
            e.printStackTrace();
          }

        }

      }
    }

  }


}
