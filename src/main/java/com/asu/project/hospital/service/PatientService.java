package com.asu.project.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.PatientRepository;

@Service
public class PatientService {
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	UserService userService;
	
	public void updatePatientInfo(Patient patient) {
		User user= userService.getLoggedUser();
		patient.setUser(user);
		patientRepository.save(patient);
	}

}
