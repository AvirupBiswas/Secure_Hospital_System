package com.asu.project.hospital.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.Appointment;
import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.PatientPayment;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.AppointmentRepository;
import com.asu.project.hospital.repository.InsuranceClaimsRepository;
import com.asu.project.hospital.repository.InsuranceDetailsRepository;
import com.asu.project.hospital.repository.LabTestRepository;
import com.asu.project.hospital.repository.PatientPaymentRepository;
import com.asu.project.hospital.repository.PatientRepository;

@Service
public class PatientService {
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	InsuranceDetailsRepository insuranceDetailsRepository;
	
	@Autowired
	InsuranceClaimsRepository insuranceClaimRepository;
	
	@Autowired
	PatientPaymentRepository patientPaymentRepository;
	
	@Autowired
	AppointmentRepository appointmentRepository;
	
	@Autowired
	LabTestRepository labTestRepository;
	
	@Autowired
	UserService userService;
	
	public void updatePatientInfo(Patient patient) {
		User user= userService.getLoggedUser();
		patient.setUser(user);
		patientRepository.save(patient);
	}
	
	public void addInsuranceDetails(InsuranceDetails insuranceDetails) {
		User user= userService.getLoggedUser();
		insuranceDetails.setUser(user);
		insuranceDetailsRepository.save(insuranceDetails);
	}
	
	public void editInsuranceDetails(InsuranceDetails insuranceDetails) {
		User user= userService.getLoggedUser();
		InsuranceDetails details=insuranceDetailsRepository.findByUser(user);
		details.setInsuranceId(insuranceDetails.getInsuranceId());
		details.setInsuranceName(insuranceDetails.getInsuranceName());
		details.setProvider(insuranceDetails.provider);
		insuranceDetailsRepository.save(details);
	}
	
	public void addInsuranceClaimRequest(InsuranceClaims claim) {
		User user= userService.getLoggedUser();
		InsuranceDetails details=insuranceDetailsRepository.findByUser(user);
		claim.setInsuranceDetails(details);
		claim.setUser(user);
		insuranceClaimRepository.save(claim);
	}
	
	public InsuranceDetails getInsuranceDetails(User user) {
		return insuranceDetailsRepository.findByUser(user);
	}
	
	public List<InsuranceClaims> findAllClaims(User user){
		return insuranceClaimRepository.findByUser(user);
	}
	
	public List<Appointment> findAllAppointments(User user){
		return appointmentRepository.findByUser(user);
	}
	
	public List<PatientPayment> findAllPaymentsByStatus(){
		User user=userService.getLoggedUser();
		List<PatientPayment> patientPayments=patientPaymentRepository.findByUser(user)
				.stream().filter(e -> e.getStatus().equals("Pending"))
				.collect(Collectors.toList());
		return  patientPayments;
	}
	
	public void createLabRequest(LabTest labTest) {
		User user=userService.getLoggedUser();
		labTest.setUser(user);
		labTest.setStatus("Requested");
		labTest.setPrice(new BigDecimal(100));
		labTestRepository.save(labTest);
	}

}
