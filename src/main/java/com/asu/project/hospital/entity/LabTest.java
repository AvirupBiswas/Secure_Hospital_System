package com.asu.project.hospital.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="LabTest")
public class LabTest {
	
	@Column(name="testId", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int labTestId;
	
	@Column(name="testName")
	private String testName;
	
	@Column(name="description")
	private String description;
	
	@Column(name="status")
	private String status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userId")
	private User user;
	
	@Column(name="price")
	private BigDecimal price;
	
	@OneToOne
	@JoinColumn(name="diagnosisId")
	private Diagnosis diagnosis;
	
	

	public Diagnosis getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(Diagnosis diagnosis) {
		this.diagnosis = diagnosis;
	}

	public int getLabTestId() {
		return labTestId;
	}

	public void setLabTestId(int labTestId) {
		this.labTestId = labTestId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public LabTest(int labTestId, String testName, String description, String status, User user, BigDecimal price) {
		this.labTestId = labTestId;
		this.testName = testName;
		this.description = description;
		this.status = status;
		this.user = user;
		this.price = price;
	}

	public LabTest() {
	}
	
	

	
}
