package com.asu.project.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asu.project.hospital.service.MailService;
import com.asu.project.hospital.service.OtpService;

@RestController
@RequestMapping("/otp")
public class OTPController {
	
	 @Autowired
	 private MailService emailService;
	 
	 @Autowired
	    public OtpService otpService;
	
	 @GetMapping("/generateOtp")
	    public String generateOtp(){
		 int otp = otpService.generateOTP("abiswa15@asu.edu");//username/email will be passed
	        //emailService.sendOTPMail("abiswa15@asu.edu", Integer.toString(otp));
	        return ""+otp;
	    }

}
