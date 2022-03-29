package com.asu.project.hospital.controller;

import java.util.List;
import java.util.Optional;

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

import com.asu.project.hospital.entity.Diagnosis;
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
		return "doctor/doctorhome";
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
	
	@PostMapping("/createDiagnosis")
	public String createDiagnosis(@RequestParam("userId") String userId, @ModelAttribute("diagnosis") Diagnosis diagnosis) {
		User patient=userService.findByUserId(userId);
		diagnosis.setUser(patient);
		User doctor=userService.getLoggedUser();
		StringBuilder doctorName=new StringBuilder(doctor.getFirstName());
		doctorName.append(" ").append(doctor.getLastName());
		diagnosis.setDoctorName(doctorName.toString());
		doctorService.createDiagnosis(diagnosis);
		return "doctor/doctorhome";
	}
	
	@GetMapping("/viewpatients")
	public String viewPatients(Model model) {
		List<User> allPatients=doctorService.getAllPatients();
		model.addAttribute("patient",allPatients);
		return "doctor/viewpatients";
	}
	
	@RequestMapping("/viewpatientsdiagnosis")
	public String viewPatientsDiagnosis(Model model) {
		List<User> allPatients=doctorService.getAllPatients();
		model.addAttribute("patient",allPatients);
		return "doctor/viewpatientsdiagnosis";
	}
	
	@GetMapping("/viewdiagnosis")
	public String viewAlldiagnosis(@RequestParam("userId") String userId,Model model) {
		User user = userService.findByUserId(userId);
		List<Diagnosis> diagnosisList=doctorService.getAllDiagnosis(user);
		model.addAttribute("diagnosis",diagnosisList);
		return "doctor/viewdiagnosis";
	}
	
	@GetMapping("/updatediagnosis")
	public String updateDiagnosis(@RequestParam("diagnosisId") int diagnosisId,Model model) {
		Diagnosis diagnosis=doctorService.findByDiagnosis(diagnosisId);
		model.addAttribute("diagnosis",diagnosis);
		return "doctor/updatediagnosis";
	}
	
	@PostMapping("/diagnosis")
	public String createDiagnosis(@RequestParam("userId") String userId, Model model) {
		User user = userService.findByUserId(userId);
		model.addAttribute("user",user);
		return "doctor/diagnosis";
	}
	
	@PostMapping("/editDiagnosis")
	public String createDiagnosis(@RequestParam("diagnosisId") int diagnosisId,@ModelAttribute("diagnosis") Diagnosis diagnosis) {
		Diagnosis updatedDiagnosis=doctorService.findByDiagnosis(diagnosisId);
		updatedDiagnosis.setLabtests(diagnosis.getLabtests());
		updatedDiagnosis.setProblem(diagnosis.getProblem());
		updatedDiagnosis.setPrescription(diagnosis.getPrescription());
		updatedDiagnosis.setLabTestNeeded(diagnosis.getLabTestNeeded());
		updatedDiagnosis.setSymptoms(diagnosis.getSymptoms());
		doctorService.createDiagnosis(updatedDiagnosis);
		return "doctor/doctorhome";
	}
}
