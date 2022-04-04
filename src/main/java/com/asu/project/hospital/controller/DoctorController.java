package com.asu.project.hospital.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.Doctor;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.model.BlockChainDiagnosisObject;
import com.asu.project.hospital.repository.AppointmentRepository;
import com.asu.project.hospital.repository.DoctorRepository;
import com.asu.project.hospital.repository.PatientRepository;
import com.asu.project.hospital.repository.SystemLogRepository;
import com.asu.project.hospital.service.BlockChainFeignService;
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

	@Autowired
	private BlockChainFeignService blockChainService;

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private SystemLogRepository systemLogRepository;

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
			User user = userService.getLoggedUser();
			model.addAttribute("age", userForm.getAge());
			model.addAttribute("address", userForm.getAddress());
			model.addAttribute("gender", userForm.getGender());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			doctorService.updateDoctorInfo(userForm);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "doctor/doctorhome";
	}

	@PostMapping("/editinformation")
	public String editInformation(@Valid @ModelAttribute("doctor") Doctor userForm, BindingResult result, Model model) {

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
	public String createDiagnosis(@RequestParam("userId") String userId,
			@ModelAttribute("diagnosis") Diagnosis diagnosis, @RequestParam("apptId") String apptId) {
		User patient = userService.findByUserId(userId);
		diagnosis.setUser(patient);
		User doctor = userService.getLoggedUser();
		StringBuilder doctorName = new StringBuilder(doctor.getFirstName());
		doctorName.append(" ").append(doctor.getLastName());
		diagnosis.setDoctorName(doctorName.toString());
		Appointment appt = appointmentRepository.findById(Long.parseLong(apptId)).get();
		diagnosis.setAppointment(appt);
		Diagnosis diagnosisSaved = doctorService.createDiagnosis(diagnosis);
		
		BlockChainDiagnosisObject blcObj = new BlockChainDiagnosisObject();
		blcObj.setDate(new Date());
		blcObj.setId("diagnosisid: " + diagnosisSaved.getDiagnosisID()+" at "+ blcObj.getDate());
		blcObj.setPatient_name(diagnosisSaved.getUser().getFirstName() + " " + diagnosisSaved.getUser().getLastName());
		blcObj.setContent(
				"Diagnosis added by " + diagnosisSaved.getDoctorName() + ", problem: " + diagnosisSaved.getProblem());
		blockChainService.addDiagnosisToBlockChain(blcObj);
		return "doctor/doctorhome";
	}

	@GetMapping("/viewpatients")
	public String viewPatients(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Appointment> allPatients = doctorService.getAllPatients();
		model.addAttribute("appointments", allPatients);
		return "doctor/viewpatients";
	}

	@GetMapping("/viewSpecialApptPatients")
	public String viewSpecialApptPatients(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Appointment> allPatients = doctorService.getAllSpecialAppointment(user.getEmail());
		model.addAttribute("appointments", allPatients);
		return "doctor/viewpatients";
	}

	@RequestMapping("/viewpatientsdiagnosis")
	public String viewPatientsDiagnosis(Model model) {
		List<Appointment> allPatients = doctorService.getAllPatients();
		model.addAttribute("appointments", allPatients);
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "doctor/viewpatientsdiagnosis";
	}

	@RequestMapping("/viewSpApptPatientsdiagnosis")
	public String viewSpApptPatientsdiagnosis(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Appointment> allPatients = doctorService.getAllSpecialAppointment(user.getEmail());
		model.addAttribute("appointments", allPatients);
		return "doctor/viewpatientsdiagnosis";
	}

	@GetMapping("/viewdiagnosis")
	public String viewAlldiagnosis(@RequestParam("userId") String userId, Model model) {
		User user = userService.findByUserId(userId);
		List<Diagnosis> diagnosisList = doctorService.getAllDiagnosis(user);
		model.addAttribute("diagnosis", diagnosisList);
		User userLogged = userService.getLoggedUser();
		model.addAttribute("accountName", userLogged.getFirstName());
		return "doctor/viewdiagnosis";
	}

	@GetMapping("/viewpatientsrecords")
	public String viewPatientsRecords(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Appointment> allPatients = doctorService.getAllPatients();
		model.addAttribute("appointments", allPatients);
		return "doctor/viewpatientsrecords";
	}

	@GetMapping("/viewsplApptPatientsrecords")
	public String viewsplApptPatientsrecords(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<Appointment> allPatients = doctorService.getAllSpecialAppointment(user.getEmail());
		model.addAttribute("appointments", allPatients);
		return "doctor/viewpatientsrecords";
	}

	@GetMapping("/updatediagnosis")
	public String updateDiagnosis(@RequestParam("diagnosisId") int diagnosisId, Model model) {
		Diagnosis diagnosis = doctorService.findByDiagnosis(diagnosisId);
		model.addAttribute("diagnosis", diagnosis);
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "doctor/updatediagnosis";
	}

	@GetMapping("/deletediagnosis")
	public String deleteDiagnosis(@RequestParam("diagnosisId") int diagnosisId, Model model) {
		Diagnosis diagnosis = doctorService.findByDiagnosis(diagnosisId);
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Doctor with email "+userService.getLoggedUser().getEmail()+ " deleted diagnosis report of Patient email "+diagnosis.getUser().getEmail()
				+" who has appointment start "+df.format(diagnosis.getAppointment().getStartTime())+" and appointment end "+df.format(diagnosis.getAppointment().getEndTime()));
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		doctorService.deleteDiagnosis(diagnosis);
		return "doctor/doctorhome";
	}

	@PostMapping("/diagnosis")
	public String createDiagnosis(@RequestParam("userId") String userId, Model model,
			@RequestParam("apptId") String apptId) {
		User account = userService.getLoggedUser();
		model.addAttribute("accountName", account.getFirstName());
		User user = userService.findByUserId(userId);
		model.addAttribute("user", user);
		model.addAttribute("apptId", apptId);
		return "doctor/diagnosis";
	}

	@PostMapping("/editDiagnosis")
	public String editDiagnosis(@RequestParam("diagnosisId") int diagnosisId,
			@ModelAttribute("diagnosis") Diagnosis diagnosis) {
		Diagnosis updatedDiagnosis = doctorService.findByDiagnosis(diagnosisId);
		updatedDiagnosis.setLabtests(diagnosis.getLabtests());
		updatedDiagnosis.setProblem(diagnosis.getProblem());
		updatedDiagnosis.setPrescription(diagnosis.getPrescription());
		updatedDiagnosis.setLabTestNeeded(diagnosis.getLabTestNeeded());
		updatedDiagnosis.setSymptoms(diagnosis.getSymptoms());
		BlockChainDiagnosisObject blcObj = new BlockChainDiagnosisObject();
		blcObj.setDate(new Date());
		blcObj.setId("diagnosisid: " + updatedDiagnosis.getDiagnosisID()+" at "+ blcObj.getDate());
		blcObj.setPatient_name(updatedDiagnosis.getUser().getFirstName() + " " + updatedDiagnosis.getUser().getLastName());
		blcObj.setContent(
				"Diagnosis added by " + updatedDiagnosis.getDoctorName() + ", problem: " + updatedDiagnosis.getProblem());
		blockChainService.addDiagnosisToBlockChain(blcObj);
		doctorService.createDiagnosis(updatedDiagnosis);
		return "doctor/doctorhome";
	}

	@PostMapping("/updatepatientinfo")
	public String updatePatientInfo(@RequestParam("userId") String userId, Model model) {
		User user1 = userService.getLoggedUser();
		model.addAttribute("accountName", user1.getFirstName());
		User user = userService.findByUserId(userId);
		Patient patientdetails = patientRepository.findByUser(user);
		model.addAttribute("user", user);
		model.addAttribute("patientdetails", patientdetails);
		return "doctor/updatepatientinfo";
	}

	@PostMapping("/updatepatientinformation")
	public String updatepatientinformation(@ModelAttribute("updatepatientinformation") Patient patient,
			@ModelAttribute("userId") String userId) {
		User user = userService.findByUserId(userId);
		patient.setUser(user);
		patientRepository.save(patient);
		return "doctor/doctorhome";
	}

	@PostMapping("/editpatientinformation")
	public String editpatientinformation(@ModelAttribute("updatepatientinformation") Patient patient,
			@ModelAttribute("userId") String userId) {
		User user = userService.findByUserId(userId);

		try {
			Patient oldpatient = patientRepository.findByUser(user);
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

	@GetMapping("/viewpatientsforreports")
	public String viewPatientsforReports(Model model) {
		User account = userService.getLoggedUser();
		model.addAttribute("accountName", account.getFirstName());
		List<Appointment> allPatients = doctorService.getAllPatients();
		model.addAttribute("appointments", allPatients);
		return "doctor/viewpatientsforreports";
	}

	@GetMapping("/viewsplApptPatientsforreports")
	public String viewsplApptPatientsforreports(Model model) {
		User account = userService.getLoggedUser();
		model.addAttribute("accountName", account.getFirstName());
		List<Appointment> allPatients = doctorService.getAllSpecialAppointment(account.getEmail());
		model.addAttribute("appointments", allPatients);
		return "doctor/viewpatientsforreports";
	}

	@GetMapping("/viewlabreports")
	public String viewLabTests(@RequestParam("userId") String userId, Model model) {
		User account = userService.getLoggedUser();
		model.addAttribute("accountName", account.getFirstName());
		User patientUser = userService.findByUserId(userId);
		List<LabTest> labTests = doctorService.viewLabTests(patientUser);
		model.addAttribute("labTests", labTests);
		return "doctor/viewlabreports";
	}
}
