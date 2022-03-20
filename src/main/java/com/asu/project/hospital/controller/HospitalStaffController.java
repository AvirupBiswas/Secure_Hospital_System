package com.asu.project.hospital.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asu.project.hospital.entity.AdminDecisionForUser;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.AdminDecisionForUserRepository;
import com.asu.project.hospital.service.MailService;
import com.asu.project.hospital.service.PatientService;
import com.asu.project.hospital.service.UserService;
import com.asu.project.hospital.service.HospitalStaffService;
import com.asu.project.hospital.entity.HospitalStaff;

@Controller
@RequestMapping("/hospitalstaff")
public class HospitalStaffController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private HospitalStaffService hospitalStaffService;


	@GetMapping("/home")
	public String adminHome(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "hospitalstaff/home";
	}
	
	@GetMapping("/updateinfo")
	public String register(Model model) {
		model.addAttribute("hospitalstaff", new HospitalStaff());
		return "hospitalstaff/updateinfo";
	}
	
	@PostMapping("/updateinformation")
	public String register(@Valid @ModelAttribute("hospitalstaff") HospitalStaff userForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "hospitalstaff/updateinfo";
		}
		try {
			User user=userService.getLoggedUser();
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address",userForm.getAddress());
			hospitalStaffService.updateHospitalStaffInfo(userForm);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "hospitalstaff/home";
	}

}
