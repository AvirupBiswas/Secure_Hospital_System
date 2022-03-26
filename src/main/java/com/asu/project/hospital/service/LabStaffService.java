package com.asu.project.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.LabStaff;
import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.LabStaffRepository;

@Service
public class LabStaffService {
	@Autowired
	LabStaffRepository labStaffRepository;
	
	@Autowired
	UserService userService;
	
	public void updateLabStaffInfo(LabStaff labStaff) {
		User user = userService.getLoggedUser();
		labStaff.setUser(user);
		labStaffRepository.save(labStaff);
	}
	
//	public void getPendingLabRequests(Patient patient) {
//		
//	}
}
