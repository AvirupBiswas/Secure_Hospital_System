package com.asu.project.hospital.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.LabTestReport;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.repository.DiagnosisRepository;
import com.asu.project.hospital.repository.LabStaffRepository;
import com.asu.project.hospital.repository.LabTestReportRepository;
import com.asu.project.hospital.repository.LabTestRepository;
import com.asu.project.hospital.repository.SystemLogRepository;
import com.asu.project.hospital.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.LabStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.service.LabStaffService;
import com.asu.project.hospital.service.UserService;

@Controller
@RequestMapping("/labstaff")
public class LabStaffController {

	@Autowired
	private UserService userService;

	@Autowired
	private MailService emailService;

	@Autowired
	private LabStaffService labStaffService;

	@Autowired
	private LabStaffRepository labStaffRepository;

	@Autowired
	private SystemLogRepository systemLogRepository;
	
	@Autowired
	private DiagnosisRepository diagnosisRepository;
	
	@Autowired
	private LabTestRepository labTestRepository;
	
	@Autowired
	private LabTestReportRepository labTestReportRepository;

	@GetMapping("/home")
	public String labStaffHome(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "labstaff/labstaffhome";
	}

	@GetMapping("/updateinfo")
	public String register(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		LabStaff labStaffUser = labStaffRepository.findByUser(user);
		model.addAttribute("labstaff", new LabStaff());
		model.addAttribute("userInfo", labStaffUser);
		return "labstaff/updateinfo";
	}

	@PostMapping("/updateinformation")
	public String register(@Valid @ModelAttribute("labstaff") LabStaff userForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "labstaff/updateinfo";
		}
		try {
			User user = userService.getLoggedUser();
			model.addAttribute("accountName", user.getFirstName());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address", userForm.getAddress());
			labStaffService.updateLabStaffInfo(userForm);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "redirect:/labstaff/home";
	}

	@PostMapping("/editinformation")
	public String editInformation(@Valid @ModelAttribute("labstaff") LabStaff userForm, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "labstaff/updateinfo";
		}
		try {
			User user = userService.getLoggedUser();
			model.addAttribute("accountName", user.getFirstName());
			LabStaff labStaffUser = labStaffRepository.findByUser(user);
			labStaffUser.setPhoneNumber(userForm.getPhoneNumber());
			labStaffUser.setAddress(userForm.getAddress());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address", userForm.getAddress());
			labStaffService.updateLabStaffInfo(labStaffUser);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "redirect:/labstaff/home";
	}

	@GetMapping("/getLabTestRequests")
	public String getLabTestRequests(Model model, @RequestParam(name = "status") String status) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		if (status.equals("Requested")) {
			model.addAttribute("allLabTests", labStaffService.getLabTestsByStatus(status));
			return "labstaff/labtestrequests";
		} else if (status.equals("Pending")) {
			model.addAttribute("allLabTests", labStaffService.getLabTestsByStatus(status));
			return "labstaff/createreport";
		} else {
			return "redirect:/labstaff/home";
		}

	}

	@GetMapping("/ViewOrUpdateOrDeleteLabTest")
	public String ViewOrUpdateOrDeleteLabTest(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("allLabTestReports", labStaffService.getAllLabTestReports());
		return "labstaff/ViewOrUpdateOrDeleteLabTest";
	}

	@GetMapping("/approvelabtest/{labTestId}")
	public String approveLabTest(@PathVariable("labTestId") String labTestId, Model model) {
		User user = labStaffService.updateLabTestStatus("Approved", Integer.parseInt(labTestId));
		LabTest labTestObj = labStaffService.getLabTest(Integer.parseInt(labTestId));
		emailService.sendLabTestApprovalMail(user.getEmail(), user.getFirstName(), user.getLastName(),
				labTestObj.getTestName());
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage(user.getEmail() + " lab report request for Test Name: " + labTestObj.getTestName()
				+ " approved by  labstaff " + userService.getLoggedUser().getEmail());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return "labstaff/labtestrequests";
	}

	@GetMapping("/denylabtest/{labTestId}")
	public String denyLabTest(@PathVariable("labTestId") String labTestId, Model model) {
		User user = labStaffService.updateLabTestStatus("Denied", Integer.parseInt(labTestId));
		LabTest labTestObj = labStaffService.getLabTest(Integer.parseInt(labTestId));
		emailService.sendLabTestDenyMail(user.getEmail(), user.getFirstName(), user.getLastName(),
				labTestObj.getTestName());
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage(user.getEmail() + " lab report request for Test Name: " + labTestObj.getTestName()
				+ " denied by  labstaff " + userService.getLoggedUser().getEmail());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return "labstaff/labtestrequests";
	}

	@GetMapping("/createReport/{labTestId}")
	public String openCreateLabTestReportPage(@PathVariable("labTestId") String labTestId, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		LabTest labTestObj = labStaffService.getLabTest(Integer.parseInt(labTestId));
		model.addAttribute("labTest", labTestObj);
		model.addAttribute("labTestId", labTestObj.getLabTestId());
		return "labstaff/createUserReport";
	}

	@PostMapping("/createReport/{labTestId}")
	public String createLabTestReport(@ModelAttribute("labTestReport") LabTestReport labTestReport,
			@PathVariable("labTestId") String labTestId, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		LabTest labTestObj = labStaffService.getLabTest(Integer.parseInt(labTestId));
		Diagnosis diagnosis = labTestObj.getDiagnosis();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("LabStaff with email "+userService.getLoggedUser().getEmail()+ " created lab report of Patient email "+diagnosis.getUser().getEmail()
				+" who has appointment start "+df.format(diagnosis.getAppointment().getStartTime())+" and appointment end "+df.format(diagnosis.getAppointment().getEndTime()));
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		labStaffService.createLabTestReport(labTestReport, labTestObj);
		return "redirect:/labstaff/getLabTestRequests?status=Pending";
	}

	@PostMapping("/manageLabTestReport")
	public String manageAccount(@RequestParam("labTestReportId") String labTestReportId,@RequestParam("action") String action,Model model) {
		if (action.equals("modify")) {
			LabTestReport labTestReport= labStaffService.getLabTestReport(Integer.parseInt(labTestReportId));
			model.addAttribute("labTestReport",labTestReport);
			return "labstaff/UpdateLabTestReport";
		} else if (action.equals("delete")) {
			Optional<LabTestReport> labTestReportOpt = labTestReportRepository.findById(Integer.parseInt(labTestReportId));
			Diagnosis diagnosis = labTestReportOpt.get().getLabTest().getDiagnosis();
			String pattern = "yyyy-MM-dd HH:mm:ss";
			DateFormat df = new SimpleDateFormat(pattern);
			SystemLog systemLog = new SystemLog();
			systemLog.setMessage("LabStaff with email "+userService.getLoggedUser().getEmail()+ " deleted lab report of Patient email "+diagnosis.getUser().getEmail()
					+" who has appointment start "+df.format(diagnosis.getAppointment().getStartTime())+" and appointment end "+df.format(diagnosis.getAppointment().getEndTime()));
			systemLog.setTimestamp(new Date());
			systemLogRepository.save(systemLog);
			labStaffService.deleteLabTestReport(Integer.parseInt(labTestReportId));
			return "redirect:/labstaff/ViewOrUpdateOrDeleteLabTest";
		}
		return "redirect:/labstaff/ViewOrUpdateOrDeleteLabTest";
	}
	
	@GetMapping("/manageLabTestReport/view/{labTestReportId}")
	public String ViewLabTestReport(Model model,@PathVariable("labTestReportId") String labTestReportId) {
		return "redirect:/viewPDF/labstaff/reportView/"+labTestReportId;
	}

	@PostMapping("/UpdateLabTestReport/{labTestReportId}")
	public String UpdateLabTestReport(@ModelAttribute("labTestReport") LabTestReport labTestReport,@PathVariable("labTestReportId") String labTestReportId, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		Optional<LabTestReport> labTestReportOpt = labTestReportRepository.findById(Integer.parseInt(labTestReportId));
		Diagnosis diagnosis = labTestReportOpt.get().getLabTest().getDiagnosis();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage("LabStaff with email "+userService.getLoggedUser().getEmail()+ " updated lab report of Patient email "+diagnosis.getUser().getEmail()
				+" who has appointment start "+df.format(diagnosis.getAppointment().getStartTime())+" and appointment end "+df.format(diagnosis.getAppointment().getEndTime()));
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		labStaffService.UpdateLabTestReport(labTestReport, Integer.parseInt(labTestReportId));
		return "redirect:/labstaff/ViewOrUpdateOrDeleteLabTest";
	}
	
	@GetMapping("/viewdiagnosis/{labTestId}")
	public String viewdiagnosis(Model model,@PathVariable("labTestId") String labTestId) {
		LabTest labTest = labTestRepository.findById(Integer.parseInt(labTestId)).get();
		Diagnosis diagnosis = diagnosisRepository.findByDiagnosisID(labTest.getDiagnosis().getDiagnosisID());
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		model.addAttribute("labtests",diagnosis.getLabtests());
		model.addAttribute("doctorName",diagnosis.getDoctorName());
		return "labstaff/viewDoctorDiagnosis";
	}
	

}
