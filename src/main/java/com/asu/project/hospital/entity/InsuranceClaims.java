package com.asu.project.hospital.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
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
@Table(name="insuranceclaims")
public class InsuranceClaims {
	
	@Column(name="claimId", nullable=false)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long claimId;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="purpose")
	private String purpose;
	

	@JoinColumn(name="insuranceId")
	@ManyToOne(fetch = FetchType.LAZY)
	private InsuranceDetails insuranceDetails;
	
	@JoinColumn(name="userId")
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
	
	@Column(name="status")
	private String status;
	
	@OneToOne
	@JoinColumn(name="paymentId")
	private PatientPayment patientPayment;

	public long getClaimId() {
		return claimId;
	}

	public void setClaimId(long claimId) {
		this.claimId = claimId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public InsuranceDetails getInsuranceDetails() {
		return insuranceDetails;
	}

	public void setInsuranceDetails(InsuranceDetails insuranceDetails) {
		this.insuranceDetails = insuranceDetails;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	public PatientPayment getPatientPayment() {
		return patientPayment;
	}

	public void setPatientPayment(PatientPayment patientPayment) {
		this.patientPayment = patientPayment;
	}

	public InsuranceClaims(long claimId, BigDecimal amount, String purpose, InsuranceDetails insuranceDetails,
			User user, String status) {
		this.claimId = claimId;
		this.amount = amount;
		this.purpose = purpose;
		this.insuranceDetails = insuranceDetails;
		this.user = user;
		this.status = status;
	}

	public InsuranceClaims() {
	}
	
	

}
