package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

}
