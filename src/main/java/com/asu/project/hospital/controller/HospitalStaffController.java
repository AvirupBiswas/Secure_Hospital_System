package com.asu.project.hospital.controller;

import java.math.BigDecimal;
import java.util.Date;
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
import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.PatientPayment;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.AdminDecisionForUserRepository;
import com.asu.project.hospital.repository.HospitalStaffDecisionForUserRepository;
import com.asu.project.hospital.repository.HospitalStaffRepository;
import com.asu.project.hospital.repository.PatientPaymentRepository;
import com.asu.project.hospital.repository.PatientRepository;
import com.asu.project.hospital.repository.SystemLogRepository;
import com.asu.project.hospital.service.MailService;
import com.asu.project.hospital.service.PatientService;
import com.asu.project.hospital.service.UserService;
import com.asu.project.hospital.service.AppointmentService;
import com.asu.project.hospital.service.HospitalStaffService;
import com.asu.project.hospital.entity.HospitalStaff;
import com.asu.project.hospital.entity.LabTest;

@Controller
@RequestMapping("/hospitalstaff")
public class HospitalStaffController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MailService emailService;
	
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
	
	@Autowired
	private SystemLogRepository systemLogRepository;

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
		model.addAttribute("accountName", user.getFirstName());
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
			model.addAttribute("accountName", user.getFirstName());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address",userForm.getAddress());
			hospitalStaffService.updateHospitalStaffInfo(userForm);
			SystemLog systemLog = new SystemLog();
			systemLog.setMessage("Added Hospital Staff "+user.getFirstName() + " "+user.getLastName()+ "'s details");
			systemLog.setTimestamp(new Date());
			systemLogRepository.save(systemLog);
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
			model.addAttribute("accountName", user.getFirstName());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address",userForm.getAddress());
			hospitalStaffService.updateHospitalStaffInfo(hospitalStaff);
			SystemLog systemLog = new SystemLog();
			systemLog.setMessage("Updated Hospital Staff "+user.getFirstName() + " "+user.getLastName()+ "'s details");
			systemLog.setTimestamp(new Date());
			systemLogRepository.save(systemLog);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "hospitalstaff/home";
	}
	
	@GetMapping("/aproveUser/{Id}")
	public ResponseEntity<String>  aproveUser(@PathVariable("Id") String Id) {
		Long id = Long.parseLong(Id);
		User user = userService.getLoggedUser();
		Appointment app = hospitalStaffDecisionForUserRepository.findByAppId(id);
		if (app != null) {
			app.setStatus("Approved");
			hospitalStaffDecisionForUserRepository.save(app);
			emailService.sendUserAppointmentAcceptanceMail(app.getUser().getEmail(),app.getUser().getFirstName(), app.getStartTime());
			SystemLog systemLog = new SystemLog();
			systemLog.setMessage("Hospital Staff "+user.getFirstName() + " "+user.getLastName()+" has approved Patient "+ app.getUser().getFirstName() +"'s appointment");
			systemLog.setTimestamp(new Date());
			systemLogRepository.save(systemLog);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/denyUser/{Id}")
	public ResponseEntity<String> denyUser(@PathVariable("Id") String Id) {
		Long id = Long.parseLong(Id);
		User user = userService.getLoggedUser();
		Appointment app = hospitalStaffDecisionForUserRepository.findByAppId(id);
		if (app != null) {
			hospitalStaffDecisionForUserRepository.delete(app);
			emailService.sendUserAppointmentDenialMail(app.getUser().getEmail(),app.getUser().getFirstName(), app.getStartTime());
			SystemLog systemLog = new SystemLog();
			systemLog.setMessage("Hospital Staff "+user.getFirstName() + " "+user.getLastName()+" has denied Patient"+ app.getUser().getFirstName()+" "+app.getUser().getLastName() +"'s appointment");
			systemLog.setTimestamp(new Date());
			systemLogRepository.save(systemLog);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/userAppPendingDecision")
	public String pendingDecisionForUsereAppointment(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Appointment> appointments = hospitalStaffDecisionForUserRepository.findByStatus("Pending");
		model.addAttribute("appointments", appointments);
		model.addAttribute("doctors", hospitalStaffService.getDoctorsList());
		return "hospitalstaff/hospitalstaffDecisionPending";
	}
	
	@GetMapping("/updateTransaction/{Id}")
	public String  updateTransaction(@PathVariable("Id") String Id, Model model) {
		User user = userService.getLoggedUser();
		Long id = Long.parseLong(Id);
		Appointment app = hospitalStaffDecisionForUserRepository.findByAppId(id);
		System.out.println(app);
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("app", app);
		return "hospitalstaff/createTransaction";
	}
	
	@GetMapping("/createTransaction/{Id}")
	public ResponseEntity<String>  createTransaction(@PathVariable("Id") String Id) {
		Long id = Long.parseLong(Id);
		User user = userService.getLoggedUser();
		BigDecimal amount=new BigDecimal(100);
		Appointment app = hospitalStaffDecisionForUserRepository.findByAppId(id);
		PatientPayment patientPayment=new PatientPayment();
		patientPayment.setAmount(amount);
		patientPayment.setPurpose("Doctor Appointment");
		patientPayment.setStatus("Pending");
		patientPayment.setUser(app.getUser());
		patientPaymentRepository.save(patientPayment);
		
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Hospital Staff "+user.getFirstName() + " "+user.getLastName()+" has created a transaction for patient "+ app.getUser().getFirstName()+" "+app.getUser().getLastName());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
	
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@GetMapping("/viewpatients")
	public String viewPatients(Model model) {
		User user = userService.getLoggedUser();
		List<User> allPatients=hospitalStaffService.getAllPatients();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("patient",allPatients);
		return "hospitalstaff/viewpatients";
	}
	
	@GetMapping("/viewPatientsforTransac")
	public String viewPatientsforTransac(Model model) {
		User user = userService.getLoggedUser();
		List<User> allPatients=hospitalStaffService.getAllPatients();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("patient",allPatients);
		return "hospitalstaff/viewPatientsforTransac";
	}
	
	@PostMapping("/updatepatientinfo")
	public String updatePatientInfo(@RequestParam("userId") String userId, Model model) {
		User user = userService.getLoggedUser();
		User patientUser = userService.findByUserId(userId);
		Patient patientdetails=patientRepository.findByUser(patientUser);
		model.addAttribute("user",patientUser);
		model.addAttribute("patientdetails", patientdetails);
		model.addAttribute("accountName", user.getFirstName());
		return "hospitalstaff/updatepatientinfo";
	}
	
	@PostMapping("/updatepatientinformation")
	public String updatepatientinformation(@ModelAttribute("updatepatientinformation") Patient patient, @ModelAttribute("userId") String userId, Model model) {
		User patientUser = userService.findByUserId(userId);
		patient.setUser(patientUser);
		patientRepository.save(patient);
		
		User user = userService.getLoggedUser();
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Hospital Staff "+user.getFirstName() + " "+user.getLastName()+" has added details of patient - "+ patientUser.getFirstName()+" "+patientUser.getLastName());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		model.addAttribute("accountName", user.getFirstName());
		return "hospitalstaff/home";
	}
	
	@PostMapping("/editpatientinformation")
	public String editpatientinformation(@ModelAttribute("updatepatientinformation") Patient patient, @ModelAttribute("userId") String userId, Model model) {
		User patientUser = userService.findByUserId(userId);
		
		try {
			Patient oldpatient=patientRepository.findByUser(patientUser);
			oldpatient.setHeight(patient.getHeight());
			oldpatient.setWeight(patient.getWeight());
			oldpatient.setAddress(patient.getAddress());
			oldpatient.setAge(patient.getAge());
			oldpatient.setPhoneNumber(patient.getPhoneNumber());
			oldpatient.setGender(patient.getGender());
			patientRepository.save(oldpatient);
			
			User user = userService.getLoggedUser();
			SystemLog systemLog = new SystemLog();
			systemLog.setMessage("Hospital Staff "+user.getFirstName() + " "+user.getLastName()+" has updated details of patient - "+ patientUser.getFirstName()+" "+patientUser.getLastName());
			systemLog.setTimestamp(new Date());
			systemLogRepository.save(systemLog);
			model.addAttribute("accountName", user.getFirstName());
		} catch (Exception e) {
			return e.getMessage();
		}
		return "hospitalstaff/home";
	}
	
	@PostMapping("/createSpecificTransac")
	public String createSpecificTransac(@RequestParam("userId") String userId, Model model) {
		User user = userService.getLoggedUser();
		User patientUser = userService.findByUserId(userId);
		model.addAttribute("user",patientUser);
		model.addAttribute("patient", new Patient());
		model.addAttribute("accountName", user.getFirstName());
		return "hospitalstaff/createSpecificTransaction";
	}
	
	@PostMapping("/createSpecificTransaction")
	public String createSpecificTransaction(@ModelAttribute("createSpecificTransaction") PatientPayment patientPayment, @ModelAttribute("userId") String userId, Model model) {
		User patientUser = userService.findByUserId(userId);
		patientPayment.setStatus("Pending");
		patientPayment.setUser(patientUser);
		if(patientPayment.getAmount()!=null && patientPayment.getPurpose()!=null) {
		patientPaymentRepository.save(patientPayment);
		}
		
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Hospital Staff "+user.getFirstName() + " "+user.getLastName()+" has created transaction for patient - "+ patientUser.getFirstName()+" "+patientUser.getLastName());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		
		return "hospitalstaff/home";
	}
	
	@GetMapping("/viewPatientsforReports")
	public String viewPatientsforReports(Model model) {
		User user = userService.getLoggedUser();
		List<User> allPatients=hospitalStaffService.getAllPatients();
		model.addAttribute("patient",allPatients);
		model.addAttribute("accountName", user.getFirstName());
		return "hospitalstaff/viewPatientsforReports";
	}
	
	@GetMapping("/viewLabTests")
	public String viewLabTests(@RequestParam("userId") String userId, Model model) {
		User patientUser = userService.findByUserId(userId);
		List<LabTest> labTests=hospitalStaffService.viewLabTests(patientUser);
		model.addAttribute("labTests", labTests);
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Hospital Staff "+user.getFirstName() + " "+user.getLastName()+" viewed Lab reports of patient - "+ patientUser.getFirstName()+" "+patientUser.getLastName());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return "hospitalstaff/viewlabreports";
	}
	
	@GetMapping("/viewAllDiagnosisReports")
	public String viewAllDiagnosisReports(@RequestParam("userId") String userId, Model model) {
		User patientUser = userService.findByUserId(userId);
		List<Diagnosis> diagnosisList=hospitalStaffService.viewAllDiagnosis(patientUser);
		model.addAttribute("diagnosisList", diagnosisList);
		
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Hospital Staff "+user.getFirstName() + " "+user.getLastName()+" viewed Diagnosis reports of patient - "+ patientUser.getFirstName()+" "+patientUser.getLastName());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return "hospitalstaff/viewDiagnosis";
	}
	
}
