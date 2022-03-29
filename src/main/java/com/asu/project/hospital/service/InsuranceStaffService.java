package com.asu.project.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.InsuranceStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.InsuranceRepository;

@Service
public class InsuranceStaffService {
	
	@Autowired
	InsuranceRepository insuranceStaffRepository;
	
	@Autowired
	UserService userService;
	
	public void updateInsuranceStaffInfo(InsuranceStaff insuranceStaff) {
		User user= userService.getLoggedUser();
		insuranceStaff.setUser(user);
		insuranceStaffRepository.save(insuranceStaff);
	}
}

