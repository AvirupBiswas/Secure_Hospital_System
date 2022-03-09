package com.asu.project.hospital.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.service.PatientService;
import com.asu.project.hospital.service.UserService;

@Controller
@RequestMapping("/patient")
public class PatientController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PatientService patientService;
	
	@GetMapping("/home")
	public String adminHome(Model model) {
		User user = userService.getLoggedUser();
		// model.addAttribute("accountName", user.getFirstName());
		return "patient/patienthome";
	}
	
	@GetMapping("/updateinfo")
	public String register(Model model) {
		model.addAttribute("patient", new Patient());
		return "patient/updateinfo";
	}
	
	@PostMapping("/updateinformation")
	public String register(@Valid @ModelAttribute("patient") Patient userForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "patient/updateinfo";
		}
		try {
			User user=userService.getLoggedUser();
			model.addAttribute("height", userForm.getHeight());
			model.addAttribute("weight", userForm.getWeight());
			model.addAttribute("age", userForm.getAge());
			model.addAttribute("address",userForm.getAddress());
			model.addAttribute("gender", userForm.getGender());
			patientService.updatePatientInfo(userForm);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "patient/patienthome";
	}
	

}
