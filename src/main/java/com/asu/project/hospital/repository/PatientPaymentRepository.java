package com.asu.project.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.PatientPayment;
import com.asu.project.hospital.entity.User;

public interface PatientPaymentRepository extends JpaRepository<PatientPayment, Long>{

	List<PatientPayment> findByUser(User user);
	List<PatientPayment> findByStatus(String status);
}