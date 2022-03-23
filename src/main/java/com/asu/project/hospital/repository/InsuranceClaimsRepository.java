package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.InsuranceClaims;

public interface InsuranceClaimsRepository extends JpaRepository<InsuranceClaims, Long>{

}
