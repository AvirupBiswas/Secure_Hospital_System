package com.asu.project.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.InsuranceStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.InsuranceStaffRepository;

@Service
public class InsuranceStaffService {

	@Autowired
	private InsuranceStaffRepository insuranceStaffRepository;
	
	@Autowired
	UserService userService;
	
	

	public InsuranceStaff getInsuranceStaff(User user) {
		InsuranceStaff InsuranceStaff = insuranceStaffRepository.findByUser(user);
		return InsuranceStaff;
	}
	
	public void updateInsuranceStaffInfo(InsuranceStaff insuranceStaff) {
		User user = userService.getLoggedUser();
		insuranceStaff.setUser(user);
		insuranceStaffRepository.save(insuranceStaff);
	}

}