package com.asu.project.hospital.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.service.OtpService;
import com.asu.project.hospital.service.UserService;

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
	public String register(Model model, @RequestParam(name = "registeredBy", required = false) String registeredBy) {
		model.addAttribute("user", new User());
		model.addAttribute("registeredBy", registeredBy);
		return "register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User userForm, BindingResult result, Model model,
			@ModelAttribute("registeredBy") String registeredBy) {

		if (result.hasErrors()) {
			return "register";
		}
		try {
			userService.registerUser(userForm, registeredBy);
			String role = userForm.getRole();
			model.addAttribute("firstName", userForm.getFirstName());
			model.addAttribute("lastName", userForm.getLastName());
			model.addAttribute("email", userForm.getEmail());
			if (registeredBy != null && registeredBy.equals("admin")) {
				return "admin/registrationByAdminSucessfull";
			}
			if ((registeredBy != null && registeredBy.equals("admin")) || role.equals("PATIENT")) {
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
		}
		return "redirect:/login?logout";
	}
}
