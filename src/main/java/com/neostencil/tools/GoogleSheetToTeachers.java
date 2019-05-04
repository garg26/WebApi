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
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.Subjects;
import com.neostencil.common.enums.TeacherCategory;
import com.neostencil.common.enums.TeacherSubjects;
import com.neostencil.model.entities.*;
import com.neostencil.model.repositories.InstituteRepository;
import com.neostencil.model.repositories.TeacherDetailsRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleSheetToTeachers {

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

  public List<HashMap<String, String>> getTeacherDetails()
      throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> courseDetails = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1cFa71YcNEQ4HhWFy4si9oktDrPUwdo16HAXTj051HkM";

    // final String spreadsheetId = "1HLQ9PtmPwLr7lvFbDlQsXFmjxG7mtO5YZOTSOlSHJo8";
    final String range = "Work Sheet!A1:AN";

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

  public void updateTeachers(List<HashMap<String, String>> teacherDetailsList) {

    List<TeacherDetails> toUpdateTeachers = new ArrayList<>();

    for (HashMap<String, String> teacher : teacherDetailsList) {

      if (teacher.size() > 0) {

        if (teacher.containsKey("id") && teacher.get("id") != "") {

          try {
            String teacherId = teacher.get("id");

            TeacherDetails toUpdateTeacher =
                teacherDetailsRepository.findById(Integer.parseInt(teacherId));

            /*List<TeacherAchievements> teacherAchievements = new ArrayList<>();
            List<TeacherEducation> education = new ArrayList<>();
            List<TeacherExperience> experience = new ArrayList<>();*/

            String teacher_slug = teacher.get("teacher_slug").trim();
            if(!TextUtils.isEmpty(teacher_slug)){
              int i = teacher_slug.lastIndexOf("/");
              String substring = teacher_slug.substring(i + 1);
              toUpdateTeacher.setSlug(substring);
            }
            else{
              LOGGER.error("id " + teacher.get("id") + "  " + "in teacher slug");
            }
            String teacherName = teacher.get("teacher_name");
            if (!TextUtils.isEmpty(teacherName)) {
              toUpdateTeacher.setTeacherName(teacherName);
            } else {
              LOGGER.error("id " + teacher.get("id") + "  " + "in teacher name");
            }

            String instituteId = teacher.get("institute_id");
            if (!TextUtils.isEmpty(instituteId)) {
              Institute institute = new Institute();
              institute.setId(Integer.parseInt(instituteId));
              toUpdateTeacher.setInstitute(institute);
            } else {
              Institute instituteRepositoryByName = instituteRepository
                  .findByInstituteSlug("neostencil");
              if (instituteRepositoryByName != null && instituteRepositoryByName.getId() != 0) {
                Institute institute = new Institute();
                institute.setId(instituteRepositoryByName.getId());
                toUpdateTeacher.setInstitute(institute);
                LOGGER.error("id " + teacher.get("id") + "  " + "in teacher institute");

              }

            }

            String teacherLocation = teacher.get("teacher_location");
            if (!TextUtils.isEmpty(teacherLocation)) {
              toUpdateTeacher.setTeacherLocation(teacherLocation);
            } else {
              LOGGER.error("id " + teacher.get("id") + "  " + "in teacher location");
            }

            String teacherEmailId = teacher.get("teacher_email_id");
            if(!TextUtils.isEmpty(teacherEmailId)){
              toUpdateTeacher.setEmailId(teacherEmailId);
            }
            else{
              LOGGER.error("id " + teacher.get("id") + "  " + "in teacher emailId");
            }
            String teacherWebsite = teacher.get("teacher_website");
            if (!TextUtils.isEmpty(teacherWebsite)) {
              toUpdateTeacher.setWebsite(teacherWebsite);
            } else {
              LOGGER.error("id " + teacher.get("id") + "  " + "in teacher website");
            }

            String teacherExamSegment = teacher.get("teacher_exam_segment");
            if (!TextUtils.isEmpty(teacherExamSegment)) {
              String[] split = teacherExamSegment.split(", ");
              Set<String> examSegmentTypesSet = new HashSet<>();
              for (String str : split) {
                ExamSegmentTypes examSegmentTypes = ExamSegmentTypes.valueOf(str);
                if (examSegmentTypes != null) {
                  String jsonValue = examSegmentTypes.jsonValue();
                  examSegmentTypesSet.add(jsonValue);
                } else {
                  LOGGER.error("id " + teacher.get("id") + "  " + "in teacher exam");
                }
              }
              String s = StringUtils.join(examSegmentTypesSet, ',');
              toUpdateTeacher.setTeacherExamSegment(s);
            } else {
              LOGGER.error("id " + teacher.get("id") + "  " + "in teacher exam");
            }

            String category = teacher.get("teacher_category");
            if (!TextUtils.isEmpty(category)) {
              String[] split = category.split(", ");
              Set<String> teacherCategories = new HashSet<>();

              for (String str : split) {
                TeacherCategory teacherCategory = TeacherCategory.valueOf(str);
                if (teacherCategory != null) {
                  String jsonValue = teacherCategory.jsonValue();
                  teacherCategories.add(jsonValue);
                } else {
                  LOGGER.error("id " + teacher.get("id") + "  " + "in teacher category");
                }
              }
              String s = StringUtils.join(teacherCategories, ',');
              toUpdateTeacher.setTeacherCategory(s);
            } else {
              LOGGER.error("id " + teacher.get("id") + "  " + "in teacher category");
            }

            String subjects = teacher.get("subjects");
            if (!TextUtils.isEmpty(subjects)) {
              String[] split = subjects.split(", ");
              List<String> subjectList = new ArrayList<>();

              for (String str : split) {
                TeacherSubjects teacherSubjects = TeacherSubjects.valueOf(str);
                if (teacherSubjects != null) {
                  String jsonValue = teacherSubjects.getJsonValue();
                  subjectList.add(jsonValue);
                } else {
                  LOGGER.error("id " + teacher.get("id") + "  " + "in teacher subject enum");
                }
              }
              String s = StringUtils.join(subjectList, ',');
              toUpdateTeacher.setSubjects(s);
            } else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher subject");
            }

            String teacherRating = teacher.get("teacher_rating");
            if (!TextUtils.isEmpty(teacherRating)) {
              toUpdateTeacher.setTeacherRating(Float.parseFloat(teacherRating));
            } else {
              toUpdateTeacher.setTeacherRating(5);
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher rating");
            }

            String totalExperience = teacher.get("total_experience");
            if (!TextUtils.isEmpty(totalExperience)) {
              toUpdateTeacher.setTotalExperience(Integer.parseInt(totalExperience));
            } else {
              toUpdateTeacher.setTotalExperience(0);
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher experience");
            }

            String about = teacher.get("teacher_about (in Para form)");
            if (!TextUtils.isEmpty(about)) {
              toUpdateTeacher.setTeacherDescription(about);
            } else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher about");
            }

            String education1 = teacher.get("teacher education heading 1");
            String educationDescription1 =
                teacher.get("teacher education description 1 (Max 210 Charecters)");

            /*if(!TextUtils.isEmpty(education1) && !TextUtils.isEmpty(educationDescription1)){
              TeacherEducation teacherEducationTmp = new TeacherEducation();
              teacherEducationTmp.setTeacherDetails(toUpdateTeacher);
              Highlights highlights = new Highlights();
              teacherEducationTmp.setHighlights(highlights);
              teacherEducationTmp.getHighlights().setText(education1);
              teacherEducationTmp.getHighlights().setDescription(educationDescription1);
              education.add(teacherEducationTmp);
            }
            else{
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher education 1");
            }

            String education2 = teacher.get("teacher education heading 2");
            String educationDescription2 =
                teacher.get("teacher education description 2 (Max 210 Charecters)");

            if(!TextUtils.isEmpty(education2) && !TextUtils.isEmpty(educationDescription2)){
              TeacherEducation teacherEducationTmp = new TeacherEducation();
              teacherEducationTmp.setTeacherDetails(toUpdateTeacher);
              Highlights highlights = new Highlights();
              teacherEducationTmp.setHighlights(highlights);
              teacherEducationTmp.getHighlights().setText(education2);
              teacherEducationTmp.getHighlights().setDescription(educationDescription2);
              education.add(teacherEducationTmp);
            }

            else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher education 2");
            }

            String education3 = teacher.get("teacher education heading 3");
            String educationDescription3 = teacher.get("teacher education description 3 (Max 210 Charecters)");
            if(!TextUtils.isEmpty(education3) && !TextUtils.isEmpty(educationDescription3)) {
              TeacherEducation teacherEducationTmp = new TeacherEducation();
              teacherEducationTmp.setTeacherDetails(toUpdateTeacher);
              Highlights highlights = new Highlights();
              teacherEducationTmp.setHighlights(highlights);
              teacherEducationTmp.getHighlights().setText(education3);
              teacherEducationTmp.getHighlights().setDescription(educationDescription3);
              education.add(teacherEducationTmp);
            }

            else{
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher education 3");
            }

            String education4 = teacher.get("teacher education heading 4");
            String educationDescription4 =
                teacher.get("teacher education description 4 (Max 210 Charecters)");

            if(!TextUtils.isEmpty(education4) && !TextUtils.isEmpty(educationDescription4)) {
              TeacherEducation teacherEducationTmp = new TeacherEducation();
              teacherEducationTmp.setTeacherDetails(toUpdateTeacher);
              Highlights highlights = new Highlights();
              teacherEducationTmp.setHighlights(highlights);
              teacherEducationTmp.getHighlights().setText(education4);
              teacherEducationTmp.getHighlights().setDescription(educationDescription4);
              education.add(teacherEducationTmp);
            }else{
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher education 4");
            }

            String experience1 = teacher.get("teacher experience heading 1");
            String experienceDescription1 =
                teacher.get("teacher experience description 1 (Max 210 Charecters)");

            if(!TextUtils.isEmpty(experience1) && !TextUtils.isEmpty(experienceDescription1)){
              TeacherExperience teacherExperienceTmp = new TeacherExperience();
              teacherExperienceTmp.setTeacherDetails(toUpdateTeacher);
              Highlights highlights = new Highlights();
              teacherExperienceTmp.setHighlights(highlights);
              teacherExperienceTmp.getHighlights().setText(experience1);
              teacherExperienceTmp.getHighlights().setDescription(experienceDescription1);
              experience.add(teacherExperienceTmp);
            }
            else{
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher experience 1");
            }

            String experience2 = teacher.get("teacher experience heading 2");
            String experienceDescription2 =
                teacher.get("teacher experience description 2 (Max 210 Charecters)");

            if(!TextUtils.isEmpty(experience2) && !TextUtils.isEmpty(experienceDescription2)){
              TeacherExperience teacherExperienceTmp = new TeacherExperience();
              teacherExperienceTmp.setTeacherDetails(toUpdateTeacher);
              Highlights highlights = new Highlights();
              teacherExperienceTmp.setHighlights(highlights);
              teacherExperienceTmp.getHighlights().setText(experience2);
              teacherExperienceTmp.getHighlights().setDescription(experienceDescription2);
              experience.add(teacherExperienceTmp);

            } else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher experience 2");
            }

            String experience3 = teacher.get("teacher experience heading 3");
            String experienceDescription3 =
                teacher.get("teacher experience description 3 (Max 210 Charecters)");

            if(!TextUtils.isEmpty(experience3) && !TextUtils.isEmpty(experienceDescription3)){
              TeacherExperience teacherExperienceTmp = new TeacherExperience();
              teacherExperienceTmp.setTeacherDetails(toUpdateTeacher);
              Highlights highlights = new Highlights();
              teacherExperienceTmp.setHighlights(highlights);
              teacherExperienceTmp.getHighlights().setText(experience3);
              teacherExperienceTmp.getHighlights().setDescription(experienceDescription3);
              experience.add(teacherExperienceTmp);
            }
            else{
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher experience 3");
            }

            String experience4 = teacher.get("teacher experience heading 4");
            String experienceDescription4 =
                teacher.get("teacher experience description 4 (Max 210 Charecters)");

            if(!TextUtils.isEmpty(experience4) && !TextUtils.isEmpty(experienceDescription4)){
              TeacherExperience teacherExperienceTmp = new TeacherExperience();
              teacherExperienceTmp.setTeacherDetails(toUpdateTeacher);
              Highlights highlights = new Highlights();
              teacherExperienceTmp.setHighlights(highlights);
              teacherExperienceTmp.getHighlights().setText(experience4);
              teacherExperienceTmp.getHighlights().setDescription(experienceDescription4);
              experience.add(teacherExperienceTmp);
            }
            else{
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher experience 4");
            }

            String highlight1 = teacher.get("teacher highlight 1 (Max 210 Charecters)");
            if (!TextUtils.isEmpty(highlight1)) {

              TeacherAchievements achievementsTmp = new TeacherAchievements();
              Achievements achievements1 = new Achievements();
              achievementsTmp.setAchievements(achievements1);
              achievementsTmp.getAchievements().setIconUrl("https://lh3.googleusercontent.com/naqRLmYLgDnzjsCBgNnsR6Cdkgpnad6XhMqwelPx6xXseHktB44zs4kzCu2N1Vsl_iy2j-JKE9s1ctbgN01ngJkJKw");
              achievementsTmp.getAchievements().setDescription(highlight1);
              teacherAchievements.add(achievementsTmp);
            } else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher achievemnet 1");
            }

            String highlight2 = teacher.get("teacher highlight 2 (Max 210 Charecters)");
            if (!TextUtils.isEmpty(highlight2)) {
              TeacherAchievements achievementsTmp = new TeacherAchievements();
              Achievements achievements1 = new Achievements();
              achievementsTmp.setAchievements(achievements1);
              achievementsTmp.getAchievements().setIconUrl("https://lh3.googleusercontent.com/vZ3ftEvAjW0Dv1XFR5o5OKUuman3SOw27fHA7ZWB-GkwuMrHuSlguCY3UttMKVxg3btxHObNUcz21hpH_OTLUR2xQA");
              achievementsTmp.getAchievements().setDescription(highlight2);
              teacherAchievements.add(achievementsTmp);
            } else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher achievemnet 2");
            }

            String highlight3 = teacher.get("teacher highlight 3 (Max 210 Charecters)");
            if (!TextUtils.isEmpty(highlight3)) {

              TeacherAchievements achievementsTmp = new TeacherAchievements();
              Achievements achievements1 = new Achievements();
              achievementsTmp.setAchievements(achievements1);
              achievementsTmp.getAchievements().setIconUrl("https://lh3.googleusercontent.com/L8cP6154cjWsh6FxiEQTvE1TwyKaTwBqko96z92w8ioLpaWJvfrvHBCwyB00rPRhbn0TVV0-2CQVZQIqII7IDMji2fse");
              achievementsTmp.getAchievements().setDescription(highlight3);
              teacherAchievements.add(achievementsTmp);
            } else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher achievemnet 3");
            }

            String highlight4 = teacher.get("teacher highlight 4 (Max 210 Charecters)");
            if (!TextUtils.isEmpty(highlight4)) {

              TeacherAchievements achievementsTmp = new TeacherAchievements();
              Achievements achievements1 = new Achievements();
              achievementsTmp.setAchievements(achievements1);
              achievementsTmp.getAchievements().setIconUrl("https://lh3.googleusercontent.com/pwyDxqkIywq6Pfl8NZ05SGEJMBuLN-Iy_H2vyrpJQ9ZUt9XFFBu05OETx5wAMOxVtI6-ppxuKcunOj5sJd7r2X2gTOo");
              achievementsTmp.getAchievements().setDescription(highlight4);
              teacherAchievements.add(achievementsTmp);
            } else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher achievemnet 4");
            }
*/
//            String imageUrl = teacher.get("image url");
//            if (!TextUtils.isEmpty(imageUrl)) {
//              toUpdateTeacher.setDisplayPictureUrl(imageUrl);
//            } else {
//              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher image url");
//              toUpdateTeacher.setDisplayPictureUrl(null);
//            }

            String teacherPosition = teacher.get("teacher_position");
            if (!TextUtils.isEmpty(teacherPosition)) {
              toUpdateTeacher.setPosition(Integer.parseInt(teacherPosition));
            } else {
              LOGGER.warn("id " + teacher.get("id") + "  " + "in teacher image url");
              toUpdateTeacher.setPosition(0);
            }

           /* if(teacherAchievements.size()>0){
              toUpdateTeacher.getTeacherAchievements().clear();
              toUpdateTeacher.getTeacherAchievements().addAll(teacherAchievements);
            }

            if(education.size()>0){
              toUpdateTeacher.getEducation().clear();
              toUpdateTeacher.getEducation().addAll(education);
            }

            if(experience.size()>0){
              toUpdateTeacher.getExperience().clear();
              toUpdateTeacher.getExperience().addAll(experience);
            }*/


            System.out.println(toUpdateTeacher.getId() + "   " + toUpdateTeacher.toString());
            try {
              baseService.createTeacher(toUpdateTeacher);
            } catch (Exception e) {
              System.err.println("something wrong with teacher " + toUpdateTeacher.getId() + "  "
                  + toUpdateTeacher.toString());
            }
          } catch (Exception e) {
            System.out.println();
            e.printStackTrace();
          }
        } else {
          System.err.println(
              "something wrong with course " + teacher.get("id") + "  " + teacher.toString());
        }

        System.out.println();
      }

    }

  }
}
