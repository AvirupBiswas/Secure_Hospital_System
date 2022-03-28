package com.asu.project.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.User;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {
	
	List<Diagnosis> findByUser(User user);
	
	Diagnosis findByDiagnosisID(int diagnosisID);

}
