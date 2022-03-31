package com.asu.project.hospital.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.InsuranceStaff;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.model.Insurance;
import com.asu.project.hospital.repository.InsuranceClaimsRepository;
import com.asu.project.hospital.repository.SystemLogRepository;
import com.asu.project.hospital.repository.UserRepository;
import com.asu.project.hospital.service.InsuranceStaffService;
import com.asu.project.hospital.service.MailService;
import com.asu.project.hospital.service.UserService;

@Controller
@RequestMapping("/insurancestaff")
public class InsuranceStatfController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private InsuranceStaffService insuranceStaffService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SystemLogRepository systemLogRepository;
	
	@Autowired
	private MailService emailService;
	
	@Autowired
	private InsuranceClaimsRepository insuranceClaimsRepository;
	
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
	
	@GetMapping("/createInsurance")
	public String createInsurance(Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		return "insurancestaff/createInsurance";
	}
	
	@PostMapping("/addInsuranceDetails")
	public String addInsuranceDetails(@ModelAttribute("insurance") Insurance insurance,Model model) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		InsuranceDetails insuranceDetails = new InsuranceDetails();
		insuranceDetails.setInsuranceId(insurance.getInsuranceId());
		insuranceDetails.setInsuranceName(insurance.getInsuranceName());
		insuranceDetails.setProvider(insurance.getProvider());
		Optional<User> userVal= userRepository.findOneByEmailIgnoreCase(insurance.getEmail());
		insuranceDetails.setUser(userVal.get());
		insuranceStaffService.addInsuranceDetails(insuranceDetails);
		return "redirect:/insurancestaff/home";
	}
	
	@GetMapping("/getInsuranceClaim")
	public String getInsuranceClaim(Model model, @RequestParam(name = "status") String status) {
		User user = userService.getLoggedUser();
		model.addAttribute("accountName", user.getFirstName());
		if(status.equals("Pending")) {
		List<InsuranceClaims> claims = insuranceStaffService.getInsuranceClaimByStatus(status);
		model.addAttribute("allclaims", claims);
		return "insurancestaff/viewClaim";
		}
		else {
			List<InsuranceClaims> claims = insuranceStaffService.getInsuranceClaimByStatus(status);
			model.addAttribute("allclaims", claims);
			return "insurancestaff/viewClaimToDisburse";
		}
	}
	
	@GetMapping("/approveclaim/{claimId}")
	public String approveLabTest(@PathVariable("claimId") String claimId, Model model) {
		User user = insuranceStaffService.updateClaimStatus("Approved", Long.parseLong(claimId));
		emailService.sendInsuranceClaimApprovalMail(user.getEmail(), user.getFirstName(), user.getLastName(),
				claimId);
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage(user.getEmail() + " claim " + claimId
				+ " approved by  insuranceStaff " + userService.getLoggedUser().getEmail());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return "insurancestaff/viewClaim";
	}

	@GetMapping("/denyclaim/{claimId}")
	public String denyLabTest(@PathVariable("claimId") String claimId, Model model) {
		User user = insuranceStaffService.updateClaimStatus("Denied", Long.parseLong(claimId));
		emailService.sendInsuranceClaimDenyMail(user.getEmail(), user.getFirstName(), user.getLastName(),
				claimId);
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage(user.getEmail() + " claim " + claimId
				+ " denied by  insuranceStaff " + userService.getLoggedUser().getEmail());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return "insurancestaff/viewClaim";
	}
	
	
	@GetMapping("/disburse/{claimId}")
	public String disburse(@PathVariable("claimId") String claimId, Model model) {
		User user = insuranceStaffService.updateClaimStatus("Disbursed", Long.parseLong(claimId));
		Optional<InsuranceClaims> claim = insuranceClaimsRepository.findById( Long.parseLong(claimId));
		InsuranceClaims claimObj = claim.get();
		emailService.sendInsuranceClaimAmountDisburseMail(user.getEmail(), user.getFirstName(), user.getLastName(),
				claimId,claimObj.getAmount());
		SystemLog systemLog = new SystemLog();
		systemLog.setMessage(user.getEmail() + " claim amount " +claimObj.getAmount()+" corresponding to claim id "+ claimId
				+ " has been disburse by  insuranceStaff " + userService.getLoggedUser().getEmail());
		systemLog.setTimestamp(new Date());
		systemLogRepository.save(systemLog);
		return "insurancestaff/viewClaimToDisburse";
	}
	
}