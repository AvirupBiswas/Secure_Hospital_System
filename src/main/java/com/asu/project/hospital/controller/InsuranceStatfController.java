package com.asu.project.hospital.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asu.project.hospital.entity.InsuranceStaff;
import com.asu.project.hospital.entity.LabStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.InsuranceRepository;
import com.asu.project.hospital.repository.InsuranceStaffRepository;
import com.asu.project.hospital.service.InsuranceStaffService;
import com.asu.project.hospital.service.UserService;

@Controller
@RequestMapping("/insurancestaff")
public class InsuranceStatfController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private InsuranceStaffService insuranceStaffService;
	
	@GetMapping("/home")
	public String inSuranceStaffHome(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "insurancestaff/insurancestaffhome";
	}

	@GetMapping("/updateinfo")
	public String register(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		InsuranceStaff InsuranceStaffUser = insuranceStaffService.getInsuranceStaff(user);
		model.addAttribute("insuranceStaff", new InsuranceStaff());
		model.addAttribute("userInfo", InsuranceStaffUser);
		return "insurancestaff/updateinfo";
	}
	
	@PostMapping("/updateinformation")
	public String register(@Valid @ModelAttribute("insuranceStaff") InsuranceStaff userForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "redirect:/insuranceStaff/updateinfo";
		}
		try {
			User user = userService.getLoggedUser();
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address", userForm.getAddress());
			model.addAttribute("accountName", user.getFirstName());
			insuranceStaffService.updateInsuranceStaffInfo(userForm);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "redirect:/insurancestaff/home";
	}

	@PostMapping("/editinformation")
	public String editInformation(@Valid @ModelAttribute("insuranceStaff") InsuranceStaff userForm, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			return "redirect:/insuranceStaff/updateinfo";
		}
		try {
			User user = userService.getLoggedUser();
			InsuranceStaff insuranceStaff = insuranceStaffService.getInsuranceStaff(user);
			insuranceStaff.setPhoneNumber(userForm.getPhoneNumber());
			insuranceStaff.setAddress(userForm.getAddress());
			model.addAttribute("phoneNumber", userForm.getPhoneNumber());
			model.addAttribute("address", userForm.getAddress());
			model.addAttribute("accountName", user.getFirstName());
			insuranceStaffService.updateInsuranceStaffInfo(insuranceStaff);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "redirect:/insurancestaff/home";
	}
}