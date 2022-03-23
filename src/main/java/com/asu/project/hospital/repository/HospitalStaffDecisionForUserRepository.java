package com.asu.project.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.AdminDecisionForUser;
import com.asu.project.hospital.entity.Appointment;

public interface HospitalStaffDecisionForUserRepository extends JpaRepository<Appointment, String> {
	
	List<Appointment> findByStatus(String status);

	Optional<Appointment> findOneByStatusIgnoreCase(String username);
	
	Appointment findByAppId(Long id);
	
	Appointment deleteByAppId(Long id);
}