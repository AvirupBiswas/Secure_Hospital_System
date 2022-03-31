package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.InsuranceStaff;

public interface InsuranceRepository extends JpaRepository<InsuranceStaff, String>{

}
