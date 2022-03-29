package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.Patient;
import com.asu.project.hospital.entity.User;

public interface PatientRepository extends JpaRepository<Patient, String>{

	Patient findByUser(User user);
}
