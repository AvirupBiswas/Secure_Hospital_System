package com.asu.project.hospital.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.PatientInformation;
import com.asu.project.hospital.entity.User;

public interface PatientRepository extends JpaRepository<PatientInformation, Integer>{

}
