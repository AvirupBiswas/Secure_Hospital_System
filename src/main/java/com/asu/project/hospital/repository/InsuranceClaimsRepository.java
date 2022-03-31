package com.asu.project.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.InsuranceClaims;
import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.User;

public interface InsuranceClaimsRepository extends JpaRepository<InsuranceClaims, Long> {

	List<InsuranceClaims> findByUser(User user);

	List<InsuranceClaims> findByStatus(String status);
	
}
