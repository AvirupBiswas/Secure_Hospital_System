package com.asu.project.hospital.model;

public class PatientDiagnosisReport {
	
	private String patientName;
	
	private String doctorName;
	
	private String problem;
	
	private String prescription;
	
	private String symptoms;
	
	private String labtests;

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getPrescription() {
		return prescription;
	}

	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public String getLabtests() {
		return labtests;
	}

	public void setLabtests(String labtests) {
		this.labtests = labtests;
	}

	public PatientDiagnosisReport(String patientName, String doctorName, String problem, String prescription,
			String symptoms, String labtests) {
		this.patientName = patientName;
		this.doctorName = doctorName;
		this.problem = problem;
		this.prescription = prescription;
		this.symptoms = symptoms;
		this.labtests = labtests;
	}

	public PatientDiagnosisReport() {
	}
	
	

}
