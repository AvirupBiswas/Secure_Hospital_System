package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}