package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.InsuranceStaff;
import com.asu.project.hospital.entity.User;

public interface InsuranceStaffRepository extends JpaRepository<InsuranceStaff, Long> {

	InsuranceStaff findByUser(User user);

}