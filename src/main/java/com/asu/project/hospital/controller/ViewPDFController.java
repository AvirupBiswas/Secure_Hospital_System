package com.asu.project.hospital.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asu.project.hospital.entity.LabTestReport;
import com.asu.project.hospital.model.PatientLabReport;
import com.asu.project.hospital.service.LabStaffService;
import com.asu.project.hospital.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/viewPDF")
public class ViewPDFController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private LabStaffService labStaffService;
	
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
	
	@GetMapping(value = "/patient/reportView/{labTestId}")
	public String protectPatientLabReportByOTP(@PathVariable("labTestId") String labTestId) {
		return "redirect:/otp/generateOtp/viewPatientLabReport?labTestId="+labTestId;
	}
	
	@GetMapping(value = "/patient/reportViewAfterOTPValidation/{labTestId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateLabTestReportForPatient(@PathVariable("labTestId") String labTestId)
			throws FileNotFoundException, JRException {
		int labTestReportId = labStaffService.getLabTestReportId(Integer.parseInt(labTestId));
		LabTestReport labTestReport = labStaffService.getLabTestReport(labTestReportId);
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
	
	@GetMapping(value = "/hospitalStaff/reportView/{labTestId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateLabTestReportForHospitalStaff(@PathVariable("labTestId") String labTestId)
			throws FileNotFoundException, JRException {
		int labTestReportId = labStaffService.getLabTestReportId(Integer.parseInt(labTestId));
		LabTestReport labTestReport = labStaffService.getLabTestReport(labTestReportId);
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
