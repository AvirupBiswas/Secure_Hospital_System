package com.asu.project.hospital.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {

	@Id
	@Column(name = "diagnosisID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int diagnosisID;

	@ManyToOne
	@JoinColumn(name = "patientID", nullable = false)
	private User user;

	@Column(name = "doctorName")
	private String doctorName;

	@Column(name = "problem")
	private String problem;

	@Column(name = "symptoms")
	private String symptoms;

	@Column(name = "labTestNeeded")
	private String labTestNeeded;

	@Column(name = "prescription")
	private String prescription;

	@Column(name = "labtests")
	private String labtests;

	@OneToOne
	@JoinColumn(name = "appId", nullable = false)
	private Appointment appointment;

	public int getDiagnosisID() {
		return diagnosisID;
	}

	public void setDiagnosisID(int diagnosisID) {
		this.diagnosisID = diagnosisID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public String getLabTestNeeded() {
		return labTestNeeded;
	}

	public void setLabTestNeeded(String labTestNeeded) {
		this.labTestNeeded = labTestNeeded;
	}

	public String getPrescription() {
		return prescription;
	}

	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}

	public String getLabtests() {
		return labtests;
	}

	public void setLabtests(String labtests) {
		this.labtests = labtests;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public Diagnosis() {
	}

}
