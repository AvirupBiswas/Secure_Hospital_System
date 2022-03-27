package com.asu.project.hospital.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="diagnosis")
public class Diagnosis {
	
	@Id
	@Column(name="diagnosisID")
	private int diagnosisID;

	@ManyToOne
	@JoinColumn(name="patientID", nullable=false)
	private Patient patient;
	
	@OneToOne
	@JoinColumn(name="doctorID", nullable=false)
	private Doctor doctor;
	
	@Column(name="description")
	private String description;
	
	@Column(name="prescription")
	private String prescription;
	
	@Column(name="labtests")
	private String labtests;
	
	public int getDiagnosisID() {
		return diagnosisID;
	}

	public void setDiagnosisID(int diagnosisID) {
		this.diagnosisID = diagnosisID;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	

}
