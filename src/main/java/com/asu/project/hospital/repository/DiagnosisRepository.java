package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.Diagnosis;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {

}
