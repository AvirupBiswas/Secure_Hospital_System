package com.asu.project.hospital.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.InsuranceStaff;
import com.asu.project.hospital.entity.User;
import com.asu.project.hospital.repository.InsuranceClaimsRepository;
import com.asu.project.hospital.repository.InsuranceDetailsRepository;
import com.asu.project.hospital.repository.InsuranceStaffRepository;

@Service
public class InsuranceStaffService {

	@Autowired
	private InsuranceStaffRepository insuranceStaffRepository;

	@Autowired
	UserService userService;

	@Autowired
	private InsuranceDetailsRepository insuranceDetailsRepository;

	@Autowired
	private InsuranceClaimsRepository insuranceClaimsRepository;

	public InsuranceStaff getInsuranceStaff(User user) {
		InsuranceStaff InsuranceStaff = insuranceStaffRepository.findByUser(user);
		return InsuranceStaff;
	}

	public void updateInsuranceStaffInfo(InsuranceStaff insuranceStaff) {
		User user = userService.getLoggedUser();
		insuranceStaff.setUser(user);
		insuranceStaffRepository.save(insuranceStaff);
	}

	public void addInsuranceDetails(InsuranceDetails insuranceDetails) {
		insuranceDetailsRepository.save(insuranceDetails);
	}

	public List<InsuranceClaims> getInsuranceClaimByStatus(String status) {
		return insuranceClaimsRepository.findByStatus(status);
	}

	public User updateClaimStatus(String status, long calimId) {
		Optional<InsuranceClaims> claim = insuranceClaimsRepository.findById(calimId);
		if (claim.isPresent()) {
			InsuranceClaims claimObj = claim.get();
			claimObj.setStatus(status);
			insuranceClaimsRepository.save(claimObj);
			return claimObj.getUser();
		}
		return null;
	}

}