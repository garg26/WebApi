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
import com.google.gson.JsonArray;
import com.neostencil.model.entities.Question;
import com.neostencil.model.entities.Quiz;
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
import org.springframework.stereotype.Service;

@Service
public class GoogleSheetToQuestion {

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

  public List<HashMap<String, String>> getQuestionDetails(String excelId)
//      throws GeneralSecurityException, IOException
  {

    List<HashMap<String, String>> courseDetails = new ArrayList<>();
    HashMap<Integer, String> columnIndexMapping = new HashMap<>();

    NetHttpTransport HTTP_TRANSPORT = null;
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    final String spreadsheetId = excelId;

    final String range = "Quiz!A:Z";

    Sheets service =
        null;
    try {
      service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
          .setApplicationName(APPLICATION_NAME).build();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ValueRange response = null;
    try {
      response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    } catch (IOException e) {
      e.printStackTrace();
    }

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

  public List<Question> createQuestion(Quiz quiz, List<HashMap<String, String>> courseDetailsList) {
    List<Question> toSaveQuestion = new ArrayList<>();

    for (HashMap<String, String> question : courseDetailsList) {
      try {
        if (question.size() > 0) {

          Question questionModel = new Question();

          String topic = null;
          if (quiz.isTopicWiseQuestion()) {
            topic = question.get("Topic");
            questionModel.setTopic(topic);
          }

          String s1 = question.get("S/n");
          questionModel.setPosition(Integer.parseInt(s1));

          questionModel.setPositivePoints(quiz.getPositiveMarkPerQuestion());
          questionModel.setNegativePoints(quiz.getNegativeMarkPerQuestion());


          String questions = question.get("Questions").trim();
          if (questions.contains("\n")){
            questions = questions.replaceAll("\n"," ");
          }

          String question_text = question.get("Question Text");

          if (!TextUtils.isEmpty(question_text)) {
            String[] split = question_text.trim().split("\n");
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(questions);

            for (String str : split) {
              jsonArray.add(str);
            }
            questionModel.setQuestionText(jsonArray.toString());

          } else {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(questions);
            questionModel.setQuestionText(jsonArray.toString());
          }

          JsonArray jsonArrayOption = new JsonArray();

          String option_a = question.get("OPTION A").trim();
          String option_b = question.get("OPTION B").trim();
          String option_c = question.get("OPTION C").trim();
          String option_d = question.get("OPTION D").trim();

          jsonArrayOption.add(option_a);
          jsonArrayOption.add(option_b);
          jsonArrayOption.add(option_c);
          jsonArrayOption.add(option_d);

          questionModel.setOptionJson(jsonArrayOption.toString());

          String correct_answer = question.get("Correct Answer").trim();
          questionModel.setAnswer(correct_answer);

          String explanation = question.get("Explanation").trim();
          if(!TextUtils.isEmpty(explanation)){

            String[] split = explanation.trim().split("\n");
            JsonArray jsonArray = new JsonArray();
            for (String str : split) {
              jsonArray.add(str);
            }

            questionModel.setAnswerExplanation(jsonArray.toString());

          }



          String explanation_image = question.get("Explanation Image");
          if(explanation_image!=null) {
            String[] split = explanation_image.split(",");
            JsonArray explanationImageJson = new JsonArray();
            for (String str : split) {
              explanationImageJson.add(str);
            }
            questionModel.setExplanationImage(explanationImageJson.toString());

          }
          String question_html = question.get("Question HTML");
          questionModel.setQuestionHtml(question_html);

          String explanation_html = question.get("Explanation HTML");
          questionModel.setExplanationHtml(explanation_html);

          String question_image = question.get("Question Image");
          if (question_image!=null){
            String[] split1 = question_image.split(",");
            JsonArray figureJson = new JsonArray();

            for (String s : split1) {
              figureJson.add(s);
            }
            questionModel.setFigureJson(figureJson.toString());

          }

          questionModel.setQuiz(quiz);

          toSaveQuestion.add(questionModel);
        }

      } catch (Exception e) {

        e.printStackTrace();
      }
    }

    return toSaveQuestion;
  }

}
