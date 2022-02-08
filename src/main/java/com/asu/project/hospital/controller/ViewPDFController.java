package com.asu.project.hospital.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asu.project.hospital.entity.Employee;
import com.asu.project.hospital.repository.EmployeeRepository;
import com.asu.project.hospital.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/viewPDF")
public class ViewPDFController {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ReportService reportService;

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
}
