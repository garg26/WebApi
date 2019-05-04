package com.neostencil.common;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.common.collect.Lists;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;


public class StorageFactory {
  private static Storage instance = null;
  private static final String TEST_FILENAME = "ns-storage-api.json";


  public static synchronized Storage getService() throws IOException, GeneralSecurityException {
    if (instance == null) {
      instance = buildService();
    }
    return instance;
  }

  private static Storage buildService() throws IOException, GeneralSecurityException {
    HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = new JacksonFactory();

    //To Run Locally
    GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(TEST_FILENAME))
        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

    //To Run from server
    //GoogleCredential credential = GoogleCredential.getApplicationDefault(transport, jsonFactory);

    if (credential.createScopedRequired()) {
      Collection<String> scopes = StorageScopes.all();
      credential = credential.createScoped(scopes);
    }

    return new Storage.Builder(transport, jsonFactory, credential)
        .setApplicationName("GCS Samples")
        .build();
  }
}
