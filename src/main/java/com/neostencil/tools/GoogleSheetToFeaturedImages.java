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


class FeaturedImageDetails {
  String imageID;
  String imagePath;
  String oldServingURL;
  String newServingURL;
}


@Service
public class GoogleSheetToFeaturedImages {

  ConcurrentHashMap<String,FeaturedImageDetails> toUpdateFeaturedImageDetailsList = new ConcurrentHashMap<>();
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

    private String imageID;
    private String imageName;
    private String imageURL;

    String[] dirs1 =  { "JULY-2018", "AUGUST-2018", "SEPTEMBER-2018", "OCTOBER-2018" };
    String[] dirs2 =  { "courses", "teachers", "institutes", "post" };

    public WorkerThread(String imageID, String imageName, String imageURL){
      this.imageID = imageID;
      this.imageName = imageName;
      this.imageURL = imageURL;


    }

    @Override
    public void run() {

      boolean done = false;

      if (imageURL.indexOf("googleusercontent") > 0) {
        for (String dir1 : dirs1) {

          for (String dir2 : dirs2) {
            String url = null;
            String fileName = "uploads2/" + dir1 + "/featureImage/" + dir2 + "/" + imageName;
            try {

              url = "https://ns-web-storage.appspot.com/getImageUrl?fileName=" +  URLEncoder
                  .encode(fileName, "utf-8"); ;

              String newPath = fetchHTTPURL(url);
              if(newPath.indexOf("googleusercontent")==-1)
              {
                continue;

              }

              FeaturedImageDetails tmp = new FeaturedImageDetails();
              tmp.imageID = imageID;
              tmp.imagePath = imageURL;
              tmp.newServingURL = newPath;
              toUpdateFeaturedImageDetailsList.put(tmp.imageID, tmp);
              done = true;
              break;
            } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }

            if (done) {
              break;
            }
          }
          if (done) {
            break;
          }
        }
        if(!done)
        {
          System.err.println("Something went wrong with " + imageURL +  "  " + imageID);
        }
      }

      else
      {
        String url = null;
        String fileName =  imageURL.replaceAll("/uploads1", "uploads");
        if(fileName.indexOf("wp-content") > -1)
        {
          fileName = fileName.substring(fileName.indexOf("wp-content")+11 );
        }

        try {

          url = "https://ns-web-storage.appspot.com/getImageUrl?fileName=" +  URLEncoder
              .encode(fileName, "utf-8");


          String newPath = fetchHTTPURL(url);
          if (newPath.indexOf("googleusercontent") == -1) {
            System.err.println("Something appears wrong for " + imageID + imageURL);

          }
          FeaturedImageDetails tmp = new FeaturedImageDetails();
          tmp.imageID = imageID;
          tmp.imagePath = imageURL;
          tmp.newServingURL = newPath;
          toUpdateFeaturedImageDetailsList.put(tmp.imageID, tmp);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }


  public static Connection getPostgreSqlConnection() throws Exception {
    String driver = "org.postgresql.Driver";
    // String url = "jdbc:postgresql://35.200.160.179:5432/nsdb";
    /*
     * String username = "postgres"; String password = "n3o@#$42";
     */
    String url = "jdbc:postgresql://beta4-db.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
    //String url = "jdbc:postgresql://beta2-db-new.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
    //String url = "jdbc:postgresql://beta2-20180928.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
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

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetToFeaturedImages.class);

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
    InputStream in = GoogleSheetToFeaturedImages.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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

  public List<HashMap<String, String>> getTeacherImages()
      throws GeneralSecurityException, IOException {

    List<HashMap<String, String>> courseDetails = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1eKQtAAkpruTndv510yN9f8QT4wJ2P5r6a20fShtefo8";

    final String range = "feature_image!A1:C";

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
        if (row.containsKey("url") && row.get("url") != "") {
          try {
            String imageID = row.get("image_id");
            String imageName = row.get("name");
            String imageURL = row.get("url");
/*
            if(imageURL.indexOf("Free-Demo-Lecture-of-Public-Administration-by-Pavan-Kumar.jpg") <0)
            {
              continue;
            }

*/
            Runnable worker = new WorkerThread(imageID, imageName, imageURL);
            executor.execute(worker);

          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          System.err.println("something wrong with institute " + row.get("image_id") + row);
        }
      }
    }
    executor.shutdown();
    while (!executor.isTerminated()) {
    }
    System.out.println("Finished all threads");
    updateImagePaths(toUpdateFeaturedImageDetailsList);
  }



  public static void updateImagePaths(ConcurrentHashMap<String,FeaturedImageDetails> teachersImageMap) {
    Connection postgresqlConn = null;
    try {
      postgresqlConn = getPostgreSqlConnection();
      int batchCount = 0;

      String updateCourseImages = "update ns_featured_images set url=?,name=? where image_id=?";
      PreparedStatement ps = postgresqlConn.prepareStatement(updateCourseImages);

      for (FeaturedImageDetails teacher: teachersImageMap.values()) {

        ps.setString(1, teacher.newServingURL);
        ps.setString(2, teacher.imagePath);
        ps.setInt(3, Integer.parseInt(teacher.imageID));
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
    GoogleSheetToFeaturedImages gs = new GoogleSheetToFeaturedImages();
    List<HashMap<String, String>> courseImagesDetails = gs.getTeacherImages();
    gs.fetchImages(courseImagesDetails);
    System.out.println(courseImagesDetails);
  }
}