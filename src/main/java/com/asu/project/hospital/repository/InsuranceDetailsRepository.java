package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.InsuranceDetails;
import com.asu.project.hospital.entity.User;

public interface InsuranceDetailsRepository extends JpaRepository<InsuranceDetails, String>{

	InsuranceDetails findByUser(User user);
}
