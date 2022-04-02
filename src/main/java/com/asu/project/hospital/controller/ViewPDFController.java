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

import com.asu.project.hospital.entity.Diagnosis;
import com.asu.project.hospital.entity.LabTestReport;
import com.asu.project.hospital.model.PatientDiagnosisReport;
import com.asu.project.hospital.model.PatientLabReport;
import com.asu.project.hospital.repository.DiagnosisRepository;
import com.asu.project.hospital.service.LabStaffService;
import com.asu.project.hospital.service.PatientService;
import com.asu.project.hospital.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/viewPDF")
public class ViewPDFController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private LabStaffService labStaffService;
	
	@Autowired
	private DiagnosisRepository diagnosisRepository;
	
	
	
	
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
	
	@GetMapping(value = "/doctor/reportView/{labTestId}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateLabTestReportForDoctor(@PathVariable("labTestId") String labTestId)
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
	
	@GetMapping(value = "/patient/diagnosisreport/{diagnosisID}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateDiagnosisReportForPatient(@PathVariable("diagnosisID") String diagnosisID)
			throws FileNotFoundException, JRException {
		Diagnosis diagnosis=diagnosisRepository.getById(Integer.parseInt(diagnosisID));
		List<PatientDiagnosisReport> diagnosisReports = new ArrayList<>();
		PatientDiagnosisReport patientDiagnosisReport = new PatientDiagnosisReport();
		patientDiagnosisReport.setPatientName(diagnosis.getUser().getFirstName() + " "
				+ diagnosis.getUser().getLastName());
		patientDiagnosisReport.setDoctorName(diagnosis.getDoctorName());
		patientDiagnosisReport.setLabtests(diagnosis.getLabtests());
		patientDiagnosisReport.setPrescription(diagnosis.getPrescription());
		patientDiagnosisReport.setSymptoms(diagnosis.getSymptoms());
		patientDiagnosisReport.setProblem(diagnosis.getProblem());
		diagnosisReports.add(patientDiagnosisReport);
		byte data[] = reportService.exportReport(diagnosisReports, "viewalldiagnosis.jrxml");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(data);
	}
	
	@GetMapping(value = "/hospitalStaff/diagnosisreport/{diagnosisID}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generateDiagnosisReportForHospitalStaff(@PathVariable("diagnosisID") String diagnosisID)
			throws FileNotFoundException, JRException {
		Diagnosis diagnosis=diagnosisRepository.getById(Integer.parseInt(diagnosisID));
		List<PatientDiagnosisReport> diagnosisReports = new ArrayList<>();
		PatientDiagnosisReport patientDiagnosisReport = new PatientDiagnosisReport();
		patientDiagnosisReport.setPatientName(diagnosis.getUser().getFirstName() + " "
				+ diagnosis.getUser().getLastName());
		patientDiagnosisReport.setDoctorName(diagnosis.getDoctorName());
		patientDiagnosisReport.setLabtests(diagnosis.getLabtests());
		patientDiagnosisReport.setPrescription(diagnosis.getPrescription());
		patientDiagnosisReport.setSymptoms(diagnosis.getSymptoms());
		patientDiagnosisReport.setProblem(diagnosis.getProblem());
		diagnosisReports.add(patientDiagnosisReport);
		byte data[] = reportService.exportReport(diagnosisReports, "viewalldiagnosis.jrxml");
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(data);
	}
}
