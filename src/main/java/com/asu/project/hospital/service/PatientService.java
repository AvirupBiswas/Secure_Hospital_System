package com.asu.project.hospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.Appointment;
import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.AppointmentRepository;
import com.asu.project.hospital.repository.InsuranceClaimsRepository;
import com.asu.project.hospital.repository.InsuranceDetailsRepository;
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
	AppointmentRepository appointmentRepository;
	
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

}
