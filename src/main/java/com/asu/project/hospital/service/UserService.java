package com.asu.project.hospital.service;

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
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository userRepository;
	
	public User registerUser(User user){
        return registerUser(user, user.getRole());
    }
	
	public User registerUser(User user, String role) {
		validateUser(user);
		validateUserRole(role);
		User u = new User();
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setEmail(user.getEmail());
		u.setRole(role);
//        u.setPassword(user.getPassword());
		u.setPassword(passwordEncoder.encode(user.getPassword()));
		u.setActive(true);
		userRepository.save(u);
		return u;
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	  public void validateUser(User user){
	        userRepository.findOneByEmailIgnoreCase(user.getEmail()).
	            ifPresent(existing ->{
	                if(existing.getEmail().equalsIgnoreCase(user.getEmail())){
	                    throw new EmailUsedException();
	                } 
	            });
	    }

	    public void validateUserRole(String role){
	        if(!(role.equals("PATIENT") || role.equals("LABSTAFF") || role.equals("HOSPITALSTAFF") || role.equals("INSURANCESTAFF") || role.equals("ADMIN") || role.equals("DOCTOR"))){
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
}
