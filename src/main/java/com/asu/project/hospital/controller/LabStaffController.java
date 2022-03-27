package com.asu.project.hospital.controller;

import javax.validation.Valid;

import com.asu.project.hospital.entity.HospitalStaff;
import com.asu.project.hospital.repository.LabStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	private LabStaffService labStaffService;

	@Autowired
	private LabStaffRepository labStaffRepository;

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
}
