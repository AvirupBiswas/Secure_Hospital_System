package com.asu.project.hospital.security;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.asu.project.hospital.entity.SignInHistory;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.model.LoginSingleTon;
import com.asu.project.hospital.repository.SignInHistoryRepository;
import com.asu.project.hospital.repository.UserRepository;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	private LoginSingleTon loginSingleTon;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SignInHistoryRepository signInHistoryRepository;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if(authentication!=null) {
		Optional<User> user = userRepository.findOneByEmailIgnoreCase(authentication.getName());
		if (user.isPresent()) {
			SignInHistory signInHistory = new SignInHistory();
			User userObj = user.get();
			signInHistory.setFirstName(userObj.getFirstName());
			signInHistory.setLastName(userObj.getLastName());
			signInHistory.setEmail(userObj.getEmail());
			signInHistory.setRole(userObj.getRole());
			signInHistory.setLoginTimeStamp(loginSingleTon.getTimestamp());
			signInHistory.setLogoutTimeStamp(new Date());
			signInHistoryRepository.save(signInHistory);
		}
		}
		super.onLogoutSuccess(request, response, authentication);
	}
}
