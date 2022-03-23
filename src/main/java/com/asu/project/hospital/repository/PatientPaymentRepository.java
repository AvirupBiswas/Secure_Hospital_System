package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.PatientPayment;

public interface PatientPaymentRepository extends JpaRepository<PatientPayment, String>{

}