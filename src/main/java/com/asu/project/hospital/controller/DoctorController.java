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

import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.Doctor;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.DoctorRepository;
import com.asu.project.hospital.repository.PatientRepository;
import com.asu.project.hospital.service.DoctorService;
import com.asu.project.hospital.service.UserService;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	@GetMapping("/home")
	public String doctorHome(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "doctor/doctorhome";
	}
	
	@GetMapping("/updateinfo")
	public String register(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		Doctor doctorUser = doctorRepository.findByUser(user);
		model.addAttribute("doctor", new Doctor());
		model.addAttribute("userInfo", doctorUser);
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
	
	@PostMapping("/editinformation")
	public String editInformation(@Valid @ModelAttribute("doctor") Doctor userForm, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "doctor/updateinfo";
		}
		try {
			User user = userService.getLoggedUser();
			model.addAttribute("accountName", user.getFirstName());
			Doctor doctorUser = doctorRepository.findByUser(user);
			doctorUser.setPhoneNumber(userForm.getPhoneNumber());
			doctorUser.setAddress(userForm.getAddress());
			doctorUser.setAge(userForm.getAge());
			doctorUser.setGender(userForm.getGender());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address", userForm.getAddress());
			model.addAttribute("age", userForm.getAge());
			model.addAttribute("gender", userForm.getGender());
			doctorService.updateDoctorInfo(doctorUser);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "redirect:/doctor/home";
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
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<User> allPatients=doctorService.getAllPatients();
		model.addAttribute("patient",allPatients);
		return "doctor/viewpatients";
	}
	
	@RequestMapping("/viewpatientsdiagnosis")
	public String viewPatientsDiagnosis(Model model) {
		List<User> allPatients=doctorService.getAllPatients();
		model.addAttribute("patient",allPatients);
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "doctor/viewpatientsdiagnosis";
	}
	
	@GetMapping("/viewdiagnosis")
	public String viewAlldiagnosis(@RequestParam("userId") String userId,Model model) {
		User user = userService.findByUserId(userId);
		List<Diagnosis> diagnosisList=doctorService.getAllDiagnosis(user);
		model.addAttribute("diagnosis",diagnosisList);
		User userLogged = userService.getLoggedUser();
		model.addAttribute("accountName", userLogged.getFirstName());
		return "doctor/viewdiagnosis";
	}
	
	@GetMapping("/viewpatientsrecords")
	public String viewPatientsRecords(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<User> allPatients=doctorService.getAllPatients();
		model.addAttribute("patient",allPatients);
		return "doctor/viewpatientsrecords";
	}
	
	@GetMapping("/updatediagnosis")
	public String updateDiagnosis(@RequestParam("diagnosisId") int diagnosisId,Model model) {
		Diagnosis diagnosis=doctorService.findByDiagnosis(diagnosisId);
		model.addAttribute("diagnosis",diagnosis);
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "doctor/updatediagnosis";
	}

	@GetMapping("/deletediagnosis")
	public String deleteDiagnosis(@RequestParam("diagnosisId") int diagnosisId,Model model) {
		Diagnosis diagnosis=doctorService.findByDiagnosis(diagnosisId);
		System.out.println("Delete diagnosis"+diagnosisId);
		doctorService.deleteDiagnosis(diagnosis);
		return "doctor/doctorhome";
	}
	
	@PostMapping("/diagnosis")
	public String createDiagnosis(@RequestParam("userId") String userId, Model model) {
		User account = userService.getLoggedUser();
		model.addAttribute("accountName", account.getFirstName());
		User user = userService.findByUserId(userId);
		model.addAttribute("user",user);
		return "doctor/diagnosis";
	}
	
	@PostMapping("/editDiagnosis")
	public String editDiagnosis(@RequestParam("diagnosisId") int diagnosisId,@ModelAttribute("diagnosis") Diagnosis diagnosis) {
		Diagnosis updatedDiagnosis=doctorService.findByDiagnosis(diagnosisId);
		updatedDiagnosis.setLabtests(diagnosis.getLabtests());
		updatedDiagnosis.setProblem(diagnosis.getProblem());
		updatedDiagnosis.setPrescription(diagnosis.getPrescription());
		updatedDiagnosis.setLabTestNeeded(diagnosis.getLabTestNeeded());
		updatedDiagnosis.setSymptoms(diagnosis.getSymptoms());
		doctorService.createDiagnosis(updatedDiagnosis);
		return "doctor/doctorhome";
	}
	
	@PostMapping("/updatepatientinfo")
	public String updatePatientInfo(@RequestParam("userId") String userId, Model model) {
		User user1 = userService.getLoggedUser();
		model.addAttribute("accountName", user1.getFirstName());
		User user = userService.findByUserId(userId);
		Patient patientdetails=patientRepository.findByUser(user);
		model.addAttribute("user",user);
		model.addAttribute("patientdetails", patientdetails);
		return "doctor/updatepatientinfo";
	}
	
	@PostMapping("/updatepatientinformation")
	public String updatepatientinformation(@ModelAttribute("updatepatientinformation") Patient patient, @ModelAttribute("userId") String userId) {
		User user = userService.findByUserId(userId);
		patient.setUser(user);
		patientRepository.save(patient);
		return "doctor/doctorhome";
	}
	
	@PostMapping("/editpatientinformation")
	public String editpatientinformation(@ModelAttribute("updatepatientinformation") Patient patient, @ModelAttribute("userId") String userId) {
		User user = userService.findByUserId(userId);
		
		try {
			Patient oldpatient=patientRepository.findByUser(user);
			oldpatient.setHeight(patient.getHeight());
			oldpatient.setWeight(patient.getWeight());
			oldpatient.setAddress(patient.getAddress());
			oldpatient.setAge(patient.getAge());
			oldpatient.setPhoneNumber(patient.getPhoneNumber());
			patientRepository.save(oldpatient);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "doctor/doctorhome";
	}
}
