package com.neostencil.services;

import com.neostencil.model.entities.AssignmentSubmission;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.neostencil.config.Constants;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.NewsletterSubscriber;
import com.neostencil.model.entities.Order;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.UserUnitLinkage;
import com.neostencil.requests.PayLaterRequest;
import com.neostencil.security.JwtTokenUtil;

@Component
public class EmailService {

  private MailSender mailSender;
  private Constants redCarpetEmailList = new Constants();
  private AmazonSimpleEmailService amazonSimpleEmailService;

  private TemplateEngine templateEngine;
  @Value("${neostencil.website.url}")
  private String websiteHomePageURL;
  @Value("${mail.from.emailid}")
  private String senderEmail;
  @Value("${mail.from.name}")
  private String senderName;

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Autowired
  public void setAmazonSimpleEmailService(AmazonSimpleEmailService amazonSimpleEmailService) {
    this.amazonSimpleEmailService = amazonSimpleEmailService;
  }

  @Autowired
  public void setTemplateEngine(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public void sendSimpleMessage(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(senderEmail);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
    System.out.println("Message Sent");
  }

  public void sendHTMLMessage(String to, String subject, String htmlBody, String textBody)
      throws MessagingException, UnsupportedEncodingException {

    SendEmailRequest request =
        new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
            .withMessage(new Message()
                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody))
                    .withText(new Content().withCharset("UTF-8").withData(textBody)))
                .withSubject(new Content().withCharset("UTF-8").withData(subject)))
            .withSource(senderEmail);
    amazonSimpleEmailService.sendEmail(request);
  }


  public void sendHTMLMessageWithCC(String to, String subject, String htmlBody, String textBody,
      String... cc) throws MessagingException, UnsupportedEncodingException {

    SendEmailRequest request =
        new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
            .withMessage(new Message()
                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody))
                    .withText(new Content().withCharset("UTF-8").withData(textBody)))
                .withSubject(new Content().withCharset("UTF-8").withData(subject)))
            .withSource(senderEmail);

    if (cc != null) {
      request.getDestination().withCcAddresses(cc);
    }
    amazonSimpleEmailService.sendEmail(request);
  }

  public void sendHTMLMessageToMultipleString(List<String> to, String subject, String htmlBody,
      String textBody) throws MessagingException, UnsupportedEncodingException {

    SendEmailRequest request =
        new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
            .withMessage(new Message()
                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody))
                    .withText(new Content().withCharset("UTF-8").withData(textBody)))
                .withSubject(new Content().withCharset("UTF-8").withData(subject)))
            .withSource(senderEmail);
    amazonSimpleEmailService.sendEmail(request);
  }

  public void sendHTMLMessageWithAttachments(String to, String subject, String htmlBody,
      String textBody, String attachmentPath, String fileName) throws MessagingException {

    Session session = Session.getDefaultInstance(new Properties());

    // Create a new MimeMessage object.
    MimeMessage message = new MimeMessage(session);

    // Add subject, from and to lines.
    message.setSubject(subject, "UTF-8");
    message.setFrom(new InternetAddress(senderEmail));
    message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));

    // Create a multipart/alternative child container.
    MimeMultipart msg_body = new MimeMultipart("alternative");

    // Create a wrapper for the HTML and text parts.
    MimeBodyPart wrap = new MimeBodyPart();

    // Define the text part.
    MimeBodyPart textPart = new MimeBodyPart();
    textPart.setContent(textBody, "text/plain; charset=UTF-8");

    // Define the HTML part.
    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(htmlBody, "text/html; charset=UTF-8");

    // Add the text and HTML parts to the child container.
    msg_body.addBodyPart(textPart);
    msg_body.addBodyPart(htmlPart);

    // Add the child container to the wrapper object.
    wrap.setContent(msg_body);

    // Create a multipart/mixed parent container.
    MimeMultipart msg = new MimeMultipart("mixed");

    // Add the parent container to the message.
    message.setContent(msg);

    // Add the multipart/alternative part to the message.
    msg.addBodyPart(wrap);

    // Define the attachment
    MimeBodyPart att = new MimeBodyPart();
    try {
      URL url = new URL(attachmentPath);

      ReadableByteChannel rbc = Channels.newChannel(url.openStream());
      FileOutputStream fos = new FileOutputStream(url.getPath());
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

      DataSource fds = new FileDataSource(url.getFile());
      att.setDataHandler(new DataHandler(fds));
      att.setFileName(fileName);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Add the attachment to the message.
    msg.addBodyPart(att);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      message.writeTo(outputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

    SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);

    amazonSimpleEmailService.sendRawEmail(rawEmailRequest);
    // Display an error if something goes wrong.
  }

  public void sendEmailOnSignUp(User user) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    ctx.setVariable("email", user.getEmailId());
    ctx.setVariable("fullName", user.getFullName());
    final String htmlContent = templateEngine.process("mail/signup.html", ctx);
    final String htmlContent1 = templateEngine.process("mail/signupToAdmin.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      sendHTMLMessage(user.getEmailId(), "Welcome To Neostencil", htmlContent, " hello world");
      sendHTMLMessage("admin@neostencil.com", "[Neostencil] New User Registration", htmlContent1,
          " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendCourseSubscriptionMailToStudent(User user, String courseName, Order order) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);

    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    String strDate = formatter.format(order.getPaymentDate());
    ctx.setVariable("userName", user.getUserName());
    ctx.setVariable("fullName", user.getFullName());
    ctx.setVariable("courseName", courseName);
    ctx.setVariable("orderId", order.getOrderId());
    ctx.setVariable("payableAmount", order.getPayableAmount());
    ctx.setVariable("paymentMode", order.getPaymentMode());
    ctx.setVariable("paymentDate", strDate);
    ctx.setVariable("totalAmount", order.getTotalAmount());
    ctx.setVariable("orderItems", order.getOrderItems());
    ctx.setVariable("orderAddress", order.getAddress());
    ctx.setVariable("discount", order.getDiscount());
    ctx.setVariable("orderNotes", order.getOrderNotes());
    final String htmlContent = templateEngine.process("mail/orderconfirmed.html", ctx);
    final String subjectContent =
        "[NeoStencil] New Student Order (" + order.getOrderId() + ") -" + strDate;
    final String htmlContent1 = templateEngine.process("mail/orderPlacedToAdmin.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      sendHTMLMessage(user.getEmailId(), "Thanks for enrolling with us.", htmlContent,
          " hello world");
      sendHTMLMessage("admin@neostencil.com", subjectContent, htmlContent1, " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sentToTeacherAssignmentAlertHTMLMessage(User user, String teacherName,
      String assignmentName,Date submittedDate) {
    Locale locale = Locale.ENGLISH;

    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    String submittedOn = formatter.format(submittedDate);

    final Context ctx = new Context(locale);
    ctx.setVariable("teacherName", teacherName);
    ctx.setVariable("assignmentName", assignmentName);
    ctx.setVariable("userName", user.getFullName());
    ctx.setVariable("submittedOn", submittedOn);
    final String htmlContent = templateEngine.process("mail/assignment_submit.html", ctx);

    try {
      sendHTMLMessage(user.getEmailId(),
          "Assignment Status", htmlContent, " hello world");
    } catch (MessagingException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }


  }

  public void sentToStudentCheckedAssignmentAlertHTMLMessage(User user, String teacherName,
      String assignmentName, AssignmentSubmission assignmentSubmission) {
    Locale locale = Locale.ENGLISH;


    final Context ctx = new Context(locale);
    ctx.setVariable("teacherName", teacherName);
    ctx.setVariable("assignmentName", assignmentName);
    ctx.setVariable("userName", user.getFullName());
    ctx.setVariable("assignmentUrl",assignmentSubmission.getAttachmentUrl());

    final String htmlContent = templateEngine.process("mail/assignment_evaluated.html", ctx);

    try {
      sendHTMLMessage(user.getEmailId(),
          "Assignment Status", htmlContent, " hello world");
    } catch (MessagingException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }


  }

  public void sendEmailToRedcarpet(PayLaterRequest rcuser) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    ctx.setVariable("firstName", rcuser.getFirstName());
    ctx.setVariable("lastName", rcuser.getLastName());
    ctx.setVariable("email", rcuser.getEmail());
    ctx.setVariable("mobile", rcuser.getMobile());
    ctx.setVariable("collegeName", rcuser.getCollegeName());
    ctx.setVariable("city", rcuser.getCity());
    ctx.setVariable("passyear", rcuser.getPassyear());
    ctx.setVariable("courseName", rcuser.getCourseName());
    ctx.setVariable("coursePrice", rcuser.getCoursePrice());
    ctx.setVariable("coursePage", rcuser.getCoursePage());
    final String htmlContent = templateEngine.process("mail/paylaterredcarpet.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      sendHTMLMessageToMultipleString(Constants.redCarpetEmailList,
          "Neostencil Pay Later Entry", htmlContent, " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendCourseSubscriptionMailToTeacher(User user, CourseBatch batch, Order order) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    String strDate = formatter.format(batch.getStartDate());
    String instructorName = batch.getCourse().getInstructors().iterator().next().getTeacherName();
    ctx.setVariable("name", instructorName);
    ctx.setVariable("courseName", batch.getCourse().getCourseName());
    ctx.setVariable("courseFees", batch.getCourse().getPrice());
    ctx.setVariable("paidAmt",order.getPayableAmount());
    ctx.setVariable("courseBatchDate", strDate);
    ctx.setVariable("courseBatchTime", batch.getClassTiming());
    ctx.setVariable("emailId", user.getEmailId());
    ctx.setVariable("studentName", user.getFullName());
    ctx.setVariable("orderAddress", order.getAddress());
    final String htmlContent = templateEngine.process("mail/courseenrollteacher.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      // TODO:To comment out before live
      /*
       * if(batch!= null && batch.getCourse()!=null && batch.getCourse().getInstructors()!=null &&
       * batch.getCourse().getInstructors().size()>0) { for(TeacherDetails td:
       * batch.getCourse().getInstructors()) { sendHTMLMessage(td.getEmailId(),
       * "New Enrollment in your course", htmlContent, " hello world"); } }
       */
      String to = batch.getCourse().getInstructors().iterator().next().getEmailId();
      Double salePrice = batch.getSalePrice();
      String subject = null;
      if(salePrice>0.0){
        subject = "New Enrollment in your paid course";
      }
      else{
        subject = "New Enrollment in your free course";
      }

      if (to != null && !to.isEmpty()) {
        sendHTMLMessage(to, subject, htmlContent, " hello world");
      } else {
        sendErrorLogsToDevelopers(
            "Email not present for teacher for order Id " + order.getOrderId());
      }

    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendRefundMailToStudent(User user, Order order) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    /*
     * ctx.setVariable("name", batch.getCourse().getInstructorName()); ctx.setVariable("courseName",
     * batch.getCourse().getCourseName());
     */
    ctx.setVariable("emailId", user.getEmailId());
    ctx.setVariable("fullName", user.getFullName());
    ctx.setVariable("orderId", order.getOrderId());
    ctx.setVariable("refundAmount", order.getRefundAmount());
    ctx.setVariable("orderItems", order.getOrderItems());
    final String htmlContent = templateEngine.process("mail/refund.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      // batch.getCourse().getInstitu0te().getEmailId()
      sendHTMLMessage(user.getEmailId(), "Your refund has been initiated", htmlContent,
          " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendOrderPlacementEmailToCustomerNEFT(User user, Order order) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    /*
     * ctx.setVariable("name", batch.getCourse().getInstructorName()); ctx.setVariable("courseName",
     * batch.getCourse().getCourseName());
     */
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    String strDate = formatter.format(order.getPaymentDate());
    ctx.setVariable("emailId", user.getEmailId());
    ctx.setVariable("studentName", user.getFullName());
    ctx.setVariable("orderId", order.getOrderId());
    ctx.setVariable("orderItems", order.getOrderItems());
    ctx.setVariable("paymentMode", order.getPaymentMode());
    ctx.setVariable("paymentDate", strDate);
    ctx.setVariable("totalAmount", order.getTotalAmount());
    ctx.setVariable("orderAddress", order.getAddress());
    ctx.setVariable("discount", order.getDiscount());
    ctx.setVariable("payableAmount", order.getPayableAmount());
    ctx.setVariable("orderNotes", order.getOrderNotes());
    final String htmlContent = templateEngine.process("mail/orderplacedneft.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      // batch.getCourse().getInstitu0te().getEmailId()
      sendHTMLMessage(user.getEmailId(), "Your order placement process initiated", htmlContent,
          " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendOrderPlacementEmailToCustomerRazorPay(User user, Order order) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    /*
     * ctx.setVariable("name", batch.getCourse().getInstructorName()); ctx.setVariable("courseName",
     * batch.getCourse().getCourseName());
     */
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    String strDate = formatter.format(order.getPaymentDate());
    ctx.setVariable("emailId", user.getEmailId());
    ctx.setVariable("fullName", user.getFullName());
    ctx.setVariable("orderId", order.getOrderId());
    ctx.setVariable("orderItems", order.getOrderItems());
    ctx.setVariable("paymentMode", order.getPaymentMode());
    ctx.setVariable("paymentDate", strDate);
    ctx.setVariable("totalAmount", order.getTotalAmount());
    ctx.setVariable("orderAddress", order.getAddress());
    ctx.setVariable("discount", order.getDiscount());
    ctx.setVariable("payableAmount", order.getPayableAmount());
    ctx.setVariable("orderNotes", order.getOrderNotes());
    final String htmlContent = templateEngine.process("mail/orderplaced.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      // batch.getCourse().getInstitu0te().getEmailId()
      sendHTMLMessage(user.getEmailId(), "Your order is placed", htmlContent, " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendAssignmentAlert(UserUnitLinkage userUnitLinkage) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);

    ctx.setVariable("name", userUnitLinkage.getUser().getFullName());
    ctx.setVariable("emailId", userUnitLinkage.getUser().getEmailId());
    ctx.setVariable("assignmentName",
        userUnitLinkage.getAssignmentSubmission().getAttachmentName());
    ctx.setVariable("assignmentUrl", userUnitLinkage.getAssignmentSubmission().getAttachmentUrl());
    ctx.setVariable("assignmentStatus", userUnitLinkage.getAssignmentSubmission().getStatus());
    final String htmlContent = templateEngine.process("mail/assignmentAlert.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      // batch.getCourse().getInstitu0te().getEmailId()
      sendHTMLMessageToMultipleString(Constants.sendAssignmentAlertList, "Assignment Submission",
          htmlContent, " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendPaymentConfirmationEmail(User user, Order order) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    /*
     * ctx.setVariable("name", batch.getCourse().getInstructorName()); ctx.setVariable("courseName",
     * batch.getCourse().getCourseName());
     */
    ctx.setVariable("emailId", user.getEmailId());
    ctx.setVariable("fullName", user.getFullName());
    ctx.setVariable("orderId", order.getOrderId());
    ctx.setVariable("orderItems", order.getOrderItems());
    ctx.setVariable("paymentMode", order.getPaymentMode());
    ctx.setVariable("paymentDate", order.getPaymentDate());
    ctx.setVariable("totalAmount", order.getTotalAmount());
    ctx.setVariable("orderAddress", order.getAddress());
    final String htmlContent = templateEngine.process("mail/orderplaced.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      // batch.getCourse().getInstitu0te().getEmailId()
      sendHTMLMessage(user.getEmailId(), "Your payment has been recieved", htmlContent,
          " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendNewVideoRequestMail(String message,String sendTo,String subject)
  {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);

    ctx.setVariable("message", message);
    ctx.setVariable("sendTo", sendTo);
    ctx.setVariable("linkToDashboard", "https://localhost:9000/approver_admin/video-requests");

    final String htmlContent = templateEngine.process("mail/video-request.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      // batch.getCourse().getInstitu0te().getEmailId()
      sendHTMLMessage(sendTo, subject, htmlContent,
          "New Video Edit Request");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }


  public void sendPasswordRequestEmail(String to, String token, String fullName) {
    String resetURL = websiteHomePageURL + "/reset?token=" + token;

    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    ctx.setVariable("email", to);
    ctx.setVariable("fullName", fullName);
    ctx.setVariable("passwordResetUrl", resetURL);
    ctx.setVariable("passwordResetUrlText", "Click here to reset password");
    final String htmlContent = templateEngine.process("mail/resetpasswordrequest.html", ctx);
    final String htmlContent1 = templateEngine.process("mail/resetPasswordToAdmin.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      sendHTMLMessage(to, "Reset Password Request", htmlContent, " hello world");
      sendHTMLMessage("admin@neostencil.com", "[NeoStencil] Password Lost/Changed", htmlContent1,
          "hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public void sendEmailUsingTestMailTemplate(String to, String message) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    ctx.setVariable("email", to);
    ctx.setVariable("message", "Can Modi defeat Thanos?");
    ctx.setVariable("htmlcode", getHrefTag("https://v2dev.neostencil.com", "shiny new website"));
    ctx.setVariable("activateLink", "https://v2dev.neostencil.com");
    ctx.setVariable("activateLinkText", "another shining link example");

    final String htmlContent = this.templateEngine.process("mail/testmail.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      sendHTMLMessage(to, "TestMail", htmlContent, " hello world");
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public String sendEmailToNewsletterSubscriber(@Valid NewsletterSubscriber request) {

    if (request != null) {
      try {

        Locale locale = Locale.ENGLISH;
        final Context ctx = new Context(locale);
        ctx.setVariable("emailId", request.getEmailId());
        final String htmlContent = templateEngine.process("mail/SubscriberMail.html", ctx);
        System.out.println("Content is " + htmlContent);
        sendHTMLMessage(request.getEmailId(), "Thanks for Subscribing our Newsletter", htmlContent,
            "Hello World");

      } catch (NullPointerException | MessagingException | UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }

    return "Email Sent Successfully";

  }

  public void sendMessageNotification(String to, String message, String from, String topic,
                                      String messageUrl,String mobileNo) {
    Locale locale = Locale.ENGLISH;
    final Context ctx = new Context(locale);
    ctx.setVariable("email", from);
    ctx.setVariable("message", message);
    ctx.setVariable("messageFrom", from);
    ctx.setVariable("messageUrl", messageUrl);
    ctx.setVariable("mobileNo", mobileNo);
    String title = "Enquiry - ";
    if (topic != null && !topic.isEmpty()) {
      title = title + topic;
    }
    else{
      title = title + "From LMS Page";
    }

    final String htmlContent = this.templateEngine.process("mail/testmail.html", ctx);
    System.out.println("Content is " + htmlContent);
    try {
      sendHTMLMessage("info@neostencil.com", title, htmlContent,
              " New Message Received from " + from);
    } catch (MessagingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  private String getHrefTag(String url, String text) {
    String tag = "<a href=\"" + url + "\" >" + text + "t</a>";
    return tag;
  }

  public void sendErrorLogsToDevelopers(String message) {
    String to = "dev@neostencil.com";
    InetAddress ip;
    String hostname = "";
    try {
      ip = InetAddress.getLocalHost();
      hostname = ip.getHostName();
    } catch (UnknownHostException e) {

      e.printStackTrace();
    }

    String subject = "Exception Logs from " + hostname;
    sendSimpleMessage(to, subject, message);
  }

  public void sendNotificationToDevelopers(String message) {
    String to = "dev@neostencil.com";
    InetAddress ip;
    String hostname = "";
    try {
      ip = InetAddress.getLocalHost();
      hostname = ip.getHostName();
    } catch (UnknownHostException e) {

      e.printStackTrace();
    }

    String subject = "New Manual order created from " + hostname;
    sendSimpleMessage(to, subject, message);
  }

  public void sendNotificationToContentTeam(Integer commentId ,String text, String commentedBy, String slug) {
    String to = "gautam@neostencil.com";
    if(!commentedBy.contains("@neostencil.com")){
      String subject = "New Comment Posted ";
      String message = commentedBy + " commented '" +text + "' on https://neostencil.com/" + slug + "#reply" + commentId;
      sendSimpleMessage(to, subject, message);
    }
  }

}
