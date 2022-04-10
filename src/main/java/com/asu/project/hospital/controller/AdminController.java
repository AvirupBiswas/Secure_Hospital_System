package com.asu.project.hospital.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.asu.project.hospital.entity.AdminDecisionForUser;
import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.Doctor;
import com.asu.project.hospital.entity.HospitalStaff;
import com.asu.project.hospital.entity.InsuranceStaff;
import com.asu.project.hospital.entity.LabStaff;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.LabTestReport;
import com.asu.project.hospital.entity.PatientPayment;
import com.asu.project.hospital.entity.PatientQuery;
import com.asu.project.hospital.entity.SignInHistory;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.model.BlockChainDiagnosisObject;
import com.asu.project.hospital.model.SignInhistorySearchResult;
import com.asu.project.hospital.model.SystemLogsSearchResult;
import com.asu.project.hospital.model.ViewDiagNosticAndLabTestReport;
import com.asu.project.hospital.repository.AdminDecisionForUserRepository;
import com.asu.project.hospital.repository.DiagnosisRepository;
import com.asu.project.hospital.repository.DoctorRepository;
import com.asu.project.hospital.repository.HospitalStaffRepository;
import com.asu.project.hospital.repository.InsuranceStaffRepository;
import com.asu.project.hospital.repository.LabStaffRepository;
import com.asu.project.hospital.repository.LabTestReportRepository;
import com.asu.project.hospital.repository.LabTestRepository;
import com.asu.project.hospital.repository.PatientPaymentRepository;
import com.asu.project.hospital.repository.PatientQueryRepository;
import com.asu.project.hospital.repository.SignInHistoryRepository;
import com.asu.project.hospital.repository.SystemLogRepository;
import com.asu.project.hospital.repository.UserRepository;
import com.asu.project.hospital.service.BlockChainFeignService;
import com.asu.project.hospital.service.DoctorService;
import com.asu.project.hospital.service.LabStaffService;
import com.asu.project.hospital.service.MailService;
import com.asu.project.hospital.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminDecisionForUserRepository adminDecisionForUserRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MailService emailService;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BlockChainFeignService blockChainService;

	@Autowired
	private SignInHistoryRepository signInHistoryRepository;

	@Autowired
	private SystemLogRepository systemLogRepository;

	@Autowired
	private HospitalStaffRepository hospitalStaffRepository;

	@Autowired
	private InsuranceStaffRepository insuranceStaffRepository;

	@Autowired
	private PatientPaymentRepository patientPaymentRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private LabStaffRepository labStaffRepository;

	@Autowired
	private LabTestRepository labTestRepository;

	@Autowired
	private DiagnosisRepository diagnosisRepository;

	@Autowired
	private PatientQueryRepository patientQueryRepository;

	@Autowired
	private LabStaffService labStaffService;
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private LabTestReportRepository labTestReportRepository;

	@GetMapping("/aproveUser/{Id}")
	public ResponseEntity<String> aproveUser(@PathVariable("Id") String Id) {
		Long id = Long.parseLong(Id);
		Optional<AdminDecisionForUser> user = adminDecisionForUserRepository.findById(id);
		if (user.isPresent()) {
			userService.registerUserAfterAdminApproval(user.get());
			adminDecisionForUserRepository.deleteById(id);
			emailService.sendUserRegistrationAcceptanceMail(user.get().getEmail(), user.get().getFirstName(),
					user.get().getLastName());
		}
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage(user.get().getEmail() + " account creation approved By admin "
				+ userService.getLoggedUser().getEmail());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@GetMapping("/denyUser/{Id}")
	public ResponseEntity<String> denyUser(@PathVariable("Id") String Id) {
		Long id = Long.parseLong(Id);
		Optional<AdminDecisionForUser> user = adminDecisionForUserRepository.findById(id);
		if (user.isPresent()) {
			adminDecisionForUserRepository.deleteById(id);
			emailService.sendUserRegistrationDenialMail(user.get().getEmail(), user.get().getFirstName(),
					user.get().getLastName());
		}
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage(
				user.get().getEmail() + " account creation denied By admin " + userService.getLoggedUser().getEmail());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@GetMapping("/home")
	public String adminHome(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "admin/home";
	}

	@GetMapping("/userAccPendingDecision")
	public String pendingDecisionForUsereAccount(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<AdminDecisionForUser> users = adminDecisionForUserRepository.findAll();
		model.addAttribute("userList", users);
		return "admin/adminDecisionPending";
	}

	@RequestMapping("/manageAccounts")
	public String manageAccounts(Model model) {
		List<User> employeeList = userService.findAll().stream()
				.filter(e -> e.getActive() && !e.getRole().equals("ADMIN") && !e.getRole().equals("PATIENT"))
				.collect(Collectors.toList());
		model.addAttribute("employees", employeeList);
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "admin/manageAccounts";
	}

	@RequestMapping(value = "/manageAccounts/{userId}", method = RequestMethod.GET)
	public String manageAccount(@PathVariable("userId") String userId, Model model) {
		User user = userService.findByUserId(userId);
		String address = null;
		Long phoneNumber = null;
		if (user.getRole().equals("HOSPITALSTAFF")) {

			HospitalStaff hsStaff = hospitalStaffRepository.findByUser(user);
			if (hsStaff != null) {
				address = hsStaff.getAddress();
				phoneNumber = hsStaff.getPhoneNumber();
			}

		} else if (user.getRole().equals("DOCTOR")) {

			Doctor doctor = doctorRepository.findByUser(user);
			if (doctor != null) {
				address = doctor.getAddress();
				phoneNumber = doctor.getPhoneNumber();
				model.addAttribute("age", doctor.getAge());
				model.addAttribute("gender", doctor.getGender());
			}

		} else if (user.getRole().equals("LABSTAFF")) {

			LabStaff labStaff = labStaffRepository.findByUser(user);
			if (labStaff != null) {
				address = labStaff.getAddress();
				phoneNumber = labStaff.getPhoneNumber();
			}

		} else if (user.getRole().equals("INSURANCESTAFF")) {

			InsuranceStaff insuranceStaff = insuranceStaffRepository.findByUser(user);
			if (insuranceStaff != null) {
				address = insuranceStaff.getAddress();
				phoneNumber = insuranceStaff.getPhoneNumber();
			}
		}
		User userLoggedIn = userService.getLoggedUser();
		model.addAttribute("accountName", userLoggedIn.getFirstName());
		model.addAttribute("user", user);
		model.addAttribute("address", address);
		model.addAttribute("phoneNumber", phoneNumber);
		model.addAttribute("role", user.getRole());
		return "admin/updateEmployeeProfile";
	}

	@PostMapping("/manageAccount")
	public String manageAccount(@RequestParam("userId") String userId, @RequestParam("action") String action) {
		if (action.equals("delete")) {
			User user = userService.findByUserId(userId);
			userService.delete(user);
			return "redirect:manageAccounts";
		} else {
			return "redirect:manageAccounts/" + userId;
		}
	}

	@PostMapping("/updateEmployeeProfile")
	public String updateEmployeeProfile(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("email") String email,
			@RequestParam("userId") String userId, @RequestParam("address") String address,
			@RequestParam("phone") String phone, @RequestParam(value = "age", required = false) String age,
			@RequestParam(value = "gender", required = false) String gender) {

		try {
			User user = userRepository.findByUserId(userId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			userRepository.save(user);

			Long phoneNumber = phone != null && !phone.isEmpty() && !phone.equals("NA") ? Long.parseLong(phone) : null;
			address = address != null && !address.isEmpty() && !address.equals("NA") ? address : null;

			if (user.getRole().equals("HOSPITALSTAFF")) {

				HospitalStaff hsStaff = hospitalStaffRepository.findByUser(user);
				if (hsStaff != null) {
					hsStaff.setAddress(address);
					hsStaff.setPhoneNumber(phoneNumber);
					hospitalStaffRepository.save(hsStaff);
				} else {
					HospitalStaff hsStaffObj = new HospitalStaff();
					hsStaffObj.setAddress(address);
					hsStaffObj.setPhoneNumber(phoneNumber);
					hsStaffObj.setUser(user);
					hospitalStaffRepository.save(hsStaffObj);
				}

			} else if (user.getRole().equals("DOCTOR")) {

				Integer ageVal = age != null && !age.isEmpty() && !age.equals("NA") ? Integer.parseInt(age) : null;
				String genderVal = gender != null && !gender.isEmpty() && !gender.equals("NA") ? gender : null;

				Doctor doctor = doctorRepository.findByUser(user);
				if (doctor != null) {
					doctor.setAddress(address);
					doctor.setPhoneNumber(phoneNumber);
					doctor.setAge(ageVal);
					doctor.setGender(genderVal);
					doctorRepository.save(doctor);
				} else {
					Doctor doctorObj = new Doctor();
					doctorObj.setAddress(address);
					doctorObj.setPhoneNumber(phoneNumber);
					doctorObj.setAge(ageVal);
					doctorObj.setGender(genderVal);
					doctorObj.setUser(user);
					doctorRepository.save(doctorObj);
				}

			} else if (user.getRole().equals("LABSTAFF")) {

				LabStaff labStaff = labStaffRepository.findByUser(user);
				if (labStaff != null) {
					labStaff.setAddress(address);
					labStaff.setPhoneNumber(phoneNumber);
					labStaffRepository.save(labStaff);
				} else {
					LabStaff labStaffObj = new LabStaff();
					labStaffObj.setAddress(address);
					labStaffObj.setPhoneNumber(phoneNumber);
					labStaffObj.setUser(user);
					labStaffRepository.save(labStaffObj);
				}

			} else if (user.getRole().equals("INSURANCESTAFF")) {

				InsuranceStaff insuranceStaff = insuranceStaffRepository.findByUser(user);
				if (insuranceStaff != null) {
					insuranceStaff.setAddress(address);
					insuranceStaff.setPhoneNumber(phoneNumber);
					insuranceStaffRepository.save(insuranceStaff);
				} else {
					InsuranceStaff insuranceStaffObj = new InsuranceStaff();
					insuranceStaffObj.setAddress(address);
					insuranceStaffObj.setPhoneNumber(phoneNumber);
					insuranceStaffObj.setUser(user);
					insuranceStaffRepository.save(insuranceStaffObj);
				}
			}
			return "redirect:manageAccounts";
		} catch (Exception e) {
			return "redirect:/admin/error";
		}
	}

	@RequestMapping("/error")
	public String error(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "admin/error";
	}

	@GetMapping("/signInHistory")
	public String signInHistoryDefaultPage(
			@RequestParam(value = "pagenum", required = false, defaultValue = "1") String pagenum, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		int pageNumber = Integer.parseInt(pagenum);
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		Pageable requestedPage = PageRequest.of(pageNumber - 1, 10);// 10);
		Page<SignInHistory> signInHistoryPage = signInHistoryRepository.findAll(requestedPage);
		int totalPage = signInHistoryPage.getTotalPages();
		if (pageNumber > totalPage) {
			totalPage = totalPage == 0 ? 1 : totalPage;
			requestedPage = PageRequest.of(totalPage - 1, 10);// 10);
			signInHistoryPage = signInHistoryRepository.findAll(requestedPage);
		}
		model.addAttribute("currentPageNumber", pageNumber);
		model.addAttribute("totalPages", totalPage);

		model.addAttribute("signInHistoryList", signInHistoryPage.getContent());
		return "admin/signInHistory";
	}

	@GetMapping("/signInHistory/pageWise")
	public ResponseEntity<SignInhistorySearchResult> signInHistoryPageWise(
			@RequestParam(value = "pagenum", required = false, defaultValue = "1") String pagenum, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		int pageNumber = Integer.parseInt(pagenum);
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		Pageable requestedPage = PageRequest.of(pageNumber - 1, 10);// in one page there are 10 entries
		Page<SignInHistory> signInHistoryPage = signInHistoryRepository.findAll(requestedPage);
		int totalPage = signInHistoryPage.getTotalPages();
		if (pageNumber > totalPage) {
			totalPage = totalPage == 0 ? 1 : totalPage;
			requestedPage = PageRequest.of(totalPage - 1, 10);// in one page there are 10 entries
			signInHistoryPage = signInHistoryRepository.findAll(requestedPage);
		}
		model.addAttribute("currentPageNumber", pageNumber);
		model.addAttribute("totalPages", totalPage);

		SignInhistorySearchResult signInhistorySearchResult = new SignInhistorySearchResult();
		signInhistorySearchResult.setSignInHistoryList(signInHistoryPage.getContent());
		return new ResponseEntity<SignInhistorySearchResult>(signInhistorySearchResult, HttpStatus.OK);
	}

	@RequestMapping("/logs")
	public String systemLogsDefaultPage(
			@RequestParam(value = "pagenum", required = false, defaultValue = "1") String pagenum, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		int pageNumber = Integer.parseInt(pagenum);
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		Pageable requestedPage = PageRequest.of(pageNumber - 1, 10);
		Page<SystemLog> systemLogPage = systemLogRepository.findAll(requestedPage);
		int totalPage = systemLogPage.getTotalPages();
		if (pageNumber > totalPage) {
			totalPage = totalPage == 0 ? 1 : totalPage;
			requestedPage = PageRequest.of(totalPage - 1, 10);
			systemLogPage = systemLogRepository.findAll(requestedPage);
		}
		model.addAttribute("currentPageNumber", pageNumber);
		model.addAttribute("totalPages", totalPage);

		model.addAttribute("systemLogList", systemLogPage.getContent());

		return "admin/logs";
	}

	@GetMapping("/systemLogs/pageWise")
	public ResponseEntity<SystemLogsSearchResult> systemLogsPageWise(
			@RequestParam(value = "pagenum", required = false, defaultValue = "1") String pagenum, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		int pageNumber = Integer.parseInt(pagenum);
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		Pageable requestedPage = PageRequest.of(pageNumber - 1, 10);// in one page there are 10 entries
		Page<SystemLog> systemLogPage = systemLogRepository.findAll(requestedPage);
		int totalPage = systemLogPage.getTotalPages();
		if (pageNumber > totalPage) {
			totalPage = totalPage == 0 ? 1 : totalPage;
			requestedPage = PageRequest.of(totalPage - 1, 10);// in one page there are 10 entries
			systemLogPage = systemLogRepository.findAll(requestedPage);
		}
		model.addAttribute("currentPageNumber", pageNumber);
		model.addAttribute("totalPages", totalPage);

		SystemLogsSearchResult systemLogsSearchResult = new SystemLogsSearchResult();
		systemLogsSearchResult.setSystemLogsList(systemLogPage.getContent());
		return new ResponseEntity<SystemLogsSearchResult>(systemLogsSearchResult, HttpStatus.OK);
	}

	@GetMapping("/paymentApproval")
	public String paymentApprovalList(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("allPaymentList", patientPaymentRepository.findByStatus("paid"));
		return "admin/pendingPaymentApprovalList";
	}

	@GetMapping("/approveEachPayment/{paymentId}")
	public String approveEachPayment(Model model, @PathVariable("paymentId") String paymentId) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		Optional<PatientPayment> patientPaymentOpt = patientPaymentRepository.findById(Long.parseLong(paymentId));
		if (patientPaymentOpt.isPresent()) {
			PatientPayment patientPayment = patientPaymentOpt.get();
			patientPayment.setStatus("Approved");
			patientPaymentRepository.save(patientPayment);
			BlockChainDiagnosisObject blcObj=new BlockChainDiagnosisObject();
			blcObj.setDate(new Date());
			blcObj.setId("Paymentid: " + patientPayment.getPaymentID()+" at "+ blcObj.getDate());
			blcObj.setPatient_name(patientPayment.getUser().getFirstName() + " " + patientPayment.getUser().getLastName());
			blcObj.setContent(
					"Payment approved for " + user.getFirstName() + " " + user.getLastName());
			blockChainService.addDiagnosisToBlockChain(blcObj);
			SystemLog systemLog = new SystemLog();
			systemLog.setMessage("payment of patient with email "+patientPayment.getUser().getEmail() + " has approved By admin with email "
					+ user.getEmail());
			systemLog.setTimestamp(new Date());
			systemLogRepository.save(systemLog);
		}
		
		return "admin/pendingPaymentApprovalList";
	}

	@GetMapping("/getInternalPatientFiles")
	public String getReportsOfPatient(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<ViewDiagNosticAndLabTestReport> labreportList = new ArrayList<>();
		List<ViewDiagNosticAndLabTestReport> diagreportList = new ArrayList<>();
		List<Diagnosis> diagnosisList = diagnosisRepository.findAll();
		List<LabTest> labTests = labTestRepository.findByStatus("Reported");
		for (Diagnosis d : diagnosisList) {
			List<LabTest> tempLabTests = labTests.stream()
					.filter(e -> e.getDiagnosis().getDiagnosisID() == d.getDiagnosisID()).collect(Collectors.toList());
			if (tempLabTests != null && !tempLabTests.isEmpty()) {
				for (LabTest l : tempLabTests) {
					ViewDiagNosticAndLabTestReport obj = new ViewDiagNosticAndLabTestReport();
					obj.setFirstName(d.getUser().getFirstName());
					obj.setLastName(d.getUser().getLastName());
					obj.setEmail(d.getUser().getEmail());
					obj.setDescription(l.getDescription());
					obj.setLabTestId(l.getLabTestId());
					obj.setLabTestReportId(labTestReportRepository.findByLabTest(l).getLabTestReportId());
					obj.setTestNameReported(l.getTestName());
					obj.setTestNameRecommendedByDoctor(d.getLabtests());
					obj.setDiagnosisID(d.getDiagnosisID());
					obj.setDoctorName(d.getDoctorName());
					obj.setStartTime(d.getAppointment().getStartTime());
					obj.setEndTime(d.getAppointment().getEndTime());
					labreportList.add(obj);
				}
			} 
		}
		for (Diagnosis d : diagnosisList) {
				ViewDiagNosticAndLabTestReport obj = new ViewDiagNosticAndLabTestReport();
				obj.setFirstName(d.getUser().getFirstName());
				obj.setLastName(d.getUser().getLastName());
				obj.setEmail(d.getUser().getEmail());
				obj.setTestNameRecommendedByDoctor(d.getLabtests());
				obj.setDiagnosisID(d.getDiagnosisID());
				obj.setDoctorName(d.getDoctorName());
				obj.setStartTime(d.getAppointment().getStartTime());
				obj.setEndTime(d.getAppointment().getEndTime());
				diagreportList.add(obj);
		}
		model.addAttribute("allPatientWithLabReport", labreportList);
		model.addAttribute("allPatientWithDiagReport", diagreportList);
		return "admin/internalFile";
	}

	@GetMapping("/viewSubmitedQueries")
	public String viewSubmitedQueries(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		List<PatientQuery> patientQueries = patientQueryRepository.findAll();
		model.addAttribute("patientQueries", patientQueries);
		return "admin/viewAllSubmittedQueries";
	}

	@GetMapping("/takeaction/{queryId}")
	public String takeaction(@PathVariable("queryId") String queryId, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		Optional<PatientQuery> patientQuery = patientQueryRepository.findById(Long.parseLong(queryId));
		model.addAttribute("patientQuery", patientQuery.get());
		model.addAttribute("queryId", queryId);
		return "admin/submitResolution";
	}

	@PostMapping("/takeaction/{queryId}")
	public String takeaction(@RequestParam("queryresolution") String queryresolution,
			@PathVariable("queryId") String queryId, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		Optional<PatientQuery> patientQueryOptional = patientQueryRepository.findById(Long.parseLong(queryId));
		PatientQuery patientQuery = patientQueryOptional.get();
		patientQuery.setQueryresolution(queryresolution);
		patientQuery.setQuerystatus("Resolved");
		patientQueryRepository.save(patientQuery);
		return "redirect:/admin/viewSubmitedQueries";
	}

	@GetMapping("/DeleteLabTestReport/{labTestReportId}")
	public String DeleteLabTestReport(@PathVariable("labTestReportId") String labTestReportId, Model model) {
		User user = userService.getLoggedUser();
		Optional<LabTestReport> labTestReportOpt = labTestReportRepository.findById(Integer.parseInt(labTestReportId));
		Diagnosis diagnosis = labTestReportOpt.get().getLabTest().getDiagnosis();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Admin with email "+user.getEmail()+ " deleted lab report of Patient email "+diagnosis.getUser().getEmail()
				+" who has appointment start "+df.format(diagnosis.getAppointment().getStartTime())+" and appointment end "+df.format(diagnosis.getAppointment().getEndTime()));
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		labStaffService.deleteLabTestReport(Integer.parseInt(labTestReportId));
		
		model.addAttribute("accountName", user.getFirstName());
		return "redirect:/admin/getInternalPatientFiles";
	}
	
	@GetMapping("/DeleteDiagnosisReport/{diagnosisId}")
	public String deleteDiagnosis(@PathVariable("diagnosisId") String diagnosisId, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		Diagnosis diagnosis = doctorService.findByDiagnosis(Integer.parseInt(diagnosisId));
		
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("Admin with email "+user.getEmail()+ " deleted diagnosis report of Patient email "+diagnosis.getUser().getEmail()
				+" who has appointment start "+df.format(diagnosis.getAppointment().getStartTime())+" and appointment end "+df.format(diagnosis.getAppointment().getEndTime()));
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		doctorService.deleteDiagnosis(diagnosis);
		return "redirect:/admin/getInternalPatientFiles";
	}

}
