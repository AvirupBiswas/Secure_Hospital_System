package com.asu.project.hospital.repository;

import com.asu.project.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.LabStaff;

public interface LabStaffRepository extends JpaRepository<LabStaff, String>{
    LabStaff findByUser(User user);
}