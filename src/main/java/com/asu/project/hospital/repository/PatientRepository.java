package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, String>{

}
