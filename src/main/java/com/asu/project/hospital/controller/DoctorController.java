package com.asu.project.hospital.controller;

import java.util.List;

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

import com.asu.project.hospital.entity.Doctor;
import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.service.DoctorService;
import com.asu.project.hospital.service.UserService;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DoctorService doctorService;
	
	@GetMapping("/home")
	public String doctorHome(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "doctor/viewpatientshome";
	}
	
	@GetMapping("/updateinfo")
	public String register(Model model) {
		model.addAttribute("doctor", new Doctor());
		return "doctor/updatedocinfo";
	}
	
	@PostMapping("/updateinformation")
	public String register(@Valid @ModelAttribute("doctor") Doctor userForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "doctor/updatedocinfo";
		}
		try {
			User user=userService.getLoggedUser();
			model.addAttribute("age", userForm.getAge());
			model.addAttribute("address",userForm.getAddress());
			model.addAttribute("gender", userForm.getGender());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			doctorService.updateDoctorInfo(userForm);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "doctor/doctorhome";
	}
	
	@GetMapping("/diagnosis")
	public String createDiagnosis(Model model) {
		return "doctor/diagnosis";
	}
	
	@GetMapping("/viewpatients")
	public String viewPatients(Model model) {
		List<User> allPatients=doctorService.getAllPatients();
		model.addAttribute("patient",allPatients);
		return "doctor/viewpatients";
	}
	
	@PostMapping("/doctorhome")
	public String patientTasks(@RequestParam("userId") String userId, Model model) {
		User user = userService.findByUserId(userId);
		model.addAttribute("user",user);
		return "doctor/doctorhome";
	}
}
