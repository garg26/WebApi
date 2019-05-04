package com.neostencil.tools;

import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class VariousUtils {

  private static final String STORAGE_CREDENTIALS_FILE_PATH = "ns-storage-api.json";

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

  class WorkerThread implements Callable<String> {

    private String imageGoogleStoragePath;

    public WorkerThread(String imageGoogleStoragePath) {
      this.imageGoogleStoragePath = imageGoogleStoragePath;
    }

    private String fetchServingUrl() throws IOException {

      String url = "https://ns-web-storage.appspot.com/getImageUrl?fileName=" + URLEncoder
          .encode(this.imageGoogleStoragePath, "utf-8");
      String newPath = fetchHTTPURL(url);
      return newPath;
    }

    @Override
    public String call() {

      String url = null;
      for (int i = 0; i < 5; i++) {
        try {

          String newPath = fetchServingUrl();
          if (newPath.indexOf("googleusercontent") == -1) {
            System.err.println("Something appears wrong for " + this.imageGoogleStoragePath);
            return "";
          }
          return newPath;
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
      return "";
    }
  }

  /**
   * For connecting to MySql database
   */
  public static Connection getMySqlConnection() throws Exception {
    String driver = "org.gjt.mm.mysql.Driver";
    //String url = "jdbc:mysql://dev-20180928.cpquzrpumq9t.ap-southeast-1.rds.amazonaws.com:3306/neosqldb";
    String url = "jdbc:mysql://n30pr0duc710ndb.cpquzrpumq9t.ap-southeast-1.rds.amazonaws.com:3306/neosqldb";
    String username = "neosqladmin";
   //   String password = "gurgaon337";
    String password = "$L?pi$cieFRoutR6eviE";
    Class.forName(driver); // load MySQL driver
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }

  public static void insertWrenchTokenToMysql(String username, String ip, String token, String stream, String application ) throws Exception {

      Connection mySqlConnection = getMySqlConnection();

    String insertWPTokenQuery = "insert into n0e0o7_wp_tokens(username, ts, ip, token, status_token, stream, application) values (?, ?, ?, ?, 'NEW', ?,?)";
    PreparedStatement pstmt = mySqlConnection.prepareStatement(insertWPTokenQuery);
    pstmt.setString(1, username);
    pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
    pstmt.setString(3, ip);
    pstmt.setString(4, token);
    pstmt.setString(5, stream);
    pstmt.setString(6, application);
    pstmt.execute();
  }




  public static Connection getPostgreSqlConnection() throws Exception {
    String driver = "org.postgresql.Driver";
    // String url = "jdbc:postgresql://35.200.160.179:5432/nsdb";
    /*
     * String username = "postgres"; String password = "n3o@#$42";
     */
  //  String url = "jdbc:postgresql://beta4-db.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
    //String url = "jdbc:postgresql://beta2-db-new.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
    String url = "jdbc:postgresql://beta2-20180928.cg44pqyzxfk0.ap-south-1.rds.amazonaws.com:5432/nsdb_2";
    String username = "beta2DBAdmin";
    String password = "gurgaon337";
    Class.forName(driver); // load Postgresql driver
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(VariousUtils.class);


  public void updateUserAvatars() {
    Connection postgresqlConn = null;
    try {
      postgresqlConn = getPostgreSqlConnection();
      String selectUserQuery = "select * from ns_users";
      Statement selectUserStatement = postgresqlConn.createStatement();
      ResultSet userResult = selectUserStatement.executeQuery(selectUserQuery);

      List<Integer> userIDs = new ArrayList<>();

      while (userResult.next()) {
        int tempId = userResult.getInt("ID");
        userIDs.add(tempId);
      }

      ExecutorService executor = Executors.newFixedThreadPool(500);
      for (int userID : userIDs) {

        String imageName = "uploads/avatars/" + userID;
        WorkerThread worker = new WorkerThread(imageName);

        Future<String> result = executor.submit(worker);

      }

      executor.shutdown();
      while (!executor.isTerminated()) {
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateAllImageServingUrls()
      throws IOException, ExecutionException, InterruptedException {

    List<String> imagePaths = fetchAllImagesFromBucket("ns-web-storage.appspot.com");

    ExecutorService executor = Executors.newFixedThreadPool(200);
    HashMap<String, Future<String>> results = new HashMap<>();
    HashMap<String, String> imageMap = new HashMap<>();
    for (String imagePath : imagePaths) {

      WorkerThread worker = new WorkerThread(imagePath);
      Future<String> result = executor.submit(worker);
      results.put(imagePath, result);
    }

    for (String path : results.keySet()) {
      String servingUrl = results.get(path).get();
      imageMap.put(path, servingUrl);
    }
    executor.shutdown();
    while (!executor.isTerminated()) {
    }
    updateAllImageServingUrlsDB(imageMap);
  }


  public void updateAllImageServingUrlsDB(HashMap<String, String> imageUrls)
  {


    try {
      Connection postgresqlConn = getPostgreSqlConnection();
      String insertImage =
          "insert into ns_image_url(image_path, image_serving_url) values(?,?)";
      PreparedStatement pstmtImage = postgresqlConn.prepareStatement(insertImage);

      int batchCount = 0;
      for (String imagePath: imageUrls.keySet()) {
        pstmtImage.setString(1, imagePath);
        pstmtImage.setString(2, imageUrls.get(imagePath));
        pstmtImage.addBatch();
        batchCount++;
        if (batchCount == 1000) {
          try {
            batchCount = 0;
            pstmtImage.executeBatch();
          } catch (SQLException e) {
            Exception exception = e;
            while ((exception = e.getNextException()) != null) {
              exception.printStackTrace();
            }
            pstmtImage.clearBatch();
          }
        }
      }
      pstmtImage.executeBatch();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  public List<String> fetchAllImagesFromBucket(String storageBucketName)
      throws IOException {

    Credentials credentials = GoogleCredentials
        .fromStream(new FileInputStream(STORAGE_CREDENTIALS_FILE_PATH));
    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

    // Creates the new bucket
    Bucket bucket = storage.get(storageBucketName);

    List<String> allImageList = new ArrayList<>();
    Page<Blob> blobs = bucket.list();
    for (Blob blob : blobs.iterateAll()) {
      // System.out.println(blob.getName());
      if (blob != null && blob.getContentType() != null && blob.getContentType()
          .contains("image")) {

        allImageList.add(blob.getName());
      }

    }

    return allImageList;
  }


  public static void main(String... args)
      throws InterruptedException, ExecutionException, IOException {
    VariousUtils dbUtils = new VariousUtils();
    dbUtils.updateAllImageServingUrls();
  }
}