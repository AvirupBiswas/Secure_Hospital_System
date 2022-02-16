package com.asu.project.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.service.OtpService;
import com.asu.project.hospital.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

@Controller
public class ViewController {

	@Autowired
	private UserService userService;

	@Autowired
	private OtpService otpService;

	@GetMapping("/login")
	public String login() {
		return "signin";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User userForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "register";
		}
		try {
			userService.registerUser(userForm);

			/*
			 * SystemLog systemLog=new SystemLog(); systemLog.setMessage(userForm.getEmail()
			 * + " successfully registered"); systemLog.setTimestamp(new Date());
			 * systemLogRepository.save(systemLog);
			 */
			String role = userForm.getRole();
			model.addAttribute("firstName", userForm.getFirstName());
			model.addAttribute("lastName", userForm.getLastName());
			model.addAttribute("email", userForm.getEmail());
			if (role.equals("PATIENT")) {
				return "registrationSucessfull";
			} else {
				return "registrationPending";
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping(value = "/logout")
	public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			String username = auth.getName();
			// Remove the recently used OTP from server.
			otpService.clearOTP(username);
			new SecurityContextLogoutHandler().logout(request, response, auth);

			/*
			 * SystemLog systemLog=new SystemLog(); systemLog.setMessage(username +
			 * " logged out"); systemLog.setTimestamp(new Date());
			 * systemLogRepository.save(systemLog);
			 */
		}
		return "redirect:/login?logout";
	}
}
