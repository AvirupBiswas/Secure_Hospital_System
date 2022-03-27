package com.asu.project.hospital.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.asu.project.hospital.entity.AdminDecisionForUser;
import com.asu.project.hospital.entity.SignInHistory;
import com.asu.project.hospital.entity.SystemLog;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.model.SignInhistorySearchResult;
import com.asu.project.hospital.model.SystemLogsSearchResult;
import com.asu.project.hospital.repository.AdminDecisionForUserRepository;
import com.asu.project.hospital.repository.SignInHistoryRepository;
import com.asu.project.hospital.repository.SystemLogRepository;
import com.asu.project.hospital.repository.UserRepository;
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
	private SignInHistoryRepository signInHistoryRepository;

	@Autowired
	private SystemLogRepository systemLogRepository;

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
		systemLog.setMessage(user.get().getEmail() +" account creation approved By admin "+userService.getLoggedUser().getEmail());
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
		systemLog.setMessage(user.get().getEmail() +" account creation denied By admin "+userService.getLoggedUser().getEmail());
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
		model.addAttribute("user", user);
		User userLoggedIn = userService.getLoggedUser();
		model.addAttribute("accountName", userLoggedIn.getFirstName());
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
			@RequestParam("userId") String userId) {

		try {
			User user = userRepository.findByUserId(userId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			userRepository.save(user);
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

	/*
	 * @GetMapping("/signInHistory") public String signInHistory(Model model) { User
	 * user = userService.getLoggedUser(); model.addAttribute("accountName",
	 * user.getFirstName()); List<SignInHistory> signInHistoryList =
	 * signInHistoryRepository.findAll(); model.addAttribute("signInHistoryList",
	 * signInHistoryList); return "admin/signInHistory"; }
	 */

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
		Pageable requestedPage = PageRequest.of(pageNumber - 1, 2);// 10);
		Page<SystemLog> systemLogPage = systemLogRepository.findAll(requestedPage);
		int totalPage = systemLogPage.getTotalPages();
		if (pageNumber > totalPage) {
			totalPage = totalPage == 0 ? 1 : totalPage;
			requestedPage = PageRequest.of(totalPage - 1, 2);// 10);
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
		Pageable requestedPage = PageRequest.of(pageNumber - 1, 2);// in one page there are 10 entries
		Page<SystemLog> systemLogPage = systemLogRepository.findAll(requestedPage);
		int totalPage = systemLogPage.getTotalPages();
		if (pageNumber > totalPage) {
			totalPage = totalPage == 0 ? 1 : totalPage;
			requestedPage = PageRequest.of(totalPage - 1, 2);// in one page there are 10 entries
			systemLogPage = systemLogRepository.findAll(requestedPage);
		}
		model.addAttribute("currentPageNumber", pageNumber);
		model.addAttribute("totalPages", totalPage);

		SystemLogsSearchResult systemLogsSearchResult = new SystemLogsSearchResult();
		systemLogsSearchResult.setSystemLogsList(systemLogPage.getContent());
		return new ResponseEntity<SystemLogsSearchResult>(systemLogsSearchResult, HttpStatus.OK);
	}

}
