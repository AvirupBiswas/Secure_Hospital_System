package com.asu.project.hospital.service;

import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.repository.LabTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.LabStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.LabStaffRepository;

import java.util.List;
import java.util.Optional;

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
	public List<LabTest> getLabTestsByStatus(String status) {
		return labTestRepository.findByStatus(status);
	}

	public User updateLabTestStatus(String status, Integer labTestId ){
		Optional<LabTest> labTestObj = labTestRepository.findById(labTestId);
		if (labTestObj.isPresent()){
			labTestObj.get().setStatus(status);
			labTestRepository.save(labTestObj.get());
			return labTestObj.get().getUser();
		}
		return null;
	}

	public LabTest getLabTest(Integer labTestId){
		Optional<LabTest> labTestObj = labTestRepository.findById(labTestId);
		if (labTestObj.isPresent()){
			return labTestObj.get();
		}
		return null;
	}


//	public void getPendingLabRequests(Patient patient) {
//		
//	}
}
