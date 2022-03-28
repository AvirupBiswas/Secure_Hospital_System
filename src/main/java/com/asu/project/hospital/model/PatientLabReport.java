package com.asu.project.hospital.model;

import java.math.BigDecimal;

public class PatientLabReport {

	private String patientName;

	private String testName;

	private String testResult;

	private BigDecimal price;

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	private PatientLabReport(String patientName, String testName, String testResult, BigDecimal price) {
		super();
		this.patientName = patientName;
		this.testName = testName;
		this.testResult = testResult;
		this.price = price;
	}

	public PatientLabReport() {
		super();
	}
	
}
