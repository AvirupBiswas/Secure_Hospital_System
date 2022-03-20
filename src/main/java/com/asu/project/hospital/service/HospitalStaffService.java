package com.asu.project.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.HospitalStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.HospitalStaffRepository;

@Service
public class HospitalStaffService {
	
	@Autowired
	HospitalStaffRepository hospitalStaffRepository;
	
	@Autowired
	UserService userService;
	
	public void updateHospitalStaffInfo(HospitalStaff hospitalStaff) {
		User user= userService.getLoggedUser();
		hospitalStaff.setUser(user);
		hospitalStaffRepository.save(hospitalStaff);
	}
}
