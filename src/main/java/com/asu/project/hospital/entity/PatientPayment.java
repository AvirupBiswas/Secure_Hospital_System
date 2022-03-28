package com.asu.project.hospital.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="patient_payment")
public class PatientPayment {
	
	@Id
    @Column(name = "paymentID",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long paymentID;
	
	@ManyToOne
	@JoinColumn(name="userId", nullable=false)
	private User user;

	@Column(name="status")
	private String status;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="purpose")
	private String purpose;
	
	@Column(name="paymentType")
	private String paymentType;

	
	
	public PatientPayment(Long paymentID, User user, String status, BigDecimal amount, String purpose,
			InsuranceClaims insuranceClaims, String paymentType) {
		this.paymentID = paymentID;
		this.user = user;
		this.status = status;
		this.amount = amount;
		this.purpose = purpose;
		this.paymentType = paymentType;
	}

	public PatientPayment() {
		super();
	}

	public Long getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(Long paymentID) {
		this.paymentID = paymentID;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	

}
