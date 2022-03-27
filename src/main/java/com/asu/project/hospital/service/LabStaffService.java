package com.asu.project.hospital.service;

import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.repository.LabTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.LabStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.LabStaffRepository;

import java.util.List;

@Service
public class LabStaffService {
	@Autowired
	LabStaffRepository labStaffRepository;

	@Autowired
	LabTestRepository labTestRepository;

	@Autowired
	UserService userService;
	
	public void updateLabStaffInfo(LabStaff labStaff) {
		User user = userService.getLoggedUser();
		labStaff.setUser(user);
		labStaffRepository.save(labStaff);
	}
	public List<LabTest> getAllLabTests() {
		return labTestRepository.findByStatus("Requested");
	}



//	public void getPendingLabRequests(Patient patient) {
//		
//	}
}
