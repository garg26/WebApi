package com.neostencil.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.Query;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.User;
import com.neostencil.model.repositories.CourseRepository;
import com.neostencil.model.repositories.QueryRepository;
import com.neostencil.model.repositories.UnitRepository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.requests.StudentInquiryRequest;
import com.neostencil.security.JwtTokenUtil;

@Service
public class InquiryService {

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  UserRepository userRepository;

  @Autowired
  EmailService emailService;

  @Autowired
  SMSService smsService;

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  QueryRepository queryRepository;

  @Autowired
  UnitRepository unitRepository;

  public void sendMessageToTeacher(String queryText, String contactNo, String email) {

    long userId = jwtTokenUtil.getLoggedInUserID();
    User u = userRepository.findByUserId(userId);
    String message = "NeoStencil Student " + u.getFullName() + " ( "+u.getMobileNumber() + " ) "+" has a question : " + queryText;

    if (contactNo != null && contactNo.startsWith("+")) {
      smsService.sendSMSMessage(message, contactNo);
    } else {
      contactNo = "+91" + contactNo;
      smsService.sendSMSMessage(message, contactNo);
    }
    if (email != null) {
      emailService.sendSimpleMessage(email, "NeoStencil student Has a question ",
          "NeoStencil Student " + u.getFullName() + " has a question : " + queryText);
    }
  }

  public String sendQuery(StudentInquiryRequest request) {
    // Finding the teacher's user account. We have to use the courseSlug as the input for this.

    Course course = courseRepository.findByCourseOldSlug(request.getCourseSlug());

    String queriedBy = jwtTokenUtil.getLoggedInUserEmail();

    User student = userRepository.findByEmailId(queriedBy);

    Unit unit = unitRepository.findByUnitId(request.getUnitId());

    if (course != null) {
      Set<TeacherDetails> teachers = course.getInstructors();

      for (TeacherDetails td : teachers) {
        if (td.isReceiveQueries()) {
          User teacherUser = td.getUserAccount();

          Query newQuery = new Query();
          newQuery.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
          newQuery.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
          newQuery.setText(request.getMessage());
          newQuery.setQueriedBy(student);
          newQuery.setQueriedTo(teacherUser);
          newQuery.setUnit(unit);

          Query savedQuery = queryRepository.saveAndFlush(newQuery);

          // Sending notification to teacher about this query
          if (savedQuery.getQueriedTo() != null) {
            sendMessageToTeacher(savedQuery.getText(), savedQuery.getQueriedTo().getMobileNumber(),
                savedQuery.getQueriedTo().getEmailId());
          }
        }
      }
    }

    return "success";
  }
}
