package com.asu.project.hospital.security;


import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.asu.project.hospital.entity.User;

public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserServiceToCheckFailedLogin userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String email = request.getParameter("username");
		Optional<User> userByEmail = userService.getByEmail(email);

		if (userByEmail.isPresent()) {
			User user = userByEmail.get();
			if (user.isActive() && !user.isAccountLocked()) {
				if (user.getFailedAttempt() < UserServiceToCheckFailedLogin.MAX_FAILED_ATTEMPTS - 1) {
					userService.increaseFailedAttempts(user);
				} else {
					userService.lock(user);
					exception = new LockedException("Your account has been locked due to 3 failed attempts."
							+ " It will be unlocked after 1 minutes.");
				}
			} else if (user.isAccountLocked()) {
				if (userService.unlockWhenTimeExpired(user)) {
					exception = new LockedException("Your account has been unlocked. Please try to login again.");
				}
			}

		}

		super.setDefaultFailureUrl("/login?error");
		super.onAuthenticationFailure(request, response, exception);

	}
}