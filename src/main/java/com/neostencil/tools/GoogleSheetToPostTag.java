package com.neostencil.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.neostencil.model.entities.Post;
import com.neostencil.model.repositories.PostRepository;

@Service
public class GoogleSheetToPostTag {

  @Autowired
  BaseService baseService;

  @Autowired
  PostRepository postRepository;

  private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
   * your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections
      .singletonList(SheetsScopes.SPREADSHEETS_READONLY);
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

  public List<HashMap<String, String>> getPostTags() throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> postTags = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "161EWMDEHBzqgMBe65jj7Ku029DRWWHVvIp6L1ESu06Q";

    // final String spreadsheetId =
    // "1HLQ9PtmPwLr7lvFbDlQsXFmjxG7mtO5YZOTSOlSHJo8";
    final String range = "Blog Urls!A:L";

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
      postTags.add(singleCourse);

    }
    return postTags;
  }

  public void updatePostTags(List<HashMap<String, String>> postTagList) {
    for (HashMap<String, String> postTags : postTagList) {

      if (postTags.size() > 0) {

        if (postTags.containsKey("URLs") && postTags.get("URLs") != null) {
          String sNo = postTags.get("S. No");

          List<String> tagList = new ArrayList<>();

          String urLs = postTags.get("URLs");
          String[] split = urLs.split("https://neostencil.com/");
          String s = split[1];
          s = s.substring(0, s.length() - 1);
          Post post = postRepository.findByPostSlug(s);

          if (post != null) {

            String tag_l11 = postTags.get("Tag L1");
            if (!TextUtils.isEmpty(postTags.get("Tag L1"))) {
              String tag_l1 = postTags.get("Tag L1");
              tagList.add(tag_l1.trim());
            } else {
              LOGGER.error("T1 Does not have tag:- " + sNo);
              break;
            }

            String tag_l21 = postTags.get("Tag L2");
            if (!TextUtils.isEmpty(postTags.get("Tag L2"))) {
              String tag_l2 = postTags.get("Tag L2");
              tagList.add(tag_l2.trim());
            } else {
              LOGGER.error("T2 Does not have tag:- " + sNo);
            }

            String tag_l31 = postTags.get("Tag L3");
            if (!TextUtils.isEmpty(postTags.get("Tag L3"))) {
              String tag_l3 = postTags.get("Tag L3");
              tagList.add(tag_l3.trim());
            } else {
              LOGGER.error("T3 Does not have tag:- " + sNo);
            }

            String tag_l41 = postTags.get("Tag L4");
            if (!TextUtils.isEmpty(postTags.get("Tag L4"))) {
              String tag_l4 = postTags.get("Tag L4");
              tagList.add(tag_l4.trim());
            } else {
              LOGGER.error("T4 Does not have tag:- " + sNo);
            }

            if (tagList.size() > 0) {
              String tag = StringUtils.join(tagList, ',');
              post.setTags(tag);
            }

            Post save = postRepository.save(post);
            if (save == null) {
              LOGGER.error("S.No:- " + sNo + " " + post.toString());
            }
          } else {
            LOGGER.error("Not Find Any Post:- " + sNo);

          }

        }
      }
    }
  }

}