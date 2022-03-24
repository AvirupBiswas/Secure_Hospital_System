package com.asu.project.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.Appointment;
import com.asu.project.hospital.entity.User;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	
	List<Appointment> findByUser(User user);

}
