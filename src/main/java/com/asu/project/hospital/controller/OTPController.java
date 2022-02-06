package com.asu.project.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.asu.project.hospital.service.MailService;
import com.asu.project.hospital.service.OtpService;

@Controller
@RequestMapping("/otp")
public class OTPController {

	@Autowired
	private MailService emailService;

	@Autowired
	public OtpService otpService;

	@GetMapping("/generateOtp/{pageToView}")
	public String generateOtp(@PathVariable("pageToView") String pageToView, Model model) {
		int otp = otpService.generateOTP("abiswa15@asu.edu");// username/email will be passed
		emailService.sendOTPMail("abiswa15@asu.edu", Integer.toString(otp));
		model.addAttribute("email", "abiswa15@asu.edu");
		if (pageToView.equals("viewPDF")) {
			model.addAttribute("viewPage", pageToView);
		}
		model.addAttribute("expiry_mins", OtpService.EXPIRE_MINS);
		return "otp/otppage";
	}

	@RequestMapping(value = "/validateOtp", method = RequestMethod.POST)
	public String validateOtp(@ModelAttribute("otp") String otpnum, @ModelAttribute("viewPage") String viewPage) {
		String username = "abiswa15@asu.edu";
		otpnum = otpnum.trim();
		if (Integer.parseInt(otpnum) >= 0) {
			int serverOtp = otpService.getOtp(username);
			if (serverOtp > 0) {
				if (Integer.parseInt(otpnum) == serverOtp) {
					otpService.clearOTP(username);
					if (viewPage != null && viewPage.equals("viewPDF")) {
						return "redirect:/viewPDF/reportView";
					}
				}
			}
		}
		return "otp/invalid";
	}
}
