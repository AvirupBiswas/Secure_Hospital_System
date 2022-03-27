package com.asu.project.hospital.controller;

import javax.validation.Valid;

import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.LabTestReport;
import com.asu.project.hospital.repository.LabStaffRepository;
import com.asu.project.hospital.repository.LabTestRepository;
import com.asu.project.hospital.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
	private LabTestRepository labTestRepository;

	@GetMapping("/home")
	public String labStaffHome(Model model) {
		User user = userService.getLoggedUser();
//		System.out.println("user obj:" + user);
		model.addAttribute("accountName", user.getFirstName());
//		model.addAttribute("user", user);
		return "labstaff/labstaffhome";
	}

	@GetMapping("/updateinfo")
	public String register(Model model) {
		User user = userService.getLoggedUser();
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
			User user=userService.getLoggedUser();
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address",userForm.getAddress());
			labStaffService.updateLabStaffInfo(userForm);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "labstaff/labstaffhome";
	}

	@PostMapping("/editinformation")
	public String editInformation(@Valid @ModelAttribute("labstaff") LabStaff userForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "labstaff/updateinfo";
		}
		try {
			User user=userService.getLoggedUser();
			LabStaff labStaffUser=labStaffRepository.findByUser(user);
			labStaffUser.setPhoneNumber(userForm.getPhoneNumber());
			labStaffUser.setAddress(userForm.getAddress());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address",userForm.getAddress());
			labStaffService.updateLabStaffInfo(labStaffUser);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "labstaff/labstaffhome";
	}

	@GetMapping("/getLabTestRequests")
	public String getLabTestRequests(Model model, @RequestParam(name="status") String status) {
		User user = userService.getLoggedUser();
//		System.out.println("user obj:" + user);
		model.addAttribute("accountName", user.getFirstName());
//		model.addAttribute("user", user);

		if (status.equals("Requested")){
			model.addAttribute("allLabTests", labStaffService.getLabTestsByStatus(status));
			return "labstaff/labtestrequests";
		}
		else if (status.equals("Generate")){
			model.addAttribute("allLabTests", labStaffService.getLabTestsByStatus(status));
			return "labstaff/createreport";
		}
		else {
			return "labstaff/labstaffhome";
		}

	}

//	approvelabtest

	@GetMapping("/approvelabtest/{labTestId}")
	public String approveLabTest(@PathVariable("labTestId") String labTestId, Model model) {
		User user = labStaffService.updateLabTestStatus("Approved", Integer.parseInt(labTestId));
		LabTest labTestObj = labStaffService.getLabTest(Integer.parseInt(labTestId));
		emailService.sendLabTestApprovalMail(user.getEmail(), user.getFirstName(),user.getLastName(), labTestObj.getTestName());
		return "labstaff/labtestrequests";
	}

	@GetMapping("/denylabtest/{labTestId}")
	public String denyLabTest(@PathVariable("labTestId") String labTestId, Model model) {
		User user = labStaffService.updateLabTestStatus("Denied", Integer.parseInt(labTestId));
		LabTest labTestObj = labStaffService.getLabTest(Integer.parseInt(labTestId));
		emailService.sendLabTestDenyMail(user.getEmail(), user.getFirstName(), user.getLastName(), labTestObj.getTestName());
		return "labstaff/labtestrequests";
	}

//	createReport

	@GetMapping("/createReport/{labTestId}")
	public String createReport(@Valid @ModelAttribute("labTestReport") LabTestReport userForm, BindingResult result, @PathVariable("labTestId") String labTestId, Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		LabTest labTestObj = labStaffService.getLabTest(Integer.parseInt(labTestId));

		model.addAttribute("labTest", labTestObj);
		return "labstaff/createUserReport";
	}

}
