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
import com.neostencil.model.entities.Institute;
import com.neostencil.model.entities.MetaInformation;
import com.neostencil.model.entities.Post;
import com.neostencil.model.entities.PostMetaInformation;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.repositories.CourseRepository;
import com.neostencil.model.repositories.InstituteRepository;
import com.neostencil.model.repositories.PostRepository;
import com.neostencil.model.repositories.TeacherDetailsRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleSheetToPostMeta {

  @Autowired
  CourseRepository courseRepository;
  @Autowired
  TeacherDetailsRepository teacherDetailsRepository;
  @Autowired
  InstituteRepository instituteRepository;
  @Autowired
  BaseService baseService;
  @Autowired
  PostRepository postRepository;

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
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
        JSON_FACTORY,
        clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline").build();
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  public List<HashMap<String, String>> getInstituteDetails()
      throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> courseDetails = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1S4242yxI48hm82bl2YRchQl6QKducomHj21prXTqj8I";

    // final String spreadsheetId =
    // "1HLQ9PtmPwLr7lvFbDlQsXFmjxG7mtO5YZOTSOlSHJo8";
    final String range = "Blogs - Meta Tags!A:E";

    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        getCredentials(HTTP_TRANSPORT))
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

  public void updatePostMeta(List<HashMap<String, String>> instituteDetailsList) {

    List<String> postNotChecked = Arrays.asList("upsc-prelims-answer-key-cut-off", "upsc-syllabus",
        "ras-prelims-answer-key-cut-off", "upsc-civil-ias", "upsc-mains-optional-subjects",
        "upsc-cut-off",
        "bpsc-prelims-answer-key-cut-off", "upsc-prelims-syllabus",
        "bpsc-syllabus-state-services-exam",
        "upsc-capf-answer-key", "ppsc-prelims-answer-key-cut-off", "ese-ies-exam",
        "uppsc-pre-answer-key-question-paper-cut-off", "mpsc-syllabus",
        "wbcs-answer-key-cut-off-question-paper-answers", "mpsc-answer-key-question-paper-cut-off",
        "upsc-capf-answer-key", "ies-syllabus", "upsc-general-studies-syllabus",
        "cgpsc-exam-pattern-syllabus",
        "ias-preparation-timetable-create-your-own-for-2018", "rpsc-syllabus-ras-exam",
        "upsc-ncert-books",
        "opsc-exam-pattern-and-syllabus", "toppers-answer-booklet", "upsc-exam-notification",
        "bpsc-exam-pattern-state-services-exam",
        "smart-strategy-for-2018-upsc-ias-exam-preparation",
        "opsc-answer-key-cut-off-question-paper", "uppsc-exam-pattern-state-services-exam",
        "mpsc-exam-pattern",
        "upsc-prelims-result", "study-material-ias-exam", "mppsc-answer-key-question-paper-cut-off",
        "upsc-eligibility-criteria", "kpsc-kas-prelims-answer-key-kpsc-cut-off",
        "upsc-political-science-and-international-relations-syllabus",
        "upsc-public-administration-syllabus",
        "free-ias-preparation", "upsc-mathematics-syllabus", "upsc-geography-syllabus",
        "upsc-sociology-syllabus", "upsc-indian-language-english-syllabus", "upsc-history-syllabus",
        "ies-ese-cutoff", "upsc-epfo-recruitment-2018",
        "upsc-cds-answer-key-question-paper-cut-off",
        "current-affairs-analysis", "uppsc-syllabus-state-services-exam", "ias-toppers-notes",
        "prepare-upsc-main-hindi-literature", "ies-prelims-answer-key-cut-off",
        "indian-polity-prelims-mcqs",
        "upsc-anthropology-syllabus",
        "ias-toppers-preparation-strategy-ashima-mittal-air-12-cse-2017",
        "mppsc-exam-pattern-state-services-exam", "naga-peace-accord", "upsc-gs-books",
        "answer-writing-challenge-for-optional-subjects", "ies-previous-year-papers",
        "upsc-mains-answer-writing-challenge-sociology", "upsc-management-syllabus",
        "history-optional-questions-answers", "upsc-economics-syllabus",
        "upsc-mains-history-optional",
        "gpsc-class-1-2-answer-key-cut-off", "upsc-psychology-syllabus",
        "how-do-i-choose-an-ias-optional-subject",
        "upsc-mains-answer-writing-challenge-for-geography",
        "upsc-mains-answer-writing-challenge-public-administration", "ras-test-series",
        "upsc-answer-writing-practice-political-science", "upsc-english-syllabus",
        "upsc-hindi-syllabus",
        "upsc-books-topper-recommended", "upsc-physics-syllabus", "upsc-agriculture-syllabus",
        "upsc-law-syllabus", "sociology-answer-writing-practice-week-1-question-set-1",
        "sociology-answer-writing-practice-week-1-question-1", "upsc-prelims-daily-mcq",
        "art-culture-mcqs-day-1-questions", "general-studies-geography-for-upsc",
        "interior-of-the-earth",
        "current-affairs-quiz", "upsc-agriculture-syllabus",
        "upsc-animal-husbandry-and-veterinary-science-syllabus", "upsc-botany-syllabus",
        "upsc-chemistry-syllabus", "upsc-civil-engineering-syllabus",
        "upsc-commerce-and-accountancy-syllabus",
        "upsc-economics-syllabus", "upsc-electrical-engineering-syllabus", "upsc-geology-syllabus",
        "rpsc-syllabus-ras-exam", "upsc-management-syllabus", "upsc-mathematics-syllabus",
        "upsc-mechanical-engineering-syllabus", "upsc-medical-science-syllabus",
        "upsc-physics-syllabus",
        "upsc-psychology-syllabus", "upsc-statistics-syllabus", "upsc-zoology-syllabus",
        "upsc-assamese-syllabus", "upsc-bengali-syllabus", "upsc-bodo-syllabus",
        "upsc-dogri-syllabus",
        "upsc-english-syllabus", "upsc-gujarati-syllabus", "upsc-hindi-syllabus",
        "upsc-kannada-syllabus",
        "upsc-kashmiri-syllabus", "upsc-konkani-syllabus", "upsc-maithili-syllabus",
        "upsc-general-studies-syllabus", "upsc-indian-language-english-syllabus",
        "upsc-malayalam-syllabus",
        "upsc-manipuri-syllabus", "upsc-marathi-syllabus", "upsc-nepali-syllabus",
        "upsc-oriya-syllabus",
        "upsc-punjabi-syllabus", "upsc-sanskrit-syllabus", "upsc-santhali-syllabus",
        "upsc-sindhi-syllabus",
        "upsc-tamil-syllabus", "upsc-telugu-syllabus", "upsc-urdu-syllabus",
        "upsc-answer-writing-practice-anthropology",
        "upsc-mains-answer-writing-challenge-for-geography",
        "upsc-mains-answer-writing-challenge-sociology", "upsc-answer-writing-practice-history",
        "upsc-answer-writing-practice-political-science",
        "upsc-answer-writing-practice-philosophy");

    List<String> notFoundPostSlug = new ArrayList<>();

    for (HashMap<String, String> post : instituteDetailsList) {

      boolean isDesription = false;

      if (post.size() > 0) {

        if (post.containsKey("Post Name") && post.get("Post Name") != "") {

          try {
            String postName = post.get("Post Name");

            if (!postNotChecked.contains(postName)) {

              Post byPostId = postRepository.findByPostSlug(postName);

              if (byPostId != null) {

                String meta_title = post.get("Meta Title");

                if (!TextUtils.isEmpty(meta_title)) {
                  byPostId.setTitleTag(meta_title + " | NeoStencil");
                } else {
                  byPostId.setTitleTag(byPostId.getTitle() + " | NeoStencil");

                }

                List<PostMetaInformation> metaList = byPostId.getMetaList();

                if (metaList != null && metaList.size() > 0) {

                  for (PostMetaInformation metaInformation : metaList) {

                    if (metaInformation.getMetaInformation().getType().equals("description")) {
                      isDesription = true;
                      String meta_description = post.get("Meta Description");

                      if (!TextUtils.isEmpty(meta_description)) {

                        metaInformation.getMetaInformation().setType("description");
                        metaInformation.getMetaInformation().setAttributeType("name");
                        metaInformation.getMetaInformation().setContent(meta_description);

                      } else {

                        if (!TextUtils.isEmpty(byPostId.getText())) {
                          Document parse = Jsoup.parse(byPostId.getText());
                          String text = parse.body().text();
                          String substring = text.substring(0, 320);

                          if (!TextUtils.isEmpty(substring)) {
                            LOGGER.error("Does Not Find 320 Character of Text");

                            metaInformation.getMetaInformation().setType("description");
                            metaInformation.getMetaInformation().setAttributeType("name");
                            metaInformation.getMetaInformation().setContent(substring);
                          }
                        }

                      }
                    }

                  }

                  if (!isDesription) {
                    if (!TextUtils.isEmpty(byPostId.getText())) {
                      Document parse = Jsoup.parse(byPostId.getText());
                      String text = parse.body().text();
                      String substring = text.substring(0, 320);

                      if (!TextUtils.isEmpty(substring)) {
                        LOGGER.error("Does Not Find 320 Character of Text");
                        MetaInformation metaInformation = new MetaInformation();
                        metaInformation.setType("description");
                        metaInformation.setAttributeType("name");
                        metaInformation.setContent(substring);
                        PostMetaInformation postMeta = new PostMetaInformation();
                        postMeta.setMetaInformation(metaInformation);

                        byPostId.getMetaList().add(postMeta);
                      }
                    }
                  }
                  Post a = baseService.updatePost(byPostId.getPostId(), byPostId, 102801);
                  if (a == null) {
                    LOGGER.error("Does Not Update Post " + postName);
                  }
                } else {
                  metaList = new ArrayList<>();
                  String meta_description = post.get("Meta Description");

                  if (!TextUtils.isEmpty(meta_description)) {
                    MetaInformation metaInformation = new MetaInformation();
                    metaInformation.setType("description");
                    metaInformation.setAttributeType("name");
                    metaInformation.setContent(meta_description);
                    PostMetaInformation postMeta = new PostMetaInformation();
                    postMeta.setMetaInformation(metaInformation);
                    metaList.add(postMeta);

                  } else {

                    if (!TextUtils.isEmpty(byPostId.getText())) {
                      Document parse = Jsoup.parse(byPostId.getText());
                      String text = parse.body().text();
                      String substring = text.substring(0, 320);

                      if (!TextUtils.isEmpty(substring)) {

                        MetaInformation metaInformation = new MetaInformation();
                        metaInformation.setType("description");
                        metaInformation.setAttributeType("name");
                        metaInformation.setContent(substring);
                        PostMetaInformation postMeta = new PostMetaInformation();
                        postMeta.setMetaInformation(metaInformation);

                        metaList.add(postMeta);
                      }
                    }

                  }
                  byPostId.getMetaList().clear();
                  byPostId.getMetaList().addAll(metaList);
                  baseService.updatePost(byPostId.getPostId(), byPostId, 102801);
                }

              } else {
                notFoundPostSlug.add(postName);
              }

            }

          } catch (Exception e) {
            e.printStackTrace();
          }

        }

      }
    }

    String join = StringUtils.join(notFoundPostSlug, ',');
    System.out.println();
    System.out.println(join);

  }

}