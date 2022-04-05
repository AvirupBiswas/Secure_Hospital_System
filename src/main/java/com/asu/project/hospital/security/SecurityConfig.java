package com.asu.project.hospital.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Qualifier("hospitalUserDetailsService")
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/viewPDF/**").hasAnyAuthority("ADMIN", "HOSPITALSTAFF","PATIENT","LABSTAFF","DOCTOR","INSURANCESTAFF")
				.antMatchers("/admin/**").hasAuthority("ADMIN").antMatchers("/patient/**").hasAuthority("PATIENT").antMatchers("/hospitalstaff/**").hasAuthority("HOSPITALSTAFF")
				.antMatchers("/labstaff/**").hasAuthority("LABSTAFF")
				.antMatchers("/doctor/**").hasAuthority("DOCTOR")
				.antMatchers("/insurancestaff/**").hasAuthority("INSURANCESTAFF")
				.antMatchers("/").permitAll().and().formLogin().loginPage("/login")
				.failureHandler(handleAuthenticationFailure()).successHandler(myAuthenticationSuccessHandler())
				.permitAll().and()
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login")
						.logoutSuccessHandler(logoutSuccessHandler()).invalidateHttpSession(true))
				.exceptionHandling().and().sessionManagement().invalidSessionUrl("/login?invalid_session=true").and().csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().headers().frameOptions().and()
				.defaultsDisabled().xssProtection();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new UrlAuthenticationSuccessHandler();
	}
	
	@Bean
	public AuthenticationFailureHandler handleAuthenticationFailure() {
		return new CustomLoginFailureHandler();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}
}