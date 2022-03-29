package com.asu.project.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String generateOtp(@PathVariable("pageToView") String pageToView, @RequestParam("labTestId") String labTestId,Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		int otp = otpService.generateOTP(username);
		emailService.sendOTPMail(username, Integer.toString(otp));
		model.addAttribute("email", username);
		if (pageToView.equals("viewPatientLabReport")) {
			model.addAttribute("viewPage", pageToView);
			model.addAttribute("labTestId", labTestId);
		}
		model.addAttribute("expiry_mins", OtpService.EXPIRE_MINS);
		return "otp/otppage";
	}

	@RequestMapping(value = "/validateOtp", method = RequestMethod.POST)
	public String validateOtp(@ModelAttribute("otp") String otpnum, @ModelAttribute("viewPage") String viewPage,
			@ModelAttribute("labTestId") String labTestId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		otpnum = otpnum.trim();
		if (Integer.parseInt(otpnum) >= 0) {
			int serverOtp = otpService.getOtp(username);
			if (serverOtp > 0) {
				if (Integer.parseInt(otpnum) == serverOtp) {
					otpService.clearOTP(username);
					if (viewPage != null && viewPage.equals("viewPatientLabReport")) {
						return "redirect:/viewPDF/patient/reportViewAfterOTPValidation/" + labTestId;
					}
				}
			}
		}
		return "otp/invalid";
	}
}
