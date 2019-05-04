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
import com.neostencil.model.entities.*;
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
public class GoogleSheetToInstitute {

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
    final String spreadsheetId = "1cbCUQzw0JDvcc_lEy8wNHmROqSPDYOZ6F3-9hDif2Vo";

//    final String spreadsheetId = "1HLQ9PtmPwLr7lvFbDlQsXFmjxG7mtO5YZOTSOlSHJo8";
    final String range = "Work Sheet!A:AA";

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

  public void updateInstitute(List<HashMap<String, String>> instituteDetailsList) {
    List<Institute> toUpdateInstitutes = new ArrayList<>();

    for (HashMap<String, String> institute : instituteDetailsList) {

      if (institute.size() > 0) {

        if (institute.containsKey("institute_id") && institute.get("institute_id") != "") {

          try {
            String instituteId = institute.get("institute_id");
            Institute toUpdateInstitute = instituteRepository
                .findById(Integer.parseInt(instituteId));

           /* List<InstituteHighlights> highlightsInfo = new ArrayList<>();
            List<InstituteRelated> relatedsInfo = new ArrayList<>();
            List<InstituteAchievements> achievementsInfo = new ArrayList<>();*/

            String instituteSlug = institute.get("institute_slug");
            if (!TextUtils.isEmpty(instituteSlug)) {
              toUpdateInstitute.setInstituteSlug(instituteSlug);
            } else {
              toUpdateInstitute.setInstituteSlug(null);
              LOGGER.error("S. No " + institute.get("institute_id") + "  " + "in institute slug");
            }

            String instituteName = institute.get("institute_name");
            if (!TextUtils.isEmpty(instituteName)) {
              toUpdateInstitute.setName(instituteName);
              toUpdateInstitute.setTitleTag(instituteName);
            } else {
              LOGGER.error("S. No " + institute.get("institute_id") + "  " + "in institute name");
            }

            String ownerName = institute.get("owner_name");
            if (!TextUtils.isEmpty(ownerName)) {
              toUpdateInstitute.setOwnerName(ownerName);
            } else {
              LOGGER.error("S. No " + institute.get("institute_id") + "  " + "in owner name");
            }

            String instituteAddress = institute.get("institute_address");
            if (!TextUtils.isEmpty(instituteAddress)) {
              toUpdateInstitute.setAddress(instituteAddress);
            } else {
              LOGGER
                  .error("S. No " + institute.get("institute_id") + "  " + "in institute address");
            }

            String contactNo = institute.get("contact_no");
            if (!TextUtils.isEmpty(contactNo)) {
              toUpdateInstitute.setContactNo(contactNo);
            } else {
              toUpdateInstitute.setContactNo(null);
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute contactNo");
            }

            String emailId = institute.get("email_id");
            if (!TextUtils.isEmpty(emailId)) {
              toUpdateInstitute.setEmailId(emailId);
            } else {
              toUpdateInstitute.setEmailId(null);
              LOGGER
                  .error("S. No " + institute.get("institute_id") + "  " + "in institute emailId");
            }

            String instituteUrl = institute.get("institute_url");
            if (!TextUtils.isEmpty(instituteUrl)) {
              toUpdateInstitute.setUrl(instituteUrl);
            } else {
              toUpdateInstitute.setUrl(null);
              LOGGER
                  .error("S. No " + institute.get("institute_id") + "  " + "in institute emailId");
            }

            String instituteFacebookUrl = institute.get("institute_facebook_url");
            if (!TextUtils.isEmpty(instituteFacebookUrl)) {
              toUpdateInstitute.setFacebookUrl(instituteFacebookUrl);
            } else {
              toUpdateInstitute.setFacebookUrl(null);
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute facebook url");
            }

            String instituteGooglePlusUrl = institute.get("institute_google_plus_url");
            if (!TextUtils.isEmpty(instituteGooglePlusUrl)) {
              toUpdateInstitute.setGooglePlusUrl(instituteGooglePlusUrl);
            } else {
              toUpdateInstitute.setGooglePlusUrl(null);
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute google url");
            }

            String instituteTwitterUrl = institute.get("institute_twitter_url");
            if (!TextUtils.isEmpty(instituteTwitterUrl)) {
              toUpdateInstitute.setTwitterUrl(instituteTwitterUrl);
            } else {
              toUpdateInstitute.setTwitterUrl(null);
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute twitter url");
            }

            String instituteLinkedinUrl = institute.get("institute_linkedin_url");
            if (!TextUtils.isEmpty(instituteLinkedinUrl)) {
              toUpdateInstitute.setLinkedinUrl(instituteLinkedinUrl);
            } else {
              toUpdateInstitute.setLinkedinUrl(null);
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute linkedin url");
            }

            String instituteDescription = institute.get("institute_description");
            if (!TextUtils.isEmpty(instituteDescription)) {
              toUpdateInstitute.setDescription(instituteDescription);
            } else {
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute description");
            }

            String achievement1 = institute.get("Achievement card 1");
            /*if (!TextUtils.isEmpty(achievement1)) {

              InstituteAchievements achievementsTmp = new InstituteAchievements();
              Achievements achievements1 = new Achievements();
              achievementsTmp.setAchievements(achievements1);
              achievementsTmp.getAchievements().setIconUrl(
                  "https://lh3.googleusercontent.com/naqRLmYLgDnzjsCBgNnsR6Cdkgpnad6XhMqwelPx6xXseHktB44zs4kzCu2N1Vsl_iy2j-JKE9s1ctbgN01ngJkJKw");
              achievementsTmp.getAchievements().setDescription(achievement1);
              achievementsInfo.add(achievementsTmp);
            } else {
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute achievements 1");
            }

            String achievement2 = institute.get("Achievement card 2");
            if (!TextUtils.isEmpty(achievement2)) {

              InstituteAchievements achievementsTmp = new InstituteAchievements();
              Achievements achievements1 = new Achievements();
              achievementsTmp.setAchievements(achievements1);
              achievementsTmp.getAchievements().setIconUrl("https://lh3.googleusercontent.com/vZ3ftEvAjW0Dv1XFR5o5OKUuman3SOw27fHA7ZWB-GkwuMrHuSlguCY3UttMKVxg3btxHObNUcz21hpH_OTLUR2xQA");
              achievementsTmp.getAchievements().setDescription(achievement2);
              achievementsInfo.add(achievementsTmp);
            } else {
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute achievements 2");
            }

            String achievement3 = institute.get("Achievement card 3");
            if (!TextUtils.isEmpty(achievement3)) {

              InstituteAchievements achievementsTmp = new InstituteAchievements();
              Achievements achievements1 = new Achievements();
              achievementsTmp.setAchievements(achievements1);
              achievementsTmp.getAchievements().setIconUrl(
                  "https://lh3.googleusercontent.com/L8cP6154cjWsh6FxiEQTvE1TwyKaTwBqko96z92w8ioLpaWJvfrvHBCwyB00rPRhbn0TVV0-2CQVZQIqII7IDMji2fse");
              achievementsTmp.getAchievements().setDescription(achievement3);
              achievementsInfo.add(achievementsTmp);
            } else {
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute achievements 3");
            }

            String achievement4 = institute.get("Achievement card 4");
            if (!TextUtils.isEmpty(achievement4)) {

              InstituteAchievements achievementsTmp = new InstituteAchievements();
              Achievements achievements1 = new Achievements();
              achievementsTmp.setAchievements(achievements1);
              achievementsTmp.getAchievements().setIconUrl(
                  "https://lh3.googleusercontent.com/pwyDxqkIywq6Pfl8NZ05SGEJMBuLN-Iy_H2vyrpJQ9ZUt9XFFBu05OETx5wAMOxVtI6-ppxuKcunOj5sJd7r2X2gTOo");
              achievementsTmp.getAchievements().setDescription(achievement4);
              achievementsInfo.add(achievementsTmp);
            } else {
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute achievements 4");
            }

            String faculty = institute.get("Faculty");
            String facultyDescription = institute.get("faculty description ");
            if (!TextUtils.isEmpty(faculty) && !TextUtils.isEmpty(facultyDescription)) {

              InstituteHighlights instituteHighlightsTmp = new InstituteHighlights();
              instituteHighlightsTmp.setInstitute(toUpdateInstitute);
              Highlights highlights = new Highlights();
              instituteHighlightsTmp.setHighlights(highlights);
              instituteHighlightsTmp.getHighlights().setText(faculty);
              instituteHighlightsTmp.getHighlights().setDescription(facultyDescription);

              highlightsInfo.add(instituteHighlightsTmp);
            } else {
              LOGGER.error(
                  "S. No " + institute.get("institute_id") + "  " + "in institute highlight");
            }

            String stellarResults = institute.get("Stellar Results");
            String stellarResultsDescription = institute.get("\"Stellar Results\" description ");

            if (!TextUtils.isEmpty(stellarResults) && !TextUtils
                .isEmpty(stellarResultsDescription)) {

              InstituteRelated instituteRelatedTmp = new InstituteRelated();
              instituteRelatedTmp.setInstitute(toUpdateInstitute);
              Highlights highlights = new Highlights();
              instituteRelatedTmp.setHighlights(highlights);
              instituteRelatedTmp.getHighlights().setText(stellarResults);
              instituteRelatedTmp.getHighlights().setDescription(stellarResultsDescription);

              relatedsInfo.add(instituteRelatedTmp);
            } else {
              LOGGER
                  .error("S. No " + institute.get("institute_id") + "  " + "in institute related");
            }

            if(highlightsInfo.size()>0){
              toUpdateInstitute.setInstituteHighlights(highlightsInfo);
            }

            if(relatedsInfo.size()>0){
              toUpdateInstitute.setInstituteRelateds(relatedsInfo);
            }

            if(achievementsInfo.size()>0){
              toUpdateInstitute.setInstituteAchievements(achievementsInfo);
            }*/
            toUpdateInstitutes.add(toUpdateInstitute);
            baseService.createInstitute(toUpdateInstitute);


          } catch (Exception e) {
            e.printStackTrace();
          }

        }

      }
    }

  }

}
