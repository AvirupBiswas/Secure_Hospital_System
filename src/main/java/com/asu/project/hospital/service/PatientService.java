package com.asu.project.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.dao.PatientDao;
import com.asu.project.hospital.entity.PatientInformation;
import com.asu.project.hospital.entity.User;
@Service
public class PatientService {
	
	private final PatientDao patientDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	public PatientService(@Qualifier("patient") PatientDao patientDao) {
		this.patientDao=patientDao;
	}
	
	public PatientInformation addPatientInformation(PatientInformation patientInformation) {
		User user=userService.getLoggedUser();
		patientInformation.setUser(user);
		patientDao.persist(patientInformation);
		return patientInformation;
	}
	

}
