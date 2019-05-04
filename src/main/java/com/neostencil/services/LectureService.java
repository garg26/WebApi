package com.neostencil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.neostencil.common.CommonUtil;
import com.neostencil.model.entities.*;
import com.neostencil.model.repositories.CustomQuestionRepository;
import com.neostencil.model.repositories.QuizRepository;
import com.neostencil.model.repositories.QuizTemplateRepository;
import com.neostencil.model.repositories.UserMetaDataRespository;
import com.neostencil.model.repositories.UserQuizSubmissionRepository;
import com.neostencil.requests.QuizSubmissionRequest;
import com.neostencil.responses.DRMResponseForMobile;
import com.neostencil.responses.MobileNote;
import com.neostencil.responses.PlayLectureOnMobile;
import com.neostencil.responses.QuizLeaderBoardResponse;
import com.neostencil.responses.QuizWithQuestionResponse;
import com.neostencil.responses.UnitOnMobile;
import com.neostencil.responses.UserQuizSubmissionResponse;
import com.neostencil.responses.quizresponse.QuizWithAnswerResponse;
import com.neostencil.responses.quizresponse.SectionLevel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.management.timer.Timer;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.neostencil.common.enums.AssignmentStatus;
import com.neostencil.common.enums.DeviceType;
import com.neostencil.common.enums.StreamType;
import com.neostencil.common.enums.UserCourseLinkageStatus;
import com.neostencil.common.enums.WatchStatus;
import com.neostencil.config.Constants;
import com.neostencil.exceptions.InvalidInputException;
import com.neostencil.model.entities.AssignmentSubmission;
import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.AuthorityName;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.JWMacro;
import com.neostencil.model.entities.Lecture;
import com.neostencil.model.entities.Question;
import com.neostencil.model.entities.Quiz;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.UserCourseBatchLinkage;
import com.neostencil.model.entities.UserDevice;
import com.neostencil.model.entities.UserDeviceLog;
import com.neostencil.model.entities.UserQuizSubmission;
import com.neostencil.model.entities.UserUnitLinkage;
import com.neostencil.model.entities.WowzaMacro;
import com.neostencil.model.repositories.CourseBatchRepository;
import com.neostencil.model.repositories.CourseRepository;
import com.neostencil.model.repositories.LectureRepository;
import com.neostencil.model.repositories.UnitRepository;
import com.neostencil.model.repositories.UserCourseBatchLinkageRepository;
import com.neostencil.model.repositories.UserDeviceLogRepository;
import com.neostencil.model.repositories.UserDeviceRepository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.model.repositories.UserUnitLinkageRepository;
import com.neostencil.projections.UnitProjection;
import com.neostencil.requests.UnitRequest;
import com.neostencil.responses.AssignmentSubmissionRequest;
import com.neostencil.responses.SignUpResponse;
import com.neostencil.responses.UnitDetailResponse;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.tools.VariousUtils;
import com.neostencil.utils.Utils;
import keyos.authxml.generator.AuthXMLGenerator;
import keyos.authxml.generator.FairplayPolicy;
import keyos.authxml.generator.License;
import keyos.authxml.generator.Policy;
import keyos.authxml.generator.WidevineContentKeySpec;
import keyos.authxml.generator.WidevinePolicy;
import keyos.authxml.generator.utils.XElement;

@Service
public class LectureService {

  @Autowired
  private EmailService emailService;

  @Value("${drm.keyFilePath}")
  private String drmKeyFile;

  @Value("${drm.keyName}")
  private String drmKeyName;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  OTPService otpService;

  @Autowired
  SMSService smsService;

  @Autowired
  LectureRepository lectureRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  UserDeviceRepository userDeviceRepository;

  @Autowired
  UserDeviceLogRepository userDeviceLogRepository;

  @Autowired
  UserUnitLinkageRepository userUnitLinkageRepository;

  @Autowired
  UserCourseBatchLinkageRepository userCourseBatchLinkageRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private CourseBatchRepository courseBatchRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private QuizRepository quizRepository;

  @Autowired
  private CustomQuestionRepository customQuestionRepository;

  @Autowired
  private UserQuizSubmissionRepository userQuizSubmissionRepository;

  @Autowired
  private QuizTemplateRepository quizTemplateRepository;

  @Value("${neostencil.api.url}")
  private String websiteApiUrl;

  @Autowired
  private UserMetaDataRespository userMetaDataRespository;

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private String generateJWPlayerPlayScript(User user, Unit unit, Lecture lecture) {

    long userId = 0;
    if (user != null) {
      userId = user.getUserId();

    }

    UserLectureStats userLectureStats = userMetaDataRespository
        .findByIdUserUserIdAndIdUnitUnitId(userId, unit.getUnitId());

    double resumeFromtmp = 0;
    if (userLectureStats !=null)
    {
       resumeFromtmp = userLectureStats.getResumeFrom();
    }


    long resumeFrom = (long) resumeFromtmp;


    // jwMacroRepository.findByName(request.getJwMacro());

    // wowzaMacroRepository.findByName(request.getWowzaMacro());

    String playerId = "myplayer" + unit.getUnitId();

    JWMacro jwMacro = lecture.getJwMacro();
    WowzaMacro wowzaMacro = jwMacro.getWowzaMacro();
    // For creating the actual Tag
    StringBuilder scriptTag = new StringBuilder();
    scriptTag.append("<script type=\"text/javascript\">");
    scriptTag.append("var playerInstance = jwplayer('" + "myplayer" + unit.getUnitId() + "');");
    //if (jwMacro != null && jwMacro.getStreamType() != null && jwMacro.getStreamType()
      //  .equals(StreamType.VOD)
       // || StreamType.LOCAL.equals(jwMacro.getStreamType())) {
      scriptTag.append(
          "var _contentId;var base64EncodeUint8Array=function(a){for(var d,e,f,g,h,i,j,b=\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=\",c=\"\",k=0;k<a.length;)d=a[k++],e=k<a.length?a[k++]:Number.NaN,f=k<a.length?a[k++]:Number.NaN,g=d>>2,h=(3&d)<<4|e>>4,i=(15&e)<<2|f>>6,j=63&f,isNaN(e)?i=j=64:isNaN(f)&&(j=64),c+=b.charAt(g)+b.charAt(h)+b.charAt(i)+b.charAt(j);return c};");
    //}
    scriptTag.append("playerInstance.setup({");
    StringBuilder fileScript = new StringBuilder();
    StringBuilder fileScript2 = new StringBuilder();

    String url = lecture.getUrl();

    StringBuilder contentPath = new StringBuilder();
    if (StreamType.LIVE.equals(jwMacro.getStreamType())) {
      contentPath.append("live_" + lecture.getUrl() + ".stream.smil");
    } else {
      //handle vod/local from cloud storage
      String replace = null;
      if (url.contains("smil:")) {
        replace = url.replace("smil:", "");
      }

      String finalUrl = "";
      if (url.contains("seminar_")) {
        finalUrl =
            "free_vod/_definst_/smil:freevideolectures/mp4/" + replace.split("_")[0] + "_courses/"
                + replace;
      } else {
        finalUrl =
            "default_vod/_definst_/smil:videolectures/mp4/" + replace.split("_")[0] + "_courses/"
                + replace;
      }

      if (StreamType.VOD.equals(jwMacro.getStreamType())) {
        contentPath.append(finalUrl + ".stream.smil");
      } else if (StreamType.LOCAL.equals(jwMacro.getStreamType())) {
        contentPath.append(finalUrl + ".smil");
      }

    }

    String stream = "";
    int pos = -1;
    if (lecture.getUrl().indexOf(":") != -1) {
      pos = lecture.getUrl().indexOf(":");
      String temp = lecture.getUrl().substring(pos + 1);
      stream = temp + ".stream.smil";
    }

    // Figuring out the OS and browser information
    // && !userAgent.getOperatingSystem().equals(OperatingSystem.MAC_OS)
    // && !userAgent.getOperatingSystem().equals(OperatingSystem.MAC_OS_X
    /*
     * if (userAgent != null && userAgent.getOperatingSystem() != null &&
     * (userAgent.getOperatingSystem().getManufacturer().equals(Manufacturer
     * .APPLE) &&
     * !userAgent.getOperatingSystem().equals(OperatingSystem.MAC_OS) &&
     * !userAgent.getOperatingSystem().equals(OperatingSystem.MAC_OS_X))) {
     * fileScript.append(wowzaMacro.getServerIp() + contentPath +
     * "/playlist.m3u8?"); if
     * (StreamType.LIVE.equals(jwMacro.getStreamType())) {
     * fileScript.append("DVR&"); }  */
    if (StreamType.LIVE.equals(jwMacro.getStreamType())) {

      fileScript.append(wowzaMacro.getServerIp() + contentPath + "/playlist.m3u8?DVR&");
      fileScript2.append(wowzaMacro.getServerIp() + contentPath + "/playlist.m3u8?DVR&");
    } else {
      fileScript.append(wowzaMacro.getServerIp() + contentPath + "/manifest.mpd?");
      fileScript2.append(wowzaMacro.getServerIp() + contentPath + "/playlist.m3u8?");
    }

    if (jwMacro.isWowzaHashEnabled()) {
      Calendar calendar = Calendar.getInstance();
      // calendar.add(Calendar.SECOND, wowzaMacro.getValidity());
      calendar.add(Calendar.SECOND, 6048000);

      long wowzaEndStr = calendar.getTimeInMillis();
      String hashStr =
          contentPath.toString() + "?" + wowzaMacro.getSecret() + "&" + wowzaMacro.getToken()
              + "endtime=" + wowzaEndStr + "&" + wowzaMacro.getToken() + "user=" + userId;
      MessageDigest digest = null;
      byte[] hash = null;
      String wowzaHash = "";

      try {
        digest = MessageDigest.getInstance("SHA-256");
        hash = digest.digest(hashStr.getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(hash));
        byte[] encodedBytes = Base64.getEncoder().encode(hash);
        System.out.println("encodedBytes " + new String(encodedBytes));
        wowzaHash = new String(encodedBytes);
        wowzaHash = wowzaHash.replace("+", "-");
        wowzaHash = wowzaHash.replace("/", "_");

        System.out.println("Wowza Hash=" + wowzaHash);

        digest = MessageDigest.getInstance("MD5");
        fileScript.append(
            wowzaMacro.getToken() + "user=" + userId + "&" + wowzaMacro.getToken() + "endtime="
                + wowzaEndStr + "&" + wowzaMacro.getToken() + "hash=" + wowzaHash);
        fileScript2.append(
            wowzaMacro.getToken() + "user=" + userId + "&" + wowzaMacro.getToken() + "endtime="
                + wowzaEndStr + "&" + wowzaMacro.getToken() + "hash=" + wowzaHash);
      } catch (NoSuchAlgorithmException e) { // TODO Auto-generated catch
        // block
        e.printStackTrace();
      }
    }

    if (jwMacro.isWrenchEnabled()) {
      String wrenchHash = UUID.randomUUID().toString();
      fileScript.append("&auth_id=" + wrenchHash);
      fileScript2.append("&auth_id=" + wrenchHash);

      String ip = Utils.getClientRealIP();
      String fullPath = contentPath.toString();
      String streamFileName = fullPath.substring(fullPath.indexOf(":") + 1);

      MessageDigest md = null;
      try {
        md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(wrenchHash.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
          sb.append(String.format("%02x", b));
        }
        VariousUtils
            .insertWrenchTokenToMysql(userId + "", ip, sb.toString(), streamFileName, fullPath);

      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }

    }

    fileScript.append("&user=" + userId);
    fileScript2.append("&user=" + userId);

    /*
     * if(fileScript.indexOf("/playlist.m3u8")!=0) {
     * fileScript.replace(fileScript.indexOf("/playlist.m3u8"), end, str) }
     */
    // fileScript2.append(fileScript);
    if (!fileScript2.toString().isEmpty()) {
      scriptTag.append("sources:[{");
      scriptTag.append("'file':'" + fileScript + "'},");
      scriptTag.append("{'file':'" + fileScript2 + "'}]");
    } else {
      scriptTag.append("file:'" + fileScript + "'");
    }

    scriptTag.append(",hlshtml:" + (jwMacro.isHlshtml() ? "true" : "false"));
    scriptTag.append(",logo:{file:'/api/v1/player/watermark',position:'bottom-left'}");

    // For enabling conditional addition of drm
    // &&
    // !(userAgent.getOperatingSystem().getManufacturer().equals(Manufacturer.APPLE)
    // && !userAgent.getOperatingSystem().equals(OperatingSystem.MAC_OS)
    // && !userAgent.getOperatingSystem().equals(OperatingSystem.MAC_OS_X))
    if (jwMacro.isDrmEnabled()) {
      String encodedXML = getDRMEncodedXML();
      if (encodedXML.length() > 0) {
        String tmp =
            " \"drm\": { playready: {url: \"https://pr-keyos.licensekeyserver.com/core/rightsmanager.asmx\","
                + " customData:\"" + encodedXML
                + "\"}, widevine: {url: \"https://wv-keyos.licensekeyserver.net/\", "
                + "customData:\"" + encodedXML
                + "\"}, "
                + "fairplay: {licenseResponseType:\"text\", processSpcUrl: \"https://fp-keyos.licensekeyserver.com/getkey\", certificateUrl:"
                + " \"https://fp-keyos.licensekeyserver.com/cert/3cc0a231317f9c7101d8dba82ae5efd7.der\", licenseRequestHeaders:"
                + " [{ name: \"customdata\", value:\"" + encodedXML
                + "\"}],licenseRequestMessage: function (licenseKeyMessage) "
                + "{ return \"spc=\" + base64EncodeUint8Array(licenseKeyMessage) + \"&assetId=\" + _contentId;},"
                + " extractContentId: function (contentId) { if (contentId.indexOf(\"skd://\") > -1)"
                + " { _contentId = contentId.split(\"skd://\")[1].substr(0, 32); return _contentId; } "
                + "console.log(\"Invalid Content ID format. The format of the Content ID must be the following:"
                + " skd://xxx where xxx is the Key ID in hex format.\"); }, "
                + "extractKey: function(ckc) { var base64EncodedKey = ckc.trim(); "
                + "if (base64EncodedKey.substr(0, 5) === \"\" && base64EncodedKey.substr(-6) === \"\") "
                + "{ base64EncodedKey = base64EncodedKey.slice(5,-6); } return base64EncodedKey;} }}";
        scriptTag.append("," + tmp);
      }
    }

    scriptTag.append(", autostart:" + (jwMacro.isAutoStart() ? "true" : "false"));
    // scriptTag.append(",start: <?= intval($_COOKIE['mv_'.$id]) ?>");
    scriptTag.append(",playbackRateControls:[1, 1.25, 1.5, 2]");

    scriptTag.append("});");

    // TODO: To check how to make it dynamic

    scriptTag.append("jwplayer().on('firstFrame',function(e){\n" +

        "var unitId = " + unit.getUnitId() + ";" +

        "    var input = {" +
        "'type':'onFirstFrame','currentState':jwplayer().getState(),'count':count,'time':jwplayer().getPosition(),'duration':jwplayer().getDuration()"
        +

        "};" +
        "    $.ajax({\n" +
        "      type: 'POST',\n" +
        "      data: JSON.stringify(input)" + "," +
        "contentType: \"application/json\",\n"
        + "                processData: false," +
        "      url: '" + websiteApiUrl + "/user-unit-stats/" + unit.getUnitId() + "'" + ',' +
        "\n" +
        "      success: function (response) {\n" +
        "        if (response != null) {\n" +
        "              count = 0;" +
        "\n" +
        "        } else {\n" +
        "          \n" +
        "        }\n" +
        "      },\n" +
        "\n" +
        "      error: function (response) {\n" +
        "\n" +
        "      }\n" +
        "    });});");

    scriptTag.append("jwplayer().on('idle',function(e){\n" +

        "var unitId = " + unit.getUnitId() + ";" +

        "    var input = {" +
        "'type':'onIdle','currentState':jwplayer().getState(),'count':count,'time':jwplayer().getPosition()"
        +

        "};" +
        "    $.ajax({\n" +
        "      type: 'POST',\n" +
        "      data: JSON.stringify(input)" + "," +
        "contentType: \"application/json\",\n"
        + "                processData: false," +
        "      url: '" + websiteApiUrl + "/user-unit-stats/" + unit.getUnitId() + "'" + ',' +
        "\n" +
        "      success: function (response) {\n" +
        "        if (response != null) {\n" +
        "              count = 0;" +
        "\n" +
        "        } else {\n" +
        "          \n" +
        "        }\n" +
        "      },\n" +
        "\n" +
        "      error: function (response) {\n" +
        "\n" +
        "      }\n" +
        "    });});");

    scriptTag.append("jwplayer().on('pause',function(e){\n" +

        "    var input = {" +
        "'type':'onPause','currentState':jwplayer().getState(),'count':count,'time':jwplayer().getPosition()"
        +

        "};" +

        "    $.ajax({\n" +
        "      type: 'POST',\n" +
        "      data: JSON.stringify(input)" + "," +
        "contentType: \"application/json\",\n"
        + "                processData: false," +
        "      url: '" + websiteApiUrl + "/user-unit-stats/" + unit.getUnitId() + "'" + ',' +
        "\n" +
        "      success: function (response) {\n" +
        "        if (response != null) {\n" +
        "              count = 0;" +
        "\n" +
        "        } else {\n" +
        "          \n" +
        "        }\n" +
        "      },\n" +
        "\n" +
        "      error: function (response) {\n" +
        "\n" +
        "      }\n" +
        "    });});");

    scriptTag.append("jwplayer().on('ready',function(){\n" +

        "var resumeAt = " + resumeFrom + ";" +

        "    jwplayer().seek(resumeAt);" +

        "});");

    scriptTag
        .append(
            "jwplayer().on('setupError', function(e) {if (e.message == 'Error loading player: No playable sources found') {document.getElementById('"
                + playerId
                + "').innerHTML = 'The current settings of your browser do not support NeoStencil Video Player. "
                + "Please try changing your browser/device or contact tech@neostencil.com for further support.';} else {  "
                + " return;};});"
                + "jwplayer().on('error', function(error) {if (error.message == 'Cannot load M3U8:"
                + " You do not have permission to access this content'|| error.message == 'Network Error: http status 403') {jwplayer('"
                + playerId
                + "').load({file : '/player/jwplayer_432p.mp4',image : '/player/jwplayer_432p.png'});}});\t  playerInstance.on('firstFrame', () => {\n"
                + "\t\t  logo = document.querySelector('.jw-logo')\n"
                + "\t\t  logo.classList.add('transition')\n"
                + "\t  })</script>");

    return scriptTag.toString();

  }

  public String generateJsonToPlayLectureOnMobile(User user, Lecture lecture) {

    long userId = 0;
    if (user != null) {
      userId = user.getUserId();

    }

    PlayLectureOnMobile playLectureOnMobile = new PlayLectureOnMobile();

    JWMacro jwMacro = lecture.getJwMacro();
    WowzaMacro wowzaMacro = jwMacro.getWowzaMacro();

    StringBuilder fileScript = new StringBuilder();
    StringBuilder fileScript2 = new StringBuilder();

    String url = lecture.getUrl();

    StringBuilder contentPath = new StringBuilder();
    if (StreamType.LIVE.equals(jwMacro.getStreamType())) {
      contentPath.append("live_" + lecture.getUrl() + ".stream.smil");
    } else {
      //handle vod/local from cloud storage
      String replace = null;
      if (url.contains("smil:")) {
        replace = url.replace("smil:", "");
      }

      String finalUrl = "";
      if (url.contains("seminar_")) {
        finalUrl =
            "free_vod/_definst_/smil:freevideolectures/mp4/" + replace.split("_")[0] + "_courses/"
                + replace;
      } else {
        finalUrl =
            "default_vod/_definst_/smil:videolectures/mp4/" + replace.split("_")[0] + "_courses/"
                + replace;
      }

      if (StreamType.VOD.equals(jwMacro.getStreamType())) {
        contentPath.append(finalUrl + ".stream.smil");
      } else if (StreamType.LOCAL.equals(jwMacro.getStreamType())) {
        contentPath.append(finalUrl + ".smil");
      }

    }

    String stream = "";
    int pos = -1;
    if (lecture.getUrl().indexOf(":") != -1) {
      pos = lecture.getUrl().indexOf(":");
      String temp = lecture.getUrl().substring(pos + 1);
      stream = temp + ".stream.smil";
    }

    if (StreamType.LIVE.equals(jwMacro.getStreamType())) {

      fileScript.append(wowzaMacro.getServerIp() + contentPath + "/playlist.m3u8?DVR&");
    } else {
      fileScript.append(wowzaMacro.getServerIp() + contentPath + "/manifest.mpd?");
      fileScript2.append(wowzaMacro.getServerIp() + contentPath + "/playlist.m3u8?");
    }

    if (jwMacro.isWowzaHashEnabled()) {
      Calendar calendar = Calendar.getInstance();
      // calendar.add(Calendar.SECOND, wowzaMacro.getValidity());
      calendar.add(Calendar.SECOND, 6048000);

      long wowzaEndStr = calendar.getTimeInMillis();
      String hashStr =
          contentPath.toString() + "?" + wowzaMacro.getSecret() + "&" + wowzaMacro.getToken()
              + "endtime=" + wowzaEndStr + "&" + wowzaMacro.getToken() + "user=" + userId;
      MessageDigest digest = null;
      byte[] hash = null;
      String wowzaHash = "";

      try {
        digest = MessageDigest.getInstance("SHA-256");
        hash = digest.digest(hashStr.getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(hash));
        byte[] encodedBytes = Base64.getEncoder().encode(hash);
        System.out.println("encodedBytes " + new String(encodedBytes));
        wowzaHash = new String(encodedBytes);
        wowzaHash = wowzaHash.replace("+", "-");
        wowzaHash = wowzaHash.replace("/", "_");

        System.out.println("Wowza Hash=" + wowzaHash);

        digest = MessageDigest.getInstance("MD5");
        fileScript.append(
            wowzaMacro.getToken() + "user=" + userId + "&" + wowzaMacro.getToken() + "endtime="
                + wowzaEndStr + "&" + wowzaMacro.getToken() + "hash=" + wowzaHash);
        fileScript2.append(
            wowzaMacro.getToken() + "user=" + userId + "&" + wowzaMacro.getToken() + "endtime="
                + wowzaEndStr + "&" + wowzaMacro.getToken() + "hash=" + wowzaHash);
      } catch (NoSuchAlgorithmException e) { // TODO Auto-generated catch
        // block
        e.printStackTrace();
      }
    }

    if (jwMacro.isWrenchEnabled()) {
      String wrenchHash = UUID.randomUUID().toString();
      fileScript.append("&auth_id=" + wrenchHash);
      fileScript2.append("&auth_id=" + wrenchHash);

      String ip = Utils.getClientRealIP();
      String fullPath = contentPath.toString();
      String streamFileName = fullPath.substring(fullPath.indexOf(":") + 1);

      MessageDigest md = null;
      try {
        md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(wrenchHash.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
          sb.append(String.format("%02x", b));
        }
        VariousUtils
            .insertWrenchTokenToMysql(userId + "", ip, sb.toString(), streamFileName, fullPath);

      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }

    }

    fileScript.append("&user=" + userId);
    fileScript2.append("&user=" + userId);

    playLectureOnMobile.setManifestFile(fileScript.toString());
    playLectureOnMobile.setPlaylistFile(fileScript2.toString());

    DRMResponseForMobile widevineDRM = null;
    DRMResponseForMobile playreadyDRM = null;

    if (jwMacro.isDrmEnabled()) {
      String encodedXML = getDRMEncodedXML();
      if (encodedXML.length() > 0) {

        widevineDRM = new DRMResponseForMobile();
        widevineDRM.setUrl("https://wv-keyos.licensekeyserver.net/");
        widevineDRM.setCustomData(encodedXML);

        playreadyDRM = new DRMResponseForMobile();
        playreadyDRM.setUrl("https://pr-keyos.licensekeyserver.com/core/rightsmanager.asmx");
        playreadyDRM.setCustomData(encodedXML);


      }
    }

    playLectureOnMobile.setWidevine(widevineDRM);
    playLectureOnMobile.setPlayreadyDRM(playreadyDRM);

    JSONObject jsonObjectWaterMark = new JSONObject();
    jsonObjectWaterMark.put("file", "/api/v1/player/watermark");
    jsonObjectWaterMark.put("position", "bottom-left");
    playLectureOnMobile.setIcon(jsonObjectWaterMark.toString());

    ObjectMapper mapper = new ObjectMapper();

    try {
      return mapper.writeValueAsString(playLectureOnMobile);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String playLectureFree(int unitID) {

    long userId = jwtTokenUtil.getLoggedInUserID();
    User user = userRepository.findByUserId(userId);
    int lectureID = 0;
    if (unitID == 0) {
      return "Error";
    }

    Unit unit = unitRepository.findByUnitId(unitID);
    lectureID = unit.getTypeId();

    if (!unit.isFree()) {
      return "Error";
    }
    if (lectureID == 0) {
      return "Error";
    }
    Lecture l = lectureRepository.findById(unit.getTypeId());
    // For incrementing noOfViews feature
    if (l != null) {
      int noOfViews = l.getNoOfViews();
      noOfViews += 1;
      l.setNoOfViews(noOfViews);

      lectureRepository.saveAndFlush(l);

      unit.setNoOfViews(noOfViews);
      unitRepository.saveAndFlush(unit);
    }

    if (unit != null && unit.getBatch() != null && unit.getBatch().getCourse() != null) {
      Course c = unit.getBatch().getCourse();
      int totalNoOfViews = c.getTotalNoOfViews();

      totalNoOfViews += 1;
      c.setTotalNoOfViews(totalNoOfViews);
      courseRepository.saveAndFlush(c);
    }
    String playScript = generateJWPlayerPlayScript(user, unit, l);
    return playScript;
  }

  public String playLectureAdmin(int unitID) {
    long userId = jwtTokenUtil.getLoggedInUserID();
    User user = userRepository.findByUserId(userId);
    String playScript = "";
    int lectureID = 0;
    if (unitID == 0) {
      return "Error";
    }
    Unit unit = unitRepository.findByUnitId(unitID);
    lectureID = unit.getTypeId();
    if (lectureID == 0) {
      return "Error";
    }
    Lecture l = lectureRepository.findById(unit.getTypeId());
    // For incrementing noOfViews feature
    if (l != null) {
      int noOfViews = l.getNoOfViews();
      noOfViews += 1;
      l.setNoOfViews(noOfViews);
      lectureRepository.saveAndFlush(l);

      unit.setNoOfViews(noOfViews);
      unitRepository.saveAndFlush(unit);
    }
    if (unit != null && unit.getBatch() != null && unit.getBatch().getCourse() != null) {
      Course c = unit.getBatch().getCourse();
      int totalNoOfViews = c.getTotalNoOfViews();

      totalNoOfViews += 1;
      c.setTotalNoOfViews(totalNoOfViews);
      courseRepository.saveAndFlush(c);
    }
    playScript = generateJWPlayerPlayScript(user, unit, l);
    return playScript;
  }


  private void updateUserUnitStatus(User user, Unit unit)
  {
    // TODO check if validity has expired at unit level too
    UserUnitLinkage linkage = userUnitLinkageRepository
        .findByUserAndUnit(user.getUserId(), unit.getUnitId());
    if (linkage != null) {
      linkage.setNoOfClicks(linkage.getNoOfClicks() + 1);
      linkage.setWatchStatus(WatchStatus.WATCHED);
      linkage.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      userUnitLinkageRepository.save(linkage);
    } else if (unit.isFree()) {
      UserUnitLinkage linkage2 = new UserUnitLinkage();
      linkage2.setUnit(unit);
      linkage2.setUser(user);
      linkage2.setNoOfClicks(linkage.getNoOfClicks() + 1);
      linkage2.setWatchStatus(WatchStatus.WATCHED);
      linkage2.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      userUnitLinkageRepository.save(linkage2);
    }
  }

  public String playLectureForStudent(UnitRequest unitRequest, boolean playForMobile) {

    int lectureID = 0;
    String playScript = "";
    try {
      if (unitRequest == null || unitRequest.getUnitID() == 0) {
        return "Error";
      }

      Unit unit = unitRepository.findByUnitId(unitRequest.getUnitID());
      long userId = jwtTokenUtil.getLoggedInUserID();
      User user = userRepository.findByUserId(userId);

      Set<Authority> authorities = user.getAuthorities();
      boolean isSpecialAccess = false;
      for (Authority a : authorities) {
        //giving special access to all mobile app users for now
        if (a.getName().equals(AuthorityName.ROLE_SPECIAL_ACCESS) || playForMobile) {
          isSpecialAccess = true;
          break;
        }
      }

      if (!unit.isFree() && !isSpecialAccess) {
        // TODO: Only for testing the OTP flow
        if (!checkAndUpdateDevice(unitRequest, user)) {
          // TODO for now lets just track user devices properly
          // Trying to send OTP
          /*
           * String username = jwtTokenUtil.getLoggedInUserName(); int
           * otp = otpService.generateOTP(username);
           * LOGGER.info("OTP : " + otp); // Generate The Template to
           * send OTP smsService.sendSMSMessage("Your OTP is " + otp,
           * "+917676024159");
           */
          return "generateOTP";
        }
      }
      lectureID = unit.getTypeId();
      if (lectureID == 0) {
        return "Error";
      }
      Lecture l = lectureRepository.findById(unit.getTypeId());
      // For incrementing noOfViews feature
      if (l != null) {
        int noOfViews = l.getNoOfViews();
        noOfViews += 1;
        l.setNoOfViews(noOfViews);
        lectureRepository.saveAndFlush(l);

        unit.setNoOfViews(noOfViews);
        unitRepository.saveAndFlush(unit);
      }

      if (unit != null && unit.getBatch() != null && unit.getBatch().getCourse() != null) {
        Course c = unit.getBatch().getCourse();
        int totalNoOfViews = c.getTotalNoOfViews();
        totalNoOfViews += 1;
        c.setTotalNoOfViews(totalNoOfViews);
        courseRepository.saveAndFlush(c);

        if (l.getJwMacro().equals("cancelled")) {
          playScript = "";
        }
        if (playForMobile) {
          playScript = generateJsonToPlayLectureOnMobile(user, l);
        } else {
          playScript = generateJWPlayerPlayScript(user, unit, l);
        }

      }
    } catch (Exception e) {
      LOGGER.error(Lecture.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return playScript;
  }

  public boolean updateDevice(UnitRequest unitRequest) {
    long userId = jwtTokenUtil.getLoggedInUserID();
    User u = userRepository.findByUserId(userId);

    Set<UserDevice> devices = u.getDevices();
    Date oldestRecordDate = new Date();
    UserDevice oldestDevice = null;

    // check if fingerprint is okay
    for (UserDevice device : devices) {
      if (device.getFingerPrint().equals(unitRequest.getNewFingerprint())) {
        return true;
      }

      if (device.getCreatedAt() == null) {
        oldestDevice = device;
        continue;
      }
      if (device.getCreatedAt().before(oldestRecordDate)) {
        oldestRecordDate = device.getCreatedAt();
        oldestDevice = device;
      }
    }

    List<UserDevice> newDevices = userDeviceRepository
        .findByFingerPrintOrderByDeviceIDDesc(unitRequest.getNewFingerprint());
    UserDevice newDevice = null;
    if (newDevices != null && newDevices.size() > 0) {
      newDevice = newDevices.get(0);
    }

    if (newDevice != null) {
      u.getDevices().remove(oldestDevice);
      u.getDevices().add(newDevice);
      userRepository.save(u);
    }

    String subject = "Device has got successfully changed for user " + u.getEmailId();
    String message =
        " New Device details are : \n" + newDevice.getInfo() + " \n" + "Old device details are : \n"
            + oldestDevice.getInfo();
    emailService.sendSimpleMessage("security@neostencil.com", subject, message);

    UserDeviceLog userDeviceLog = new UserDeviceLog();
    userDeviceLog.setOldDeviceID(oldestDevice.getDeviceID());
    userDeviceLog.setNewDeviceID(newDevice.getDeviceID());
    userDeviceLog.setUserID(u.getUserId());
    userDeviceLog.setUpdateCause("Forced Change: Need to investigate");
    userDeviceLogRepository.save(userDeviceLog);
    return true;
  }

  private boolean checkAndUpdateDevice(UnitRequest unitRequest, User u) {

    Set<UserDevice> devices = u.getDevices();
    Date oldestRecordDate = new Date();
    UserDevice oldestDevice = null;

    // check if fingerprint is okay
    for (UserDevice device : devices) {
      if (device.getFingerPrint().equals(unitRequest.getNewFingerprint())) {
        return true;
      }

      if (device.getCreatedAt() == null) {
        oldestDevice = device;
        continue;
      }
      if (device.getCreatedAt().before(oldestRecordDate)) {
        oldestRecordDate = device.getCreatedAt();
        oldestDevice = device;
      }
    }

    // save device anyway
    UserDevice newDevice = new UserDevice();
    newDevice.setFingerPrint(unitRequest.getNewFingerprint());
    newDevice.setInfo(unitRequest.getFingerprintInfo());
    newDevice.setShortInfo(unitRequest.getFingerprintShortInfo());
    newDevice.setType(unitRequest.getDeviceType());
    newDevice.addMetadata();

    newDevice = userDeviceRepository.save(newDevice);

    // check if old fingerprint needs to be removed and updated
    for (UserDevice device : devices) {
      System.out.println(device.toString());
      if (device.getFingerPrint().equals(unitRequest.getOldFingerprint())) {
        u.getDevices().remove(device);
        u.getDevices().add(newDevice);
        userRepository.save(u);

        UserDeviceLog userDeviceLog = new UserDeviceLog();
        userDeviceLog.setOldDeviceID(device.getDeviceID());

        userDeviceLog.setNewDeviceID(newDevice.getDeviceID());

        userDeviceLog.setUserID(u.getUserId());
        userDeviceLog.setUpdateCause("BrowserUpdateOrSimilar");
        userDeviceLogRepository.save(userDeviceLog);
        return true;
      }
    }

    // Counting no. of PCs and mobiles currently active for the user
    int noOfMobiles = 0;
    int noOfPCs = 0;

    for (UserDevice ud : devices) {
      if (ud.getType() != null && ud.getType().equals(DeviceType.PC)) {
        noOfPCs++;
      } else if (ud.getType() != null && ud.getType().equals(DeviceType.Mobile)) {
        noOfMobiles++;
      }
    }

    // just add a new device if allowed
    if ((noOfMobiles < Constants.maxAllowedMobiles && DeviceType.Mobile
        .equals(newDevice.getType()))
        || (noOfPCs < Constants.maxAllowedPCs && DeviceType.PC.equals(newDevice.getType()))) {
      u.getDevices().add(newDevice);
      userRepository.save(u);

      UserDeviceLog userDeviceLog = new UserDeviceLog();
      userDeviceLog.setNewDeviceID(newDevice.getDeviceID());
      userDeviceLog.setUserID(u.getUserId());
      userDeviceLogRepository.save(userDeviceLog);
    } else {
      //send this email with details to operations team not developers in case of device change
      //	String textToEmail= "Check what is going on for user " + u.getEmailId()  + " " + unitRequest.toString();
      //	emailService.sendErrorLogsToDevelopers(textToEmail);
      return false;
    }

    return true;
  }

  private String getDRMEncodedXML() {
    AuthXMLGenerator authxmlGenerator = new AuthXMLGenerator();

    authxmlGenerator.setPrivateKey(drmKeyFile);
    authxmlGenerator.setRsaPubKeyId(drmKeyName);

    authxmlGenerator
        .setAuthXMLExpirationTime(new Date(new Date().getTime() + 200000L * Timer.ONE_MINUTE));
    // PlayReady License
    License prLicense = new License();
    Policy prLicensePolicy = new Policy();
    try {
      prLicensePolicy.setExpirationAfterFirstPlay(60 * 300);

      prLicensePolicy.setIsPersistent(true);
      prLicense.setPolicyId(prLicensePolicy.getId());

      authxmlGenerator.getLicenses().add(prLicense);
      authxmlGenerator.getPolicies().add(prLicensePolicy);

      // Widevine license
      WidevinePolicy wvLicensePolicy = new WidevinePolicy();
      wvLicensePolicy.setCanPlay(true);
      wvLicensePolicy.setCanPersist(true);
      wvLicensePolicy.setLicenseDuration(60 * 300);

      WidevineContentKeySpec wvKeySpec = new WidevineContentKeySpec();
      wvKeySpec.setTrackType("HD");
      wvKeySpec.setSecurityLevel(1);

      authxmlGenerator.setWidevinePolicy(wvLicensePolicy);
      authxmlGenerator.setWidevineContentKeySpec(wvKeySpec);

      // Fairplay license
      FairplayPolicy fpPolicy = new FairplayPolicy();
      fpPolicy.setIsPersistent(true);
      fpPolicy.setPersistenceSeconds(60 * 300);

      authxmlGenerator.setFairplayPolicy(fpPolicy);
      XElement generatedXML = authxmlGenerator.generateAuthenticationXML();
      XElement signedAuthXML = authxmlGenerator.SignAuthenticationXML(generatedXML);
      String encodedXML = authxmlGenerator.EncodeAuthenticationXML(signedAuthXML);
      System.out.println(encodedXML);

      return encodedXML;
    } catch (Exception e) {
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      return "";
    }

  }

  public UnitDetailResponse getUnitDetailForAdmin(int unitId) {
    UnitDetailResponse response = new UnitDetailResponse();
    long userID = jwtTokenUtil.getLoggedInUserID();
    Unit unit = unitRepository.findByUnitId(unitId);

    if (unit != null && unit.getType() != null) {
      CourseBatch batch = unit.getBatch();
      if (batch != null && batch.getId() != 0) {
        UserCourseBatchLinkage byUserAndBatchAndStatus = userCourseBatchLinkageRepository
            .findByUserAndBatchAndStatus(userID, batch.getId(), UserCourseLinkageStatus.ACTIVE);

        //			if (byUserAndBatchAndStatus != null) {

        switch (unit.getType()) {
          case LECTURE:

            String script = this.playLectureAdmin(unitId);
            response.setLectureScript(script);
            Lecture l = lectureRepository.findById(unit.getTypeId());
            response.setLecture(l);
            break;
          case ASSIGNMENT:
            response.setAssignment(unit);
            break;
          case NOTES:
            response.setNote(unit);
            break;
        }
        //			}

      }
    }

    return response;
  }

  public UnitDetailResponse getUnitDetail(UnitRequest unitRequest, boolean playForMobile) {
    UnitDetailResponse response = new UnitDetailResponse();
    UnitOnMobile unitOnMobile = new UnitOnMobile();

    long userID = jwtTokenUtil.getLoggedInUserID();
    User loggedInUser = userRepository.findByUserId(userID);
    Unit unit = unitRepository.findByUnitId(unitRequest.getUnitID());

    if (unit != null && unit.getType() != null) {
      CourseBatch batch = unit.getBatch();
      if (batch != null && batch.getId() != 0) {
        UserCourseBatchLinkage byUserAndBatchAndStatus = userCourseBatchLinkageRepository
            .findByUserAndBatchAndStatus(userID, batch.getId(), UserCourseLinkageStatus.ACTIVE);
        if (batch.getCourse() != null && batch.getCourse().getInstructors() != null
            && batch.getCourse().getInstructors().size() > 0) {
          for (TeacherDetails td : batch.getCourse().getInstructors()) {
            if (td.isReceiveQueries()) {
              response.setEnableQueryButton(true);
              break;
            }
          }
        }
        if (byUserAndBatchAndStatus != null) {

          switch (unit.getType()) {
            case LECTURE:

              String script = this.playLectureForStudent(unitRequest, playForMobile);
              if (playForMobile) {
                PlayLectureOnMobile playLectureOnMobile = new Gson()
                    .fromJson(script, PlayLectureOnMobile.class);
                unitOnMobile.setLectureOnMobile(playLectureOnMobile);

              } else {
                response.setLectureScript(script);
              }

              Lecture l = lectureRepository.findById(unit.getTypeId());
              response.setLecture(l);

              break;
            case ASSIGNMENT:
              response.setAssignment(unit);
              break;
            case NOTES:

              if (playForMobile) {

                MobileNote mobileNote = new MobileNote();

                Document parse = Jsoup.parse(unit.getDescription());
                Element iframe = parse.select("iframe").first();
                String iframeSrc = iframe.attr("src");

                mobileNote.setTitle(unit.getTitle());
                mobileNote.setUrl(iframeSrc);

                unitOnMobile.setNote(mobileNote);
              } else {
                response.setNote(unit);
              }

              break;

            case QUIZ:
              QuizWithQuestionResponse quizResponse = null;
              Quiz quiz = quizRepository.findByQuizId(unit.getTypeId());
              List<Question> saveQuestionList = customQuestionRepository
                  .findQuestionsByQuizId(quiz);
              if (quiz.isTopicWiseQuestion()) {
                quizResponse = createQuizResponseWithTopic(quiz,
                    saveQuestionList);
              } else {
                quizResponse = createQuizResponse(quiz,
                    saveQuestionList);
              }
              quizResponse.setUnit(unit);
              response.setQuiz(quizResponse);
              break;
          }

          updateUserUnitStatus(loggedInUser, unit);
        }

      }

      if (playForMobile) {
        unitOnMobile.setUnit(unit);
        response.setUnitOnMobile(unitOnMobile);
      }
      response.setUnitType(unit.getType().toString());
    }

    return response;
  }

  public UnitDetailResponse getFreeUnitDetails(int unitId) {
    UnitDetailResponse response = new UnitDetailResponse();

    Unit unit = unitRepository.findByUnitId(unitId);

    if (unit != null && unit.getType() != null && unit.isFree()) {
      switch (unit.getType()) {
        case LECTURE:
          String script = this.playLectureFree(unitId);
          response.setLectureScript(script);
          Lecture l = lectureRepository.findById(unit.getTypeId());
          response.setLecture(l);
          break;
        case ASSIGNMENT:
          response.setAssignment(unit);
          break;
        case NOTES:
          response.setNote(unit);
          break;

        case QUIZ:
          QuizWithQuestionResponse quizResponse = null;
          Quiz quiz = quizRepository.findByQuizId(unit.getTypeId());
          List<Question> saveQuestionList = customQuestionRepository
              .findQuestionsByQuizId(quiz);
          if (quiz.isTopicWiseQuestion()) {
            quizResponse = createQuizResponseWithTopic(quiz,
                saveQuestionList);
          } else {
            quizResponse = createQuizResponse(quiz,
                saveQuestionList);
          }
          quizResponse.setUnit(unit);
          response.setQuiz(quizResponse);
          break;
      }
      response.setUnitType(unit.getType().toString());
    }

    return response;
  }

  private QuizWithQuestionResponse createQuizResponse(Quiz quiz, List<Question> questionList) {
    QuizWithQuestionResponse response = new QuizWithQuestionResponse();
    response.setQuiz(quiz);
    response.setQuestionList(questionList);
    return response;
  }

  private QuizWithQuestionResponse createQuizResponseWithTopic(Quiz quiz,
      List<Question> questionList) {

    QuizWithQuestionResponse response = null;

    if (questionList != null && questionList.size() > 0) {

      response = new QuizWithQuestionResponse();
      LinkedHashMap<String, List<Question>> questionMap = new LinkedHashMap<>();

      for (Question question : questionList) {
        if (question != null && question.getTopic() != null) {
          if (questionMap.get(question.getTopic()) != null
              && questionMap.get(question.getTopic()).size() > 0) {
            List<Question> temp = questionMap.get(question.getTopic());
            temp.add(question);
            questionMap.put(question.getTopic(), temp);
          } else {
            List<Question> temp = new ArrayList<>();
            temp.add(question);
            questionMap.put(question.getTopic(), temp);
          }
        }
      }

      response.setQuiz(quiz);
      response.setQuestionMap(questionMap);
    }

    return response;
  }

  public UnitDetailResponse getTeacherUnitDetails(int unitId) {
    UnitDetailResponse response = new UnitDetailResponse();
    if (unitId != 0) {
      Unit unit = unitRepository.findByUnitId(unitId);

      if (unit != null) {

        // Checking if the unit belongs to the loggedInTeacher
        boolean valid = false;
        long loggedInTeacherId = jwtTokenUtil.getLoggedInUserID();

        if (unit.getBatch() != null && unit.getBatch().getCourse() != null
            && unit.getBatch().getCourse().getInstructors() != null
            && unit.getBatch().getCourse().getInstructors().size() > 0) {
          for (TeacherDetails td : unit.getBatch().getCourse().getInstructors()) {
            if (td != null && td.getUserAccount() != null
                && td.getUserAccount().getUserId() == loggedInTeacherId) {
              valid = true;
              break;
            }
          }
        }

        if (valid) {
          switch (unit.getType()) {
            case LECTURE:

              Lecture l = lectureRepository.findById(unit.getTypeId());
              response.setLecture(l);
              String script = this.playLectureAdmin(unitId);
              response.setLectureScript(script);
              break;
            case ASSIGNMENT:
              response.setAssignment(unit);
              break;
            case NOTES:
              response.setNote(unit);
              break;
            case QUIZ:
              QuizWithQuestionResponse quizResponseWithTopic = null;
              Quiz quiz = quizRepository.findByQuizId(unit.getTypeId());
              List<Question> saveQuestionList = customQuestionRepository
                  .findQuestionsByQuizId(quiz);
              if (quiz.isTopicWiseQuestion()) {
                quizResponseWithTopic = createQuizResponseWithTopic(quiz,
                    saveQuestionList);
              } else {
                quizResponseWithTopic = createQuizResponse(quiz,
                    saveQuestionList);
              }
              quizResponseWithTopic.setUnit(unit);
              response.setQuiz(quizResponseWithTopic);
              break;
          }
        }
        response.setUnitType(unit.getType().toString());
      }

    }
    return response;
  }

  public String updateUserToAssignmentUnit(List<UserUnitLinkage> request) {

    if (request == null && request.size() == 0) {
      throw new InvalidInputException(UserUnitLinkage.class.getName(), null, null);
    }

    for (UserUnitLinkage userUnitLinkage : request) {

      if (userUnitLinkage.getUser().getEmailId() != null && !userUnitLinkage.getUser().getEmailId()
          .isEmpty()) {

        User user = userRepository.findByEmailId(userUnitLinkage.getUser().getEmailId());

        if (user == null) {
          user = new User();
          user.setFullName(userUnitLinkage.getUser().getFullName());
          user.setEmailId(userUnitLinkage.getUser().getEmailId());
          user.setPassword(userUnitLinkage.getUser().getFullName());
          SignUpResponse signUpResponse = userService.signUp(user);
          if (signUpResponse.isLoginSucces()) {
            User newUser = userRepository.findByEmailId(userUnitLinkage.getUser().getEmailId());
            userUnitLinkage.setUser(newUser);
          }

        } else {
          userUnitLinkage.setUser(user);
        }
        Unit unit = unitRepository.findByUnitId(userUnitLinkage.getUnit().getUnitId());

        if (unit == null) {
          throw new InvalidInputException(Unit.class.getName(), "id", "id");
        }

        userUnitLinkage.setUnit(unit);

        userUnitLinkageRepository.save(userUnitLinkage);

        List<UserCourseBatchLinkage> savedLinkages = userCourseBatchLinkageRepository
            .findByUserAndBatch(
                userUnitLinkage.getUser().getUserId(),
                userUnitLinkage.getUnit().getBatch().getId());
        UserCourseBatchLinkage byUserAndBatch = null;
        if (savedLinkages != null && savedLinkages.size() > 1) {
          for (UserCourseBatchLinkage link : savedLinkages) {
            if (UserCourseLinkageStatus.ACTIVE.equals(link.getStatus())) {
              byUserAndBatch = link;
              break;
            }
          }
        } else if (savedLinkages != null) {
          byUserAndBatch = savedLinkages.get(0);
        }

        if (byUserAndBatch == null) {
          byUserAndBatch = new UserCourseBatchLinkage();
          byUserAndBatch.setCourseBatch(unit.getBatch());

          byUserAndBatch.setUser(userUnitLinkage.getUser());
          byUserAndBatch.addMetadata();

          userCourseBatchLinkageRepository.save(byUserAndBatch);

        }

      }
    }

    return "Successfully update the status";

  }


  public String sendAssignmentEmails(List<UserUnitLinkage> request, int courseId) {

    Course course = courseRepository.findById(courseId);
    String teacherName = null;
    if (course != null) {
      if (course.getInstructors() != null && course.getInstructors().size() > 1) {
        teacherName = course.getInstructorName();
      } else if (course.getInstructors() != null && course.getInstructors().size() == 1) {
        teacherName = course.getInstructors().iterator().next().getTeacherName();
      }
    }

    if (request != null && request.size() > 0) {
      for (UserUnitLinkage userUnitLinkage : request) {
        if (userUnitLinkage != null) {
          int unitId = userUnitLinkage.getUnit().getUnitId();
          Unit unit = unitRepository.findByUnitId(unitId);
          String title = unit.getTitle();
          if (userUnitLinkage.getAssignmentSubmission().getStatus().name()
              .equalsIgnoreCase(AssignmentStatus.CHECKED.name())) {
            emailService.sentToStudentCheckedAssignmentAlertHTMLMessage(userUnitLinkage.getUser(),
                teacherName, title, userUnitLinkage.getAssignmentSubmission());
          } else if (userUnitLinkage.getAssignmentSubmission().getStatus().name()
              .equalsIgnoreCase(AssignmentStatus.SENT_TO_TEACHER.name())) {
            emailService
                .sentToTeacherAssignmentAlertHTMLMessage(userUnitLinkage.getUser(), teacherName,
                    title, userUnitLinkage.getAssignmentSubmission().getSubmittedDate());
          }
        }
      }
    }

    return "Successfully send the emails";

  }

  public UserUnitLinkage assignmentSubmissionByUser(@Valid AssignmentSubmissionRequest request,
      long userID) {

    UserUnitLinkage response = null;
    if (request != null && request.getUnitId() != 0 && !TextUtils.isEmpty(request.getUrl())
        && !TextUtils.isEmpty(request.getName())) {

      UserUnitLinkage byUserAndUnit = userUnitLinkageRepository.findByUserAndUnit(userID,
          request.getUnitId());

      if (byUserAndUnit != null) {
        AssignmentSubmission assignmentSubmission = new AssignmentSubmission();
        assignmentSubmission.setType(request.getType());
        assignmentSubmission.setAttachmentName(request.getName());
        assignmentSubmission.setAttachmentUrl(request.getUrl());
        assignmentSubmission.setStatus(AssignmentStatus.RECEIVED);
        assignmentSubmission.setSubmittedDate(new Date());

        byUserAndUnit.setAssignmentSubmission(assignmentSubmission);
        response = userUnitLinkageRepository.save(byUserAndUnit);
      } else {
        throw new InvalidInputException(UserUnitLinkage.class.getName(), null, null);
      }

    } else {
      throw new InvalidInputException(UserUnitLinkage.class.getName(), null, null);
    }

    if (response != null) {
      emailService.sendAssignmentAlert(response);

    }

    return response;

  }

  /**
   * For fetching all the units on admin dashboard all units page.
   */
  public Collection<UnitProjection> fetchAllUnits() {
    Collection<UnitProjection> response = null;
    Date referenceDate = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(referenceDate);
    c.add(Calendar.MONTH, -2);
    Date fetchFrom = c.getTime();
    response = unitRepository.findAllProjectedByUpdatedAtGreaterThan(fetchFrom);

    return response;
  }

  public Unit fetchUnitById(int id) {
    Unit response = null;
    if (id != 0) {
      response = unitRepository.findByUnitId(id);
    } else {
      throw new InvalidInputException(Unit.class.getName(), null, null);
    }
    return response;
  }

  public UserQuizSubmissionResponse calculateUserQuizStats(long userID, int unitId, int quizId,
      String timeTaken, List<QuizSubmissionRequest> quizSubmissionRequest) {

    UserQuizSubmission userQuizSubmission = null;
    UserQuizSubmissionResponse response = null;
    SectionLevel sectionLevelResponse = null;
    LinkedHashMap<String, SectionLevel> topicLevelResponse = null;
    List<QuizWithAnswerResponse> quizWithAnswerResponse = null;
    User user = null;

    if (unitId != 0 && quizId != 0 && userID != 0 && quizSubmissionRequest != null
        && quizSubmissionRequest.size() > 0) {

      Quiz quiz = quizRepository.findByQuizId(quizId);

      user = userRepository.findByUserId(userID);

      if (quiz != null) {

        quizWithAnswerResponse = new ArrayList<>();
        userQuizSubmission = new UserQuizSubmission();
        userQuizSubmission.setQuiz(quiz);
        userQuizSubmission.setUser(user);

        String listToJson = new Gson().toJson(quizSubmissionRequest);
        userQuizSubmission.setUserSubmission(new Gson().toJson(listToJson));

        userQuizSubmission.addMetadata();

        if (quiz.isTopicWiseAnalysis()) {
          topicLevelResponse = new LinkedHashMap<>();
        }

        response = new UserQuizSubmissionResponse();

        sectionLevelResponse = new SectionLevel();

        for (QuizSubmissionRequest request : quizSubmissionRequest) {
          if (request != null) {
            int questionId = request.getQuestionId();
            String userAnswer = request.getAnswer();
            boolean attempt = request.isAttempt();
            Question question = customQuestionRepository.findQuestionById(questionId);
            String answer = question.getAnswer();
            if (question != null) {

              QuizWithAnswerResponse withAnswerResponse = new QuizWithAnswerResponse();
              withAnswerResponse.setQuestion(question);
              withAnswerResponse.setAnswer(answer);
              withAnswerResponse.setUserAnswer(userAnswer);
              withAnswerResponse.setExplanation(question.getAnswerExplanation());

              quizWithAnswerResponse.add(withAnswerResponse);

              if (quiz.isTopicWiseAnalysis()) {

                if (!topicLevelResponse.containsKey(question.getTopic())) {
                  topicLevelResponse.put(question.getTopic(), new SectionLevel());
                }

                SectionLevel sectionLevel = topicLevelResponse.get(question.getTopic());

                if (attempt) {

                  if (!TextUtils.isEmpty(answer) && Objects
                      .equals(answer.trim(), userAnswer.trim())) {
                    sectionLevel
                        .setNoOfCorrectQuestion(sectionLevel.getNoOfCorrectQuestion() + 1);
                    sectionLevel.setMarksObtained(
                        sectionLevel.getMarksObtained() + question.getPositivePoints());
                  } else {
                    sectionLevel
                        .setNoOfIncorrectQuestion(sectionLevel.getNoOfIncorrectQuestion() + 1);

                    sectionLevel.setMarksObtained(
                        sectionLevel.getMarksObtained() - question.getNegativePoints());
                  }
                  sectionLevel.setNoOfAttemptQuestion(sectionLevel.getNoOfAttemptQuestion() + 1);

                } else {
                  sectionLevel.setNoOfSkippedQuestion(sectionLevel.getNoOfSkippedQuestion() + 1);
                }

                sectionLevel.setTotalNoOfQuestion(sectionLevel.getTotalNoOfQuestion() + 1);

                topicLevelResponse.put(question.getTopic(), sectionLevel);
              }

              if (attempt) {

                if (!TextUtils.isEmpty(answer) && Objects
                    .equals(answer.trim(), userAnswer.trim())) {
                  sectionLevelResponse.setNoOfCorrectQuestion(
                      sectionLevelResponse.getNoOfCorrectQuestion() + 1);
                  sectionLevelResponse.setMarksObtained(
                      sectionLevelResponse.getMarksObtained() + question.getPositivePoints());
                } else {
                  sectionLevelResponse
                      .setNoOfIncorrectQuestion(
                          sectionLevelResponse.getNoOfIncorrectQuestion() + 1);

                  sectionLevelResponse.setMarksObtained(
                      sectionLevelResponse.getMarksObtained() - question.getNegativePoints());
                }
                sectionLevelResponse
                    .setNoOfAttemptQuestion(sectionLevelResponse.getNoOfAttemptQuestion() + 1);

              } else {
                sectionLevelResponse
                    .setNoOfSkippedQuestion(sectionLevelResponse.getNoOfSkippedQuestion() + 1);
              }

              sectionLevelResponse
                  .setTotalNoOfQuestion(sectionLevelResponse.getTotalNoOfQuestion() + 1);
            }


          }


        }
      }

      long hours = TimeUnit.MINUTES.toHours(Long.valueOf((long) quiz.getTimeLimit()));
      long remainMinutes = (long) (quiz.getTimeLimit() - TimeUnit.HOURS.toMinutes(hours));

      String format = String.format("%02d:%02d:%02d", hours, remainMinutes, 00);

      if (timeTaken.equalsIgnoreCase("time over")) {

        timeTaken = String.format("%02d:%02d:%02d", 00, 00, 00);


      } else {
        timeTaken = timeTaken.replace("" + timeTaken.charAt(0), "");
      }

      String userTimeTaken = CommonUtil.comapreTime(format, timeTaken);
      userQuizSubmission.setTimeTaken(userTimeTaken);


    }

    assert userQuizSubmission != null;
    userQuizSubmission.setMarksObtained(sectionLevelResponse.getMarksObtained());
    userQuizSubmissionRepository.save(userQuizSubmission);

    LinkedList<QuizLeaderBoardResponse> quizLeaderBoardResponses = fetchLeaderboardbyQuizId(quizId);

    if (response != null) {

      /*userService.hubspotSignUpWithExtraData(user.getFullName(),user.getMobileNumber(),user.getEmailId(),user.getCity(),user.getExamSegment(),
          String.valueOf(sectionLevelResponse.getMarksObtained()),"Quiz");*/
      response.setSectionLevel(sectionLevelResponse);
      response.setTopicLevel(topicLevelResponse);
      response.setSolution(quizWithAnswerResponse);
      response.setLeaderBoard(quizLeaderBoardResponses);
    }

    return response;

  }

  public LinkedList<QuizLeaderBoardResponse> fetchLeaderboardbyQuizId(int quizId) {
    List<UserQuizSubmission> userQuizSubmissions = userQuizSubmissionRepository
        .findByQuizQuizIdOrderByMarksObtainedDesc(quizId);
    LinkedList<QuizLeaderBoardResponse> leaderBoardResponseList = new LinkedList<>();

    if (userQuizSubmissions != null & userQuizSubmissions.size() > 0) {
      for (int i = 0; i < userQuizSubmissions.size(); i++) {
        UserQuizSubmission userQuizSubmission = userQuizSubmissions.get(i);
        QuizLeaderBoardResponse quizLeaderBoardResponse = new QuizLeaderBoardResponse();
        User user = userQuizSubmission.getUser();
        double marksObtained = userQuizSubmission.getMarksObtained();

        quizLeaderBoardResponse.setEmail(user.getEmailId());
        quizLeaderBoardResponse.setName(user.getFullName());
        quizLeaderBoardResponse.setRank(String.valueOf(i + 1));
        quizLeaderBoardResponse.setMarks(String.valueOf(marksObtained));

        leaderBoardResponseList.add(quizLeaderBoardResponse);
      }
    }

    return leaderBoardResponseList;
  }

  public QuizTemplate fetchQuizTemplateBySlug(String slug) {
    return quizTemplateRepository.findQuizTemplateBySlug(slug);
  }

  /**
   * For marking a lecture as started
   */
  public void markClassStarted(int unitId) {
    Unit unit = unitRepository.findByUnitId(unitId);
    unit.setHasStarted(true);
    unitRepository.saveAndFlush(unit);
    try {
      TimeUnit.MINUTES.sleep(15);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    List<UserUnitLinkage> linkages = userUnitLinkageRepository.findByUnit(unitId);

    if (linkages != null && linkages.size() > 0) {
      for (UserUnitLinkage linkage : linkages) {
        if (WatchStatus.NOT_STARTED.equals(linkage.getWatchStatus())) {
          String lectureDetails = linkage.getUnit().getTitle();
          userService.markStudentAbsent(linkage.getUser(), lectureDetails);
        }
      }
    }
  }
}
