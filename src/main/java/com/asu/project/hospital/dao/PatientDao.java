package com.asu.project.hospital.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.asu.project.hospital.entity.PatientInformation;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.PatientRepository;
import com.asu.project.hospital.repository.UserRepository;

@Repository("patient")
public class PatientDao implements PatientDaoInterface<PatientInformation, String> {
	
	@Autowired
	PatientRepository patientRepository;

	@Override
	public void persist(PatientInformation userEntity) {
		patientRepository.save(userEntity);
	}

}
