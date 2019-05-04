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
import com.neostencil.common.enums.CourseCategory;
import com.neostencil.common.enums.CourseSubCategory;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.Subjects;
import com.neostencil.model.entities.BreadCrumb;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.Highlights;
import com.neostencil.model.entities.Institute;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.repositories.BreadCrumbRepository;
import com.neostencil.model.repositories.CourseRepository;
import com.neostencil.model.repositories.InstituteRepository;
import com.neostencil.model.repositories.PostRepository;
import com.neostencil.model.repositories.TeacherDetailsRepository;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
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
public class GoogleSheetToBreadCrumb {

  @Autowired
  PostRepository postRepository;

  @Autowired
  BreadCrumbRepository breadCrumbRepository;

  @Autowired
  BaseService baseService;

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

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetToBreadCrumb.class);

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = GoogleSheetToBreadCrumb.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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

  public List<HashMap<String, String>> getBreadCrumbs()
      throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> courseDetails = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    //final String spreadsheetId = "1dv6T5h45B_dABnYkuS41Gx2F2u-JQtR2ne-OuRqah5I";

    final String spreadsheetId = "1HaoS33oxucOEKrK4qgU3Dic0p3mF5ENEYJ2745U-dps";
    final String range = "VJ Worksheet for V2!A1:E";

    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT)) .setApplicationName(APPLICATION_NAME)
        .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();

    int index1 = 0;

    for (List row : values) {
      index1++;
      int index2 = 0;
      HashMap<String, String> singleRow = new HashMap<>();
      for (Object temp : row) {

        if (index1 == 1) {
          columnIndexMapping.put(index2, temp.toString());
        } else {
          singleRow.put(columnIndexMapping.get(index2), temp.toString());
        }

        index2++;
      }
      courseDetails.add(singleRow);

    }
    return courseDetails;
  }

  public void processBreadCrumbs(List<HashMap<String, String>> breadCrumbDetailsList) {
    HashMap<String, BreadCrumb> breadCrumLinkMap = new HashMap<>();
    HashMap<String, String> breadCrumbParentMap = new HashMap<>();
    List<BreadCrumb> breadCrumbList = new ArrayList<>();


    for (HashMap<String, String> rowBread : breadCrumbDetailsList) {
      if (rowBread.size() > 0) {
        if (rowBread.containsKey("Child Anchor Text") && rowBread.get("Child Anchor Text") != "") {
          try {
            String breadLink = rowBread.get("Child URL");
            String breadText = rowBread.get("Child Anchor Text");
            String breadCrumbParentLink = rowBread.get("Parent URL");

            if(breadLink !=null && !breadLink.endsWith("/"))
            {
              breadLink = breadLink + "/";
            }
            if(breadCrumbParentLink!=null && !breadCrumbParentLink.endsWith("/"))
            {
              breadCrumbParentLink = breadCrumbParentLink + "/";
            }


            BreadCrumb toSave = breadCrumbRepository.findByLink(breadLink);
            BreadCrumb saved = null;
            if(toSave == null)
            {
              toSave = new BreadCrumb();
              toSave.setLink(breadLink);
              toSave.setText(breadText);
              saved = breadCrumbRepository.save(toSave);

            }
            else{
              saved = toSave;
            }

            breadCrumLinkMap.put(breadLink, saved);
            breadCrumbList.add(saved);

            if(breadCrumbParentLink !=null && breadCrumbParentLink.length()>0)
            {
              breadCrumbParentMap.put(breadLink, breadCrumbParentLink);
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          System.err.println("something wrong with breadCrumb " + rowBread.get("Child URL") + rowBread) ;
        }
        System.out.println();
      }


    }


    for (BreadCrumb bread : breadCrumbList) {

      if(breadCrumbParentMap.containsKey(bread.getLink()))
      {
        String parentLink = breadCrumbParentMap.get(bread.getLink());
        BreadCrumb parentBreadCrumb = breadCrumLinkMap.get(parentLink);
        if(parentBreadCrumb == null)
        {
          System.err.println("SOmething appears wrong with " + bread  + " parent " + parentBreadCrumb);
          continue;
        }
        bread.setParentId(parentBreadCrumb.getBreadcrumbId());
        breadCrumbRepository.save(bread);
      }
    }

    updateEntitiesMapping();
  }

  public static Connection getPostgreSqlConnection() throws Exception {
    String driver = "org.postgresql.Driver";
    String url = "jdbc:postgresql://beta4db.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/betadb";
    String username = "betadbAdmin";
    String password = "Gurgaon337";
    Class.forName(driver); // load Postgresql driver
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }

  public void updateEntitiesMapping()
  {

    Connection c = null;
    try {
      c = getPostgreSqlConnection();
      String updateSql = "update ns_courses set breadcrumb_id =b.breadcrumb_id from ns_breadcrumbs b where b.breadcrumb_link=('/course/' || course_old_slug ||'/')";
      PreparedStatement ps = c.prepareStatement(updateSql);
      ps.execute();

      updateSql = "update ns_teacher_details set breadcrumb_id =b.breadcrumb_id from ns_breadcrumbs b where b.breadcrumb_link=('/teacher/' || teacher_slug ||'/')";
      ps = c.prepareStatement(updateSql);
      ps.execute();

      updateSql = "update ns_institutes set breadcrumb_id =b.breadcrumb_id from ns_breadcrumbs b where b.breadcrumb_link=('/institute/' || institute_slug ||'/')";
      ps = c.prepareStatement(updateSql);
      ps.execute();

      updateSql = "update ns_posts set breadcrumb_id =b.breadcrumb_id from ns_breadcrumbs b where b.breadcrumb_link=('/' || post_id ||'/')";
      ps = c.prepareStatement(updateSql);
      ps.execute();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public static void main(String... args) throws IOException, GeneralSecurityException {
  GoogleSheetToBreadCrumb gs = new GoogleSheetToBreadCrumb();
   List<HashMap<String, String>> breadCrumbDetails =  gs.getBreadCrumbs();
   gs.processBreadCrumbs(breadCrumbDetails);
   System.out.println(breadCrumbDetails);
  }
}