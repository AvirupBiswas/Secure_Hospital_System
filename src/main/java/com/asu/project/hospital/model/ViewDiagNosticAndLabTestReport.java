package com.asu.project.hospital.model;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class ViewDiagNosticAndLabTestReport {

	private String firstName;

	private String lastName;

	private String email;

	private String description;

	private String doctorName;

	private String testNameRecommendedByDoctor;

	private String testNameReported;

	private Integer labTestReportId;
	
	private Integer labTestId;

	private Integer diagnosisID;

	private Date startTime;

	private Date endTime;

	public String getTestNameReported() {
		return testNameReported;
	}

	public void setTestNameReported(String testNameReported) {
		this.testNameReported = testNameReported;
	}

	public String getTestNameRecommendedByDoctor() {
		return testNameRecommendedByDoctor;
	}

	public void setTestNameRecommendedByDoctor(String testNameRecommendedByDoctor) {
		this.testNameRecommendedByDoctor = testNameRecommendedByDoctor;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getLabTestReportId() {
		return labTestReportId;
	}

	public void setLabTestReportId(Integer labTestReportId) {
		this.labTestReportId = labTestReportId;
	}

	public Integer getDiagnosisID() {
		return diagnosisID;
	}

	public void setDiagnosisID(Integer diagnosisID) {
		this.diagnosisID = diagnosisID;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getLabTestId() {
		return labTestId;
	}

	public void setLabTestId(Integer labTestId) {
		this.labTestId = labTestId;
	}
}
