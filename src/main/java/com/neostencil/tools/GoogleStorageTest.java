package com.neostencil.tools;

// Imports the Google Cloud client library
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
import com.google.api.gax.paging.Page;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.storage.StorageScopes;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GoogleStorageTest {
  private static final String CREDENTIALS_FILE_PATH = "ns-storage-api.json";

  public static void main(String... args) throws Exception {
    // Instantiates a client
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH));
    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

    // The name for the new bucket
    String bucketName = "ns-web-storage.appspot.com";
    // Creates the new bucket
    Bucket bucket = storage.get(bucketName);

    List<String> allImageList = new ArrayList<>();
    Page<Blob> blobs = bucket.list();
    for (Blob blob : blobs.iterateAll()) {
      System.out.println(blob.getName());
      allImageList.add(blob.getName());
    }


  }
}