package com.asu.project.hospital.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.Exception.EmailUsedException;
import com.asu.project.hospital.Exception.RoleException;
import com.asu.project.hospital.entity.AdminDecisionForUser;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.AdminDecisionForUserRepository;
import com.asu.project.hospital.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdminDecisionForUserRepository adminDecisionForUserRepository;

	public void registerUser(User user, String registeredBy) {
		String role = user.getRole();
		validateUserRole(role);
		if ((registeredBy != null && registeredBy.equals("admin")) || role.equals("PATIENT")) {
			validateUser(user);
			User u = new User();
			u.setFirstName(user.getFirstName());
			u.setLastName(user.getLastName());
			u.setEmail(user.getEmail());
			u.setRole(role);
			u.setPassword(passwordEncoder.encode(user.getPassword()));
			u.setActive(true);
			userRepository.save(u);
		} else {
			validateUserBeforeAdminApproval(user);
			AdminDecisionForUser u = new AdminDecisionForUser();
			u.setFirstName(user.getFirstName());
			u.setLastName(user.getLastName());
			u.setEmail(user.getEmail());
			u.setRole(role);
			u.setPassword(passwordEncoder.encode(user.getPassword()));
			adminDecisionForUserRepository.save(u);
		}
	}

	public void registerUserAfterAdminApproval(AdminDecisionForUser user) {
		User u = new User();
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setEmail(user.getEmail());
		u.setRole(user.getRole());
		u.setPassword(user.getPassword());
		u.setActive(true);
		userRepository.save(u);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public void validateUser(User user) {
		userRepository.findOneByEmailIgnoreCase(user.getEmail()).ifPresent(existing -> {
			if (existing.getEmail().equalsIgnoreCase(user.getEmail())) {
				throw new EmailUsedException();
			}
		});
	}

	public void validateUserBeforeAdminApproval(User user) {
		adminDecisionForUserRepository.findOneByEmailIgnoreCase(user.getEmail()).ifPresent(existing -> {
			if (existing.getEmail().equalsIgnoreCase(user.getEmail())) {
				throw new EmailUsedException();
			}
		});
	}

	public void validateUserRole(String role) {
		if (!(role.equals("PATIENT") || role.equals("LABSTAFF") || role.equals("HOSPITALSTAFF")
				|| role.equals("INSURANCESTAFF") || role.equals("ADMIN") || role.equals("DOCTOR"))) {
			throw new RoleException();
		}
	}

	public User getLoggedUser() {
		String loggedUserName = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			loggedUserName = authentication.getName();
		}
		return userRepository.findByEmail(loggedUserName).orElse(null);
	}
	
	public List<User> findAll() {
        return userRepository.findAll();
    }
	
	public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
	
	public void delete(User userEntity) {
        userRepository.delete(userEntity);
    }
}
