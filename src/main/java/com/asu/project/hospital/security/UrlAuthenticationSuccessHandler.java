package com.asu.project.hospital.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.model.LoginSingleTon;
import com.asu.project.hospital.repository.UserRepository;

public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private LoginSingleTon loginSingleTon;

	@Autowired
	private UserServiceToCheckFailedLogin userServiceToCheckFailedLogin;

	@Autowired
	private UserRepository userRepository;

	protected Log logger = LogFactory.getLog(this.getClass());

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {

		User user = userRepository.findByEmail(authentication.getName()).orElse(null);
		if (!user.getActive()) {
			CustomLoginFailureHandler obj = new CustomLoginFailureHandler();
			obj.doNotProceed(httpServletRequest, httpServletResponse, new LockedException("Bad credentials"));
		}
		else {
		if (user.getFailedAttempt() > 0) {
			userServiceToCheckFailedLogin.resetFailedAttempts(user.getEmail());
		}
		handle(httpServletRequest, httpServletResponse, authentication);
		clearAuthenticationAttributes(httpServletRequest);
		}
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {
			logger.debug("Response is already committed. Unable to redirect to" + targetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(final Authentication authentication) {

		loginSingleTon.setTimestamp(new Date());
		Map<String, String> roleTargetUrlMap = new HashMap<>();
		roleTargetUrlMap.put("ADMIN", "/admin/home");
		roleTargetUrlMap.put("PATIENT", "/patient/home");
		roleTargetUrlMap.put("HOSPITALSTAFF", "/hospitalstaff/home");
		roleTargetUrlMap.put("LABSTAFF", "/labstaff/home");

		final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		for (final GrantedAuthority grantedAuthority : authorities) {
			String authorityName = grantedAuthority.getAuthority();
			if (roleTargetUrlMap.containsKey(authorityName)) {
				return roleTargetUrlMap.get(authorityName);
			}
		}
		throw new IllegalStateException();
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession httpSession = request.getSession(false);
		if (httpSession == null) {
			return;
		}
		httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}