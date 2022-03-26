package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.Doctor;
import com.asu.project.hospital.entity.User;

public interface DoctorRepository extends JpaRepository<Doctor, String>{
	
	Doctor findByUser(User user);

}
