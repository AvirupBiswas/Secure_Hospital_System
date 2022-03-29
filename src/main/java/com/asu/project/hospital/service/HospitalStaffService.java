package com.asu.project.hospital.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.HospitalStaff;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.HospitalStaffRepository;
import com.asu.project.hospital.repository.PatientRepository;
import com.asu.project.hospital.repository.UserRepository;

@Service
public class HospitalStaffService {
	
	@Autowired
	HospitalStaffRepository hospitalStaffRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	
	public void updateHospitalStaffInfo(HospitalStaff hospitalStaff) {
		User user= userService.getLoggedUser();
		hospitalStaff.setUser(user);
		hospitalStaffRepository.save(hospitalStaff);
	}
	
	public List<User> getAllPatients(){
		List <User> users = userRepository.findAll().stream().filter(e->e.getRole().equals("PATIENT")).collect(Collectors.toList());
		return users;
	}
	
}
