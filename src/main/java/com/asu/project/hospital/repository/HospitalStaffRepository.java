package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.HospitalStaff;
import com.asu.project.hospital.entity.User;

public interface HospitalStaffRepository extends JpaRepository<HospitalStaff, String>{

	HospitalStaff findByUser(User user);
}
