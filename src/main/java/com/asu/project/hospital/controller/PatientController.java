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

import com.asu.project.hospital.entity.Appointment;
import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.service.AppointmentService;
import com.asu.project.hospital.service.PatientService;
import com.asu.project.hospital.service.UserService;

@Controller
@RequestMapping("/patient")
public class PatientController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@GetMapping("/home")
	public String adminHome(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
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
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			patientService.updatePatientInfo(userForm);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "patient/patienthome";
	}
	
	@GetMapping("/bookappointment")
	public String bookAppointment(Model model) {
		model.addAttribute("Appointment", new Appointment());
		return "patient/bookappointment";
	}
	
	@GetMapping("/editinsurance")
	public String claimInsurance(Model model) {
		model.addAttribute("InsuranceDetails", new InsuranceDetails());
		return "patient/editinsurance";
	}
	
	@GetMapping("/labrequest")
	public String labRequest(Model model) {
		model.addAttribute("labTest", new LabTest());
		return "patient/requestlabtest";
	}
	
	@PostMapping("/createappointment")
	public String createAppointment(@ModelAttribute("scheduleApp") Appointment appointment, @RequestParam("date") String date, @RequestParam("time") String time) throws Exception {
        User user = userService.getLoggedUser();
        System.out.println(user.getUserId());
        appointment.setStatus("Pending");
        appointmentService.createAppointment(user, appointment, date, time);
        return "patient/patienthome";
	}
	
	@PostMapping("/addInsuranceDetails")
	public String addInsuranceDetails(@ModelAttribute("insurance") InsuranceDetails insuranceDetails) {
		User user = userService.getLoggedUser();
		patientService.addInsuranceDetails(insuranceDetails);
		return "patient/insuranceclaim";
	}
	
	@PostMapping("/editInsuranceDetails")
	public String editInsuranceDetails(@ModelAttribute("insurance") InsuranceDetails insuranceDetails) {
		User user = userService.getLoggedUser();
		patientService.editInsuranceDetails(insuranceDetails);
		return "patient/insuranceclaim";
	}
	
	@PostMapping("/addClaimDetails")
	public String addInsuranceClaims(@ModelAttribute("insuranceclaim") InsuranceClaims insuranceclaim) {
		insuranceclaim.setStatus("Pending");
		patientService.addInsuranceClaimRequest(insuranceclaim);
		return "patient/patienthome";
	}
	
	@RequestMapping("/getInsuranceDetails")
	public String getInsuranceDetails(Model model) {
		User user=userService.getLoggedUser();
		InsuranceDetails details=patientService.getInsuranceDetails(user);
		model.addAttribute("insurancedetails",details);
		return "patient/insuranceclaim";
	}
	
	@GetMapping("/viewClaimHistory")
	public String getClaimsHistory(Model model) {
		User user=userService.getLoggedUser();
		List<InsuranceClaims> claims=patientService.findAllClaims(user);
		model.addAttribute("insuranceClaims", claims);
		return "patient/viewClaimHistory";
	}
	
	@GetMapping("/viewAppointmentHistory")
	public String getAppointmentHistory(Model model) {
		User user=userService.getLoggedUser();
		List<Appointment> appointment=patientService.findAllAppointments(user);
		model.addAttribute("appointments", appointment);
		return "patient/viewAppointmentHistory";
	}
	
	
	@PostMapping("/createLabRequest")
	public String createLabTestRequest(@ModelAttribute LabTest labTest) {
		patientService.createLabRequest(labTest);
		return "patient/patienthome";
	}
	
	

}
