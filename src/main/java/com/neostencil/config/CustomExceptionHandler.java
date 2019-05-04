package com.neostencil.config;

import com.neostencil.model.entities.User;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.services.EmailService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private EmailService emailService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private UserRepository userRepository;

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
      throws Exception {
    long userID = jwtTokenUtil.getLoggedInUserID();
    String message = request.toString();

    if (userID !=0)
    {
      User loggedInUser = userRepository.findByUserId(jwtTokenUtil.getLoggedInUserID());
      message = "User login was :-- " + loggedInUser.getEmailId()  +"\n" + message;
    }

    message = message + "\n" + ExceptionUtils.getStackTrace(ex);


    emailService.sendErrorLogsToDevelopers(message);
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    ex.printStackTrace();
    return handleException(ex, request);
  }
}