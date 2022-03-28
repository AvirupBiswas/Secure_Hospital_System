package com.asu.project.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asu.project.hospital.entity.LabTest;
import com.asu.project.hospital.entity.LabTestReport;

public interface LabTestReportRepository extends JpaRepository<LabTestReport, Integer>{

}
