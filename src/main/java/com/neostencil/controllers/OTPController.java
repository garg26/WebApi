package com.neostencil.controllers;

import com.neostencil.services.EmailService;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neostencil.model.entities.Address;
import com.neostencil.model.entities.User;
import com.neostencil.model.repositories.AddressRepository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.requests.ValidateOTPRequest;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.services.LectureService;
import com.neostencil.services.OTPService;
import com.neostencil.services.SMSService;

@Controller
@RequestMapping({"/api/v1"})
public class OTPController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	public OTPService otpService;
	@Autowired
	public SMSService smsService;

	@Autowired
	private EmailService emailService;

	@Autowired
	public LectureService lectureService;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public AddressRepository addressRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@GetMapping("/student_admin/generate-otp")
	@ResponseBody
	public String generateOtp() {
		String username = jwtTokenUtil.getLoggedInUserName();
		long userId = jwtTokenUtil.getLoggedInUserID();
		User u = userRepository.findByUserId(userId);
		int otp = otpService.generateOTP(username);
		//assuming number has full +91 or +1 etc ISD code in that already
		String contactNo =  u.getMobileNumber();
		if(contactNo == null || contactNo.length()!=10)
		{
			return "failure";
		}

		contactNo = "+91" + contactNo;
		String message = "Your OTP is " + otp;
		smsService.sendSMSMessage(message, contactNo);
		emailService.sendSimpleMessage("security@neostencil.com", "OTP Request for user " + u.getEmailId() , message);
		return contactNo;
	}

	@RequestMapping(value = "/student_admin/validate-otp", method = RequestMethod.POST)
	public @ResponseBody String validateOtp(@Valid @RequestBody ValidateOTPRequest request) {
		final String SUCCESS = "Entered Otp is valid";
		final String FAIL = "Entered Otp is NOT valid. Please Retry!";
		String username = jwtTokenUtil.getLoggedInUserName();
		int otpnum = request.getOtpnum();
		logger.info(" Otp Number : " + otpnum);
		// Validate the Otp
		if (otpnum >= 0) {
			int serverOtp = otpService.getOtp(username);
			if (serverOtp > 0) {
				if (otpnum == serverOtp) {
					lectureService.updateDevice(request.getUnitRequest());
					otpService.clearOTP(username);

					return ("SUCCESS");

				} else {
					return "FAIL";
				}
			} else {
				return "FAIL";
			}
		} else {
			return "FAIL";
		}
	}

}
