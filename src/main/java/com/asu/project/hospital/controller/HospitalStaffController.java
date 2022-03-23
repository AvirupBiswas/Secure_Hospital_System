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
import com.asu.project.hospital.repository.PatientPaymentRepository;
import com.asu.project.hospital.service.MailService;
import com.asu.project.hospital.service.PatientService;
import com.asu.project.hospital.service.UserService;
import com.asu.project.hospital.service.AppointmentService;
import com.asu.project.hospital.service.HospitalStaffService;
import com.asu.project.hospital.entity.HospitalStaff;

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
		patientPayment.setStatus("Pending");
		patientPayment.setUser(App.getUser());
		patientPaymentRepository.save(patientPayment);
	
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
