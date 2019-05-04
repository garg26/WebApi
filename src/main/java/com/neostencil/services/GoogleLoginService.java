package com.neostencil.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.neostencil.common.enums.LoginType;
import com.neostencil.domain.google.GoogleToken;
import com.neostencil.domain.google.GoogleUserDetails;
import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.AuthorityName;
import com.neostencil.model.entities.User;
import com.neostencil.model.repositories.AuthorityRepository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.responses.LoginResponse;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.security.JwtUser;

@Service
public class GoogleLoginService {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Value("${google.redirecturl}")
  private String REDIRECT_URI;

  @Value("${google.clientid}")
  private String CLIENT_ID;
  @Value("${google.clientsecret}")
  private String CLIENT_SECRET;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authRepository;

  @Autowired
  private JwtTokenUtil jwtUtil;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private EmailService emailService;
  
  @Autowired
  UserService userManagementService;

  private RestTemplate restTemplate = new RestTemplate();

  public LoginResponse googleLogin(String googleAccessCode) {

    LoginResponse response = new LoginResponse();
    if (googleAccessCode == null || googleAccessCode.isEmpty()) {
      response.setLoginSucces(false);
      response.setErrorMessage("Wrong Google credentials");
      return response;
    }

    GoogleToken googleToken;
    googleToken = geGoogleAccessTokenFromCode(googleAccessCode);
    if (googleToken == null)
    {
      response.setLoginSucces(false);
      response.setErrorMessage("Something appears to have gone wrong with request");
      return response;
    }


    LOGGER.info("Token Object = {}", googleToken);

    if (googleToken.getExpires_in() <= 0) {
      response.setLoginSucces(false);
      response.setErrorMessage("Token appears to have expired google");
      return response;
    }


    if (googleToken.getExpires_in() <= 0) {
      response.setLoginSucces(false);
      response.setErrorMessage("Token appears to have expired google");
      return response;
    }

    LOGGER.debug("Token appears to be valid, fetching user details from token");
    GoogleUserDetails googleUserDetails;
    googleUserDetails = getUserDetailsFromAIDToken(googleToken.getId_token());


    return saveLoginWithGoogle(googleUserDetails,response);
  }

  public LoginResponse saveLoginWithGoogle(GoogleUserDetails googleUserDetails, LoginResponse response) {

    if(googleUserDetails!=null) {
      User savedUser = userRepository.findByEmailId((String) googleUserDetails.getEmail());
      if (savedUser != null) {
        response.setLoginSucces(true);
        JwtUser jwtUserDetails =
            (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUserName());
        String accessToken = jwtUtil.generateToken(jwtUserDetails);
        response.setUserName(savedUser.getUserName());
        response.setAccessToken(accessToken);
        return response;
      }

      LOGGER.info("In this case, use doesn't exist, lets create a new user");
      String accessToken = "";

      // TODO save details for Google ID, other info to fetch from Google
      User user = new User();
      user.setEmailId(googleUserDetails.getEmail());
      user.setFullName(googleUserDetails.getName());
      user.setUserName(googleUserDetails.getEmail());
      Set<Authority> auths = new HashSet<Authority>();
      Authority authority = authRepository.findByName(AuthorityName.ROLE_USER);
      if (authority == null) {
        authority = new Authority();
        authority.setName(AuthorityName.ROLE_USER);
      }
      auths.add(authority);
      user.setAuthorities(auths);
      user.setEnabled(true);
      user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
      user.setLoginType(LoginType.GOOGLE);

      savedUser = userRepository.save(user);
      if (savedUser != null) {
        response.setUserName(savedUser.getUserName());
        response.setLoginSucces(true);
        userManagementService.hubspotSignUpData(savedUser.getEmailId(),"Website Registration/ First Login");

        JwtUser userDetails =
            (JwtUser) userDetailsService.loadUserByUsername(savedUser.getUserName());
        accessToken = jwtUtil.generateToken(userDetails);
        response.setAccessToken(accessToken);
        emailService.sendEmailOnSignUp(savedUser);
      } else {
        response.setErrorMessage("Error signing up. Please try again.");
        response.setLoginSucces(false);
      }
    }else{
      response.setLoginSucces(false);
      response.setErrorMessage("Wrong Google credentials");
    }

    return response;
  }


    private GoogleToken geGoogleAccessTokenFromCode(String code) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("client_id", CLIENT_ID);
    map.add("redirect_uri", REDIRECT_URI);
    map.add("client_secret", CLIENT_SECRET);
    map.add("code", code);
    map.add("grant_type", "authorization_code");
    map.add("scope", "email");

    HttpEntity<MultiValueMap<String, String>> request =
          new HttpEntity<MultiValueMap<String, String>>(map, headers);
    String url = "https://www.googleapis.com/oauth2/v4/token";

    try {
      ResponseEntity<GoogleToken> response2 =
          restTemplate.postForEntity(url, request, GoogleToken.class);

      LOGGER.info(response2.toString());

      return response2.getBody();
    }
    catch (Exception e)
    {
      //TODO collect report for such failures and send daily
      e.printStackTrace();
      return null;
    }
  }

  public GoogleUserDetails getUserDetailsFromAIDToken(String idToken) {

    Map<String, String> urlparams = new HashMap<>();
    urlparams.put("id_token", idToken);
    GoogleUserDetails data = restTemplate.getForObject("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token={id_token}",
          GoogleUserDetails.class, urlparams);
    return data;
  }
}
