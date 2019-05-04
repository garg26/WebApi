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
import com.neostencil.model.repositories.BreadCrumbRepository;
import com.neostencil.model.repositories.PostRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


class CourseImageDetails {
  String courseID;
  String imagePath;
  String oldServingURL;
  String newServingURL;
}





@Service
public class GoogleSheetToCourseImages {

  ConcurrentHashMap<String,TeacherImageDetails> toUpdateCourseImageDetailsList = new ConcurrentHashMap<>();
  public static String fetchHTTPURL(String url) throws IOException {
    URL yahoo = new URL(url);
    URLConnection yc = yahoo.openConnection();
    BufferedReader in = new BufferedReader(
        new InputStreamReader(
            yc.getInputStream()));
    String inputLine;

    String rv = "";

    while ((inputLine = in.readLine()) != null) {
      // System.out.println(inputLine);
      rv = rv + inputLine;
    }
    in.close();
    return rv;
  }
  class WorkerThread implements Runnable {

    private String courseID;
    private String imageFileName;


    public WorkerThread(String courseID, String s){
      this.courseID = courseID;
      this.imageFileName = s;
    }

    @Override
    public void run() {

      String url = null;
      try {
        url = "https://ns-web-storage.appspot.com/getImageUrl?fileName=" + URLEncoder
            .encode(this.imageFileName, "utf-8");

        String newPath = fetchHTTPURL(url);
        if(newPath.indexOf("googleusercontent")==-1)
        {
          System.err.println("Something appears wrong for " + courseID + imageFileName);

        }

        TeacherImageDetails tmp = new TeacherImageDetails();
        tmp.instituteID = courseID;
        tmp.imagePath = imageFileName;
        tmp.newServingURL = newPath;
        toUpdateCourseImageDetailsList.put(tmp.instituteID, tmp);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  public static Connection getPostgreSqlConnection() throws Exception {
    String driver = "org.postgresql.Driver";
    // String url = "jdbc:postgresql://35.200.160.179:5432/nsdb";
    /*
     * String username = "postgres"; String password = "n3o@#$42";
     */
    //String url = "jdbc:postgresql://beta4-db.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
    //String url = "jdbc:postgresql://beta2-db-new.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
    String url = "jdbc:postgresql://beta2-20180928.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
    String username = "beta2DBAdmin";
    String password = "gurgaon337";
    Class.forName(driver); // load Postgresql driver
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }


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

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetToTeacherImages.class);

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
    InputStream in = GoogleSheetToTeacherImages.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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

  public List<HashMap<String, String>> getCourseImages()
      throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> courseDetails = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1XGT88Ma-sLORrbn6SZzL1-LWkz5ZliYrLlhV3yg36o0";

    final String range = "course_image!A1:D";

    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME)
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

  public void fetchImages(List<HashMap<String, String>> courseDetails) {

    ExecutorService executor = Executors.newFixedThreadPool(500);
    for (HashMap<String, String> row : courseDetails) {
      if (row.size() > 0) {
        if (row.containsKey("course_image_url") && row.get("course_image_url") != "") {
          try {
            String courseID = row.get("course_id");
            String imageName = row.get("course_image");
            String imageURL = row.get("course_image_url");
            String createdAt = row.get("created_at");

            String dir = "";
            int tmpIndex1 = createdAt.indexOf("2018-07");
            int tmpIndex2 = createdAt.indexOf("2018-08");
            int tmpIndex3 = createdAt.indexOf("2018-09");
            int tmpIndex4 = createdAt.indexOf("2018-10");

            if (tmpIndex1 > -1) {
              dir = "JULY-2018";
            } else if (tmpIndex2 > -1) {
              dir = "AUGUST-2018";
            } else if (tmpIndex3 > -1) {
              dir = "SEPTEMBER-2018";
            } else if (tmpIndex4 > -1) {
              dir = "OCTOBER-2018";
            } else {
              System.err.println("Something seems to have gone wrong horribly");
            }

            String fileName = "uploads2/" + dir + "/courses/" + imageName;
            Runnable worker = new WorkerThread(courseID, fileName);
            executor.execute(worker);


          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          System.err.println("something wrong with breadCrumb " + row.get("Child URL") + row);
        }
      }
    }
    executor.shutdown();
    while (!executor.isTerminated()) {
    }
    System.out.println("Finished all threads");
    updateImagePaths(toUpdateCourseImageDetailsList);
  }



  public static void updateImagePaths(ConcurrentHashMap<String,TeacherImageDetails> courseImageDetailsList) {
    Connection postgresqlConn = null;
    try {
      postgresqlConn = getPostgreSqlConnection();
      int batchCount = 0;

      String updateCourseImages = "update ns_courses set course_image_url=?, course_image=? where course_id=?";
      PreparedStatement ps = postgresqlConn.prepareStatement(updateCourseImages);

      for (TeacherImageDetails course: courseImageDetailsList.values()) {

        ps.setString(1, course.newServingURL);
        ps.setString(2, course.imagePath);
        ps.setInt(3, Integer.parseInt(course.instituteID));
        ps.addBatch();
        batchCount++;
        if (batchCount == 1000) {
          try {
            batchCount = 0;
            ps.executeBatch();
          } catch (SQLException e) {
            Exception exception = e;
            while ((exception = e.getNextException()) != null) {
              exception.printStackTrace();
            }
            ps.clearBatch();
          }
        }


      }
      ps.executeBatch();


    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    } finally {
      try {
        postgresqlConn.close();

      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //UPDATE ns_comments SET user_id = ns_users.user_id FROM ns_users where ns_users.email_id = ns_comments.user_email;
  }

  public static void main(String... args) throws IOException, GeneralSecurityException {
    GoogleSheetToTeacherImages gs = new GoogleSheetToTeacherImages();
    List<HashMap<String, String>> courseImagesDetails = gs.getTeacherImages();
    gs.fetchImages(courseImagesDetails);
    System.out.println(courseImagesDetails);
  }
}