package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.User;

import java.util.List;

public interface LabTestRepository extends JpaRepository<LabTest, Integer>{
    public List<LabTest> findByStatus(String status);
    
}