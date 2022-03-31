package com.asu.project.hospital.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.asu.project.hospital.entity.AdminDecisionForUser;
import com.asu.project.hospital.entity.Appointment;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.PatientPayment;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.AdminDecisionForUserRepository;
import com.asu.project.hospital.repository.HospitalStaffDecisionForUserRepository;
import com.asu.project.hospital.repository.HospitalStaffRepository;
import com.asu.project.hospital.repository.PatientPaymentRepository;
import com.asu.project.hospital.repository.PatientRepository;
import com.asu.project.hospital.service.MailService;
import com.asu.project.hospital.service.PatientService;
import com.asu.project.hospital.service.UserService;
import com.asu.project.hospital.service.AppointmentService;
import com.asu.project.hospital.service.HospitalStaffService;
import com.asu.project.hospital.entity.HospitalStaff;
import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.LabTest;

@Controller
@RequestMapping("/hospitalstaff")
public class HospitalStaffController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MailService emailService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private HospitalStaffService hospitalStaffService;
	
	@Autowired
	private HospitalStaffRepository hospitalStaffRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	private HospitalStaffDecisionForUserRepository hospitalStaffDecisionForUserRepository;
	
	@Autowired
	private PatientPaymentRepository patientPaymentRepository;

	@GetMapping("/home")
	public String hospitalStaffHome(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "hospitalstaff/home";
	}
	
	@GetMapping("/updateinfo")
	public String updateInfo(Model model) {
		User user = userService.getLoggedUser();
		HospitalStaff hStaff=hospitalStaffRepository.findByUser(user);
		model.addAttribute("hospitalstaff", new HospitalStaff());
		model.addAttribute("hospitalstaffdetails", hStaff);
		return "hospitalstaff/updateinfo";
	}
	
	@PostMapping("/updateinformation")
	public String updateInformation(@Valid @ModelAttribute("hospitalstaff") HospitalStaff userForm, BindingResult result, Model model) {

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
	
	@PostMapping("/editinformation")
	public String editInformation(@Valid @ModelAttribute("hospitalstaff") HospitalStaff userForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "hospitalstaff/updateinfo";
		}
		try {
			User user=userService.getLoggedUser();
			HospitalStaff hospitalStaff=hospitalStaffRepository.findByUser(user);
			hospitalStaff.setPhoneNumber(userForm.getPhoneNumber());
			hospitalStaff.setAddress(userForm.getAddress());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address",userForm.getAddress());
			hospitalStaffService.updateHospitalStaffInfo(hospitalStaff);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "hospitalstaff/home";
	}
	
	@GetMapping("/aproveUser/{Id}")
	public ResponseEntity<String>  aproveUser(@PathVariable("Id") String Id) {
		Long id = Long.parseLong(Id);
		Appointment user = hospitalStaffDecisionForUserRepository.findByAppId(id);
		if (user != null) {
			user.setStatus("Approved");
			hospitalStaffDecisionForUserRepository.save(user);
			emailService.sendUserAppointmentAcceptanceMail(user.getUser().getEmail(),user.getUser().getFirstName(), user.getStartTime());
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/denyUser/{Id}")
	public ResponseEntity<String> denyUser(@PathVariable("Id") String Id) {
		Long id = Long.parseLong(Id);
		Appointment user = hospitalStaffDecisionForUserRepository.findByAppId(id);
		if (user != null) {
			hospitalStaffDecisionForUserRepository.delete(user);
			emailService.sendUserAppointmentDenialMail(user.getUser().getEmail(),user.getUser().getFirstName(), user.getStartTime());
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/userAppPendingDecision")
	public String pendingDecisionForUsereAppointment(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Appointment> users = hospitalStaffDecisionForUserRepository.findByStatus("Pending");
		model.addAttribute("userList", users);
		return "hospitalstaff/hospitalstaffDecisionPending";
	}
	
	@GetMapping("/updateTransaction/{Id}")
	public String  updateTransaction(@PathVariable("Id") String Id, Model model) {
		Long id = Long.parseLong(Id);
		Appointment app = hospitalStaffDecisionForUserRepository.findByAppId(id);
		model.addAttribute("app", app);
		return "hospitalstaff/createTransaction";
	}
	
	@GetMapping("/createTransaction/{Id}")
	public ResponseEntity<String>  createTransaction(@PathVariable("Id") String Id) {
		Long id = Long.parseLong(Id);
		BigDecimal amount=new BigDecimal(100);
		Appointment App = hospitalStaffDecisionForUserRepository.findByAppId(id);
		PatientPayment patientPayment=new PatientPayment();
		patientPayment.setAmount(amount);
		patientPayment.setPurpose("Doctor Appointment");
		patientPayment.setStatus("Pending");
		patientPayment.setUser(App.getUser());
		patientPaymentRepository.save(patientPayment);
	
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/viewpatients")
	public String viewPatients(Model model) {
		List<User> allPatients=hospitalStaffService.getAllPatients();
		model.addAttribute("patient",allPatients);
		return "hospitalstaff/viewpatients";
	}
	
	@GetMapping("/viewPatientsforTransac")
	public String viewPatientsforTransac(Model model) {
		List<User> allPatients=hospitalStaffService.getAllPatients();
		model.addAttribute("patient",allPatients);
		return "hospitalstaff/viewPatientsforTransac";
	}
	
	@PostMapping("/updatepatientinfo")
	public String updatePatientInfo(@RequestParam("userId") String userId, Model model) {
		User user = userService.findByUserId(userId);
		Patient patientdetails=patientRepository.findByUser(user);
		model.addAttribute("user",user);
		model.addAttribute("patientdetails", patientdetails);
		return "hospitalstaff/updatepatientinfo";
	}
	
	@PostMapping("/updatepatientinformation")
	public String updatepatientinformation(@ModelAttribute("updatepatientinformation") Patient patient, @ModelAttribute("userId") String userId) {
		User user = userService.findByUserId(userId);
		patient.setUser(user);
		patientRepository.save(patient);
		return "hospitalstaff/home";
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
			oldpatient.setGender(patient.getGender());
			patientRepository.save(oldpatient);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "hospitalstaff/home";
	}
	
	
//	@PostMapping("/updatepatientinformation")
//	public String register(@Valid @ModelAttribute("patient") Patient userForm, BindingResult result, Model model) {
//
//		if (result.hasErrors()) {
//			return "hospitalstaff/updateinfo";
//		}
//		try {
//			User user=userService.getLoggedUser();
//			model.addAttribute("height", userForm.getHeight());
//			model.addAttribute("weight", userForm.getWeight());
//			model.addAttribute("age", userForm.getAge());
//			model.addAttribute("address",userForm.getAddress());
//			model.addAttribute("gender", userForm.getGender());
//			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
//			//patientService.updatePatientInfo(userForm);
//		} catch (Exception e) {
//			return e.getMessage();
//		}
//		return "hospitalstaff/home";
//	}
	
//	@PostMapping("/editPatientinformation")
//	public String editPatientinformation(@ModelAttribute("patient") Patient patient,@ModelAttribute("user") User user, BindingResult result, Model model) {
//
//		if (result.hasErrors()) {
//			return "hospitalstaff/updatepatientinfo";
//		}
//		try {
//			Patient oldpatient=patientRepository.findByUser(user);
//			oldpatient.setHeight(patient.getHeight());
//			oldpatient.setWeight(patient.getWeight());
//			oldpatient.setAddress(patient.getAddress());
//			oldpatient.setAge(patient.getAge());
//			oldpatient.setPhoneNumber(patient.getPhoneNumber());
//			oldpatient.setGender(patient.getGender());
//			model.addAttribute("height", patient.getHeight());
//			model.addAttribute("weight", patient.getWeight());
//			model.addAttribute("address", patient.getAddress());
//			model.addAttribute("age", patient.getAge());
//			model.addAttribute("phoneNumber", patient.getPhoneNumber());
//			model.addAttribute("gender", patient.getGender());
//			patientRepository.save(oldpatient);
//		} catch (Exception e) {
//			return e.getMessage();
//		}
//		return "hospitalstaff/home";
//	}
	
	@PostMapping("/createSpecificTransac")
	public String createSpecificTransac(@RequestParam("userId") String userId, Model model) {
		User user = userService.findByUserId(userId);
		model.addAttribute("user",user);
		model.addAttribute("patient", new Patient());
		return "hospitalstaff/createSpecificTransaction";
	}
	
	@PostMapping("/createSpecificTransaction")
	public String createSpecificTransaction(@ModelAttribute("createSpecificTransaction") PatientPayment patientPayment, @ModelAttribute("userId") String userId) {
		User user = userService.findByUserId(userId);
		patientPayment.setStatus("Pending");
		patientPayment.setUser(user);
		patientPaymentRepository.save(patientPayment);
		return "hospitalstaff/home";
	}
	
	@GetMapping("/viewPatientsforReports")
	public String viewPatientsforReports(Model model) {
		List<User> allPatients=hospitalStaffService.getAllPatients();
		model.addAttribute("patient",allPatients);
		return "hospitalstaff/viewPatientsforReports";
	}
	
	@GetMapping("/viewLabTests")
	public String viewLabTests(@RequestParam("userId") String userId, Model model) {
		User user = userService.findByUserId(userId);
		List<LabTest> labTests=hospitalStaffService.viewLabTests(user);
		model.addAttribute("labTests", labTests);
		return "hospitalstaff/viewlabreports";
	}
	
}
