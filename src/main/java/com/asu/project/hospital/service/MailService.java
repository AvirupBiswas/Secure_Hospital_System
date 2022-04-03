package com.asu.project.hospital.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Value("${mail.username}")
	String fromMail;

	@Value("${mail.host}")
	String mailHost;

	@Value("${mail.username}")
	String mailUsername;

	@Value("${mail.password}")
	String mailPwd;

	@Value("${mail.port}")
	String mailPort;

	@Async
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
		MimeMessage mimeMessage = getJavaMailSender().createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
			message.setTo(to);
			message.setFrom(fromMail);
			message.setSubject(subject);
			message.setText(content, isHtml);
			getJavaMailSender().send(mimeMessage);

		} catch (MailException | MessagingException e) {
			System.out.println("Email could not be sent to user '{}'" + to + e);
		}
	}

	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailHost);
		mailSender.setPort(Integer.parseInt(mailPort));

		mailSender.setUsername(mailUsername);
		mailSender.setPassword(mailPwd);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

	@Async
	public void sendOTPMail(String email, String otp) {
		sendEmail(email, "OTP for the transaction:", "<html><body>" + otp + "</body></html>", false, true);
	}
	
	@Async
	public void sendUserRegistrationAcceptanceMail(String email,String firstName,String lastName) {
		sendEmail(email, "Registration approval", "<html><body>" + "Congraluation "+firstName+" "+lastName+ ".Your registration has been approved by admin.You can now login to Secure Hospital System with your email and password." + "</body></html>", false, true);
	}
	
	@Async
	public void sendUserRegistrationDenialMail(String email,String firstName,String lastName) {
		sendEmail(email, "Registration rejection", "<html><body>" + "Sorry "+firstName+" "+lastName+ ".Your registration has been declined by admin.Please try again." + "</body></html>", false, true);
	}
	
	@Async
	public void sendUserAppointmentAcceptanceMail(String email,String firstName,Date startTime) {
		sendEmail(email, "Appointment approval", "<html><body>" + "Congraluation "+firstName+ ".Your Appointment has been approved by admin.You can visit Doctor at "+startTime + "</body></html>", false, true);
	}
	
	@Async
	public void sendUserAppointmentDenialMail(String email,String firstName,Date startTime) {
		sendEmail(email, "Appointment rejection", "<html><body>" + "Congraluation "+firstName+ ".Your Appointment has been declined by admin. Please try again."+"</body></html>", false, true);
	}

	// For lab test approval
	@Async
	public void sendLabTestApprovalMail(String email,String firstName,String lastName, String testName) {
		sendEmail(email, "Lab test request for " + testName + " approved!", "<html><body>" + "Congraluation "+firstName+" "+lastName+ ".Your lab test "+ testName +" request has been approved by lab staff." + "</body></html>", false, true);
	}

	@Async
	public void sendLabTestDenyMail(String email,String firstName,String lastName, String testName) {
		sendEmail(email, "Lab test request for " + testName + " has been denied!", "<html><body>" + "Sorry "+firstName+" "+lastName+ ".Your lab test " + testName +" request has been denied by lab staff." + "</body></html>", false, true);
	}
	
	// For Insurance Claim 
	@Async
	public void sendInsuranceClaimApprovalMail(String email,String firstName,String lastName, String calimId) {
		sendEmail(email, "Insurance Claim request for claim id " + calimId + " has been approved!", "<html><body>" + "Congraluation "+firstName+" "+lastName+ ".Your claim id "+ calimId +" has been approved by Insurance staff." + "</body></html>", false, true);
	}

	@Async
	public void sendInsuranceClaimDenyMail(String email,String firstName,String lastName, String calimId) {
		sendEmail(email, "Insurance Claim request for claim id " + calimId + " has been denied!", "<html><body>" + "Sorry "+firstName+" "+lastName+ ".Your claim id "+ calimId +" has been denied by Insurance staff." + "</body></html>", false, true);
	
	}
	
	@Async
	public void sendClaimAsNoInsuranceDenyMail(String email,String firstName,String lastName, String calimId) {
		sendEmail(email, "Insurance Claim request for claim id " + calimId + " has been denied!", "<html><body>" + "Sorry "+firstName+" "+lastName+ ".Your claim id "+ calimId +" has been denied by Insurance Staff as there is no insurance id present in the claim." + "</body></html>", false, true);
	
	}
	
	@Async
	public void sendInsuranceClaimAmountDisburseMail(String email, String firstName, String lastName, String claimId,
			BigDecimal amount) {
		sendEmail(email, "Insurance Claim amount "+amount+ " for claim id " + claimId + " has been disbursed!", "<html><body>" + "Congraluation "+firstName+" "+lastName+ ".Your claim amount "+ amount + "corresponding to claim id "+claimId+" has been disbursed by Insurance staff." + "</body></html>", false, true);
	}
}
