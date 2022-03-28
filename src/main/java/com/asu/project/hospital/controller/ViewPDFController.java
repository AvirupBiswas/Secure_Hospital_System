package com.asu.project.hospital.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asu.project.hospital.entity.Employee;
import com.asu.project.hospital.entity.LabTestReport;
import com.asu.project.hospital.model.PatientLabReport;
import com.asu.project.hospital.repository.EmployeeRepository;
import com.asu.project.hospital.service.LabStaffService;
import com.asu.project.hospital.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/viewPDF")
public class ViewPDFController {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ReportService reportService;

	@Autowired
	private LabStaffService labStaffService;

	@GetMapping(value = "/report")
	public String viewPDF() {
		return "redirect:/otp/generateOtp/viewPDF";
	}

	@GetMapping(value = "/reportView", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateReport() throws FileNotFoundException, JRException {
		List<Employee> employees = repository.findAll();
		byte data[] = reportService.exportReport(employees, "employees.jrxml");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(data);
	}

	@GetMapping(value = "/labstaff/reportView/{labTestReportId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateLabTestReport(@PathVariable("labTestReportId") String labTestReportId)
			throws FileNotFoundException, JRException {
		LabTestReport labTestReport = labStaffService.getLabTestReport(Integer.parseInt(labTestReportId));
		List<PatientLabReport> PatientLabReports = new ArrayList<>();
		PatientLabReport patientLabReport = new PatientLabReport();
		patientLabReport.setPatientName(labTestReport.getLabTest().getUser().getFirstName() + " "
				+ labTestReport.getLabTest().getUser().getLastName());
		patientLabReport.setPrice(labTestReport.getLabTest().getPrice());
		patientLabReport.setTestName(labTestReport.getTestName());
		patientLabReport.setTestResult(labTestReport.getTestResult());
		PatientLabReports.add(patientLabReport);
		byte data[] = reportService.exportReport(PatientLabReports, "patientLabTestReport.jrxml");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(data);
	}
}
