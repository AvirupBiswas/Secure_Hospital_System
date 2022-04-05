package com.asu.project.hospital.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asu.project.hospital.entity.Appointment;
import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.PatientPayment;
import com.asu.project.hospital.entity.PatientQuery;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.DiagnosisRepository;
import com.asu.project.hospital.repository.PatientQueryRepository;
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
	
	@Autowired
	private DiagnosisRepository diagnosisRepository;
	
	@Autowired
	private PatientQueryRepository patientQueryRepository;
	
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
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("doctors", patientService.getDoctorsList());
		model.addAttribute("Appointment", new Appointment());
		return "patient/bookappointment";
	}
	
	@GetMapping("/bookspecialappointment")
	public String bookSpecialAppointment(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("doctors", patientService.getDoctorsList());
		model.addAttribute("Appointment", new Appointment());
		return "patient/bookspecialappointment";
	}
	
	
	@GetMapping("/editinsurance")
	public String claimInsurance(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("InsuranceDetails", new InsuranceDetails());
		return "patient/editinsurance";
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
		return "patient/patienthome";
	}
	
	@PostMapping("/editInsuranceDetails")
	public String editInsuranceDetails(@ModelAttribute("insurance") InsuranceDetails insuranceDetails) {
		User user = userService.getLoggedUser();
		patientService.editInsuranceDetails(insuranceDetails);
		return "patient/patienthome";
	}
	
	@PostMapping("/addClaimDetails")
	public String addInsuranceClaims(@ModelAttribute("insuranceclaim") InsuranceClaims insuranceclaim) {
		insuranceclaim.setStatus("Pending");
		patientService.addInsuranceClaimRequest(insuranceclaim);
		return "patient/patienthome";
	}
	
	@RequestMapping("/getInsuranceDetails")
	public String getInsuranceDetails(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "redirect:/otp/generateOtp/insurancedetails";
	}
	
	@GetMapping("/viewClaimHistory")
	public String getClaimsHistory(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "redirect:/otp/generateOtp/viewClaimHistory";
	}
	
	@GetMapping("/viewAppointmentHistory")
	public String getAppointmentHistory(Model model) {
		User user=userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Appointment> appointment=patientService.findAllAppointments(user);
		model.addAttribute("appointments", appointment);
		return "patient/viewAppointmentHistory";
	}
	
	
	@PostMapping("/labrequest")
	public String labRequest(@RequestParam("diagnosisID") String diagnosisID,Model model) {
		Diagnosis diagnosis=diagnosisRepository.getById(Integer.parseInt(diagnosisID));
		System.out.println(diagnosisID);
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("diagnosis", diagnosis);
		return "patient/requestlabtest";
	}
	
	
	@PostMapping("/createLabRequest")
	public String createLabTestRequest(@RequestParam("diagnosisID") String diagnosisID,@ModelAttribute LabTest labTest) {
		Diagnosis diagnosis=diagnosisRepository.getById(Integer.parseInt(diagnosisID));
		labTest.setDiagnosis(diagnosis);
		patientService.createLabRequest(labTest);
		return "patient/patienthome";
	}
	
	@GetMapping("/viewPendingPayments")
	public String getPendingPayments(Model model) {
		User user=userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<PatientPayment> patientPayments=patientService.findAllPaymentsByStatus();
		model.addAttribute("patientPayments", patientPayments);
		return "patient/viewpendingpayments";		
	}
	
	@GetMapping("/viewLabTests")
	public String viewLabTests(Model model) {
		User user=userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<LabTest> labTests=patientService.viewLabTests(user);
		model.addAttribute("labTests", labTests);
		return "patient/viewlabreports";
	}
	
	@GetMapping("/requestLabReports/{labTestId}")
	public String requestLabReports(@PathVariable("labTestId") String labTestId,Model model) {
		System.out.println("request lab request...");
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		patientService.requestLabTest(Integer.parseInt(labTestId));
		return "patient/viewlabreports";
		
	}
	
	@GetMapping("/viewAllDiagnosisReports")
	public String viewAllDiagnosisReports(Model model) {
		User user=userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Diagnosis> diagnosisList=patientService.viewAllDiagnosis(user);
		model.addAttribute("diagnosisList", diagnosisList);
		return "patient/viewDiagnosis";
	}
	
	@GetMapping("/viewPaymentHistory")
	public String viewAllPayments(Model model) {
		User user=userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<PatientPayment> patientPayments= patientService.findAllPaymentsPaid();
		model.addAttribute("patientPayments", patientPayments);
		return "patient/viewPaymentHistory";
	}
	
	@GetMapping("/makePaymentInsurance/{paymentId}")
	public String makePaymentInsurance(@PathVariable("paymentId") String paymentId,Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		patientService.makePaymentInsurance(Long.parseLong(paymentId));
		return "patient/viewlabreports";
		
	}
	
	@GetMapping("/makeSelfPayment/{paymentId}")
	public String makeSelfPayment(@PathVariable("paymentId") String paymentId,Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		patientService.makePayment(Long.parseLong(paymentId));
		return "patient/viewlabreports";
		
	}
	
	@GetMapping("/submitQuery")
	public String submitQuery( Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "patient/submitQuery";
	}
	
	@PostMapping("/savingQueries")
	public String savingQueries(@RequestParam("queryName") String queryName,Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		PatientQuery patientQuery = new PatientQuery();
		patientQuery.setQuerydescription(queryName);
		patientQuery.setQuerystatus("Submitted");
		patientQuery.setQuerySubmissionTime(new Date());
		patientQuery.setUser(user);
		patientQueryRepository.save(patientQuery);
		return "redirect:/patient/home";
	}
	
	@GetMapping("/viewQuery")
	public String viewQuery( Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<PatientQuery> patientQueries = patientQueryRepository.findAll();
		model.addAttribute("patientQueries",patientQueries);
		return "patient/viewQuery";
	}
}
